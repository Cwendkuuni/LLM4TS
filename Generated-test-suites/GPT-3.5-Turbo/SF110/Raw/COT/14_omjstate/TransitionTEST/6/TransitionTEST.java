
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        // Initialize objects for testing
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        transition = new Transition("Test", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testAddGuardCondition_withIPredicate() {
        // Test addGuardCondition() with IPredicate parameter
        IPredicate guard = new IPredicate();
        transition.addGuardCondition(guard);

        // Assert that the guard condition is added successfully
        Assert.assertTrue(transition.getGuardConditions().contains(guard));
    }

    @Test
    public void testAddGuardCondition_withIGuardCondition() {
        // Test addGuardCondition() with IGuardCondition parameter
        IGuardCondition guard = new IGuardCondition();
        transition.addGuardCondition(guard);

        // Assert that the guard condition is added successfully
        Assert.assertTrue(transition.getGuardConditions().contains(guard));
    }

    @Test
    public void testEquals() {
        // Test equals() method with the same instance of Transition
        Transition sameTransition = transition;
        Assert.assertTrue(transition.equals(sameTransition));

        // Create another Transition object with the same properties
        Transition identicalTransition = new Transition("Test", transition.getTriggerEvent(), 
                                            transition.getInitialState(), transition.getOutcomeState());
        Assert.assertTrue(transition.equals(identicalTransition));

        // Create a Transition object with different properties
        Transition differentTransition = new Transition("Different", transition.getTriggerEvent(),
                                            transition.getInitialState(), transition.getOutcomeState());
        Assert.assertFalse(transition.equals(differentTransition));
    }

    @Test
    public void testHashCode() {
        // Test hashCode() method
        Transition sameTransition = transition;
        Assert.assertEquals(transition.hashCode(), sameTransition.hashCode());

        Transition identicalTransition = new Transition("Test", transition.getTriggerEvent(), 
                                            transition.getInitialState(), transition.getOutcomeState());
        Assert.assertEquals(transition.hashCode(), identicalTransition.hashCode());

        Transition differentTransition = new Transition("Different", transition.getTriggerEvent(),
                                            transition.getInitialState(), transition.getOutcomeState());
        Assert.assertNotEquals(transition.hashCode(), differentTransition.hashCode());
    }

    @Test
    public void testGetInitialState() {
        // Test getInitialState() method
        Assert.assertEquals(transition.getInitialState(), transition.initialState);
    }

    @Test
    public void testGetOutcomeState() {
        // Test getOutcomeState() method
        Assert.assertEquals(transition.getOutcomeState(), transition.outcomeState);
    }

    @Test
    public void testGetTriggerEvent() {
        // Test getTriggerEvent() method
        Assert.assertEquals(transition.getTriggerEvent(), transition.triggerEvent);
    }

    @Test
    public void testToString() {
        // Test toString() method
        String expected = "Transition[name=Test, initialState=null, outcomeState=null, guardConditions=[], triggerEvent=null]";
        Assert.assertEquals(expected, transition.toString());
    }

    @Test
    public void testTrigger_noGuardConditions() {
        // Test trigger() method with no guard conditions
        IState initialState = transition.getInitialState();
        Event triggerEvent = transition.getTriggerEvent();
        IState outcomeState = transition.getOutcomeState();

        // Set current state and event to match transition properties
        IState currentState = initialState;
        Event currentEvent = triggerEvent;

        // Trigger the transition
        IState newState = transition.trigger(currentState, currentEvent);

        // Assert that the outcome state is returned
        Assert.assertEquals(outcomeState, newState);
    }

    @Test
    public void testTrigger_withGuardConditions() {
        // Test trigger() method with guard conditions
        IState initialState = transition.getInitialState();
        Event triggerEvent = transition.getTriggerEvent();
        IState outcomeState = transition.getOutcomeState();

        // Set current state and event to match transition properties
        IState currentState = initialState;
        Event currentEvent = triggerEvent;

        // Add a guard condition
        IGuardCondition guardCondition = new IGuardCondition();
        guardCondition.setEvaluateResult(true);
        transition.addGuardCondition(guardCondition);

        // Trigger the transition
        IState newState = transition.trigger(currentState, currentEvent);

        // Assert that the outcome state is returned
        Assert.assertEquals(outcomeState, newState);
    }

    @Test
    public void testWillTrigger_withGuardConditions() {
        // Test willTrigger() method with guard conditions
        IState initialState = transition.getInitialState();
        Event triggerEvent = transition.getTriggerEvent();

        // Set current state and event to match transition properties
        IState currentState = initialState;
        Event currentEvent = triggerEvent;

        // Add a guard condition
        IGuardCondition guardCondition = new IGuardCondition();
        guardCondition.setEvaluateResult(true);
        transition.addGuardCondition(guardCondition);

        // Check if the transition will trigger
        boolean willTrigger = transition.willTrigger(currentState, currentEvent);

        // Assert that the transition will trigger
        Assert.assertTrue(willTrigger);
    }

    @Test
    public void testWillTrigger_noGuardConditions() {
        // Test willTrigger() method with no guard conditions
        IState initialState = transition.getInitialState();
        Event triggerEvent = transition.getTriggerEvent();

        // Set current state and event to match transition properties
        IState currentState = initialState;
        Event currentEvent = triggerEvent;

        // Check if the transition will trigger
        boolean willTrigger = transition.willTrigger(currentState, currentEvent);

        // Assert that the transition will not trigger
        Assert.assertFalse(willTrigger);
    }

}

