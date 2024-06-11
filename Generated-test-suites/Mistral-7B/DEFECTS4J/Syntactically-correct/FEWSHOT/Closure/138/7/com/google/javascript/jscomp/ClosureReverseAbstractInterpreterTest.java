package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.graph.Type;
import com.google.javascript.jscomp.graph.Type.Code;
import com.google.javascript.jscomp.graph.Type.Kind;
import com.google.javascript.jscomp.graph.Type.TypeId;
import com.google.javascript.jscomp.graph.node.CallNode;
import com.google.javascript.jscomp.graph.node.GetPropNode;
import com.google.javascript.jscomp.graph.node.NameNode;
import com.google.javascript.jscomp.graph.scope.FlowScope;
import com.google.javascript.jscomp.graph.types.JSTypeRegistry;
import com.google.javascript.rhino.jstype.JSType;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {
    private static final JSTypeRegistry TYPE_REGISTRY = mock(JSTypeRegistry.class);

    @Test
    public void testIsDef() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType type = new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType result = interpreter.restricters.get("isDef").apply(restriction);
        assertSame(type.getSubType(Code.UNDEFINED), result);
    }

    @Test
    public void testIsDefFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType type = new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT);
        TypeRestriction restriction = new TypeRestriction(type, false);
        JSType result = interpreter.restricters.get("isDef").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testIsFunction() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        FunctionType functionType = new FunctionType(TypeId.FUNCTION, "FunctionType",
                new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT),
                new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT),
                null);
        TypeRestriction restriction = new TypeRestriction(functionType, true);
        JSType result = interpreter.restricters.get("isFunction").apply(restriction);
        assertSame(functionType, result);
    }

    @Test
    public void testIsFunctionFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        FunctionType functionType = new FunctionType(TypeId.FUNCTION, "FunctionType",
                new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT),
                new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT),
                null);
        TypeRestriction restriction = new TypeRestriction(functionType, false);
        JSType result = interpreter.restricters.get("isFunction").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testIsArray() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType arrayType = new ArrayType(TypeId.ARRAY, "ArrayType", new ObjectType(
                TypeId.OBJECT, "ObjectType", Code.OBJECT), null);
        TypeRestriction restriction = new TypeRestriction(null, true);
        JSType result = interpreter.restricters.get("isArray").apply(restriction);
        assertSame(arrayType, result);
    }

    @Test
    public void testIsArrayFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        TypeRestriction restriction = new TypeRestriction(null, false);
        JSType result = interpreter.restricters.get("isArray").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testIsObject() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        ObjectType objectType = new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT);
        TypeRestriction restriction = new TypeRestriction(objectType, true);
        JSType result = interpreter.restricters.get("isObject").apply(restriction);
        assertSame(objectType, result);
    }

    @Test
    public void testIsObjectFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        ObjectType objectType = new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT);
        TypeRestriction restriction = new TypeRestriction(objectType, false);
        JSType result = interpreter.restricters.get("isObject").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testIsString() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType stringType = new StringType(TypeId.STRING, "StringType", Code.STRING);
        TypeRestriction restriction = new TypeRestriction(null, true);
        JSType result = interpreter.restricters.get("isString").apply(restriction);
        assertSame(stringType, result);
    }

    @Test
    public void testIsStringFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        TypeRestriction restriction = new TypeRestriction(null, false);
        JSType result = interpreter.restricters.get("isString").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testIsBoolean() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType booleanType = new BooleanType(TypeId.BOOLEAN, "BooleanType",
                Code.BOOLEAN);
        TypeRestriction restriction = new TypeRestriction(null, true);
        JSType result = interpreter.restricters.get("isBoolean").apply(restriction);
        assertSame(booleanType, result);
    }

    @Test
    public void testIsBooleanFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        TypeRestriction restriction = new TypeRestriction(null, false);
        JSType result = interpreter.restricters.get("isBoolean").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testIsNumber() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType numberType = new NumberType(TypeId.NUMBER, "NumberType", Code.NUMBER);
        TypeRestriction restriction = new TypeRestriction(null, true);
        JSType result = interpreter.restricters.get("isNumber").apply(restriction);
        assertSame(numberType, result);
    }

    @Test
    public void testIsNumberFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        TypeRestriction restriction = new TypeRestriction(null, false);
        JSType result = interpreter.restricters.get("isNumber").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testIsDefAndNotNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType type = new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType result = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testIsDefAndNotNullTrue() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType type = new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT);
        TypeRestriction restriction = new TypeRestriction(type, true);
        JSType result = interpreter.restricters.get("isDefAndNotNull").apply(restriction);
        assertSame(type.getSubType(Code.UNDEFINED), result);
    }

    @Test
    public void testIsNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSType nullType = TYPE_REGISTRY.getNativeType(Code.NULL);
        TypeRestriction restriction = new TypeRestriction(null, true);
        JSType result = interpreter.restricters.get("isNull").apply(restriction);
        assertSame(nullType, result);
    }

    @Test
    public void testIsNullFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        TypeRestriction restriction = new TypeRestriction(null, false);
        JSType result = interpreter.restricters.get("isNull").apply(restriction);
        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
        NameNode nameNode = mock(NameNode.class);
        when(nameNode.getString()).thenReturn("goog");
        GetPropNode getPropNode = mock(GetPropNode.class);
        when(getPropNode.getLastChild()).thenReturn(nameNode);
        CallNode callNode = mock(CallNode.class);
        when(callNode.getFirstChild()).thenReturn(getPropNode);
        when(callNode.getLastChild()).thenReturn(mock(NameNode.class));
        FlowScope blindScope = mock(FlowScope.class);
        when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));
        when(blindScope.getTypeInScope(nameNode)).thenReturn(new ObjectType(
                TypeId.OBJECT, "ObjectType", Code.OBJECT));
        when(TYPE_REGISTRY.getTypeIfRefinable(nameNode, blindScope)).thenReturn(
                new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT));
        when(TYPE_REGISTRY.getNativeType(Code.ARRAY)).thenReturn(
                new ArrayType(TypeId.ARRAY, "ArrayType", new ObjectType(
                        TypeId.OBJECT, "ObjectType", Code.OBJECT), null));
        when(TYPE_REGISTRY.getNativeType(Code.OBJECT)).thenReturn(
                new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT));
        when(TYPE_REGISTRY.getNativeType(Code.STRING)).thenReturn(
                new StringType(TypeId.STRING, "StringType", Code.STRING));
        when(TYPE_REGISTRY.getNativeType(Code.BOOLEAN)).thenReturn(
                new BooleanType(TypeId.BOOLEAN, "BooleanType", Code.BOOLEAN));
        when(TYPE_REGISTRY.getNativeType(Code.NUMBER)).thenReturn(
                new NumberType(TypeId.NUMBER, "NumberType", Code.NUMBER));
        when(TYPE_REGISTRY.getNativeType(Code.FUNCTION)).thenReturn(
                new FunctionType(TypeId.FUNCTION, "FunctionType",
                        new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT),
                        new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT),
                        null));

        FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
                callNode, blindScope, true);
        assertSame(blindScope, informedScope);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeFalse() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
                null, TYPE_REGISTRY);
        JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
        NameNode nameNode = mock(NameNode.class);
        when(nameNode.getString()).thenReturn("goog");
        GetPropNode getPropNode = mock(GetPropNode.class);
        when(getPropNode.getLastChild()).thenReturn(nameNode);
        CallNode callNode = mock(CallNode.class);
        when(callNode.getFirstChild()).thenReturn(getPropNode);
        when(callNode.getLastChild()).thenReturn(mock(NameNode.class));
        FlowScope blindScope = mock(FlowScope.class);
        when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));
        when(blindScope.getTypeInScope(nameNode)).thenReturn(new ObjectType(
                TypeId.OBJECT, "ObjectType", Code.OBJECT));
        when(TYPE_REGISTRY.getTypeIfRefinable(nameNode, blindScope)).thenReturn(
                new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT));
        when(TYPE_REGISTRY.getNativeType(Code.ARRAY)).thenReturn(
                new ArrayType(TypeId.ARRAY, "ArrayType", new ObjectType(
                        TypeId.OBJECT, "ObjectType", Code.OBJECT), null));
        when(TYPE_REGISTRY.getNativeType(Code.OBJECT)).thenReturn(
                new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT));
        when(TYPE_REGISTRY.getNativeType(Code.STRING)).thenReturn(
                new StringType(TypeId.STRING, "StringType", Code.STRING));
        when(TYPE_REGISTRY.getNativeType(Code.BOOLEAN)).thenReturn(
                new BooleanType(TypeId.BOOLEAN, "BooleanType", Code.BOOLEAN));
        when(TYPE_REGISTRY.getNativeType(Code.NUMBER)).thenReturn(
                new NumberType(TypeId.NUMBER, "NumberType", Code.NUMBER));
        when(TYPE_REGISTRY.getNativeType(Code.FUNCTION)).thenReturn(
                new FunctionType(TypeId.FUNCTION, "FunctionType",
                        new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT),
                        new ObjectType(TypeId.OBJECT, "ObjectType", Code.OBJECT),
                        null));

        FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
                callNode, blindScope, false);
        assertSame(blindScope, informedScope);
    }
}