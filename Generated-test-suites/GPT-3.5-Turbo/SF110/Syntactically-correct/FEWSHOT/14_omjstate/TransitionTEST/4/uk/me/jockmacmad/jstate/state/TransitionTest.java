package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;
import org.junit.Test;

public class TransitionTest {
    @Test
    public void testConstructor() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        
        assertEquals(name, transition.name);
        assertEquals(event, transition.triggerEvent);
        assertEquals(initialState, transition.initialState);
        assertEquals(outcomeState, transition.outcomeState);
    }

    @Test
    public void testAddGuardConditionWithIPredicate() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        IPredicate predicate = new Predicate();
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        transition.addGuardCondition(predicate);
        
        assertTrue(transition.guardConditions.contains(predicate));
    }
    
    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        IGuardCondition guardCondition = new GuardCondition();
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        transition.addGuardCondition(guardCondition);
        
        assertTrue(transition.guardConditions.contains(guardCondition));
    }
    
    @Test
    public void testEquals() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition1 = new Transition(name, event, initialState, outcomeState);
        Transition transition2 = new Transition(name, event, initialState, outcomeState);
        
        assertTrue(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition1 = new Transition(name, event, initialState, outcomeState);
        Transition transition2 = new Transition(name, event, initialState, outcomeState);
        
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        
        assertEquals(event, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        
        assertEquals(ToStringBuilder.reflectionToString(transition), transition.toString());
    }
    
    @Test
    public void testTrigger() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        
        IState currentState = new State("Initial State");
        Event triggerEvent = new Event("Event");
        
        IState resultState = transition.trigger(currentState, triggerEvent);
        
        assertEquals(outcomeState, resultState);
        
        currentState = new State("Different State");
        triggerEvent = new Event("Different Event");
        
        resultState = transition.trigger(currentState, triggerEvent);
        
        assertEquals(currentState, resultState);
    }
    
    @Test
    public void testWillTrigger() {
        String name = "Transition";
        Event event = new Event("Event");
        IState initialState = new State("Initial State");
        IState outcomeState = new State("Outcome State");
        
        Transition transition = new Transition(name, event, initialState, outcomeState);
        
        IState currentState = new State("Initial State");
        Event triggerEvent = new Event("Event");
        
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertTrue(willTrigger);
        
        currentState = new State("Different State");
        triggerEvent = new Event("Different Event");
        
        willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertFalse(willTrigger);
    }
}