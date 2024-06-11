import static org.junit.Assert.*;
import org.junit.Test;

public class TransitionTest {
    
    private Transition transition;

    @Before
    public void setUp() {
        transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
    }

    @Test
    public void testAddGuardCondition() {
        IPredicate predicate = new Predicate();
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        Transition sameTransition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertTrue(transition.equals(sameTransition));
    }

    @Test
    public void testHashCode() {
        Transition sameTransition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals(transition.hashCode(), sameTransition.hashCode());
    }

    @Test
    public void testGetInitialState() {
        assertEquals("InitialState", transition.getInitialState().getName());
    }

    @Test
    public void testGetOutcomeState() {
        assertEquals("OutcomeState", transition.getOutcomeState().getName());
    }

    @Test
    public void testGetTriggerEvent() {
        assertEquals("TestEvent", transition.getTriggerEvent().getName());
    }

    @Test
    public void testToString() {
        assertEquals("Transition[name=TestTransition, initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TestEvent]", transition.toString());
    }

    @Test
    public void testTrigger() {
        State currentState = new State("InitialState");
        Event event = new Event("TestEvent");
        assertEquals("OutcomeState", transition.trigger(currentState, event).getName());
    }

    @Test
    public void testWillTrigger() {
        State currentState = new State("InitialState");
        Event event = new Event("TestEvent");
        assertTrue(transition.willTrigger(currentState, event));
    }
}