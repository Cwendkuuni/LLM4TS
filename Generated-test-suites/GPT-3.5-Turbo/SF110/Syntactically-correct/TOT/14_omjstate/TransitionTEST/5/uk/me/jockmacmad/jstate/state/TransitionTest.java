import org.junit.*;
import static org.junit.Assert.*;

public class TransitionTest {
    
    // Test case for constructor
    @Test
    public void testConstructor() {
        String name = "test";
        Event triggerEvent = new Event();
        IState initialState = new State();
        IState outcomeState = new State();
        
        Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
        
        assertEquals(name, transition.getName());
        assertEquals(triggerEvent, transition.getTriggerEvent());
        assertEquals(initialState, transition.getInitialState());
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    // Test case for addGuardCondition with IPredicate parameter
    @Test
    public void testAddGuardCondition_IPredicate() {
        Transition transition = new Transition();
        IPredicate guard = new GuardPredicate();
        
        transition.addGuardCondition(guard);
        
        assertTrue(transition.getGuardConditions().contains(guard));
    }
    
    // Test case for addGuardCondition with IGuardCondition parameter
    @Test
    public void testAddGuardCondition_IGuardCondition() {
        Transition transition = new Transition();
        IGuardCondition guard = new GuardCondition();
        
        transition.addGuardCondition(guard);
        
        assertTrue(transition.getGuardConditions().contains(guard));
    }
    
    // Test case for equals method
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("test", new Event(), new State(), new State());
        Transition transition2 = new Transition("test", new Event(), new State(), new State());
        
        assertTrue(transition1.equals(transition2));
    }
    
    // Test case for evaluateGuardConditions method
    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition();
        
        // Test when guardConditions is empty
        transition.evaluateGuardConditions();
        
        // Test when guardConditions is not empty
        transition.addGuardCondition(new GuardCondition(true));
        transition.addGuardCondition(new GuardCondition(false));
        transition.evaluateGuardConditions();
    }
    
    // Test case for getInitialState method
    @Test
    public void testGetInitialState() {
        IState initialState = new State();
        Transition transition = new Transition("test", new Event(), initialState, new State());
        
        assertEquals(initialState, transition.getInitialState());
    }
    
    // Test case for getOutcomeState method
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new State();
        Transition transition = new Transition("test", new Event(), new State(), outcomeState);
        
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    // Test case for getTriggerEvent method
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("test", triggerEvent, new State(), new State());
        
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    // Test case for toString method
    @Test
    public void testToString() {
        Transition transition = new Transition("test", new Event(), new State(), new State());
        
        assertNotNull(transition.toString());
    }
    
    // Test case for trigger method
    @Test
    public void testTrigger() {
        IState initialState = new State();
        IState outcomeState = new State();
        Transition transition = new Transition("test", new Event(), initialState, outcomeState);
        
        // Test when initialState and triggerEvent match current state and event, and guardConditions are empty
        IState currentState1 = initialState;
        Event triggerEvent1 = transition.getTriggerEvent();
        IState expectedState1 = outcomeState;
        
        assertEquals(expectedState1, transition.trigger(currentState1, triggerEvent1));
        
        // Test when initialState and triggerEvent match current state and event, and guardConditions are not empty
        transition.addGuardCondition(new GuardCondition(false));
        transition.addGuardCondition(new GuardCondition(true));
        
        IState currentState2 = initialState;
        Event triggerEvent2 = transition.getTriggerEvent();
        IState expectedState2 = outcomeState;
        
        assertEquals(expectedState2, transition.trigger(currentState2, triggerEvent2));
        
        // Test when initialState and triggerEvent do not match current state and event
        IState currentState3 = new State();
        Event triggerEvent3 = new Event();
        IState expectedState3 = currentState3;
        
        assertEquals(expectedState3, transition.trigger(currentState3, triggerEvent3));
    }
    
    // Test case for willTrigger method
    @Test
    public void testWillTrigger() {
        IState initialState = new State();
        IState outcomeState = new State();
        Transition transition = new Transition("test", new Event(), initialState, outcomeState);
        
        // Test when initialState and triggerEvent match current state and event, and all guardConditions evaluate to true
        transition.addGuardCondition(new GuardCondition(true));
        transition.addGuardCondition(new GuardCondition(true));
        
        IState currentState1 = initialState;
        Event triggerEvent1 = transition.getTriggerEvent();
        
        assertTrue(transition.willTrigger(currentState1, triggerEvent1));
        
        // Test when initialState and triggerEvent match current state and event, and at least one guardCondition evaluates to false
        transition.addGuardCondition(new GuardCondition(false));
        
        IState currentState2 = initialState;
        Event triggerEvent2 = transition.getTriggerEvent();
        
        assertFalse(transition.willTrigger(currentState2, triggerEvent2));
        
        // Test when initialState and triggerEvent do not match current state and event
        IState currentState3 = new State();
        Event triggerEvent3 = new Event();
        
        assertFalse(transition.willTrigger(currentState3, triggerEvent3));
    }
}