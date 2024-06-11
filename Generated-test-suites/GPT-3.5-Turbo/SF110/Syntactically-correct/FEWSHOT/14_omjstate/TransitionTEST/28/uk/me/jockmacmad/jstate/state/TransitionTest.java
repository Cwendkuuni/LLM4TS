
package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TransitionTest {
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;

    @Before
    public void setUp() {
        initialState = new State();
        outcomeState = new State();
        triggerEvent = new Event();
        transition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testAddGuardCondition() {
        assertFalse(transition.getGuardConditions().contains(new GuardCondition()));
        transition.addGuardCondition(new GuardCondition());
        assertTrue(transition.getGuardConditions().contains(new GuardCondition()));
    }

    @Test
    public void testEquals() {
        Transition sameTransition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        assertTrue(transition.equals(sameTransition));
    }

    @Test
    public void testHashCode() {
        Transition sameTransition = new Transition("TransitionName", triggerEvent, initialState, outcomeState);
        assertEquals(transition.hashCode(), sameTransition.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        // TODO: Implement test cases
    }

    @Test
    public void testGetInitialState() {
        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        // TODO: Implement test case
    }

    @Test
    public void testTrigger() {
        // TODO: Implement test cases
    }

    @Test
    public void testWillTrigger() {
        // TODO: Implement test cases
    }
}
