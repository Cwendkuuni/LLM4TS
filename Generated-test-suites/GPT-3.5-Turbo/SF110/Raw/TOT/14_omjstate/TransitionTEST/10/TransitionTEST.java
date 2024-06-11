import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.IPredicate;
import uk.me.jockmacmad.jstate.predicate.IGuardCondition;

public class TransitionTest {

    private Transition transition;
    
    @Before
    public void setUp() {
        transition = new Transition("TestTransition", new Event("TestEvent"), new IState("InitialState"), new IState("OutcomeState"));
    }
    
    @Test
    public void testAddGuardCondition_WithPredicate() {
        IPredicate predicate = new IPredicate();
        transition.addGuardCondition(predicate);
        
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(predicate));
    }
    
    @Test
    public void testAddGuardCondition_WithGuardCondition() {
        IGuardCondition guardCondition = new IGuardCondition();
        transition.addGuardCondition(guardCondition);
        
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }
    
    @Test
    public void testEquals_WithEqualTransitionObject_ReturnsTrue() {
        Transition otherTransition = new Transition("TestTransition", new Event("TestEvent"), new IState("InitialState"), new IState("OutcomeState"));
        
        assertTrue(transition.equals(otherTransition));
    }
    
    @Test
    public void testEquals_WithDifferentTransitionObject_ReturnsFalse() {
        Transition otherTransition = new Transition("DifferentTransition", new Event("DifferentEvent"), new IState("DifferentInitialState"), new IState("DifferentOutcomeState"));
        
        assertFalse(transition.equals(otherTransition));
    }
    
    @Test
    public void testEquals_WithNull_ReturnsFalse() {
        assertFalse(transition.equals(null));
    }
    
    @Test
    public void testHashCode_WithEqualTransitionObject_ReturnsSameHashCode() {
        Transition otherTransition = new Transition("TestTransition", new Event("TestEvent"), new IState("InitialState"), new IState("OutcomeState"));
        
        assertEquals(transition.hashCode(), otherTransition.hashCode());
    }
    
    @Test
    public void testHashCode_WithDifferentTransitionObject_ReturnsDifferentHashCode() {
        Transition otherTransition = new Transition("DifferentTransition", new Event("DifferentEvent"), new IState("DifferentInitialState"), new IState("DifferentOutcomeState"));
        
        assertNotEquals(transition.hashCode(), otherTransition.hashCode());
    }
    
    @Test
    public void testEvaluateGuardConditions_WhenCalled_DoesNotThrowException() {
        transition.evaluateGuardConditions();
    }
    
    @Test
    public void testGetInitialState_ReturnsInitialState() {
        assertEquals("InitialState", transition.getInitialState().getName());
    }
    
    @Test
    public void testGetOutcomeState_ReturnsOutcomeState() {
        assertEquals("OutcomeState", transition.getOutcomeState().getName());
    }
    
    @Test
    public void testGetTriggerEvent_ReturnsTriggerEvent() {
        assertEquals("TestEvent", transition.getTriggerEvent().getName());
    }
    
    @Test
    public void testToString_ReturnsStringRepresentation() {
        String expectedToString = "Transition[name=TestTransition, initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TestEvent]";
        
        assertEquals(expectedToString, transition.toString());
    }
    
    @Test
    public void testTrigger_WithMatchingParameters_WithoutGuardConditions_ReturnsOutcomeState() {
        IState currentState = new IState("InitialState");
        Event triggerEvent = new Event("TestEvent");
        
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("OutcomeState", outcomeState.getName());
    }
    
    @Test
    public void testTrigger_WithMatchingParameters_WithGuardConditions_ReturnsOutcomeState() {
        IState currentState = new IState("InitialState");
        Event triggerEvent = new Event("TestEvent");
        IGuardCondition guardCondition1 = new IGuardCondition(true);
        IGuardCondition guardCondition2 = new IGuardCondition(true);
        
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("OutcomeState", outcomeState.getName());
    }
    
    @Test
    public void testTrigger_WithNonMatchingInitialState_ReturnsCurrentState() {
        IState currentState = new IState("DifferentInitialState");
        Event triggerEvent = new Event("TestEvent");
        
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("DifferentInitialState", outcomeState.getName());
    }
    
    @Test
    public void testTrigger_WithNonMatchingTriggerEvent_ReturnsCurrentState() {
        IState currentState = new IState("InitialState");
        Event triggerEvent = new Event("DifferentEvent");
        
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("InitialState", outcomeState.getName());
    }
    
    @Test
    public void testTrigger_WithMatchingParameters_WithNonPassingGuardConditions_ReturnsCurrentState() {
        IState currentState = new IState("InitialState");
        Event triggerEvent = new Event("TestEvent");
        IGuardCondition guardCondition1 = new IGuardCondition(false);
        IGuardCondition guardCondition2 = new IGuardCondition(true);
        
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        
        assertEquals("InitialState", outcomeState.getName());
    }
    
    @Test
    public void testWillTrigger_WithMatchingParameters_WithoutGuardConditions_ReturnsTrue() {
        IState currentState = new IState("InitialState");
        Event triggerEvent = new Event("TestEvent");
        
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertTrue(willTrigger);
    }
    
    @Test
    public void testWillTrigger_WithMatchingParameters_WithGuardConditions_ReturnsTrue() {
        IState currentState = new IState("InitialState");
        Event triggerEvent = new Event("TestEvent");
        IGuardCondition guardCondition1 = new IGuardCondition(true);
        IGuardCondition guardCondition2 = new IGuardCondition(true);
        
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertTrue(willTrigger);
    }
    
    @Test
    public void testWillTrigger_WithNonMatchingInitialState_ReturnsFalse() {
        IState currentState = new IState("DifferentInitialState");
        Event triggerEvent = new Event("TestEvent");
        
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertFalse(willTrigger);
    }
    
    @Test
    public void testWillTrigger_WithNonMatchingTriggerEvent_ReturnsFalse() {
        IState currentState = new IState("InitialState");
        Event triggerEvent = new Event("DifferentEvent");
        
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertFalse(willTrigger);
    }
    
    @Test
    public void testWillTrigger_WithMatchingParameters_WithNonPassingGuardConditions_ReturnsFalse() {
        IState currentState = new IState("InitialState");
        Event triggerEvent = new Event("TestEvent");
        IGuardCondition guardCondition1 = new IGuardCondition(false);
        IGuardCondition guardCondition2 = new IGuardCondition(true);
        
        transition.addGuardCondition(guardCondition1);
        transition.addGuardCondition(guardCondition2);
        
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        
        assertFalse(willTrigger);
    }
}