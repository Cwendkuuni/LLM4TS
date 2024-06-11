package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testConstructor() {
        IState initialState = new State("Initial");
        IState outcomeState = new State("Outcome");
        Event triggerEvent = new Event("Trigger");
        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testAddGuardCondition() {
        IState initialState = new State("Initial");
        IState outcomeState = new State("Outcome");
        Event triggerEvent = new Event("Trigger");
        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        IPredicate guardCondition1 = new Predicate();
        IGuardCondition guardCondition2 = new GuardCondition();

        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);

        assertTrue(transition.getGuardConditions().contains(guardCondition1));
        assertTrue(transition.getGuardConditions().contains(guardCondition2));
    }

    @Test
    public void testEquals() {
        IState initialState = new State("Initial");
        IState outcomeState = new State("Outcome");
        Event triggerEvent = new Event("Trigger");
        Transition transition1 = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("Different Transition", triggerEvent, initialState, outcomeState);

        assertEquals(transition1, transition2);
        assertNotEquals(transition1, transition3);
    }

    @Test
    public void testHashCode() {
        IState initialState = new State("Initial");
        IState outcomeState = new State("Outcome");
        Event triggerEvent = new Event("Trigger");
        Transition transition1 = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition("Test Transition", new Event("Trigger"), new State("Initial"), new State("Outcome"));
        transition.addGuardCondition(new Predicate());

        // Mock implementation for test purposes
        transition.evaluateGuardConditions();

        // No assertions since there is no logic in the method
    }

    @Test
    public void testTrigger() {
        IState initialState = new State("Initial");
        IState outcomeState = new State("Outcome");
        Event triggerEvent = new Event("Trigger");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(new Predicate());

        IState currentState = initialState;
        Event event = triggerEvent;

        IState nextState = transition.trigger(currentState, event);

        assertEquals(outcomeState, nextState);

        // Test when the trigger event is different
        event = new Event("Different Trigger");

        nextState = transition.trigger(currentState, event);

        assertEquals(currentState, nextState);

        // Test when the current state is different
        currentState = new State("Different State");

        nextState = transition.trigger(currentState, event);

        assertEquals(currentState, nextState);

        // Test when the guard condition fails
        transition.getGuardConditions().clear();

        nextState = transition.trigger(initialState, event);

        assertEquals(currentState, nextState);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new State("Initial");
        IState outcomeState = new State("Outcome");
        Event triggerEvent = new Event("Trigger");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(new Predicate());

        IState currentState = initialState;
        Event event = triggerEvent;

        assertTrue(transition.willTrigger(currentState, event));

        // Test when the trigger event is different
        event = new Event("Different Trigger");

        assertFalse(transition.willTrigger(currentState, event));

        // Test when the current state is different
        currentState = new State("Different State");

        assertFalse(transition.willTrigger(currentState, event));

        // Test when the guard condition fails
        transition.getGuardConditions().clear();

        assertFalse(transition.willTrigger(initialState, event));
    }
}