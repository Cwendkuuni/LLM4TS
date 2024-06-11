package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TransitionTest {
    private Transition transition;

    @Before
    public void setup() {
        transition = new Transition("transition1", new Event(), new State(), new State());
    }

    @Test
    public void testAddGuardCondition() {
        IPredicate guard = new Predicate();
        IGuardCondition guardCondition = new GuardCondition();

        transition.addGuardCondition(guard);
        transition.addGuardCondition(guardCondition);

        assertEquals(2, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guard));
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testEquals() {
        Transition transition2 = new Transition("transition1", new Event(), new State(), new State());
        Transition transition3 = new Transition("transition2", new Event(), new State(), new State());

        assertTrue(transition.equals(transition2));
        assertFalse(transition.equals(transition3));
    }

    @Test
    public void testHashCode() {
        Transition transition2 = new Transition("transition1", new Event(), new State(), new State());

        assertEquals(transition.hashCode(), transition2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = transition.getInitialState();

        assertNull(initialState);
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = transition.getOutcomeState();

        assertNull(outcomeState);
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = transition.getTriggerEvent();

        assertNull(triggerEvent);
    }

    @Test
    public void testToString() {
        String toString = transition.toString();

        assertNotNull(toString);
    }

    @Test
    public void testTrigger() {
        IState currentState = new State();
        Event event = new Event();

        IState outcomeState = transition.trigger(currentState, event);

        assertNotNull(outcomeState);
    }

    @Test
    public void testWillTrigger() {
        IState currentState = new State();
        Event event = new Event();

        boolean willTrigger = transition.willTrigger(currentState, event);

        assertFalse(willTrigger);
    }
}