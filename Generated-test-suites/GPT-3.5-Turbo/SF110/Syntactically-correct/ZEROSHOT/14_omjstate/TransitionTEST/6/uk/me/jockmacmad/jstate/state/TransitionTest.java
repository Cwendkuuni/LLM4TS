import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        // Test when two Transitions are equal
        Transition transition1 = new Transition("Transition1", new Event("Event1"), new IState(), new IState());
        Transition transition2 = new Transition("Transition1", new Event("Event1"), new IState(), new IState());
        assertTrue(transition1.equals(transition2));
        
        // Test when two Transitions are not equal
        Transition transition3 = new Transition("Transition3", new Event("Event1"), new IState(), new IState());
        assertFalse(transition1.equals(transition3));
    }
    
    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("Transition1", new Event("Event1"), new IState(), new IState());
        Transition transition2 = new Transition("Transition1", new Event("Event1"), new IState(), new IState());
        
        // Test that the hash code is equal for two equal Transitions
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition transition = new Transition("Transition1", new Event("Event1"), initialState, new IState());
        
        // Test that the initial state is returned correctly
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition transition = new Transition("Transition1", new Event("Event1"), new IState(), outcomeState);
        
        // Test that the outcome state is returned correctly
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event("Event1");
        Transition transition = new Transition("Transition1", triggerEvent, new IState(), new IState());
        
        // Test that the trigger event is returned correctly
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("Transition1", new Event("Event1"), new IState(), new IState());
        
        // Test that the toString method returns a string representation of the Transition
        assertEquals("Transition[name=Transition1, triggerEvent=Event1, initialState=null, outcomeState=null, guardConditions=[]]", transition.toString());
    }
    
    @Test
    public void testTrigger() {
        // Test when passed in current state and event match the Transition's initial state and trigger event
        IState initialState = new IState();
        IState outcomeState = new IState();
        Transition transition = new Transition("Transition1", new Event("Event1"), initialState, outcomeState);
        IState currentState = initialState;
        Event event = new Event("Event1");
        
        // Test when there are no guard conditions
        assertEquals(outcomeState, transition.trigger(currentState, event));
        
        // Test when there are guard conditions
        IGuardCondition guardCondition = new IGuardCondition();
        transition.addGuardCondition(guardCondition);
        boolean guardPassed = true;
        guardCondition.setGuardPassed(guardPassed);
        
        assertEquals(outcomeState, transition.trigger(currentState, event));
        
        guardCondition.setGuardPassed(!guardPassed);
        assertNull(transition.trigger(currentState, event));
        
        // Test when passed in current state and event do not match the Transition's initial state and trigger event
        currentState = new IState();
        assertNull(transition.trigger(currentState, event));
    }
    
    @Test
    public void testWillTrigger() {
        // Test when passed in current state and event match the Transition's initial state and trigger event
        IState initialState = new IState();
        Transition transition = new Transition("Transition1", new Event("Event1"), initialState, new IState());
        IState currentState = initialState;
        Event event = new Event("Event1");
        
        // Test when there are no guard conditions
        assertTrue(transition.willTrigger(currentState, event));
        
        // Test when there are guard conditions
        IGuardCondition guardCondition = new IGuardCondition();
        transition.addGuardCondition(guardCondition);
        boolean guardPassed = true;
        guardCondition.setGuardPassed(guardPassed);
        
        assertTrue(transition.willTrigger(currentState, event));

        guardCondition.setGuardPassed(!guardPassed);
        assertFalse(transition.willTrigger(currentState, event));
        
        // Test when passed in current state and event do not match the Transition's initial state and trigger event
        currentState = new IState();
        assertFalse(transition.willTrigger(currentState, event));
    }
}