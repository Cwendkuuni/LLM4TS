package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope();
        Node condition = null;
        boolean outcome = false;
        FlowScope result = rai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
        assertNull(result);
    }
    
    @Test
    public void testRestrictParameter() {
        ClosureReverseAbstractInterpreter rai = new ClosureReverseAbstractInterpreter(null, null);
        Node parameter = null;
        JSType type = null;
        FlowScope blindScope = new FlowScope();
        Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
            @Override
            public JSType apply(TypeRestriction p) {
                return null;
            }
        };
        boolean outcome = false;
        FlowScope result = rai.restrictParameter(parameter, type, blindScope, restriction, outcome);
        assertEquals(blindScope, result);
    }
  
}