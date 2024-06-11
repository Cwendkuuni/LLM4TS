import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    // Test cases for addGuardCondition(IPredicate pGuard) method
    @Test
    public void testAddGuardCondition_IPredicate() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        IPredicate predicate = new TruePredicate();
        transition.addGuardCondition(predicate);
        assertTrue(transition.getGuardConditions().contains(predicate));
    }

    // Test cases for addGuardCondition(IGuardCondition pGuard) method
    @Test
    public void testAddGuardCondition_IGuardCondition() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        IGuardCondition guardCondition = new TrueGuardCondition();
        transition.addGuardCondition(guardCondition);
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    // Test cases for equals(Object pTransition) method
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        Transition transition2 = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        assertTrue(transition1.equals(transition2));
    }

    // Test cases for hashCode() method
    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        assertNotNull(transition.hashCode());
    }

    // Test cases for getInitialState() method
    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        assertEquals("Initial", transition.getInitialState().toString());
    }

    // Test cases for getOutcomeState() method
    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        assertEquals("Outcome", transition.getOutcomeState().toString());
    }

    // Test cases for getTriggerEvent() method
    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        assertEquals("Test", transition.getTriggerEvent().toString());
    }

    // Test cases for trigger(IState pCurrentState, Event pEvent) method
    @Test
    public void testTrigger() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        IState currentState = new State("Initial");
        Event event = new Event("Test");
        IState newState = transition.trigger(currentState, event);
        assertEquals("Outcome", newState.toString());
    }

    // Test cases for willTrigger(IState pCurrentState, Event pEvent) method
    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        IState currentState = new State("Initial");
        Event event = new Event("Test");
        assertTrue(transition.willTrigger(currentState, event));
    }
}