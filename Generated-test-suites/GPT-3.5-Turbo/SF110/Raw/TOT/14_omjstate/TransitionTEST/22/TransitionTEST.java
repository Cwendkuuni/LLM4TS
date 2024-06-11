import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
    }

    @Test
    public void testAddGuardCondition_IPredicate() {
        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                // Implementation of evaluate method for testing
                return false;
            }
        };

        transition.addGuardCondition(guard);

        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertSame(guard, guardConditions.get(0));
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        IGuardCondition guard = new IGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                // Implementation of evaluate method for testing
                return true;
            }
        };

        transition.addGuardCondition(guard);

        List<IGuardCondition> guardConditions = transition.getGuardConditions();
        assertEquals(1, guardConditions.size());
        assertSame(guard, guardConditions.get(0));
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(transition.equals(transition));
    }

    @Test
    public void testEquals_EqualObjects() {
        Transition anotherTransition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        assertTrue(transition.equals(anotherTransition));
        assertTrue(anotherTransition.equals(transition));
    }

    @Test
    public void testEquals_NotEqualObjects() {
        Transition anotherTransition = new Transition("Different Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        assertFalse(transition.equals(anotherTransition));
        assertFalse(anotherTransition.equals(transition));
    }

    @Test
    public void testHashCode_SameObject() {
        assertEquals(transition.hashCode(), transition.hashCode());
    }

    @Test
    public void testHashCode_EqualObjects() {
        Transition anotherTransition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        assertEquals(transition.hashCode(), anotherTransition.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        // Not possible to test directly as the method is protected and only used internally
        // Testing is done through the trigger and willTrigger methods

        // Test without any guard conditions
        IState initialState = new State("Initial State");
        Event triggerEvent = new Event("Trigger Event");
        IState outcomeState = new State("Outcome State");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;
        Event event = triggerEvent;

        IState resultState = transition.trigger(currentState, event);
        assertEquals(outcomeState, resultState);

        // Test with a guard condition
        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                // Implementation of evaluate method for testing
                return false;
            }
        };

        transition.addGuardCondition(guard);

        currentState = initialState;
        event = triggerEvent;

        resultState = transition.trigger(currentState, event);
        assertEquals(initialState, resultState);
    }

    @Test
    public void testGetInitialState() {
        IState initialState = new State("Initial State");
        Event triggerEvent = new Event("Trigger Event");
        IState outcomeState = new State("Outcome State");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        IState initialState = new State("Initial State");
        Event triggerEvent = new Event("Trigger Event");
        IState outcomeState = new State("Outcome State");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        IState initialState = new State("Initial State");
        Event triggerEvent = new Event("Trigger Event");
        IState outcomeState = new State("Outcome State");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        String expected = "Transition[name=Test Transition, initialState=Initial State, outcomeState=Outcome State, triggerEvent=Test Event, guardConditions=[]]";
        assertEquals(expected, transition.toString());
    }

    @Test
    public void testTrigger_NoGuardConditions() {
        IState initialState = new State("Initial State");
        Event triggerEvent = new Event("Trigger Event");
        IState outcomeState = new State("Outcome State");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;
        Event event = triggerEvent;

        IState resultState = transition.trigger(currentState, event);
        assertEquals(outcomeState, resultState);
    }

    @Test
    public void testTrigger_WithGuardCondition() {
        IState initialState = new State("Initial State");
        Event triggerEvent = new Event("Trigger Event");
        IState outcomeState = new State("Outcome State");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                // Implementation of evaluate method for testing
                return false;
            }
        };

        transition.addGuardCondition(guard);

        IState currentState = initialState;
        Event event = triggerEvent;

        IState resultState = transition.trigger(currentState, event);
        assertEquals(initialState, resultState);
    }

    @Test
    public void testWillTrigger_NoGuardConditions() {
        IState initialState = new State("Initial State");
        Event triggerEvent = new Event("Trigger Event");
        IState outcomeState = new State("Outcome State");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;
        Event event = triggerEvent;

        boolean willTrigger = transition.willTrigger(currentState, event);
        assertTrue(willTrigger);
    }

    @Test
    public void testWillTrigger_WithGuardCondition() {
        IState initialState = new State("Initial State");
        Event triggerEvent = new Event("Trigger Event");
        IState outcomeState = new State("Outcome State");

        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);

        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                // Implementation of evaluate method for testing
                return false;
            }
        };

        transition.addGuardCondition(guard);

        IState currentState = initialState;
        Event event = triggerEvent;

        boolean willTrigger = transition.willTrigger(currentState, event);
        assertFalse(willTrigger);
    }
}