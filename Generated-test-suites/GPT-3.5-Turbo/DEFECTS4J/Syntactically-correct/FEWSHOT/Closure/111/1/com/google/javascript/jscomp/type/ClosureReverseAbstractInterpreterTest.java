package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    private ClosureReverseAbstractInterpreter closureRAI;

    @Before
    public void setUp() {
        closureRAI = new ClosureReverseAbstractInterpreter(null, null);
    }

    @Test
    public void testIsDef() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isDef")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = null;
        assertEquals(expectedType, result);

        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = blindScope;
        assertEquals(expectedType, result);
    }

    @Test
    public void testIsNull() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isNull")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = blindScope;
        assertEquals(expectedType, result);

        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = null;
        assertEquals(expectedType, result);
    }
    
    @Test
    public void testIsDefAndNotNull() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isDefAndNotNull")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = blindScope;
        assertEquals(expectedType, result);
        
        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = null;
        assertEquals(expectedType, result);
    }
    
    @Test
    public void testIsString() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isString")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = blindScope;
        assertEquals(expectedType, result);

        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = null;
        assertEquals(expectedType, result);
    }
    
    @Test
    public void testIsBoolean() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isBoolean")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = blindScope;
        assertEquals(expectedType, result);

        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = null;
        assertEquals(expectedType, result);
    }
    
    @Test
    public void testIsNumber() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isNumber")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = blindScope;
        assertEquals(expectedType, result);

        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = null;
        assertEquals(expectedType, result);
    }
    
    @Test
    public void testIsFunction() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isFunction")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = blindScope;
        assertEquals(expectedType, result);

        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = null;
        assertEquals(expectedType, result);
    }
    
    @Test
    public void testIsArray() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isArray")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = blindScope;
        assertEquals(expectedType, result);

        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = null;
        assertEquals(expectedType, result);
    }
    
    @Test
    public void testIsObject() {
        FlowScope blindScope = new FlowScope();
        Node condition = new Node(Node.Token.CALL, new Node(Node.Token.GETPROP, new Node(Node.Token.NAME, "goog"), new Node(Node.Token.STRING, "isObject")), new Node(Node.Token.NAME, "param1"));
        boolean outcome = true;
        FlowScope result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        JSType expectedType = blindScope;
        assertEquals(expectedType, result);

        outcome = false;
        result = closureRAI.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        expectedType = null;
        assertEquals(expectedType, result);
    }
}