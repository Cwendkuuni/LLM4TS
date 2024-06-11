import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition_WithIPredicate() {
        // Create Transition object
        Transition transition = new Transition("testTransition", new Event(), new IState(), new IState());

        // Create IPredicate object
        IPredicate predicate = new IPredicate() {
            public boolean evaluate(Event event) {
                return event.isValid();
            }
        };

        // Add IPredicate guard condition
        transition.addGuardCondition(predicate);

        // Verify guard condition added
        assertTrue(transition.getGuardConditions().contains(predicate));
    }

    @Test
    public void testAddGuardCondition_WithIGuardCondition() {
        // Create Transition object
        Transition transition = new Transition("testTransition", new Event(), new IState(), new IState());

        // Create IGuardCondition object
        IGuardCondition guardCondition = new IGuardCondition() {
            public boolean evaluate(Event event) {
                return event.isValid();
            }
        };

        // Add IGuardCondition guard condition
        transition.addGuardCondition(guardCondition);

        // Verify guard condition added
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testEquals() {
        // Create Transition objects with same properties
        Transition transition1 = new Transition("testTransition", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("testTransition", new Event(), new IState(), new IState());

        // Verify transitions are equal
        assertEquals(transition1, transition2);
    }

    @Test
    public void testHashCode() {
        // Create Transition objects with same properties
        Transition transition1 = new Transition("testTransition", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("testTransition", new Event(), new IState(), new IState());

        // Verify hash codes of transitions are equal
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testTrigger_WithValidEventAndNoGuardConditions() {
        // Create Transition object
        Transition transition = new Transition("testTransition", new Event(), new IState(), new IState());

        // Create current state and event
        IState currentState = new IState();
        Event event = new Event();

        // Trigger transition
        IState outcomeState = transition.trigger(currentState, event);

        // Verify outcome state is the expected state
        assertEquals(transition.getOutcomeState(), outcomeState);
    }

    @Test
    public void testTrigger_WithValidEventAndPassedGuardConditions() {
        // Create Transition object
        Transition transition = new Transition("testTransition", new Event(), new IState(), new IState());

        // Create current state and event
        IState currentState = new IState();
        Event event = new Event();

        // Create passed guard condition
        IGuardCondition guardCondition = new IGuardCondition() {
            public boolean evaluate(Event event) {
                return true;
            }
        };
        transition.addGuardCondition(guardCondition);

        // Trigger transition
        IState outcomeState = transition.trigger(currentState, event);

        // Verify outcome state is the expected state
        assertEquals(transition.getOutcomeState(), outcomeState);
    }

    @Test
    public void testTrigger_WithValidEventAndFailedGuardConditions() {
        // Create Transition object
        Transition transition = new Transition("testTransition", new Event(), new IState(), new IState());

        // Create current state and event
        IState currentState = new IState();
        Event event = new Event();

        // Create failed guard condition
        IGuardCondition guardCondition = new IGuardCondition() {
            public boolean evaluate(Event event) {
                return false;
            }
        };
        transition.addGuardCondition(guardCondition);

        // Trigger transition
        IState outcomeState = transition.trigger(currentState, event);

        // Verify outcome state is the current state
        assertEquals(currentState, outcomeState);
    }

    @Test
    public void testWillTrigger_WithValidEventAndPassedGuardConditions() {
        // Create Transition object
        Transition transition = new Transition("testTransition", new Event(), new IState(), new IState());

        // Create current state and event
        IState currentState = new IState();
        Event event = new Event();

        // Create passed guard condition
        IGuardCondition guardCondition = new IGuardCondition() {
            public boolean evaluate(Event event) {
                return true;
            }
        };
        transition.addGuardCondition(guardCondition);

        // Verify transition will trigger
        assertTrue(transition.willTrigger(currentState, event));
    }

    @Test
    public void testWillTrigger_WithValidEventAndFailedGuardConditions() {
        // Create Transition object
        Transition transition = new Transition("testTransition", new Event(), new IState(), new IState());

        // Create current state and event
        IState currentState = new IState();
        Event event = new Event();

        // Create failed guard condition
        IGuardCondition guardCondition = new IGuardCondition() {
            public boolean evaluate(Event event) {
                return false;
            }
        };
        transition.addGuardCondition(guardCondition);

        // Verify transition will not trigger
        assertFalse(transition.willTrigger(currentState, event));
    }
}