package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testRestrictToArrayVisitor() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        JSType arrayType = typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE);
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
            new CodingConvention(), typeRegistry);
        Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;

        JSType topType = JSType.TOP;
        JSType objectType = typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);
        JSType functionType = typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE);

        assertEquals(topType, visitor.visit(topType));
        assertEquals(arrayType, visitor.visit(objectType).isSubtype(arrayType) ? arrayType : null);
        assertEquals(null, visitor.visit(null));
        assertEquals(objectType, visitor.visit(functionType));
    }

    @Test
    public void testRestrictToNotArrayVisitor() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
            new CodingConvention(), typeRegistry);
        Visitor<JSType> visitor = interpreter.restrictToNotArrayVisitor;

        JSType arrayType = typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE);
        JSType objectType = typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);

        assertEquals(null, visitor.visit(null));
        assertEquals(arrayType.isSubtype(objectType) ? null : objectType, visitor.visit(objectType));
        assertEquals(null, visitor.visit(functionType));
    }

    @Test
    public void testRestrictToObjectVisitor() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
            new CodingConvention(), typeRegistry);
        Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;

        JSType topType = JSType.TOP;
        JSType noObjectType = typeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
        JSType functionType = typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE);
        JSType arrayType = typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE);

        assertEquals(noObjectType, visitor.visit(topType));
        assertEquals(noObjectType, visitor.visit(objectType));
        assertEquals(functionType, visitor.visit(functionType));
    }

    @Test
    public void testRestrictToNotObjectVisitor() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
            new CodingConvention(), typeRegistry);
        Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;

        JSType objectType = typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);
        JSType functionType = typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE);

        assertEquals(null, visitor.visit(null));
        assertEquals(null, visitor.visit(objectType));
        assertEquals(null, visitor.visit(functionType));
    }

    @Test
    public void testRestricters() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
            new CodingConvention(), typeRegistry);

        Map<String, Function<TypeRestriction, JSType>> restricters = interpreter.restricters;

        Function<TypeRestriction, JSType> isDef = restricters.get("isDef");
        Function<TypeRestriction, JSType> isNull = restricters.get("isNull");
        Function<TypeRestriction, JSType> isDefAndNotNull = restricters.get("isDefAndNotNull");
        Function<TypeRestriction, JSType> isString = restricters.get("isString");
        Function<TypeRestriction, JSType> isBoolean = restricters.get("isBoolean");
        Function<TypeRestriction, JSType> isNumber = restricters.get("isNumber");
        Function<TypeRestriction, JSType> isFunction = restricters.get("isFunction");
        Function<TypeRestriction, JSType> isArray = restricters.get("isArray");
        Function<TypeRestriction, JSType> isObject = restricters.get("isObject");

        assertNotNull(isDef);
        assertNotNull(isNull);
        assertNotNull(isDefAndNotNull);
        assertNotNull(isString);
        assertNotNull(isBoolean);
        assertNotNull(isNumber);
        assertNotNull(isFunction);
        assertNotNull(isArray);
        assertNotNull(isObject);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        JSTypeRegistry typeRegistry = new JSTypeRegistry();
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
            new CodingConvention(), typeRegistry);
        FunctionType functionType = (FunctionType) typeRegistry.getNativeType(JSTypeNative.FUNCTION_TYPE);
        ObjectType objectType = (ObjectType) typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE);
        ObjectType arrayType = (ObjectType) typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE);

        Node condition = NodeFactory.createCall(
            NodeFactory.createGetProperty(NodeFactory.createName("goog"), "isDef"),
            NodeFactory.createQualifiedName("myParameter"));

        Node parameter = NodeFactory.createQualifiedName("myParameter");

        JSType parameterType = JSType.STRING;

        FlowScope blindScope = new FlowScope();

        FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
            condition, blindScope, true);

        assertEquals(informedScope, interpreter.restrictParameter(
            parameter, parameterType, blindScope, interpreter.restricters.get("isDef"), true));

        informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(
            condition, blindScope, false);

        assertEquals(blindScope, interpreter.restrictParameter(
            parameter, parameterType, blindScope, interpreter.restricters.get("isDef"), false));
    }
}