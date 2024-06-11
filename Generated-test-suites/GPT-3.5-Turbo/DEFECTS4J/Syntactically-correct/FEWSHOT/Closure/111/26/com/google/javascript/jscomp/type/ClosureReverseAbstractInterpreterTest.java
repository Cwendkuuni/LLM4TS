package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    
    private static ClosureReverseAbstractInterpreter closureInterpreter;

    @BeforeClass
    public static void setUp() {
        closureInterpreter = new ClosureReverseAbstractInterpreter(null, null);
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withFunctionIsNull_returnsRestrictedWithoutNull() {
        assertEquals(closureInterpreter.getRestrictedWithoutNull(closureInterpreter.getTypeRegistry().getNativeType("Function")), closureInterpreter.getPreciserScopeKnowingConditionOutcome(null, null, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withArrayIsNull_returnsNull() {
        assertNull(closureInterpreter.getPreciserScopeKnowingConditionOutcome(null, null, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withStringIsNull_returnsRestrictedWithoutUndefined() {
        assertEquals(closureInterpreter.getRestrictedWithoutUndefined(closureInterpreter.getTypeRegistry().getNativeType("String")), closureInterpreter.getPreciserScopeKnowingConditionOutcome(null, null, true));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_withNumberIsBoolean_returnsRestrictedByTypeOfResult() {
        assertEquals(closureInterpreter.getRestrictedByTypeOfResult(closureInterpreter.getTypeRegistry().getNativeType("Number"), "boolean", true), closureInterpreter.getPreciserScopeKnowingConditionOutcome(null, null, true));
    }
}