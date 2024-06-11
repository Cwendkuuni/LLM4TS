
package uk.me.jockmacmad.jstate.state;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class TransitionTest {

    @Test
    public void testAddGuardConditionWithIPredicate() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        IPredicate predicate = new AlwaysTruePredicate();
        transition.addGuardCondition(predicate);
        assertTrue(transition.getGuardConditions().contains(predicate));
    }

    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        IGuardCondition guardCondition = new TrueGuardCondition();
        transition.addGuardCondition(guardCondition);
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        Transition transition2 = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals(transition1, transition2);
    }

    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertNotNull(transition.hashCode());
    }

    // Test evaluateGuardConditions method separately from other methods due to its absence of implementation

    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("InitialState", transition.getInitialState().getName());
    }

    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("OutcomeState", transition.getOutcomeState().getName());
    }

    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("TestEvent", transition.getTriggerEvent().getName());
    }

    @Test
    public void testToString() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("Transition@1", transition.toString());
    }

    @Test
    public void testTrigger() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        State currentState = new State("InitialState");
        Event event = new Event("TestEvent");
        assertEquals("OutcomeState", transition.trigger(currentState, event).getName());
    }

    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        State currentState = new State("InitialState");
        Event event = new Event("TestEvent");
        assertTrue(transition.willTrigger(currentState, event));
    }
}

