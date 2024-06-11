package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TransitionTest {
    
    @Test
    public void testConstructor() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        
        assertEquals("transition", transition.name);
        assertEquals(event, transition.triggerEvent);
        assertEquals(initialState, transition.initialState);
        assertEquals(outcomeState, transition.outcomeState);
    }
    
    @Test
    public void testAddGuardConditionWithIPredicate() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        
        IPredicate guard = new IPredicate();
        transition.addGuardCondition(guard);
        
        assertEquals(guard, transition.guardConditions.get(0));
    }
    
    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        
        IGuardCondition guard = new IGuardCondition();
        transition.addGuardCondition(guard);
        
        assertEquals(guard, transition.guardConditions.get(0));
    }
    
    @Test
    public void testEquals() {
        Event event1 = new Event("event1");
        IState initialState1 = new IState("initialState1");
        IState outcomeState1 = new IState("outcomeState1");
        
        Transition transition1 = new Transition("transition", event1, initialState1, outcomeState1);
        
        Transition transition2 = new Transition("transition", event1, initialState1, outcomeState1);
        
        assertEquals(transition1, transition2);
    }
    
    @Test
    public void testHashCode() {
        Event event1 = new Event("event1");
        IState initialState1 = new IState("initialState1");
        IState outcomeState1 = new IState("outcomeState1");
        
        Transition transition1 = new Transition("transition", event1, initialState1, outcomeState1);
        
        assertEquals(transition1.hashCode(), transition1.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        
        assertEquals(event, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTriggerNoGuardConditions() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        
        IState currentState = initialState;
        IState nextState = transition.trigger(currentState, event);
        
        assertEquals(outcomeState, nextState);
    }
    
    @Test
    public void testTriggerWithGuardConditions() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        IGuardCondition guardCondition1 = new IGuardCondition();
        IGuardCondition guardCondition2 = new IGuardCondition();
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        
        IState currentState = initialState;
        IState nextState = transition.trigger(currentState, event);
        
        assertEquals(outcomeState, nextState);
    }
    
    @Test
    public void testTriggerWithFailedGuardConditions() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        IGuardCondition guardCondition1 = new IGuardCondition();
        IGuardCondition guardCondition2 = new IGuardCondition();
        
        guardCondition1.setEvaluateResult(false);
        guardCondition2.setEvaluateResult(false);
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        
        IState currentState = initialState;
        IState nextState = transition.trigger(currentState, event);
        
        assertEquals(initialState, nextState);
    }
    
    @Test
    public void testWillTriggerWithGuardConditions() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        IGuardCondition guardCondition1 = new IGuardCondition();
        IGuardCondition guardCondition2 = new IGuardCondition();
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        
        IState currentState = initialState;
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        assertTrue(willTrigger);
    }
    
    @Test
    public void testWillTriggerWithFailedGuardConditions() {
        Event event = new Event("event");
        IState initialState = new IState("initialState");
        IState outcomeState = new IState("outcomeState");
        
        IGuardCondition guardCondition1 = new IGuardCondition();
        IGuardCondition guardCondition2 = new IGuardCondition();
        
        guardCondition1.setEvaluateResult(false);
        guardCondition2.setEvaluateResult(false);
        
        Transition transition = new Transition("transition", event, initialState, outcomeState);
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        
        IState currentState = initialState;
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        assertFalse(willTrigger);
    }
}