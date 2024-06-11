import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setup() {
        transition = new Transition("test", new Event("trigger"), new IState(), new IState());
    }

    @Test
    public void testAddGuardCondition() {
        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        Transition transition2 = new Transition("test", new Event("trigger"), new IState(), new IState());
        assertTrue(transition.equals(transition2));
    }

    @Test
    public void testHashCode() {
        Transition transition2 = new Transition("test", new Event("trigger"), new IState(), new IState());
        assertEquals(transition.hashCode(), transition2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        transition = new Transition("test", new Event("trigger"), initialState, new IState());
        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        transition = new Transition("test", new Event("trigger"), new IState(), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event("trigger");
        transition = new Transition("test", triggerEvent, new IState(), new IState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        assertNotNull(transition.toString());
    }

    @Test
    public void testTrigger() {
        IState currentState = new IState();
        Event triggerEvent = new Event("trigger");
        IState outcomeState = new IState();
        transition = new Transition("test", triggerEvent, currentState, outcomeState);

        IState result = transition.trigger(currentState, triggerEvent);
        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        IState currentState = new IState();
        Event triggerEvent = new Event("trigger");
        transition = new Transition("test", triggerEvent, currentState, new IState());

        boolean result = transition.willTrigger(currentState, triggerEvent);
        assertTrue(result);
    }
}