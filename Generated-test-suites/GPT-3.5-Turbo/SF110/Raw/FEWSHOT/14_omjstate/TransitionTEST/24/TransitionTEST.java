package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TransitionTest {
    @Test
    public void testConstructor() {
        IState initialState = new State();
        IState outcomeState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        assertEquals("transition", transition.name);
        assertEquals(initialState, transition.initialState);
        assertEquals(outcomeState, transition.outcomeState);
        assertEquals(triggerEvent, transition.triggerEvent);
        assertTrue(transition.guardConditions.isEmpty());
    }
    
    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("transition", new Event(), new State(), new State());
        IPredicate predicate = new Predicate();
        IGuardCondition guardCondition = new GuardCondition();
        
        transition.addGuardCondition(predicate);
        transition.addGuardCondition(guardCondition);
        
        assertEquals(2, transition.guardConditions.size());
        assertTrue(transition.guardConditions.contains(predicate));
        assertTrue(transition.guardConditions.contains(guardCondition));
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("transition", new Event(), new State(), new State());
        Transition transition2 = new Transition("transition", new Event(), new State(), new State());
        Transition transition3 = new Transition("name", new Event(), new State(), new State());
        
        assertTrue(transition1.equals(transition1));
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
        
        // Test case for the evaluateGuardConditions() method
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new State();
        Transition transition = new Transition("transition", new Event(), initialState, new State());
        
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new State();
        Transition transition = new Transition("transition", new Event(), new State(), outcomeState);
        
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, new State(), new State());
        
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("transition", new Event(), new State(), new State());
        
        // Test case for the toString() method
    }
    
    @Test
    public void testTrigger() {
        IState initialState = new State();
        IState outcomeState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        // Test case for the trigger() method
    }
    
    @Test
    public void testWillTrigger() {
        IState initialState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, new State());
        
        // Test case for the willTrigger() method
    }
}