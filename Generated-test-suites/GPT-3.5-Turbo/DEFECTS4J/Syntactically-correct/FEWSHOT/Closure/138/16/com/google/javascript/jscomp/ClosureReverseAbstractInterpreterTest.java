package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.javascript.rhino.Node;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null);
        Node condition = new Node(null, null);
        boolean outcome = true;
        FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        assertNull(scope);
    }

    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        Node parameter = new Node(null, null);
        JSType type = new JSType(null, null);
        FlowScope blindScope = new FlowScope(null, null);
        Function<TypeRestriction, JSType> restriction = null;
        boolean outcome = true;
        FlowScope scope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertNull(scope);
    }

    @Test
    public void testTypeRestriction() {
        JSType type = new JSType(null, null);
        boolean outcome = true;
        TypeRestriction restriction = new TypeRestriction(type, outcome);
        assertEquals(type, restriction.type);
        assertEquals(outcome, restriction.outcome);
    }
}