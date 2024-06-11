package uk.me.jockmacmad.jstate.state;

import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

public class TransitionTest {
    @Test
    public void testEquals() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition1 = new Transition("transition1", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("transition2", triggerEvent, initialState, outcomeState);
        
        assertEquals(transition1, transition2);
    }
    
    @Test
    public void testHashCode() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        assertEquals(initialState.hashCode() + outcomeState.hashCode() + triggerEvent.hashCode(), transition.hashCode());
    }
    
    @Test
    public void testAddGuardConditionPredicate() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        IPredicate predicate = new Predicate();
        
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(predicate, transition.getGuardConditions().get(0));
    }
    
    @Test
    public void testAddGuardConditionGuardCondition() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        IGuardCondition guardCondition = new GuardCondition();
        
        transition.addGuardCondition(guardCondition);
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(guardCondition, transition.getGuardConditions().get(0));
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        assertEquals("Transition[name=transition, initialState=initialState, outcomeState=outcomeState, triggerEvent=triggerEvent]", transition.toString());
    }
    
    @Test
    public void testTrigger() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        IState currentState = initialState;
        Event event = triggerEvent;
        IState result = transition.trigger(currentState, event);
        
        assertEquals(outcomeState, result);
    }
    
    @Test
    public void testWillTrigger() {
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        Event triggerEvent = new Event("triggerEvent");
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        IState currentState = initialState;
        Event event = triggerEvent;
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        assertTrue(willTrigger);
    }
    
}