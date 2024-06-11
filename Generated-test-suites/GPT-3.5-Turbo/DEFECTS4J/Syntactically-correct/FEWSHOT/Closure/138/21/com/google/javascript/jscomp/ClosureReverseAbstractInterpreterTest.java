package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Test for `goog.isArray`
        Node condition1 = new Node(Token.newString("CALL"));
        Node callee1 = new Node(Token.newString("GETPROP"));
        Node param1 = new Node(Token.newString("NAME"));
        callee1.replaceChild(0, param1);
        Node left1 = new Node(Token.newString("NAME"));
        left1.replaceChild(0, new Node(Token.newString("goog")));
        Node right1 = new Node(Token.newString("STRING"));
        right1.setString("isArray");
        callee1.replaceChild(1, right1);
        condition1.replaceChild(0, callee1);
        condition1.addChildToBack(new Node(Token.newString("NAME")));
        Node blindScope1 = new Node(Token.newString("FLOW_SCOPE"));
        Node outcome1 = new Node(Token.newBoolean(true));
        ClosureReverseAbstractInterpreter interpreter1 = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        FlowScope result1 = interpreter1.getPreciserScopeKnowingConditionOutcome(condition1, blindScope1, outcome1.getBoolean());
        assertEquals(result1.toString(), outcome1.toString()); 

        // Test for `goog.isObject`
        Node condition2 = new Node(Token.newString("CALL"));
        Node callee2 = new Node(Token.newString("GETPROP"));
        Node param2 = new Node(Token.newString("NAME"));
        callee2.replaceChild(0, param2);
        Node left2 = new Node(Token.newString("NAME"));
        left2.replaceChild(0, new Node(Token.newString("goog")));
        Node right2 = new Node(Token.newString("STRING"));
        right2.setString("isObject");
        callee2.replaceChild(1, right2);
        condition2.replaceChild(0, callee2);
        condition2.addChildToBack(new Node(Token.newString("NAME")));
        Node blindScope2 = new Node(Token.newString("FLOW_SCOPE"));
        Node outcome2 = new Node(Token.newBoolean(true));
        ClosureReverseAbstractInterpreter interpreter2 = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        FlowScope result2 = interpreter2.getPreciserScopeKnowingConditionOutcome(condition2, blindScope2, outcome2.getBoolean());
        assertEquals(result2.toString(), outcome2.toString()); 
        
        // Test for non-existent method
        Node condition3 = new Node(Token.newString("CALL"));
        Node callee3 = new Node(Token.newString("GETPROP"));
        Node param3 = new Node(Token.newString("NAME"));
        callee3.replaceChild(0, param3);
        Node left3 = new Node(Token.newString("NAME"));
        left3.replaceChild(0, new Node(Token.newString("goog")));
        Node right3 = new Node(Token.newString("STRING"));
        right3.setString("testMethod");
        callee3.replaceChild(1, right3);
        condition3.replaceChild(0, callee3);
        condition3.addChildToBack(new Node(Token.newString("NAME")));
        Node blindScope3 = new Node(Token.newString("FLOW_SCOPE"));
        Node outcome3 = new Node(Token.newBoolean(true));
        ClosureReverseAbstractInterpreter interpreter3 = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());
        FlowScope result3 = interpreter3.getPreciserScopeKnowingConditionOutcome(condition3, blindScope3, outcome3.getBoolean());
        assertNull(result3.toString(), outcome3.toString()); 
    }
}