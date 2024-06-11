package uk.me.jockmacmad.jstate.state;

import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testConstructorAndGetters() {
        // Create objects
        Event event = new Event("event");
        IState initialState = new State("initial");
        IState outcomeState = new State("outcome");

        // Create Transition object
        Transition transition = new Transition("transition", event, initialState, outcomeState);

        // Check getters
        assertEquals("transition", transition.getName());
        assertEquals(event, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testAddGuardCondition() {
        // Create objects
        Transition transition = new Transition("transition", null, null, null);
        IPredicate predicate = new Predicate();

        // Add guard condition
        transition.addGuardCondition(predicate);

        // Check if condition was added
        assertTrue(transition.getGuardConditions().contains(predicate));
    }

    @Test
    public void testEquals() {
        // Create objects
        Transition transition1 = new Transition("transition", null, null, null);
        Transition transition2 = new Transition("transition", null, null, null);
        Transition transition3 = new Transition("transition2", null, null, null);

        // Check equality
        assertTrue(transition1.equals(transition2));
        assertFalse(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        // Create objects
        Transition transition1 = new Transition("transition", null, null, null);
        Transition transition2 = new Transition("transition", null, null, null);
        Transition transition3 = new Transition("transition2", null, null, null);

        // Check hash code
        assertEquals(transition1.hashCode(), transition2.hashCode());
        assertNotEquals(transition1.hashCode(), transition3.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        // Test method without implementation
        // Since the method is empty, there is nothing to test
    }

    @Test
    public void testTrigger() {
        // Create objects
        IState initialState = new State("initial");
        IState outcomeState = new State("outcome");
        Event event = new Event("event");
        Transition transition = new Transition("transition", event, initialState, outcomeState);

        // Test when initial state and trigger event match
        assertEquals(outcomeState, transition.trigger(initialState, event));

        // Test when initial state and trigger event do not match
        assertNull(transition.trigger(outcomeState, event));

        // Test when guard conditions are not met
        IPredicate predicate = new Predicate();
        transition.addGuardCondition(predicate);
        assertNull(transition.trigger(initialState, event));

        // Test when guard conditions are met
        predicate.setEvaluateResult(true);
        assertEquals(outcomeState, transition.trigger(initialState, event));
    }

    @Test
    public void testWillTrigger() {
        // Create objects
        IState initialState = new State("initial");
        IState outcomeState = new State("outcome");
        Event event = new Event("event");
        Transition transition = new Transition("transition", event, initialState, outcomeState);

        // Test when initial state and trigger event match
        assertTrue(transition.willTrigger(initialState, event));

        // Test when initial state and trigger event do not match
        assertFalse(transition.willTrigger(outcomeState, event));

        // Test when guard conditions are not met
        IPredicate predicate = new Predicate();
        transition.addGuardCondition(predicate);
        assertFalse(transition.willTrigger(initialState, event));

        // Test when guard conditions are met
        predicate.setEvaluateResult(true);
        assertTrue(transition.willTrigger(initialState, event));
    }
}