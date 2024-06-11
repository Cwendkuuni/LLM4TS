import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        Transition transition2 = new Transition("Transition2", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        assertFalse(transition1.equals(transition2)); // Different names
        
        Transition transition3 = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        assertTrue(transition1.equals(transition3)); // Same names
    }
    
    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        Transition transition2 = new Transition("Transition2", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        assertNotEquals(transition1.hashCode(), transition2.hashCode()); // Different names
        
        Transition transition3 = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        assertEquals(transition1.hashCode(), transition3.hashCode()); // Same names
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        // TODO: Add test cases for guard conditions
    }
    
    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        IState initialState = transition.getInitialState();
        
        assertEquals("InitialState", initialState.getName()); // Checks if the correct initial state is returned
    }
    
    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        IState outcomeState = transition.getOutcomeState();
        
        assertEquals("OutcomeState", outcomeState.getName()); // Checks if the correct outcome state is returned
    }
    
    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        Event triggerEvent = transition.getTriggerEvent();
        
        assertNotNull(triggerEvent); // Checks if the trigger event is not null
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        String transitionString = transition.toString();
        
        assertFalse(transitionString.isEmpty()); // Checks if the string representation of the transition is not empty
    }
    
    @Test
    public void testTrigger() {
        Transition transition = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        IState currentState = new State("InitialState");
        Event triggerEvent = new Event();
        
        IState newState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("OutcomeState", newState.getName()); // Checks if the outcome state is triggered correctly
        
        currentState = new State("OtherState");
        
        newState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("InitialState", newState.getName()); // Checks if the outcome state doesn't change if the current state is not the initial state
        
        currentState = new State("InitialState");
        triggerEvent = new Event();
        
        transition.addGuardCondition(new GuardCondition());
        
        newState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("InitialState", newState.getName()); // Checks if the outcome state doesn't change if the guard condition is not met
        
        transition.addGuardCondition(new GuardCondition(true));
        
        newState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("OutcomeState", newState.getName()); // Checks if the outcome state is triggered correctly when the guard condition is met
    }
    
    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("Transition1", new Event(), new State("InitialState"), new State("OutcomeState"));
        
        IState currentState = new State("InitialState");
        Event triggerEvent = new Event();
        
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertTrue(willTrigger); // Checks if the transition will trigger
        
        currentState = new State("OtherState");
        
        willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertFalse(willTrigger); // Checks if the transition will not trigger if the current state is not the initial state
        
        currentState = new State("InitialState");
        triggerEvent = new Event();
        
        transition.addGuardCondition(new GuardCondition());
        
        willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertFalse(willTrigger); // Checks if the transition will not trigger if the guard condition is not met
        
        transition.addGuardCondition(new GuardCondition(true));
        
        willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertTrue(willTrigger); // Checks if the transition will trigger when the guard condition is met
    }

}