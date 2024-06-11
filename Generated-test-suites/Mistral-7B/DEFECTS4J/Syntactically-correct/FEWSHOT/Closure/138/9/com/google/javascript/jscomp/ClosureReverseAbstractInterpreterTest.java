package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.FlowScope;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.Visitor;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private static final JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    private static final CodingConvention convention = mock(CodingConvention.class);
    private static final ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, typeRegistry);

    @Test
    public void testRestrictToArrayVisitor() {
        JSType arrayType = JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE);
        ObjectType objectType = mock(ObjectType.class);
        when(objectType.isSubtype(arrayType)).thenReturn(true);

        JSType result = interpreter.restrictToArrayVisitor.apply(objectType);
        assertEquals(arrayType, result);

        JSType result2 = interpreter.restrictToArrayVisitor.apply(null);
        assertNull(result2);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        ObjectType objectType = mock(ObjectType.class);
        JSType arrayType = JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE);
        when(objectType.isSubtype(arrayType)).thenReturn(false);

        JSType result = interpreter.restrictToNotArrayVisitor.apply(objectType);
        assertNull(result);

        JSType result2 = interpreter.restrictToNotArrayVisitor.apply(null);
        assertNull(result2);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        ObjectType objectType = mock(ObjectType.class);
        FunctionType functionType = mock(FunctionType.class);
        when(objectType.isSubtype(functionType)).thenReturn(false);
        when(objectType.isSubtype(JSTypeNative.getNativeType(JSTypeNative.NO_OBJECT_TYPE))).thenReturn(true);

        JSType result = interpreter.restrictToObjectVisitor.apply(objectType);
        assertEquals(JSTypeNative.getNativeType(JSTypeNative.NO_OBJECT_TYPE), result);

        JSType result2 = interpreter.restrictToObjectVisitor.apply(functionType);
        assertEquals(functionType, result2);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        ObjectType objectType = mock(ObjectType.class);
        FunctionType functionType = mock(FunctionType.class);

        JSType result = interpreter.restrictToNotObjectVisitor.apply(objectType);
        assertNull(result);

        JSType result2 = interpreter.restrictToNotObjectVisitor.apply(functionType);
        assertNull(result2);
    }

    @Test
    public void testRestrictParameter() {
        JSType originalType = JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        when(restriction.apply(new TypeRestriction(originalType, true))).thenReturn(JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE));

        Node parameter = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);

        JSType restrictedType = interpreter.restrictParameter(parameter, originalType, blindScope, restriction, true);
        assertEquals(JSTypeNative.getNativeType(JSTypeNative.ARRAY_TYPE), restrictedType);

        JSType restrictedType2 = interpreter.restrictParameter(parameter, originalType, blindScope, restriction, false);
        assertNull(restrictedType2);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = mock(Node.class);
        Node parameter = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        JSType type = JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE);

        when(condition.getType()).thenReturn(Node.CALL);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild().getType()).thenReturn(Node.GETPROP);
        when(condition.getLastChild().isQualifiedName()).thenReturn(true);
        when(condition.getLastChild().getString()).thenReturn("isDef");
        when(condition.getFirstChild().getFirstChild().getString()).thenReturn("goog");

        when(parameter.isQualifiedName()).thenReturn(true);
        when(parameter.getType()).thenReturn(type);

        when(type.isSubtype(JSTypeNative.getNativeType(JSTypeNative.OBJECT_TYPE))).thenReturn(true);

        when(interpreter.restricters.get("isDef")).thenReturn(mock(Function.class));

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNotNull(result);

        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeWithNullType() {
        Node condition = mock(Node.class);
        Node parameter = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);

        when(condition.getType()).thenReturn(Node.CALL);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild().getType()).thenReturn(Node.GETPROP);
        when(condition.getLastChild().isQualifiedName()).thenReturn(true);
        when(condition.getLastChild().getString()).thenReturn("isDef");
        when(condition.getFirstChild().getFirstChild().getString()).thenReturn("goog");

        when(parameter.isQualifiedName()).thenReturn(true);
        when(parameter.getType()).thenReturn(null);

        when(interpreter.restricters.get("isDef")).thenReturn(mock(Function.class));

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNull(result);

        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNull(result);
    }
}