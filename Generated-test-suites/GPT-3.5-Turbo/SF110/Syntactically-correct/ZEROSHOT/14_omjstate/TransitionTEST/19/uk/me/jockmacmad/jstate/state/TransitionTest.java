import static org.junit.Assert.*;
import org.junit.*;

public class TransitionTest {
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("transition1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("transition2", new Event(), new IState(), new IState());
        
        assertFalse(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("transition1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("transition2", new Event(), new IState(), new IState());
        
        assertNotEquals(transition1.hashCode(), transition2.hashCode());
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        // TODO: Implement test
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition transition = new Transition("transition", new Event(), initialState, new IState());
        
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition transition = new Transition("transition", new Event(), new IState(), outcomeState);
        
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, new IState(), new IState());
        
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("transition", new Event(), new IState(), new IState());
        
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        IState currentState = initialState;
        Event event = triggerEvent;
        IState actualOutcomeState = transition.trigger(currentState, event);
        
        assertEquals(outcomeState, actualOutcomeState);
    }
    
    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, new IState());
        
        IState currentState = initialState;
        Event event = triggerEvent;
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        assertTrue(willTrigger);
    }
}