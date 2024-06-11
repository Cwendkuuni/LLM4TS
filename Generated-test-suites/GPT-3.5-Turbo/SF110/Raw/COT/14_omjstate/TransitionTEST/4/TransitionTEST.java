import org.junit.*;
import static org.junit.Assert.*;

public class TransitionTest {
    private Transition transition;

    @Before
    public void setUp() {
        transition = new Transition("Transition", new Event(), new State(), new State());
    }

    @Test
    public void testAddGuardCondition() {
        transition.addGuardCondition(new GuardCondition());
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        Transition otherTransition = new Transition("Transition", new Event(), new State(), new State());
        assertTrue(transition.equals(otherTransition));
    }

    @Test
    public void testHashCode() {
        Transition otherTransition = new Transition("Transition", new Event(), new State(), new State());
        assertEquals(transition.hashCode(), otherTransition.hashCode());
    }

    @Test
    public void testGetInitialState() {
        State initialState = new State();
        transition = new Transition("Transition", new Event(), initialState, new State());
        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        State outcomeState = new State();
        transition = new Transition("Transition", new Event(), new State(), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        transition = new Transition("Transition", triggerEvent, new State(), new State());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        String str = transition.toString();
        assertNotNull(str);
    }

    @Test
    public void testTrigger() {
        IState currentState = new State();
        Event triggerEvent = new Event();
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testWillTrigger() {
        IState currentState = new State();
        Event triggerEvent = new Event();
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        assertTrue(willTrigger);
    }
}

