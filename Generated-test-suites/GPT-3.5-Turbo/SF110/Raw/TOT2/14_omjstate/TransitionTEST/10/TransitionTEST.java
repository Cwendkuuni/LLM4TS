import static org.junit.Assert.*;
import org.junit.Test;

public class TransitionTest {

    @Test
    public void testAddGuardConditionWithIPrediate() {
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        transition.addGuardCondition(new Predicate("Test Predicate"));
        assertTrue(transition.getGuardConditions().size() == 1);
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test Transition 1", new Event("Test Event 1"), new State("Initial State 1"), new State("Outcome State 1"));
        Transition transition2 = new Transition("Test Transition 2", new Event("Test Event 2"), new State("Initial State 2"), new State("Outcome State 2"));
        assertFalse(transition1.equals(transition2));
    }

    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        assertNotNull(transition.hashCode());
    }

    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        assertEquals("Initial State", transition.getInitialState().getStateName());
    }

    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        assertEquals("Outcome State", transition.getOutcomeState().getStateName());
    }

    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        assertEquals("Test Event", transition.getTriggerEvent().getEventName());
    }

    @Test
    public void testTrigger() {
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        State currentState = new State("Initial State");
        Event event = new Event("Test Event");
        assertEquals("Outcome State", transition.trigger(currentState, event).getStateName());
    }

    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("Test Transition", new Event("Test Event"), new State("Initial State"), new State("Outcome State"));
        State currentState = new State("Initial State");
        Event event = new Event("Test Event");
        assertTrue(transition.willTrigger(currentState, event));
    }
}