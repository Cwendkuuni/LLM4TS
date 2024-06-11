
package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.IGuardCondition;
import uk.me.jockmacmad.jstate.predicate.IPredicate;
import uk.me.jockmacmad.jstate.state.Event;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.Transition;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        IState initialState = new State();
        IState outcomeState = new State();
        Event triggerEvent = new Event();
        transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testAddGuardCondition_IPredicate() {
        IPredicate guard = new GuardCondition();
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guard));
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        IGuardCondition guard = new GuardCondition();
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guard));
    }

    @Test
    public void testEquals() {
        Transition otherTransition = new Transition("Test Transition", new Event(), new State(),
                new State());
        assertTrue(transition.equals(otherTransition));
    }

    @Test
    public void testHashCode() {
        Transition otherTransition = new Transition("Test Transition", new Event(), new State(),
                new State());
        assertEquals(transition.hashCode(), otherTransition.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        transition.evaluateGuardConditions();
        // add your assertions here
    }

    @Test
    public void testGetInitialState() {
        IState initialState = transition.getInitialState();
        assertNotNull(initialState);
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = transition.getOutcomeState();
        assertNotNull(outcomeState);
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = transition.getTriggerEvent();
        assertNotNull(triggerEvent);
    }

    @Test
    public void testToString() {
        String stringRepresentation = transition.toString();
        // assert the expected string representation here
    }

    @Test
    public void testTrigger() {
        IState currentState = new State();
        Event triggerEvent = new Event();
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        assertEquals(transition.getOutcomeState(), outcomeState);
    }

    @Test
    public void testWillTrigger() {
        IState currentState = new State();
        Event triggerEvent = new Event();
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        assertFalse(willTrigger);
    }
}
