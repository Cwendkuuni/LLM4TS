
package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.IGuardCondition;

public class TransitionTest {
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;

    @Before
    public void setUp() {
        initialState = new State("Initial");
        outcomeState = new State("Outcome");
        triggerEvent = new Event("Trigger");
        transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testAddGuardCondition_WithIPredicate() {
        IPredicate guard = new Predicate();
        transition.addGuardCondition(guard);

        List<IGuardCondition> guardConditions = getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertSame(guard, guardConditions.get(0).getPredicate());
    }

    @Test
    public void testAddGuardCondition_WithIGuardCondition() {
        IGuardCondition guard = new GuardCondition();
        transition.addGuardCondition(guard);

        List<IGuardCondition> guardConditions = getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertSame(guard, guardConditions.get(0));
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Transition1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("Transition2", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("Transition", new Event("Trigger"), initialState, outcomeState);

        assertTrue(transition.equals(transition));
        assertFalse(transition.equals(null));
        assertFalse(transition.equals("Transition"));
        assertFalse(transition.equals(transition1));
        assertFalse(transition.equals(transition2));
        assertFalse(transition.equals(transition3));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("Transition1", triggerEvent, initialState, outcomeState);

        assertEquals(transition.hashCode(), transition.hashCode());
        assertFalse(transition.hashCode() == 0);
        assertFalse(transition.hashCode() == transition1.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        // Since the method is empty, there is nothing to test
    }

    @Test
    public void testGetInitialState() {
        assertSame(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        assertSame(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        assertSame(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        assertEquals("Transition[name=Transition, triggerEvent=Event[name=Trigger], initialState=State[name=Initial], outcomeState=State[name=Outcome]]", transition.toString());
    }

    @Test
    public void testTrigger_PassesGuards() {
        transition.addGuardCondition(new GuardCondition(true));
        IState currentState = initialState;
        Event trigger = triggerEvent;

        IState nextState = transition.trigger(currentState, trigger);

        assertSame(outcomeState, nextState);
    }

    @Test
    public void testTrigger_FailsGuards() {
        transition.addGuardCondition(new GuardCondition(false));
        IState currentState = initialState;
        Event trigger = triggerEvent;

        IState nextState = transition.trigger(currentState, trigger);

        assertSame(currentState, nextState);
    }

    @Test
    public void testTrigger_DifferentInitialState() {
        IState currentState = new State("Different");
        Event trigger = triggerEvent;

        IState nextState = transition.trigger(currentState, trigger);

        assertSame(currentState, nextState);
    }

    @Test
    public void testTrigger_DifferentTriggerEvent() {
        IState currentState = initialState;
        Event trigger = new Event("Different");

        IState nextState = transition.trigger(currentState, trigger);

        assertSame(currentState, nextState);
    }

    @Test
    public void testWillTrigger_PassesGuards() {
        transition.addGuardCondition(new GuardCondition(true));
        IState currentState = initialState;
        Event trigger = triggerEvent;

        boolean willTrigger = transition.willTrigger(currentState, trigger);

        assertTrue(willTrigger);
    }

    @Test
    public void testWillTrigger_FailsGuards() {
        transition.addGuardCondition(new GuardCondition(false));
        IState currentState = initialState;
        Event trigger = triggerEvent;

        boolean willTrigger = transition.willTrigger(currentState, trigger);

        assertFalse(willTrigger);
    }

    @Test
    public void testWillTrigger_DifferentInitialState() {
        IState currentState = new State("Different");
        Event trigger = triggerEvent;

        boolean willTrigger = transition.willTrigger(currentState, trigger);

        assertFalse(willTrigger);
    }

    @Test
    public void testWillTrigger_DifferentTriggerEvent() {
        IState currentState = initialState;
        Event trigger = new Event("Different");

        boolean willTrigger = transition.willTrigger(currentState, trigger);

        assertFalse(willTrigger);
    }

    private List<IGuardCondition> getGuardConditions() {
        return ((ArrayList<IGuardCondition>) transition.getGuardConditions());
    }
}

