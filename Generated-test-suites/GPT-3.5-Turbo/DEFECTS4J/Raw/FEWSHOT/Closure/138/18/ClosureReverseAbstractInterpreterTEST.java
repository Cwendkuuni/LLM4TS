package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);

        // Test case where callee is "goog.isDef" and outcome is true
        Node condition1 = new Node(Token.CALL);
        Node callee1 = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isDef"));
        Node param1 = new Node(Token.NAME, "testParam");
        callee1.addChildToBack(param1);
        condition1.addChildToBack(callee1);
        FlowScope blindScope1 = null;
        boolean outcome1 = true;
        FlowScope result1 = interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope1, outcome1);
        // Expected result is the same as blindScope1 since there is no applicable restricter
        assertNull(result1);

        // Test case where callee is "goog.isArray" and outcome is false
        Node condition2 = new Node(Token.CALL);
        Node callee2 = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isArray"));
        Node param2 = new Node(Token.NAME, "testParam");
        callee2.addChildToBack(param2);
        condition2.addChildToBack(callee2);
        FlowScope blindScope2 = null;
        boolean outcome2 = false;
        FlowScope result2 = interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope2, outcome2);
        // Expected result is the same as blindScope1 since there is no applicable restricter
        assertNull(result2);

        // Test case where callee is "goog.isArray" and outcome is true
        Node condition3 = new Node(Token.CALL);
        Node callee3 = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isArray"));
        Node param3 = new Node(Token.NAME, "testParam");
        callee3.addChildToBack(param3);
        condition3.addChildToBack(callee3);
        FlowScope blindScope3 = null;
        boolean outcome3 = true;
        FlowScope result3 = interpreter.getPreciserScopeKnowingConditionOutcome(condition3, blindScope3, outcome3);
        // Expected result is the same as blindScope1 since there is no applicable restricter
        assertNull(result3);

        // Test case where callee is "goog.isObject" and outcome is false
        Node condition4 = new Node(Token.CALL);
        Node callee4 = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isObject"));
        Node param4 = new Node(Token.NAME, "testParam");
        callee4.addChildToBack(param4);
        condition4.addChildToBack(callee4);
        FlowScope blindScope4 = null;
        boolean outcome4 = false;
        FlowScope result4 = interpreter.getPreciserScopeKnowingConditionOutcome(condition4, blindScope4, outcome4);
        // Expected result is the same as blindScope1 since there is no applicable restricter
        assertNull(result4);

        // Test case where callee is "goog.isObject" and outcome is true
        Node condition5 = new Node(Token.CALL);
        Node callee5 = new Node(Token.GETPROP, new Node(Token.NAME, "goog"), new Node(Token.STRING, "isObject"));
        Node param5 = new Node(Token.NAME, "testParam");
        callee5.addChildToBack(param5);
        condition5.addChildToBack(callee5);
        FlowScope blindScope5 = null;
        boolean outcome5 = true;
        FlowScope result5 = interpreter.getPreciserScopeKnowingConditionOutcome(condition5, blindScope5, outcome5);
        // Expected result is the same as blindScope1 since there is no applicable restricter
        assertNull(result5);
    }
}