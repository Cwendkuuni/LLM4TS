package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import uk.me.jockmacmad.jstate.predicate.IPredicate;
import uk.me.jockmacmad.jstate.predicate.IGuardCondition;

public class TransitionTest {
    
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    private IPredicate guardCondition;
    
    @Before
    public void setup() {
        initialState = new State();
        outcomeState = new State();
        triggerEvent = new Event("Event");
        transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        guardCondition = new GuardCondition();
        transition.addGuardCondition(guardCondition);
    }
    
    @Test
    public void testEquals() {
        Transition sameTransition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        assertTrue(transition.equals(sameTransition));
        
        Transition differentTransition = new Transition("Different Transition", triggerEvent, initialState, outcomeState);
        assertFalse(transition.equals(differentTransition));
    }
    
    @Test
    public void testHashCode() {
        Transition sameTransition = new Transition("Transition", triggerEvent, initialState, outcomeState);
        assertEquals(transition.hashCode(), sameTransition.hashCode());
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
    public void testAddGuardCondition() {
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guardCondition));
        
        IGuardCondition newGuardCondition = new GuardCondition();
        transition.addGuardCondition(newGuardCondition);
        
        assertEquals(2, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guardCondition));
        assertTrue(transition.getGuardConditions().contains(newGuardCondition));
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        transition.evaluateGuardConditions();
        // Add assertions here based on expected behavior of evaluateGuardConditions method
    }
    
    @Test
    public void testTrigger_PassesGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState expectedState = outcomeState;
        IState actualState = transition.trigger(currentState, event);
        
        assertEquals(expectedState, actualState);
    }
    
    @Test
    public void testTrigger_FailsGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        // Modify guardCondition to return false
        guardCondition.setCondition(false);
        
        IState expectedState = initialState;
        IState actualState = transition.trigger(currentState, event);
        
        assertEquals(expectedState, actualState);
    }
    
    @Test
    public void testWillTrigger_PassesGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        assertTrue(transition.willTrigger(currentState, event));
    }
    
    @Test
    public void testWillTrigger_FailsGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        // Modify guardCondition to return false
        guardCondition.setCondition(false);
        
        assertFalse(transition.willTrigger(currentState, event));
    }
    
    @Test
    public void testToString() {
        // Add assertions here based on expected string representation of the Transition object
    }
}