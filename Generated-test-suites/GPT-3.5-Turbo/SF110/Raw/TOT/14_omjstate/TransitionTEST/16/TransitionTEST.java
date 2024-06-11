import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition_withPredicate() {
        // Arrange
        Transition transition = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        IPredicate predicate = new PredicateImpl();
        
        // Act
        transition.addGuardCondition(predicate);
        
        // Assert
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(predicate, transition.getGuardConditions().get(0));
    }
    
    @Test
    public void testAddGuardCondition_withGuardCondition() {
        // Arrange
        Transition transition = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        IGuardCondition guardCondition = new GuardConditionImpl();
        
        // Act
        transition.addGuardCondition(guardCondition);
        
        // Assert
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(guardCondition, transition.getGuardConditions().get(0));
    }
    
    @Test
    public void testEquals_equalInstances() {
        // Arrange
        Transition transition1 = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        Transition transition2 = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        
        // Act
        boolean result = transition1.equals(transition2);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testEquals_differentInstances() {
        // Arrange
        Transition transition1 = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        Transition transition2 = new Transition("transition2", new Event("event2"), new State("state2"), new State("state3"));
        
        // Act
        boolean result = transition1.equals(transition2);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHashCode_equalInstances() {
        // Arrange
        Transition transition1 = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        Transition transition2 = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        
        // Act
        int hashCode1 = transition1.hashCode();
        int hashCode2 = transition2.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testHashCode_differentInstances() {
        // Arrange
        Transition transition1 = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        Transition transition2 = new Transition("transition2", new Event("event2"), new State("state2"), new State("state3"));
        
        // Act
        int hashCode1 = transition1.hashCode();
        int hashCode2 = transition2.hashCode();
        
        // Assert
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testGetInitialState() {
        // Arrange
        State initialState = new State("state1");
        Transition transition = new Transition("transition1", new Event("event1"), initialState, new State("state2"));
        
        // Act
        IState result = transition.getInitialState();
        
        // Assert
        assertEquals(initialState, result);
    }
    
    @Test
    public void testGetOutcomeState() {
        // Arrange
        State outcomeState = new State("state2");
        Transition transition = new Transition("transition1", new Event("event1"), new State("state1"), outcomeState);
        
        // Act
        IState result = transition.getOutcomeState();
        
        // Assert
        assertEquals(outcomeState, result);
    }
    
    @Test
    public void testGetTriggerEvent() {
        // Arrange
        Event triggerEvent = new Event("event1");
        Transition transition = new Transition("transition1", triggerEvent, new State("state1"), new State("state2"));
        
        // Act
        Event result = transition.getTriggerEvent();
        
        // Assert
        assertEquals(triggerEvent, result);
    }
    
    @Test
    public void testToString() {
        // Arrange
        Transition transition = new Transition("transition1", new Event("event1"), new State("state1"), new State("state2"));
        
        // Act
        String result = transition.toString();
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testTrigger_passesGuards() {
        // Arrange
        State initialState = new State("state1");
        State outcomeState = new State("state2");
        Event triggerEvent = new Event("event1");
        Transition transition = new Transition("transition1", triggerEvent, initialState, outcomeState);
        IGuardCondition guardCondition = new GuardConditionImpl();
        transition.addGuardCondition(guardCondition);
        
        // Act
        IState result = transition.trigger(initialState, triggerEvent);
        
        // Assert
        assertEquals(outcomeState, result);
    }
    
    @Test
    public void testTrigger_doesNotPassGuards() {
        // Arrange
        State initialState = new State("state1");
        State outcomeState = new State("state2");
        Event triggerEvent = new Event("event1");
        Transition transition = new Transition("transition1", triggerEvent, initialState, outcomeState);
        IGuardCondition guardCondition = new GuardConditionImpl();
        transition.addGuardCondition(guardCondition);
        guardCondition.setResult(false);
        
        // Act
        IState result = transition.trigger(initialState, triggerEvent);
        
        // Assert
        assertEquals(initialState, result);
    }
    
    @Test
    public void testWillTrigger_passesGuards() {
        // Arrange
        State initialState = new State("state1");
        Event triggerEvent = new Event("event1");
        Transition transition = new Transition("transition1", triggerEvent, initialState, new State("state2"));
        IGuardCondition guardCondition = new GuardConditionImpl();
        transition.addGuardCondition(guardCondition);
        
        // Act
        boolean result = transition.willTrigger(initialState, triggerEvent);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testWillTrigger_doesNotPassGuards() {
        // Arrange
        State initialState = new State("state1");
        Event triggerEvent = new Event("event1");
        Transition transition = new Transition("transition1", triggerEvent, initialState, new State("state2"));
        IGuardCondition guardCondition = new GuardConditionImpl();
        transition.addGuardCondition(guardCondition);
        guardCondition.setResult(false);
        
        // Act
        boolean result = transition.willTrigger(initialState, triggerEvent);
        
        // Assert
        assertFalse(result);
    }
}