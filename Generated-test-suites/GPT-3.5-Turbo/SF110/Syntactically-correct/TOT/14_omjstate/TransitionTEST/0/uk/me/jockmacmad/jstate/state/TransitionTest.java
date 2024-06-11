import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition_WithValidPredicate_ShouldAddPredicateToGuardConditions() {
        // Arrange
        Transition transition = new Transition("Transition1", new Event(), new State(), new State());
        IPredicate predicate = new Predicate();
        
        // Act
        transition.addGuardCondition(predicate);
        
        // Assert
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(predicate));
    }

    @Test
    public void testAddGuardCondition_WithValidGuardCondition_ShouldAddGuardConditionToGuardConditions() {
        // Arrange
        Transition transition = new Transition("Transition2", new Event(), new State(), new State());
        IGuardCondition guardCondition = new GuardCondition();
        
        // Act
        transition.addGuardCondition(guardCondition);
        
        // Assert
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testEquals_WithEqualTransitions_ShouldReturnTrue() {
        // Arrange
        Transition transition1 = new Transition("Transition1", new Event(), new State(), new State());
        Transition transition2 = new Transition("Transition1", new Event(), new State(), new State());
        
        // Act
        boolean result = transition1.equals(transition2);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testEquals_WithNotEqualTransitions_ShouldReturnFalse() {
        // Arrange
        Transition transition1 = new Transition("Transition1", new Event(), new State(), new State());
        Transition transition2 = new Transition("Transition2", new Event(), new State(), new State());
        
        // Act
        boolean result = transition1.equals(transition2);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testHashCode_WithEqualTransitions_ShouldReturnEqualHashCodes() {
        // Arrange
        Transition transition1 = new Transition("Transition1", new Event(), new State(), new State());
        Transition transition2 = new Transition("Transition1", new Event(), new State(), new State());
        
        // Act
        int hashCode1 = transition1.hashCode();
        int hashCode2 = transition2.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString_ShouldReturnStringRepresentationOfTransition() {
        // Arrange
        Transition transition = new Transition("Transition1", new Event(), new State(), new State());
        
        // Act
        String result = transition.toString();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("name=Transition1"));
        assertTrue(result.contains("initialState=" + transition.getInitialState()));
        assertTrue(result.contains("outcomeState=" + transition.getOutcomeState()));
        assertTrue(result.contains("triggerEvent=" + transition.getTriggerEvent()));
    }

    @Test
    public void testTrigger_WithMatchingInitialStateAndTriggerEventAndNoGuardConditions_ShouldReturnOutcomeState() {
        // Arrange
        Transition transition = new Transition("Transition1", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        
        // Act
        IState result = transition.trigger(currentState, triggerEvent);
        
        // Assert
        assertEquals(transition.getOutcomeState(), result);
    }

    @Test
    public void testTrigger_WithMatchingInitialStateAndTriggerEventAndPassingGuardConditions_ShouldReturnOutcomeState() {
        // Arrange
        Transition transition = new Transition("Transition1", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        IGuardCondition guardCondition = new GuardCondition();
        transition.addGuardCondition(guardCondition);
        guardCondition.setEvaluationResult(true);
        
        // Act
        IState result = transition.trigger(currentState, triggerEvent);
        
        // Assert
        assertEquals(transition.getOutcomeState(), result);
    }

    @Test
    public void testTrigger_WithMatchingInitialStateAndTriggerEventAndFailingGuardCondition_ShouldReturnCurrentState() {
        // Arrange
        Transition transition = new Transition("Transition1", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        IGuardCondition guardCondition = new GuardCondition();
        transition.addGuardCondition(guardCondition);
        guardCondition.setEvaluationResult(false);
        
        // Act
        IState result = transition.trigger(currentState, triggerEvent);
        
        // Assert
        assertEquals(currentState, result);
    }

    @Test
    public void testWillTrigger_WithMatchingInitialStateAndTriggerEventAndPassingGuardConditions_ShouldReturnTrue() {
        // Arrange
        Transition transition = new Transition("Transition1", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        IGuardCondition guardCondition = new GuardCondition();
        transition.addGuardCondition(guardCondition);
        guardCondition.setEvaluationResult(true);
        
        // Act
        boolean result = transition.willTrigger(currentState, triggerEvent);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testWillTrigger_WithMatchingInitialStateAndTriggerEventAndFailingGuardCondition_ShouldReturnFalse() {
        // Arrange
        Transition transition = new Transition("Transition1", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        IGuardCondition guardCondition = new GuardCondition();
        transition.addGuardCondition(guardCondition);
        guardCondition.setEvaluationResult(false);
        
        // Act
        boolean result = transition.willTrigger(currentState, triggerEvent);
        
        // Assert
        assertFalse(result);
    }

}