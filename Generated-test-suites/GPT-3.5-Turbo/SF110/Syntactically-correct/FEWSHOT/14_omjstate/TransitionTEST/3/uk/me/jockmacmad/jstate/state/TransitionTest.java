package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;

public class TransitionTest {
    
    @Test
    public void testConstructor() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        
        assertEquals("transition1", transition.name);
        assertNotNull(transition.initialState);
        assertNotNull(transition.outcomeState);
        assertNotNull(transition.guardConditions);
        assertNotNull(transition.triggerEvent);
    }
    
    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        IPredicate guardCondition = new Predicate();
        
        transition.addGuardCondition(guardCondition);
        
        assertEquals(1, transition.guardConditions.size());
        assertSame(guardCondition, transition.guardConditions.get(0));
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("transition1", new Event(), new State(), new State());
        Transition transition2 = new Transition("transition1", new Event(), new State(), new State());
        Transition transition3 = new Transition("transition2", new Event(), new State(), new State());
        
        assertTrue(transition1.equals(transition2));
        assertFalse(transition1.equals(transition3));
    }
    
    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("transition1", new Event(), new State(), new State());
        Transition transition2 = new Transition("transition1", new Event(), new State(), new State());
        
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        
        // TODO: Add test cases for evaluating guard conditions
    }
    
    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        
        assertNotNull(transition.getInitialState());
        assertSame(transition.initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        
        assertNotNull(transition.getOutcomeState());
        assertSame(transition.outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        
        assertNotNull(transition.getTriggerEvent());
        assertSame(transition.triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTrigger() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        IState currentState = new State();
        Event event = new Event();
        
        // TODO: Add test cases for triggering transition
    }
    
    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("transition1", new Event(), new State(), new State());
        IState currentState = new State();
        Event event = new Event();
        
        // TODO: Add test cases for checking if transition will trigger
    }
}