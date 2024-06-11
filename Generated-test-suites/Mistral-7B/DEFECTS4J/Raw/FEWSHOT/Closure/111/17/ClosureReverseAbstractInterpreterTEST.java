package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ClosureReverseAbstractInterpreterTest {
    private ClosureReverseAbstractInterpreter interpreter;
    private JSTypeRegistry typeRegistry;
    private CodingConvention convention;

    @Before
    public void setup() {
        convention = mock(CodingConvention.class);
        typeRegistry = mock(JSTypeRegistry.class);
        interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsDef() {
        Node condition = createMockNode("goog.isDef(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);
        JSType expectedType = createMockType(ObjectType.class);

        when(typeRegistry.createObjectType(ArgumentMatchers.anyString()))
                .thenReturn(expectedType);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), true);

        assertNotNull(result);
        assertEquals(expectedType, result.getScopeType());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsDefFalse() {
        Node condition = createMockNode("goog.isDef(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), false);

        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsDefAndNotNull() {
        Node condition = createMockNode("goog.isDefAndNotNull(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);
        JSType expectedType = createMockType(ObjectType.class);

        when(typeRegistry.createObjectType(ArgumentMatchers.anyString()))
                .thenReturn(expectedType);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), true);

        assertNotNull(result);
        assertEquals(expectedType, result.getScopeType());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsDefAndNotNullFalse() {
        Node condition = createMockNode("goog.isDefAndNotNull(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), false);

        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsString() {
        Node condition = createMockNode("goog.isString(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);
        JSType expectedType = createMockType(JSType.class);

        when(typeRegistry.createStringType())
                .thenReturn(expectedType);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), true);

        assertNotNull(result);
        assertEquals(expectedType, result.getScopeType());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsStringFalse() {
        Node condition = createMockNode("goog.isString(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), false);

        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsFunction() {
        Node condition = createMockNode("goog.isFunction(obj)");
        JSType blindScopeType = createMockType(FunctionType.class);
        JSType expectedType = createMockType(FunctionType.class);

        when(typeRegistry.createFunctionType(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(FunctionType.class)))
                .thenReturn(expectedType);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), true);

        assertNotNull(result);
        assertEquals(expectedType, result.getScopeType());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsFunctionFalse() {
        Node condition = createMockNode("goog.isFunction(obj)");
        JSType blindScopeType = createMockType(FunctionType.class);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), false);

        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsArray() {
        Node condition = createMockNode("goog.isArray(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);
        JSType expectedType = createMockType(JSType.class);

        when(typeRegistry.createArrayType(ArgumentMatchers.any(JSType.class)))
                .thenReturn(expectedType);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), true);

        assertNotNull(result);
        assertEquals(expectedType, result.getScopeType());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsArrayFalse() {
        Node condition = createMockNode("goog.isArray(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), false);

        assertNull(result);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsObject() {
        Node condition = createMockNode("goog.isObject(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);
        JSType expectedType = createMockType(ObjectType.class);

        when(typeRegistry.createObjectType(ArgumentMatchers.anyString()))
                .thenReturn(expectedType);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), true);

        assertNotNull(result);
        assertEquals(expectedType, result.getScopeType());
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcomeIsObjectFalse() {
        Node condition = createMockNode("goog.isObject(obj)");
        JSType blindScopeType = createMockType(ObjectType.class);

        FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(
                condition, new FlowScope(blindScopeType), false);

        assertNull(result);
    }

    private Node createMockNode(String nodeName) {
        Node node = mock(Node.class);
        when(node.isName()).thenReturn(true);
        when(node.getString()).thenReturn(nodeName);
        return node;
    }

    private JSType createMockType(Class<? extends JSType> typeClass) {
        JSType type = mock(typeClass);
        return type;
    }
}