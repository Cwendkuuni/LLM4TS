import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        transition = new Transition("TestTransition", new Event("TestEvent"), new IState() {}, new IState() {});
    }

    @Test
    public void testAddGuardCondition_withIPredicate() {
        IPredicate predicate = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        };

        transition.addGuardCondition(predicate);

        // Check if the guard condition is added correctly
        assertTrue(transition.getGuardConditions().contains(predicate));
    }

    @Test
    public void testAddGuardCondition_withIGuardCondition() {
        IGuardCondition guardCondition = new IGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        };

        transition.addGuardCondition(guardCondition);

        // Check if the guard condition is added correctly
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testEquals() {
        Transition otherTransition = new Transition("TestTransition", new Event("TestEvent"), new IState() {}, new IState() {});

        // Check if two transitions with the same attributes are considered equal
        assertEquals(transition, otherTransition);
    }

    @Test
    public void testHashCode() {
        Transition otherTransition = new Transition("TestTransition", new Event("TestEvent"), new IState() {}, new IState() {});

        // Check if the hash code of two equal transitions are the same
        assertEquals(transition.hashCode(), otherTransition.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = new IState() {};

        // Check if the initial state is returned correctly
        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState() {};

        // Check if the outcome state is returned correctly
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event("TestEvent");

        // Check if the trigger event is returned correctly
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        String expectedToString = "Transition[name=TestTransition, initialState=null, outcomeState=null, triggerEvent=TestEvent, guardConditions=[]]";

        // Check if the toString() method returns the expected string representation
        assertEquals(expectedToString, transition.toString());
    }

    @Test
    public void testTrigger_withoutGuardConditions() {
        IState currentState = new IState() {};
        Event triggerEvent = new Event("TestEvent");

        IState outcomeState = transition.trigger(currentState, triggerEvent);

        // Check if the outcome state is the expected one when no guard conditions are present
        assertEquals(transition.getOutcomeState(), outcomeState);
    }

    @Test
    public void testTrigger_withGuardConditions_passing() {
        IState currentState = new IState() {};
        Event triggerEvent = new Event("TestEvent");

        transition.addGuardCondition(new IGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        });

        IState outcomeState = transition.trigger(currentState, triggerEvent);

        // Check if the outcome state is the expected one when all guard conditions pass
        assertEquals(transition.getOutcomeState(), outcomeState);
    }

    @Test
    public void testTrigger_withGuardConditions_notPassing() {
        IState currentState = new IState() {};
        Event triggerEvent = new Event("TestEvent");

        transition.addGuardCondition(new IGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        });

        IState outcomeState = transition.trigger(currentState, triggerEvent);

        // Check if the outcome state is the current state when any guard condition does not pass
        assertEquals(currentState, outcomeState);
    }

    @Test
    public void testWillTrigger_withGuardConditions_passing() {
        IState currentState = new IState() {};
        Event triggerEvent = new Event("TestEvent");

        transition.addGuardCondition(new IGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        });

        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);

        // Check if the transition will trigger when all guard conditions pass
        assertTrue(willTrigger);
    }

    @Test
    public void testWillTrigger_withGuardConditions_notPassing() {
        IState currentState = new IState() {};
        Event triggerEvent = new Event("TestEvent");

        transition.addGuardCondition(new IGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        });

        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);

        // Check if the transition will not trigger when any guard condition does not pass
        assertFalse(willTrigger);
    }
}