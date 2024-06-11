import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

public class TransitionTest {

    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    
    @Before
    public void setUp() {
        // Initialize necessary objects for testing
        initialState = new State("Initial");
        outcomeState = new State("Outcome");
        triggerEvent = new Event("Trigger");
        transition = new Transition("Transition", triggerEvent, initialState, outcomeState);
    }
    
    @Test
    public void testAddGuardCondition_withIPredicate() {
        // Ensure the guard condition is added correctly
        IPredicate guardCondition = new Predicate();
        transition.addGuardCondition(guardCondition);
        
        Assert.assertTrue(transition.getGuardConditions().contains(guardCondition));
    }
    
    @Test
    public void testAddGuardCondition_withIGuardCondition() {
        // Ensure the guard condition is added correctly
        IGuardCondition guardCondition = new GuardCondition();
        transition.addGuardCondition(guardCondition);
        
        Assert.assertTrue(transition.getGuardConditions().contains(guardCondition));
    }
    
    @Test
    public void testEquals_withSameObject() {
        // Ensure equals() returns true when comparing the same object
        Assert.assertEquals(transition, transition);
    }
    
    @Test
    public void testEquals_withDifferentObject() {
        // Ensure equals() returns false when comparing with a different object
        Transition otherTransition = new Transition("Other", triggerEvent, initialState, outcomeState);
        
        Assert.assertNotEquals(transition, otherTransition);
    }
    
    @Test
    public void testHashCode_withSameObject() {
        // Ensure hashCode() returns the same value for the same object
        Assert.assertEquals(transition.hashCode(), transition.hashCode());
    }
    
    @Test
    public void testHashCode_withDifferentObject() {
        // Ensure hashCode() returns different values for different objects
        Transition otherTransition = new Transition("Other", triggerEvent, initialState, outcomeState);
        
        Assert.assertNotEquals(transition.hashCode(), otherTransition.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        // Ensure the correct initial state is returned
        Assert.assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        // Ensure the correct outcome state is returned
        Assert.assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        // Ensure the correct trigger event is returned
        Assert.assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testTrigger_withMatchingStatesAndEvent_andNoGuardCondition() {
        // Ensure the outcome state is returned when there are no guard conditions
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState result = transition.trigger(currentState, event);
        
        Assert.assertEquals(result, outcomeState);
    }
    
    @Test
    public void testTrigger_withMatchingStatesAndEvent_andPassingGuardConditions() {
        // Ensure the outcome state is returned when all guard conditions pass
        IState currentState = initialState;
        Event event = triggerEvent;
        IGuardCondition guardCondition = new GuardCondition();
        transition.addGuardCondition(guardCondition);
        
        IState result = transition.trigger(currentState, event);
        
        Assert.assertEquals(result, outcomeState);
    }
    
    @Test
    public void testTrigger_withMatchingStatesAndEvent_andFailingGuardConditions() {
        // Ensure the current state is returned when at least one guard condition fails
        IState currentState = initialState;
        Event event = triggerEvent;
        IGuardCondition guardCondition = new GuardCondition(false);
        transition.addGuardCondition(guardCondition);
        
        IState result = transition.trigger(currentState, event);
        
        Assert.assertEquals(result, currentState);
    }
    
    @Test
    public void testWillTrigger_withMatchingStatesAndEvent_andPassingGuardConditions() {
        // Ensure true is returned when all guard conditions pass
        IState currentState = initialState;
        Event event = triggerEvent;
        IGuardCondition guardCondition = new GuardCondition();
        transition.addGuardCondition(guardCondition);
        
        boolean result = transition.willTrigger(currentState, event);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testWillTrigger_withMatchingStatesAndEvent_andFailingGuardConditions() {
        // Ensure false is returned when at least one guard condition fails
        IState currentState = initialState;
        Event event = triggerEvent;
        IGuardCondition guardCondition = new GuardCondition(false);
        transition.addGuardCondition(guardCondition);
        
        boolean result = transition.willTrigger(currentState, event);
        
        Assert.assertFalse(result);
    }
}