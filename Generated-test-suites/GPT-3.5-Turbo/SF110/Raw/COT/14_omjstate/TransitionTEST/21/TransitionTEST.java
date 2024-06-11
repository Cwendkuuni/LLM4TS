import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setup() {
        // Initialize the Transition object with necessary parameters for testing
        transition = new Transition("Test", new Event(), new IState(), new IState());
    }

    @Test
    public void addGuardCondition_withIPredicate_shouldAddGuardCondition() {
        // Arrange
        IPredicate guard = new IPredicateImpl();

        // Act
        transition.addGuardCondition(guard);

        // Assert
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guard));
    }

    @Test
    public void addGuardCondition_withIGuardCondition_shouldAddGuardCondition() {
        // Arrange
        IGuardCondition guard = new IGuardConditionImpl();

        // Act
        transition.addGuardCondition(guard);

        // Assert
        assertEquals(1, transition.getGuardConditions().size());
        assertTrue(transition.getGuardConditions().contains(guard));
    }

    @Test
    public void equals_withEqualTransitions_shouldReturnTrue() {
        // Arrange
        Transition transition2 = new Transition("Test", new Event(), new IState(), new IState());

        // Act
        boolean result = transition.equals(transition2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void equals_withDifferentTransitions_shouldReturnFalse() {
        // Arrange
        Transition transition2 = new Transition("Test2", new Event(), new IState(), new IState());

        // Act
        boolean result = transition.equals(transition2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void hashCode_withEqualTransitions_shouldReturnEqualHashCodes() {
        // Arrange
        Transition transition2 = new Transition("Test", new Event(), new IState(), new IState());

        // Act
        int hashCode1 = transition.hashCode();
        int hashCode2 = transition2.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void evaluateGuardConditions_withNoConditions_shouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> transition.evaluateGuardConditions());
    }

    @Test
    public void getInitialState_shouldReturnInitialSate() {
        // Act
        IState initialState = transition.getInitialState();

        // Assert
        assertNotNull(initialState);
    }

    @Test
    public void getOutcomeState_shouldReturnOutcomeState() {
        // Act
        IState outcomeState = transition.getOutcomeState();

        // Assert
        assertNotNull(outcomeState);
    }

    @Test
    public void getTriggerEvent_shouldReturnTriggerEvent() {
        // Act
        Event triggerEvent = transition.getTriggerEvent();

        // Assert
        assertNotNull(triggerEvent);
    }

    @Test
    public void toString_shouldReturnStringRepresentation() {
        // Act
        String str = transition.toString();

        // Assert
        assertNotNull(str);
        assertFalse(str.isEmpty());
    }

    @Test
    public void trigger_withMatchingInitialStateAndTriggerEvent_shouldReturnOutcomeState() {
        // Arrange
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);

        // Act
        IState result = transition.trigger(initialState, triggerEvent);

        // Assert
        assertEquals(outcomeState, result);
    }

    @Test
    public void trigger_withNonMatchingInitialState_shouldReturnCurrentState() {
        // Arrange
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);
        IState currentState = new IState();

        // Act
        IState result = transition.trigger(currentState, triggerEvent);

        // Assert
        assertEquals(currentState, result);
    }

    @Test
    public void trigger_withNonMatchingTriggerEvent_shouldReturnCurrentState() {
        // Arrange
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);
        Event nonMatchingTriggerEvent = new Event();

        // Act
        IState result = transition.trigger(initialState, nonMatchingTriggerEvent);

        // Assert
        assertEquals(initialState, result);
    }

    @Test
    public void trigger_withMatchingInitialStateTriggerEventAndFailingGuardCondition_shouldReturnCurrentState() {
        // Arrange
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);
        IGuardCondition failingGuardCondition = new IGuardConditionImpl(false);
        transition.addGuardCondition(failingGuardCondition);

        // Act
        IState result = transition.trigger(initialState, triggerEvent);

        // Assert
        assertEquals(initialState, result);
    }

    @Test
    public void trigger_withMatchingInitialStateTriggerEventAndPassingGuardCondition_shouldReturnOutcomeState() {
        // Arrange
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);
        IGuardCondition passingGuardCondition = new IGuardConditionImpl(true);
        transition.addGuardCondition(passingGuardCondition);

        // Act
        IState result = transition.trigger(initialState, triggerEvent);

        // Assert
        assertEquals(outcomeState, result);
    }

    @Test
    public void willTrigger_withMatchingInitialStateAndTriggerEvent_shouldReturnTrue() {
        // Arrange
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);

        // Act
        boolean result = transition.willTrigger(initialState, triggerEvent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void willTrigger_withNonMatchingInitialState_shouldReturnFalse() {
        // Arrange
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);
        IState currentState = new IState();

        // Act
        boolean result = transition.willTrigger(currentState, triggerEvent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void willTrigger_withNonMatchingTriggerEvent_shouldReturnFalse() {
        // Arrange
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);
        Event nonMatchingTriggerEvent = new Event();

        // Act
        boolean result = transition.willTrigger(initialState, nonMatchingTriggerEvent);

        // Assert
        assertFalse(result);
    }
}

