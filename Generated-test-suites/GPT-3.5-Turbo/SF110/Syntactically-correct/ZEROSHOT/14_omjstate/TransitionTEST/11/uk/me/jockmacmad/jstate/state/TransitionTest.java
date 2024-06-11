import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        Event event = new Event();
        IState initialState = new IState();
        IState outcomeState = new IState();
        transition = new Transition("Transition", event, initialState, outcomeState);
    }

    @Test
    public void testAddGuardCondition_IPredicate() {
        IPredicate guard = new IPredicate();
        transition.addGuardCondition(guard);
        List<IGuardCondition> guardConditions = transition.getGuardConditions();

        assertTrue(guardConditions.contains(guard));
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        IGuardCondition guard = new IGuardCondition();
        transition.addGuardCondition(guard);
        List<IGuardCondition> guardConditions = transition.getGuardConditions();

        assertTrue(guardConditions.contains(guard));
    }

    @Test
    public void testEquals() {
        Transition otherTransition = new Transition("Transition", new Event(), new IState(), new IState());

        assertTrue(transition.equals(otherTransition));
    }

    @Test
    public void testHashCode() {
        Transition otherTransition = new Transition("Transition", new Event(), new IState(), new IState());

        assertEquals(transition.hashCode(), otherTransition.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = transition.getInitialState();
        assertNotNull(initialState);
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = transition.getOutcomeState();
        assertNotNull(outcomeState);
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = transition.getTriggerEvent();
        assertNotNull(triggerEvent);
    }

    @Test
    public void testToString() {
        String expected = "Transition";
        String actual = transition.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testTrigger() {
        IState currentState = new IState();
        Event event = new Event();
        IState outcomeState = transition.trigger(currentState, event);

        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testWillTrigger() {
        IState currentState = new IState();
        Event event = new Event();
        boolean willTrigger = transition.willTrigger(currentState, event);

        assertTrue(willTrigger);
    }
    
    @After
    public void tearDown() {
        transition = null;
    }
}