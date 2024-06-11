import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        transition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
    }

    @Test
    public void testAddGuardCondition_IPredicate() {
        transition.addGuardCondition(new Predicate());
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        transition.addGuardCondition(new GuardCondition());
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        Transition anotherTransition = new Transition("TestTransition", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertTrue(transition.equals(anotherTransition));
    }

    @Test
    public void testHashCode() {
        assertEquals(transition.hashCode(), transition.hashCode());
    }

    @Test
    public void testEvaluateGuardConditions() {
        // Test case for evaluateGuardConditions method can be added here
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
        assertEquals("Transition{name=TestTransition, initialState=InitialState, outcomeState=OutcomeState, triggerEvent=TestEvent}", transition.toString());
    }

    @Test
    public void testTrigger() {
        State initialState = new State("InitialState");
        State outcomeState = new State("OutcomeState");
        Event triggerEvent = new Event("TestEvent");
        assertTrue(transition.willTrigger(initialState, triggerEvent));
        assertEquals(outcomeState, transition.trigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger() {
        assertTrue(transition.willTrigger(new State("InitialState"), new Event("TestEvent")));
    }
}