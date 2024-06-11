package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(new CodingConvention(), new JSTypeRegistry());

        // Test for isDef()
        Node isDefCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isDef")),
                new Node(Token.NAME, "value"));
        Node param1 = isDefCall.getLastChild();
        Node condition1 = new Node(Token.LE,
                param1,
                new Node(Token.NUMBER, 5));
        FlowScope blindScope = new FlowScope();
        FlowScope scope1 = interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope, true);
        FlowScope scope2 = interpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope, false);
        assertEquals(param1, scope1.getDeclaredType(param1));
        assertNull(scope2.getDeclaredType(param1));

        // Test for isNull()
        Node isNullCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isNull")),
                new Node(Token.NAME, "value"));
        Node param2 = isNullCall.getLastChild();
        Node condition2 = new Node(Token.GE,
                param2,
                new Node(Token.NUMBER, 5));
        FlowScope scope3 = interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope, true);
        FlowScope scope4 = interpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope, false);
        assertEquals(param2, scope4.getDeclaredType(param2));
        assertNull(scope3.getDeclaredType(param2));

        // Test for isDefAndNotNull()
        Node isDefAndNotNullCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isDefAndNotNull")),
                new Node(Token.NAME, "value"));
        Node param3 = isDefAndNotNullCall.getLastChild();
        Node condition3 = new Node(Token.GT,
                param3,
                new Node(Token.NUMBER, 5));
        FlowScope scope5 = interpreter.getPreciserScopeKnowingConditionOutcome(condition3, blindScope, true);
        FlowScope scope6 = interpreter.getPreciserScopeKnowingConditionOutcome(condition3, blindScope, false);
        assertEquals(param3, scope5.getDeclaredType(param3));
        assertNull(scope6.getDeclaredType(param3));

        // Test for isString()
        Node isStringCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isString")),
                new Node(Token.NAME, "value"));
        Node param4 = isStringCall.getLastChild();
        Node condition4 = new Node(Token.LT,
                param4,
                new Node(Token.NUMBER, 5));
        FlowScope scope7 = interpreter.getPreciserScopeKnowingConditionOutcome(condition4, blindScope, true);
        FlowScope scope8 = interpreter.getPreciserScopeKnowingConditionOutcome(condition4, blindScope, false);
        assertEquals(param4, scope7.getDeclaredType(param4));
        assertNull(scope8.getDeclaredType(param4));

        // Test for isBoolean()
        Node isBooleanCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isBoolean")),
                new Node(Token.NAME, "value"));
        Node param5 = isBooleanCall.getLastChild();
        Node condition5 = new Node(Token.GT,
                param5,
                new Node(Token.NUMBER, 5));
        FlowScope scope9 = interpreter.getPreciserScopeKnowingConditionOutcome(condition5, blindScope, true);
        FlowScope scope10 = interpreter.getPreciserScopeKnowingConditionOutcome(condition5, blindScope, false);
        assertEquals(param5, scope9.getDeclaredType(param5));
        assertNull(scope10.getDeclaredType(param5));

        // Test for isNumber()
        Node isNumberCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isNumber")),
                new Node(Token.NAME, "value"));
        Node param6 = isNumberCall.getLastChild();
        Node condition6 = new Node(Token.LT,
                param6,
                new Node(Token.NUMBER, 5));
        FlowScope scope11 = interpreter.getPreciserScopeKnowingConditionOutcome(condition6, blindScope, true);
        FlowScope scope12 = interpreter.getPreciserScopeKnowingConditionOutcome(condition6, blindScope, false);
        assertEquals(param6, scope11.getDeclaredType(param6));
        assertNull(scope12.getDeclaredType(param6));

        // Test for isFunction()
        Node isFunctionCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isFunction")),
                new Node(Token.NAME, "value"));
        Node param7 = isFunctionCall.getLastChild();
        Node condition7 = new Node(Token.GT,
                param7,
                new Node(Token.NUMBER, 5));
        FlowScope scope13 = interpreter.getPreciserScopeKnowingConditionOutcome(condition7, blindScope, true);
        FlowScope scope14 = interpreter.getPreciserScopeKnowingConditionOutcome(condition7, blindScope, false);
        assertEquals(param7, scope13.getDeclaredType(param7));
        assertNull(scope14.getDeclaredType(param7));

        // Test for isArray()
        Node isArrayCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isArray")),
                new Node(Token.NAME, "value"));
        Node param8 = isArrayCall.getLastChild();
        Node condition8 = new Node(Token.LE,
                param8,
                new Node(Token.NUMBER, 5));
        FlowScope scope15 = interpreter.getPreciserScopeKnowingConditionOutcome(condition8, blindScope, true);
        FlowScope scope16 = interpreter.getPreciserScopeKnowingConditionOutcome(condition8, blindScope, false);
        assertEquals(param8, scope15.getDeclaredType(param8));
        assertNull(scope16.getDeclaredType(param8));

        // Test for isObject()
        Node isObjectCall = new Node(Token.CALL,
                new Node(Token.GETPROP,
                        new Node(Token.NAME, "goog"),
                        new Node(Token.STRING, "isObject")),
                new Node(Token.NAME, "value"));
        Node param9 = isObjectCall.getLastChild();
        Node condition9 = new Node(Token.GE,
                param9,
                new Node(Token.NUMBER, 5));
        FlowScope scope17 = interpreter.getPreciserScopeKnowingConditionOutcome(condition9, blindScope, true);
        FlowScope scope18 = interpreter.getPreciserScopeKnowingConditionOutcome(condition9, blindScope, false);
        assertEquals(param9, scope17.getDeclaredType(param9));
        assertNull(scope18.getDeclaredType(param9));
    }
}