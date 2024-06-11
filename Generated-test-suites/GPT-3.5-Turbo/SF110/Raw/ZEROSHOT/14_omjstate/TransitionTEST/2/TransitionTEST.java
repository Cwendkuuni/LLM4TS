
package uk.me.jockmacmad.jstate.state;

import org.junit.Test;
import static org.junit.Assert.*;

import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {
    
    // Define test objects
    
    private final IState initialState = new State("Initial");
    private final IState outcomeState = new State("Outcome");
    private final Event triggerEvent = new Event("Trigger");
    
    private final Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
    
    @Test
    public void testAddGuardConditionWithPredicate() {
        IPredicate guard = new PredicateTrue();
        
        transition.addGuardCondition(guard);
        
        assertTrue(transition.getGuardConditions().contains(guard));
    }
    
    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        IGuardCondition guard = new GuardConditionTrue();
        
        transition.addGuardCondition(guard);
        
        assertTrue(transition.getGuardConditions().contains(guard));
    }
    
    @Test
    public void testEquals() {
        Transition equalTransition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        
        assertTrue(transition.equals(equalTransition));
    }
    
    @Test
    public void testHashCode() {
        Transition equalTransition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        
        assertEquals(transition.hashCode(), equalTransition.hashCode());
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
        String expected = "Transition[name=Test Transition, initialState=Initial, outcomeState=Outcome, triggerEvent=Trigger, guardConditions=[]]";
        assertEquals(expected, transition.toString());
    }
    
    @Test
    public void testTriggerWithPassingGuards() {
        transition.addGuardCondition(new PredicateTrue());
        
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState expected = outcomeState;
        IState actual = transition.trigger(currentState, event);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTriggerWithFailingGuards() {
        transition.addGuardCondition(new PredicateFalse());
        
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState expected = currentState;
        IState actual = transition.trigger(currentState, event);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testWillTriggerWithPassingGuards() {
        transition.addGuardCondition(new PredicateTrue());
        
        IState currentState = initialState;
        Event event = triggerEvent;
        
        assertTrue(transition.willTrigger(currentState, event));
    }
    
    @Test
    public void testWillTriggerWithFailingGuards() {
        transition.addGuardCondition(new PredicateFalse());
        
        IState currentState = initialState;
        Event event = triggerEvent;
        
        assertFalse(transition.willTrigger(currentState, event));
    }
}