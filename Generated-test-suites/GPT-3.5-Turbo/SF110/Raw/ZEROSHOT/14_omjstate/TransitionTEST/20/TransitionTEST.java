import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TransitionTest {
    
    @Test
    public void testEquals() {
        Transition t1 = new Transition("name1", new Event(), new IState(), new IState());
        Transition t2 = new Transition("name1", new Event(), new IState(), new IState());
        assertTrue(t1.equals(t2));
    }
    
    @Test
    public void testHashCode() {
        Transition t1 = new Transition("name1", new Event(), new IState(), new IState());
        Transition t2 = new Transition("name1", new Event(), new IState(), new IState());
        assertEquals(t1.hashCode(), t2.hashCode());
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        // TODO: Write test for evaluateGuardConditions() method
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition t = new Transition("name", new Event(), initialState, new IState());
        assertEquals(initialState, t.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition t = new Transition("name", new Event(), new IState(), outcomeState);
        assertEquals(outcomeState, t.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition t = new Transition("name", triggerEvent, new IState(), new IState());
        assertEquals(triggerEvent, t.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition t = new Transition("name", new Event(), new IState(), new IState());
        String expected = "Transition[name=name, initialState=uk.me.jockmacmad.jstate.state.IState@hashcode, outcomeState=uk.me.jockmacmad.jstate.state.IState@hashcode, triggerEvent=Event@hashcode]";
        assertEquals(expected, t.toString());
    }
    
    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition t = new Transition("name", triggerEvent, initialState, outcomeState);
        
        // Test when initial state and trigger event matches
        IState pCurrentState = initialState;
        Event pEvent = triggerEvent;
        IState expected = outcomeState;
        IState actual = t.trigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
        
        // Test when initial state does not match
        pCurrentState = new IState();
        expected = pCurrentState;
        actual = t.trigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
        
        // Test when trigger event does not match
        pCurrentState = initialState;
        pEvent = new Event();
        expected = pCurrentState;
        actual = t.trigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
        
        // Test with guard conditions
        IGuardCondition guardCondition = new IGuardCondition();
        t.addGuardCondition(guardCondition);
        
        // Test when guard conditions pass
        guardCondition.setEvaluate(true);
        pCurrentState = initialState;
        pEvent = triggerEvent;
        expected = outcomeState;
        actual = t.trigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
        
        // Test when guard conditions fail
        guardCondition.setEvaluate(false);
        expected = pCurrentState;
        actual = t.trigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition t = new Transition("name", triggerEvent, initialState, new IState());
        
        // Test when initial state and trigger event matches
        IState pCurrentState = initialState;
        Event pEvent = triggerEvent;
        boolean expected = true;
        boolean actual = t.willTrigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
        
        // Test when initial state does not match
        pCurrentState = new IState();
        expected = false;
        actual = t.willTrigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
        
        // Test when trigger event does not match
        pCurrentState = initialState;
        pEvent = new Event();
        expected = false;
        actual = t.willTrigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
        
        // Test with guard conditions
        IGuardCondition guardCondition = new IGuardCondition();
        t.addGuardCondition(guardCondition);
        
        // Test when guard conditions pass
        guardCondition.setEvaluate(true);
        pCurrentState = initialState;
        pEvent = triggerEvent;
        expected = true;
        actual = t.willTrigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
        
        // Test when guard conditions fail
        guardCondition.setEvaluate(false);
        expected = false;
        actual = t.willTrigger(pCurrentState, pEvent);
        assertEquals(expected, actual);
    }
}