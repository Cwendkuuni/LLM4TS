package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

public class TransitionTest {
    @Test
    public void testEquals() {
        Transition t1 = new Transition("t1", new Event(), new State(), new State());
        Transition t2 = new Transition("t1", new Event(), new State(), new State());
        Transition t3 = new Transition("t2", new Event(), new State(), new State());
        
        assertTrue(t1.equals(t2));
        assertFalse(t1.equals(t3));
    }
    
    @Test
    public void testHashCode() {
        Transition t1 = new Transition("t1", new Event(), new State(), new State());
        Transition t2 = new Transition("t1", new Event(), new State(), new State());
        
        assertEquals(t1.hashCode(), t2.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new State();
        Transition t = new Transition("t1", new Event(), initialState, new State());
        
        assertEquals(initialState, t.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new State();
        Transition t = new Transition("t1", new Event(), new State(), outcomeState);
        
        assertEquals(outcomeState, t.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition t = new Transition("t1", triggerEvent, new State(), new State());
        
        assertEquals(triggerEvent, t.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition t = new Transition("t1", new Event(), new State(), new State());
        
        assertNotNull(t.toString());
    }
    
    @Test
    public void testTrigger() {
        IState initialState = new State();
        IState outcomeState = new State();
        Event triggerEvent = new Event();
        Transition t = new Transition("t1", triggerEvent, initialState, outcomeState);
        
        // Test when initial state and trigger event match
        IState pCurrentState = initialState;
        Event pEvent = triggerEvent;
        
        assertEquals(outcomeState, t.trigger(pCurrentState, pEvent));
        
        // Test when initial state and trigger event doesn't match
        pCurrentState = new State();
        pEvent = new Event();
        
        assertEquals(pCurrentState, t.trigger(pCurrentState, pEvent));
    }
    
    @Test
    public void testWillTrigger() {
        IState initialState = new State();
        Event triggerEvent = new Event();
        Transition t = new Transition("t1", triggerEvent, initialState, new State());
        
        // Test when initial state and trigger event match
        IState pCurrentState = initialState;
        Event pEvent = triggerEvent;
        
        assertTrue(t.willTrigger(pCurrentState, pEvent));
        
        // Test when initial state and trigger event doesn't match
        pCurrentState = new State();
        pEvent = new Event();
        
        assertFalse(t.willTrigger(pCurrentState, pEvent));
    }
}