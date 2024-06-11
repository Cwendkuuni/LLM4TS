package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        Transition t1 = new Transition("Transition1", new Event("Event1"), new State("InitialState"), new State("OutcomeState"));
        Transition t2 = new Transition("Transition2", new Event("Event2"), new State("InitialState"), new State("OutcomeState"));
        assertFalse(t1.equals(t2));
    }

    @Test
    public void testHashCode() {
        Transition t1 = new Transition("Transition1", new Event("Event1"), new State("InitialState"), new State("OutcomeState"));
        Transition t2 = new Transition("Transition2", new Event("Event2"), new State("InitialState"), new State("OutcomeState"));
        assertFalse(t1.hashCode() == t2.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        Transition t = new Transition("Transition", new Event("Event"), new State("InitialState"), new State("OutcomeState"));
        t.addGuardCondition(new GuardCondition());
        t.evaluateGuardConditions();
    }

    @Test
    public void testGetInitialState() {
        State initialState = new State("InitialState");
        Transition t = new Transition("Transition", new Event("Event"), initialState, new State("OutcomeState"));
        assertEquals(initialState, t.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        State outcomeState = new State("OutcomeState");
        Transition t = new Transition("Transition", new Event("Event"), new State("InitialState"), outcomeState);
        assertEquals(outcomeState, t.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event("Event");
        Transition t = new Transition("Transition", triggerEvent, new State("InitialState"), new State("OutcomeState"));
        assertEquals(triggerEvent, t.getTriggerEvent());
    }

    @Test
    public void testToString() {
        Transition t = new Transition("Transition", new Event("Event"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("Transition[name=Transition, initialState=InitialState, outcomeState=OutcomeState, triggerEvent=Event]", t.toString());
    }

    @Test
    public void testTrigger() {
        State initialState = new State("InitialState");
        State outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("Event");
        Transition t = new Transition("Transition", triggerEvent, initialState, outcomeState);
        assertEquals(initialState, t.trigger(initialState, new Event("WrongEvent")));
        assertEquals(outcomeState, t.trigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger() {
        State initialState = new State("InitialState");
        Event triggerEvent = new Event("Event");
        Transition t = new Transition("Transition", triggerEvent, initialState, new State("OutcomeState"));
        assertFalse(t.willTrigger(initialState, new Event("WrongEvent")));
        assertTrue(t.willTrigger(initialState, triggerEvent));
    }
}