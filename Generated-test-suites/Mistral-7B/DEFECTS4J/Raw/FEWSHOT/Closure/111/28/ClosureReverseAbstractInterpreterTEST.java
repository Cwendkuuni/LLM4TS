package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Type;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private CodingConvention codingConvention;
    private JSTypeRegistry typeRegistry;
    private ClosureReverseAbstractInterpreter interpreter;

    @org.junit.Before
    public void setUp() {
        codingConvention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
    }

    @org.junit.Test
    public void testGetPreciserScopeKnowingConditionOutcomeWithTrueIsDef() {
        Node conditionNode = createMockNode("goog.isDef(x)");
        JSType expectedType = getNativeType(JSTypeNative.NUMBER_TYPE);
        when(typeRegistry.getNativeType(JSTypeNative.NUMBER_STRING_BOOLEAN)).thenReturn(expectedType);
        when(interpreter.getTypeIfRefinable(conditionNode, mock(FlowScope.class))).thenReturn(expectedType);

        JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, mock(FlowScope.class), true).getScope().getType();
        assertEquals(expectedType, actualType);
    }

    @org.junit.Test
    public void testGetPreciserScopeKnowingConditionOutcomeWithFalseIsDef() {
        Node conditionNode = createMockNode("goog.isDef(x)");
        when(typeRegistry.getNativeType(JSTypeNative.VOID_TYPE)).thenReturn(getNativeType(JSTypeNative.VOID_TYPE));
        when(interpreter.getTypeIfRefinable(conditionNode, mock(FlowScope.class))).thenReturn(null);

        JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNode, mock(FlowScope.class), false).getScope().getType();
        assertNull(actualType);
    }

    @org.junit.Test
    public void testRestrictParameterWithNumberTypeAndTrueIsDef() {
        Node parameterNode = createMockNode("x");
        JSType type = getNativeType(JSTypeNative.NUMBER_TYPE);
        FlowScope blindScope = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        when(restriction.apply(any(TypeRestriction.class))).thenReturn(getNativeType(JSTypeNative.NUMBER_TYPE));

        JSType actualType = interpreter.restrictParameter(parameterNode, type, blindScope, restriction, true).getScope().getType();
        assertEquals(type, actualType);
    }

    @org.junit.Test
    public void testRestrictParameterWithNullTypeAndFalseIsDef() {
        Node parameterNode = createMockNode("x");
        FlowScope blindScope = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);

        JSType actualType = interpreter.restrictParameter(parameterNode, null, blindScope, restriction, false).getScope().getType();
        assertNull(actualType);
    }

    @org.junit.Test
    public void testRestrictParameterWithFunctionTypeAndTrueIsFunction() {
        Node parameterNode = createMockNode("x");
        JSType type = createMockFunctionType();
        FlowScope blindScope = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        when(restriction.apply(any(TypeRestriction.class))).thenReturn(type);

        JSType actualType = interpreter.restrictParameter(parameterNode, type, blindScope, restriction, true).getScope().getType();
        assertEquals(type, actualType);
    }

    @org.junit.Test
    public void testGetRestrictedByTypeOfResultWithStringTypeAndTrue() {
        JSType type = getNativeType(JSTypeNative.STRING_TYPE);
        boolean outcome = true;
        Function<TypeRestriction, JSType> restriction = mock(Function.class);

        JSType actualType = interpreter.getRestrictedByTypeOfResult(type, "string", outcome).apply(new TypeRestriction(type, outcome)).getType();
        assertEquals(type, actualType);
    }

    @org.junit.Test
    public void testGetRestrictedByTypeOfResultWithStringTypeAndFalse() {
        JSType type = getNativeType(JSTypeNative.STRING_TYPE);
        boolean outcome = false;
        Function<TypeRestriction, JSType> restriction = mock(Function.class);

        JSType actualType = interpreter.getRestrictedByTypeOfResult(type, "string", outcome).apply(new TypeRestriction(type, outcome)).getType();
        assertNull(actualType);
    }

    @org.junit.Test
    public void testGetRestrictedWithoutUndefinedWithArrayType() {
        JSType type = getNativeType(JSTypeNative.ARRAY_TYPE);
        JSType actualType = interpreter.getRestrictedWithoutUndefined(type);
        assertEquals(type, actualType);
    }

    @org.junit.Test
    public void testGetRestrictedWithoutUndefinedWithNullType() {
        JSType type = getNativeType(JSTypeNative.NULL_TYPE);
        JSType actualType = interpreter.getRestrictedWithoutUndefined(type);
        assertNull(actualType);
    }

    @org.junit.Test
    public void testGetRestrictedWithoutNullWithNumberType() {
        JSType type = getNativeType(JSTypeNative.NUMBER_TYPE);
        JSType actualType = interpreter.getRestrictedWithoutNull(type);
        assertEquals(type, actualType);
    }

    private Node createMockNode(String nodeName) {
        Node node = mock(Node.class);
        when(node.isName()).thenReturn(true);
        when(node.getString()).thenReturn(nodeName);
        return node;
    }

    private FunctionType createMockFunctionType() {
        return mock(FunctionType.class);
    }

    private JSType getNativeType(int typeCode) {
        return typeRegistry.getNativeType(JSTypeNative.get(typeCode));
    }

    private JSType getNativeType(JSTypeNative jstypeNative) {
        return typeRegistry.getNativeType(jstypeNative);
    }
}