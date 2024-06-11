package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        // Test case 1
        Node condition1 = new Node();
        FlowScope blindScope1 = new FlowScope();
        boolean outcome1 = true;
        FlowScope expected1 = new FlowScope();
        assertEquals(expected1, ClosureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition1, blindScope1, outcome1));

        // Test case 2
        Node condition2 = new Node();
        FlowScope blindScope2 = new FlowScope();
        boolean outcome2 = false;
        FlowScope expected2 = blindScope2;
        assertEquals(expected2, ClosureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition2, blindScope2, outcome2));
    }

    @Test
    public void testRestrictParameter() {
        // Test case 1
        Node parameter1 = new Node();
        JSType type1 = new JSType();
        FlowScope blindScope1 = new FlowScope();
        Function<TypeRestriction, JSType> restriction1 = new Function<TypeRestriction, JSType>();
        boolean outcome1 = true;
        FlowScope expected1 = new FlowScope();
        assertEquals(expected1, ClosureReverseAbstractInterpreter.restrictParameter(parameter1, type1, blindScope1, restriction1, outcome1));

        // Test case 2
        Node parameter2 = new Node();
        JSType type2 = new JSType();
        FlowScope blindScope2 = new FlowScope();
        Function<TypeRestriction, JSType> restriction2 = new Function<TypeRestriction, JSType>();
        boolean outcome2 = false;
        FlowScope expected2 = blindScope2;
        assertEquals(expected2, ClosureReverseAbstractInterpreter.restrictParameter(parameter2, type2, blindScope2, restriction2, outcome2));
    }
}