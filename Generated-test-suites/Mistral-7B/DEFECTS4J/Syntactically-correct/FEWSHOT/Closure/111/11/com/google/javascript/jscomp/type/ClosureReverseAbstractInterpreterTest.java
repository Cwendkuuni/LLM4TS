package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

    private JSTypeRegistry typeRegistry;
    private CodingConvention convention;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        typeRegistry = new JSTypeRegistry();
        convention = new CodingConvention.NoOp();
    }

    @Test
    public void testIsDef() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = typeRegistry.createUnionType(typeRegistry.createNullType(), typeRegistry.createUndefinedType());
        JSType actualType = interpreter.getRestrictedWithoutUndefined(expectedType);
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);

        JSType inputType = typeRegistry.createStringType();
        JSType actualType2 = interpreter.restrictParameter(
                new Node(), inputType, new FlowScope(), interpreter.restricters.get("isDef"), true).getDeclaredVariableType();
        assertNull(actualType2);

        JSType actualType3 = interpreter.restrictParameter(
                new Node(), inputType, new FlowScope(), interpreter.restricters.get("isDef"), false).getDeclaredVariableType();
        assertNotNull(actualType3);
        assertEquals(typeRegistry.createVoidType(), actualType3);
    }

    @Test
    public void testIsNotNull() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = typeRegistry.createNullType();
        JSType actualType = interpreter.getRestrictedWithoutNull(expectedType);
        assertNull(actualType);

        JSType inputType = typeRegistry.createStringType();
        JSType actualType2 = interpreter.restrictParameter(
                new Node(), inputType, new FlowScope(), interpreter.restricters.get("isNull"), true).getDeclaredVariableType();
        assertNotNull(actualType2);
        assertEquals(inputType, actualType2);

        JSType actualType3 = interpreter.restrictParameter(
                new Node(), null, new FlowScope(), interpreter.restricters.get("isNull"), false).getDeclaredVariableType();
        assertNotNull(actualType3);
        assertEquals(typeRegistry.createVoidType(), actualType3);
    }

    @Test
    public void testIsDefAndNotNull() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = typeRegistry.createStringType();
        JSType actualType = interpreter.getRestrictedWithoutUndefined(
                interpreter.getRestrictedWithoutNull(expectedType));
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);

        JSType inputType = typeRegistry.createStringType();
        JSType actualType2 = interpreter.restrictParameter(
                new Node(), inputType, new FlowScope(),
                interpreter.restricters.get("isDefAndNotNull"), true).getDeclaredVariableType();
        assertNull(actualType2);

        JSType actualType3 = interpreter.restrictParameter(
                new Node(), inputType, new FlowScope(),
                interpreter.restricters.get("isDefAndNotNull"), false).getDeclaredVariableType();
        assertNotNull(actualType3);
        assertEquals(typeRegistry.createVoidType(), actualType3);
    }

    @Test
    public void testIsString() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = typeRegistry.createStringType();
        JSType actualType = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isString"), true).getDeclaredVariableType();
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);

        JSType actualType2 = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isString"), false).getDeclaredVariableType();
        assertNull(actualType2);
    }

    @Test
    public void testIsBoolean() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = typeRegistry.createBooleanType();
        JSType actualType = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isBoolean"), true).getDeclaredVariableType();
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);

        JSType actualType2 = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isBoolean"), false).getDeclaredVariableType();
        assertNull(actualType2);
    }

    @Test
    public void testIsNumber() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = typeRegistry.createNumberType();
        JSType actualType = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isNumber"), true).getDeclaredVariableType();
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);

        JSType actualType2 = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isNumber"), false).getDeclaredVariableType();
        assertNull(actualType2);
    }

    @Test
    public void testIsFunction() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = new FunctionType(new ObjectType(typeRegistry.createNullType()), new ObjectType(typeRegistry.createNullType()));
        JSType actualType = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isFunction"), true).getDeclaredVariableType();
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);

        JSType actualType2 = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isFunction"), false).getDeclaredVariableType();
        assertNull(actualType2);
    }

    @Test
    public void testIsArray() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = typeRegistry.createArrayType(typeRegistry.createNullType());
        JSType actualType = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isArray"), true).getDeclaredVariableType();
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);

        JSType actualType2 = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isArray"), false).getDeclaredVariableType();
        assertNull(actualType2);
    }

    @Test
    public void testIsObject() {
        ClosureReverseAbstractInterpreter interpreter =
                new ClosureReverseAbstractInterpreter(convention, typeRegistry);

        JSType expectedType = new ObjectType(typeRegistry.createNullType());
        JSType actualType = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isObject"), true).getDeclaredVariableType();
        assertNotNull(actualType);
        assertEquals(expectedType, actualType);

        JSType actualType2 = interpreter.restrictParameter(
                new Node(), new Node(), new FlowScope(), interpreter.restricters.get("isObject"), false).getDeclaredVariableType();
        assertNull(actualType2);
    }
}