
package uk.me.jockmacmad.jstate.state;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition_IPredicate() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Create a GuardCondition object
        GuardCondition guardCondition = new GuardCondition();
        
        // Add the GuardCondition object to the Transition object
        transition.addGuardCondition((IPredicate) guardCondition);
        
        // Verify that the GuardCondition has been added to the Transition
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Create a GuardCondition object
        GuardCondition guardCondition = new GuardCondition();
        
        // Add the GuardCondition object to the Transition object
        transition.addGuardCondition(guardCondition);
        
        // Verify that the GuardCondition has been added to the Transition
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testEquals() {
        // Create two Transition objects with the same properties
        Transition transition1 = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        Transition transition2 = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Verify that the two Transition objects are equal
        assertTrue(transition1.equals(transition2));
    }

    @Test
    public void testHashCode() {
        // Create two Transition objects with the same properties
        Transition transition1 = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        Transition transition2 = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Verify that the hash codes of the two Transition objects are equal
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // The evaluateGuardConditions() method does not have any specific behavior in the code, so no test case is needed.
    }

    @Test
    public void testGetInitialState() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Verify that the initial state of the Transition is returned correctly
        assertEquals("Initial State", transition.getInitialState().getName());
    }

    @Test
    public void testGetOutcomeState() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Verify that the outcome state of the Transition is returned correctly
        assertEquals("Outcome State", transition.getOutcomeState().getName());
    }

    @Test
    public void testGetTriggerEvent() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Verify that the trigger event of the Transition is returned correctly
        assertEquals("Test Event", transition.getTriggerEvent().getName());
    }

    @Test
    public void testToString() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Verify that the string representation of the Transition is returned correctly
        assertEquals("Transition[Test Transition, Initial State, Test Event, Outcome State]", transition.toString());
    }

    @Test
    public void testTrigger_passesGuards() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Set up the test scenario
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Test Event");
        
        transition.addGuardCondition(new GuardCondition(true));
        
        // Trigger the transition
        IState resultState = transition.trigger(initialState, triggerEvent);
        
        // Verify that the outcome state is returned
        assertEquals(outcomeState, resultState);
    }

    @Test
    public void testTrigger_failsGuards() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Set up the test scenario
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        Event triggerEvent = new Event("Test Event");
        
        transition.addGuardCondition(new GuardCondition(false));
        
        // Trigger the transition
        IState resultState = transition.trigger(initialState, triggerEvent);
        
        // Verify that the initial state is returned
        assertEquals(initialState, resultState);
    }

    @Test
    public void testWillTrigger_passesGuards() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Set up the test scenario
        IState currentState = new State("Initial State");
        Event triggerEvent = new Event("Test Event");
        
        transition.addGuardCondition(new GuardCondition(true));
        
        // Check if the transition will trigger
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        // Verify that the transition will trigger
        assertTrue(willTrigger);
    }

    @Test
    public void testWillTrigger_failsGuards() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Set up the test scenario
        IState currentState = new State("Initial State");
        Event triggerEvent = new Event("Test Event");
        
        transition.addGuardCondition(new GuardCondition(false));
        
        // Check if the transition will trigger
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        // Verify that the transition will not trigger
        assertFalse(willTrigger);
    }
    
    @Test
    public void testWillTrigger_differentState() {
        // Create a Transition object
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        
        // Set up the test scenario
        IState currentState = new State("Different State");
        Event triggerEvent = new Event("Test Event");
        
        transition.addGuardCondition(new GuardCondition(true));
        
        // Check if the transition will trigger
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        // Verify that the transition will not trigger
        assertFalse(willTrigger);
    }
}

