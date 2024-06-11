import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TransitionTest {
    
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    
    @Before
    public void setup() {
        initialState = new State("Initial");
        outcomeState = new State("Outcome");
        triggerEvent = new Event("Trigger");
        transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
    }
    
    @Test
    public void testAddGuardCondition_WithPredicate_ShouldAddConditionToList() {
        IPredicate predicate = new PredicateImpl();
        
        transition.addGuardCondition(predicate);
        
        assertTrue(transition.getGuardConditions().contains(predicate));
    }
    
    @Test
    public void testAddGuardCondition_WithGuardCondition_ShouldAddConditionToList() {
        IGuardCondition guardCondition = new GuardConditionImpl();
        
        transition.addGuardCondition(guardCondition);
        
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }
    
    @Test
    public void testEquals_WithEqualTransition_ShouldReturnTrue() {
        Transition equalTransition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        
        assertTrue(transition.equals(equalTransition));
    }
    
    @Test
    public void testEquals_WithDifferentTransition_ShouldReturnFalse() {
        Transition differentTransition = new Transition("Different Transition", triggerEvent, initialState, outcomeState);
        
        assertFalse(transition.equals(differentTransition));
    }
    
    @Test
    public void testHashCode_WithEqualTransition_ShouldReturnSameHashCode() {
        Transition equalTransition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        
        assertEquals(transition.hashCode(), equalTransition.hashCode());
    }
    
    @Test
    public void testHashCode_WithDifferentTransition_ShouldReturnDifferentHashCode() {
        Transition differentTransition = new Transition("Different Transition", triggerEvent, initialState, outcomeState);
        
        assertNotEquals(transition.hashCode(), differentTransition.hashCode());
    }
    
    @Test
    public void testGetInitialState_ShouldReturnCorrectInitialState() {
        IState returnedInitialState = transition.getInitialState();
        
        assertEquals(initialState, returnedInitialState);
    }
    
    @Test
    public void testGetOutcomeState_ShouldReturnCorrectOutcomeState() {
        IState returnedOutcomeState = transition.getOutcomeState();
        
        assertEquals(outcomeState, returnedOutcomeState);
    }
    
    @Test
    public void testGetTriggerEvent_ShouldReturnCorrectTriggerEvent() {
        Event returnedTriggerEvent = transition.getTriggerEvent();
        
        assertEquals(triggerEvent, returnedTriggerEvent);
    }
    
    @Test
    public void testToString_ShouldReturnStringRepresentation() {
        String expectedString = "Transition[name=Test Transition, initialState=Initial, outcomeState=Outcome, triggerEvent=Trigger, guardConditions=[]]";
        
        assertEquals(expectedString, transition.toString());
    }
    
    @Test
    public void testTrigger_WithInitialStateAndTriggerEvent_ShouldReturnOutcomeState() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState returnedState = transition.trigger(currentState, event);
        
        assertEquals(outcomeState, returnedState);
    }
    
    @Test
    public void testTrigger_WithInitialStateAndDifferentTriggerEvent_ShouldReturnInitialState() {
        IState currentState = initialState;
        Event event = new Event("Different Trigger");
        
        IState returnedState = transition.trigger(currentState, event);
        
        assertEquals(initialState, returnedState);
    }
    
    @Test
    public void testTrigger_WithDifferentInitialStateAndTriggerEvent_ShouldReturnInitialState() {
        IState currentState = new State("Different Initial");
        Event event = triggerEvent;
        
        IState returnedState = transition.trigger(currentState, event);
        
        assertEquals(initialState, returnedState);
    }
    
    @Test
    public void testTrigger_WithInitialStateAndTriggerEventAndInvalidGuardCondition_ShouldReturnInitialState() {
        IState currentState = initialState;
        Event event = triggerEvent;
        IGuardCondition invalidGuardCondition = new InvalidGuardCondition();
        transition.addGuardCondition(invalidGuardCondition);
        
        IState returnedState = transition.trigger(currentState, event);
        
        assertEquals(initialState, returnedState);
    }
    
    @Test
    public void testWillTrigger_WithInitialStateAndTriggerEventAndValidGuardCondition_ShouldReturnTrue() {
        IState currentState = initialState;
        Event event = triggerEvent;
        IGuardCondition validGuardCondition = new ValidGuardCondition();
        transition.addGuardCondition(validGuardCondition);
        
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        assertTrue(willTrigger);
    }
    
    @Test
    public void testWillTrigger_WithInitialStateAndTriggerEventAndInvalidGuardCondition_ShouldReturnFalse() {
        IState currentState = initialState;
        Event event = triggerEvent;
        IGuardCondition invalidGuardCondition = new InvalidGuardCondition();
        transition.addGuardCondition(invalidGuardCondition);
        
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        assertFalse(willTrigger);
    }
    
    @Test
    public void testWillTrigger_WithInitialStateAndDifferentTriggerEventAndValidGuardCondition_ShouldReturnFalse() {
        IState currentState = initialState;
        Event event = new Event("Different Trigger");
        IGuardCondition validGuardCondition = new ValidGuardCondition();
        transition.addGuardCondition(validGuardCondition);
        
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        assertFalse(willTrigger);
    }
    
    @Test
    public void testWillTrigger_WithDifferentInitialStateAndTriggerEventAndValidGuardCondition_ShouldReturnFalse() {
        IState currentState = new State("Different Initial");
        Event event = triggerEvent;
        IGuardCondition validGuardCondition = new ValidGuardCondition();
        transition.addGuardCondition(validGuardCondition);
        
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        assertFalse(willTrigger);
    }
    
}