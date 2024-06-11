package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void setup() {
        initialState = new State("initial");
        outcomeState = new State("outcome");
        triggerEvent = new Event("event");
        transition = new Transition("transition", triggerEvent, initialState, outcomeState);
    }
    
    @Test
    public void testAddGuardConditionWithIPredicate() {
        IPredicate guard = (event) -> event.equals(triggerEvent);
        transition.addGuardCondition(guard);
        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertTrue(guardConditions.contains(guard));
    }
    
    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        IGuardCondition guard = (event) -> event.equals(triggerEvent);
        transition.addGuardCondition(guard);
        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertTrue(guardConditions.contains(guard));
    }
    
    @Test
    public void testEquals() {
        Transition sameTransition = new Transition("transition", triggerEvent, initialState, outcomeState);
        Transition differentTransition = new Transition("different", triggerEvent, initialState, outcomeState);
        
        assertTrue(transition.equals(sameTransition));
        assertFalse(transition.equals(differentTransition));
    }
    
    @Test
    public void testHashCode() {
        Transition sameTransition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
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
    public void testTriggerWithoutGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        IState expectedState = outcomeState;
        
        IState result = transition.trigger(currentState, event);
        
        assertEquals(expectedState, result);
    }
    
    @Test
    public void testTriggerWithTrueGuardConditions() {
        IGuardCondition guard = (event) -> true;
        transition.addGuardCondition(guard);
        
        IState currentState = initialState;
        Event event = triggerEvent;
        IState expectedState = outcomeState;
        
        IState result = transition.trigger(currentState, event);
        
        assertEquals(expectedState, result);
    }
    
    @Test
    public void testTriggerWithFalseGuardConditions() {
        IGuardCondition guard = (event) -> false;
        transition.addGuardCondition(guard);
        
        IState currentState = initialState;
        Event event = triggerEvent;
        IState expectedState = initialState;
        
        IState result = transition.trigger(currentState, event);
        
        assertEquals(expectedState, result);
    }
    
    @Test
    public void testWillTriggerWithTrueGuardConditions() {
        IGuardCondition guard = (event) -> true;
        transition.addGuardCondition(guard);
        
        IState currentState = initialState;
        Event event = triggerEvent;
        boolean expectedValue = true;
        
        boolean result = transition.willTrigger(currentState, event);
        
        assertEquals(expectedValue, result);
    }
    
    @Test
    public void testWillTriggerWithFalseGuardConditions() {
        IGuardCondition guard = (event) -> false;
        transition.addGuardCondition(guard);
        
        IState currentState = initialState;
        Event event = triggerEvent;
        boolean expectedValue = false;
        
        boolean result = transition.willTrigger(currentState, event);
        
        assertEquals(expectedValue, result);
    }
    
    @Test
    public void testToString() {
        String expectedString = "Transition[name=transition, initialState=initial, outcomeState=outcome, triggerEvent=event, guardConditions=[]]";
        
        String result = transition.toString();
        
        assertEquals(expectedString, result);
    }
    
}