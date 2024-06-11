import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;

    @Before
    public void setUp() {
        initialState = new State("Initial");
        outcomeState = new State("Outcome");
        triggerEvent = new Event("Trigger");
        transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testTrigger() {
        IState currentState = initialState;
        Event event = triggerEvent;

        IState resultState = transition.trigger(currentState, event);

        assertEquals(outcomeState, resultState);
    }

    @Test
    public void testWillTrigger() {
        IState currentState = initialState;
        Event event = triggerEvent;

        boolean willTrigger = transition.willTrigger(currentState, event);

        assertTrue(willTrigger);
    }
}