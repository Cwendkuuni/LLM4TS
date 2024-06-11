package uk.me.jockmacmad.jstate.state;

import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        Transition t1 = new Transition("t1", new Event(), new IState(), new IState());
        Transition t2 = new Transition("t1", new Event(), new IState(), new IState());
        assertEquals(t1, t2);
    }

    @Test
    public void testAddGuardCondition() {
        Transition t = new Transition("t", new Event(), new IState(), new IState());
        IPredicate pGuard = new IPredicate();
        t.addGuardCondition(pGuard);
        assertTrue(t.getGuardConditions().contains(pGuard));
    }

    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition t = new Transition("t", new Event(), initialState, new IState());
        assertEquals(initialState, t.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition t = new Transition("t", new Event(), new IState(), outcomeState);
        assertEquals(outcomeState, t.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition t = new Transition("t", triggerEvent, new IState(), new IState());
        assertEquals(triggerEvent, t.getTriggerEvent());
    }

    @Test
    public void testToString() {
        Transition t = new Transition("t", new Event(), new IState(), new IState());
        assertEquals(t.toString(), t.toString());
    }

    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition t = new Transition("t", triggerEvent, initialState, outcomeState);
        IState currentState = initialState;
        Event event = triggerEvent;
        IState result = t.trigger(currentState, event);
        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition t = new Transition("t", triggerEvent, initialState, new IState());
        IState currentState = initialState;
        Event event = triggerEvent;
        boolean result = t.willTrigger(currentState, event);
        assertTrue(result);
    }
}