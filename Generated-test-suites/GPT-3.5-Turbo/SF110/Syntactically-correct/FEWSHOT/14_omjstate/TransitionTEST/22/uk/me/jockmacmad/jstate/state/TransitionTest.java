package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;

public class TransitionTest {
    @Test
    public void testConstructor() {
        IState initialState = new State();
        IState outcomeState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);

        assertEquals("transition", transition.getName());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testAddGuardConditionWithIPredicate() {
        Transition transition = new Transition("transition", new Event(), new State(), new State());
        IPredicate predicate = new Predicate();

        transition.addGuardCondition(predicate);

        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(predicate, transition.getGuardConditions().get(0));
    }

    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        Transition transition = new Transition("transition", new Event(), new State(), new State());
        IGuardCondition guardCondition = new GuardCondition();

        transition.addGuardCondition(guardCondition);

        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(guardCondition, transition.getGuardConditions().get(0));
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("transition", new Event(), new State(), new State());
        Transition transition2 = new Transition("transition", new Event(), new State(), new State());
        Transition transition3 = new Transition("differentTransition", new Event(), new State(), new State());

        assertTrue(transition1.equals(transition2));
        assertFalse(transition1.equals(transition3));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("transition", new Event(), new State(), new State());
        Transition transition2 = new Transition("transition", new Event(), new State(), new State());

        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition("transition", new Event(), new State(), new State());
        // TODO: Add test cases
    }

    @Test
    public void testTrigger() {
        IState initialState = new State();
        IState outcomeState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);

        IState currentState = new State();

        // TODO: Add test cases

        // Trigger event and guard conditions pass
        IState triggeredState = transition.trigger(currentState, triggerEvent);
        assertEquals(outcomeState, triggeredState);

        // Trigger event and guard conditions fail
        IState nonTriggeredState = transition.trigger(currentState, new Event());
        assertEquals(currentState, nonTriggeredState);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, new State());

        IState currentState = new State();

        // TODO: Add test cases

        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        assertTrue(willTrigger);

        boolean willNotTrigger = transition.willTrigger(currentState, new Event());
        assertFalse(willNotTrigger);
    }
}