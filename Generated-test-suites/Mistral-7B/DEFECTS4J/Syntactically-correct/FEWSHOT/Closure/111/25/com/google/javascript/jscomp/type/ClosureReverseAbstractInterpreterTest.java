package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.TypeRestriction;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

    private final JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    private final CodingConvention codingConvention = mock(CodingConvention.class);

    @Test
    public void testRestrictToArrayVisitor() {
        JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
        when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(getNativeType(JSTypeNative.ARRAY_TYPE));
        Visitor<JSType> visitor = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor() {
            @Override
            protected JSType caseTopType(JSType topType) {
                return topType;
            }

            @Override
            public JSType caseObjectType(ObjectType type) {
                JSType arrayType = getNativeType(JSTypeNative.ARRAY_TYPE);
                return arrayType.isSubtype(type) ? arrayType : null;
            }
        };

        JSType result = type.visit(visitor);
        assertNull(result);

        type = getNativeType(JSTypeNative.ARRAY_TYPE);
        result = type.visit(visitor);
        assertNotNull(result);
        assertEquals(type, result);
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
        when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(getNativeType(JSTypeNative.ARRAY_TYPE));
        Visitor<JSType> visitor = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor() {
            @Override
            public JSType caseObjectType(ObjectType type) {
                return type.isSubtype(getNativeType(JSTypeNative.ARRAY_TYPE)) ? null : type;
            }
        };

        JSType result = type.visit(visitor);
        assertNotNull(result);
        assertEquals(type, result);

        JSType arrayType = getNativeType(JSTypeNative.ARRAY_TYPE);
        result = arrayType.visit(visitor);
        assertNull(result);
    }

    @Test
    public void testRestrictToObjectVisitor() {
        JSType type = getNativeType(JSTypeNative.FUNCTION_TYPE);
        when(typeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE)).thenReturn(getNativeType(JSTypeNative.NO_OBJECT_TYPE));
        Visitor<JSType> visitor = new ClosureReverseAbstractInterpreter.RestrictByTrueTypeOfResultVisitor() {
            @Override
            protected JSType caseTopType(JSType topType) {
                return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
            }

            @Override
            public JSType caseObjectType(ObjectType type) {
                return type;
            }

            @Override
            public JSType caseFunctionType(FunctionType type) {
                return type;
            }
        };

        JSType result = type.visit(visitor);
        assertNotNull(result);
        assertEquals(type, result);
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
        Visitor<JSType> visitor = new ClosureReverseAbstractInterpreter.RestrictByFalseTypeOfResultVisitor() {
            @Override
            public JSType caseAllType() {
                return typeRegistry.createUnionType(
                        getNativeType(JSTypeNative.NUMBER_STRING_BOOLEAN),
                        getNativeType(JSTypeNative.NULL_VOID));
            }

            @Override
            public JSType caseObjectType(ObjectType type) {
                return null;
            }

            @Override
            public JSType caseFunctionType(FunctionType type) {
                return null;
            }
        };

        JSType result = type.visit(visitor);
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        Node condition = mock(Node.class);
        FlowScope blindScope = mock(FlowScope.class);
        when(condition.isCall()).thenReturn(true);
        when(condition.getChildCount()).thenReturn(2);
        when(condition.getFirstChild().isName()).thenReturn(true);
        when(condition.getFirstChild().getString()).thenReturn("goog");
        when(condition.getLastChild().isQualifiedName()).thenReturn(true);
        when(condition.getLastChild().getFirstChild().isString()).thenReturn(true);
        when(condition.getLastChild().getLastChild().isString()).thenReturn(true);
        when(condition.getLastChild().getLastChild().getString()).thenReturn("isDef");

        JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
        when(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE)).thenReturn(getNativeType(JSTypeNative.ARRAY_TYPE));
        when(typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE)).thenReturn(getNativeType(JSTypeNative.OBJECT_TYPE));
        when(typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE)).thenReturn(getNativeType(JSTypeNative.FUNCTION_TYPE));
        JSType stringType = getNativeType(JSTypeNative.STRING_TYPE);
        when(typeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE)).thenReturn(getNativeType(JSTypeNative.BOOLEAN_TYPE));
        when(typeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE)).thenReturn(getNativeType(JSTypeNative.NUMBER_TYPE));
        when(typeRegistry.getNativeType(JSTypeNative.VOID_TYPE)).thenReturn(getNativeType(JSTypeNative.VOID_TYPE));

        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
        assertNotNull(result);

        type = getNativeType(JSTypeNative.BOOLEAN_TYPE);
        result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
        assertNull(result);
    }

    @Test
    public void testRestrictParameter() {
        Node parameter = mock(Node.class);
        JSType type = getNativeType(JSTypeNative.OBJECT_TYPE);
        FlowScope blindScope = mock(FlowScope.class);
        Function<TypeRestriction, JSType> restriction = mock(Function.class);
        when(restriction.apply(any(TypeRestriction.class))).thenReturn(getNativeType(JSTypeNative.NUMBER_TYPE));
        boolean outcome = true;

        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
        JSType result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertNotNull(result);
        assertEquals(getNativeType(JSTypeNative.NUMBER_TYPE), result);
    }

    private JSType getNativeType(int nativeType) {
        return typeRegistry.getNativeType(nativeType);
    }

    private JSType getNativeType(Class<? extends JSType> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}