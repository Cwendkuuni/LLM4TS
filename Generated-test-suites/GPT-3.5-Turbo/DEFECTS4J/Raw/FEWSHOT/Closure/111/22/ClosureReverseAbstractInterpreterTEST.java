import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_array() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_object() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_def() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_null() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_defNotNull() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_string() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_boolean() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_number() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome_function() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);
        FlowScope blindScope = new FlowScope(null, null, null);
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, true));
        assertEquals(someExpectedScope, interpreter.getPreciserScopeKnowingConditionOutcome(someCondition, blindScope, false));
    }
}