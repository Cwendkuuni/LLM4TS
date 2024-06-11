package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Test condition where callee is "goog.isDef" and outcome is true
        Node condition1 = new Node(Token.CALL);
        Node callee1 = new Node(Token.GETPROP);
        Node param1 = new Node(Token.NAME, "param1");
        callee1.addChildToBack(new Node(Token.NAME, "goog"));
        callee1.addChildToBack(new Node(Token.STRING, "isDef"));
        condition1.addChildToBack(callee1);
        condition1.addChildToBack(param1);
        assertEquals(flowScope1, ClosureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope, true));

        // Test condition where callee is "goog.isDef" and outcome is false
        Node condition2 = new Node(Token.CALL);
        Node callee2 = new Node(Token.GETPROP);
        Node param2 = new Node(Token.NAME, "param2");
        callee2.addChildToBack(new Node(Token.NAME, "goog"));
        callee2.addChildToBack(new Node(Token.STRING, "isDef"));
        condition2.addChildToBack(callee2);
        condition2.addChildToBack(param2);
        assertEquals(flowScope2, ClosureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope, false));

        // Test condition where callee is "goog.isArray" and outcome is true
        Node condition3 = new Node(Token.CALL);
        Node callee3 = new Node(Token.GETPROP);
        Node param3 = new Node(Token.NAME, "param3");
        callee3.addChildToBack(new Node(Token.NAME, "goog"));
        callee3.addChildToBack(new Node(Token.STRING, "isArray"));
        condition3.addChildToBack(callee3);
        condition3.addChildToBack(param3);
        assertEquals(flowScope3, ClosureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition3, blindScope, true));

        // Test condition where callee is "goog.isArray" and outcome is false
        Node condition4 = new Node(Token.CALL);
        Node callee4 = new Node(Token.GETPROP);
        Node param4 = new Node(Token.NAME, "param4");
        callee4.addChildToBack(new Node(Token.NAME, "goog"));
        callee4.addChildToBack(new Node(Token.STRING, "isArray"));
        condition4.addChildToBack(callee4);
        condition4.addChildToBack(param4);
        assertEquals(flowScope4, ClosureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition4, blindScope, false));

        // Test condition where callee is "goog.isObject" and outcome is true
        Node condition5 = new Node(Token.CALL);
        Node callee5 = new Node(Token.GETPROP);
        Node param5 = new Node(Token.NAME, "param5");
        callee5.addChildToBack(new Node(Token.NAME, "goog"));
        callee5.addChildToBack(new Node(Token.STRING, "isObject"));
        condition5.addChildToBack(callee5);
        condition5.addChildToBack(param5);
        assertEquals(flowScope5, ClosureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition5, blindScope, true));

        // Test condition where callee is "goog.isObject" and outcome is false
        Node condition6 = new Node(Token.CALL);
        Node callee6 = new Node(Token.GETPROP);
        Node param6 = new Node(Token.NAME, "param6");
        callee6.addChildToBack(new Node(Token.NAME, "goog"));
        callee6.addChildToBack(new Node(Token.STRING, "isObject"));
        condition6.addChildToBack(callee6);
        condition6.addChildToBack(param6);
        assertEquals(flowScope6, ClosureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition6, blindScope, false));
    }

    private FlowScope blindScope;
    private FlowScope flowScope1;
    private FlowScope flowScope2;
    private FlowScope flowScope3;
    private FlowScope flowScope4;
    private FlowScope flowScope5;
    private FlowScope flowScope6;

    @Before
    public void setUp() {
        blindScope = new FlowScope();
        flowScope1 = new FlowScope();
        flowScope1.createChildFlowScope();
        flowScope2 = blindScope;
        flowScope3 = new FlowScope();
        flowScope3.createChildFlowScope();
        flowScope4 = blindScope;
        flowScope5 = new FlowScope();
        flowScope5.createChildFlowScope();
        flowScope6 = blindScope;
    }
}