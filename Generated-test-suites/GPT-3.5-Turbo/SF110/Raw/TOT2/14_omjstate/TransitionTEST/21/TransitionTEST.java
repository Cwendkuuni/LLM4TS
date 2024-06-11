import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition_IPredicate() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        transition.addGuardCondition(new Predicate());
        assertEquals(1, transition.guardConditions.size());
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        transition.addGuardCondition(new GuardCondition());
        assertEquals(1, transition.guardConditions.size());
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test1", new Event(), new State(), new State());
        Transition transition2 = new Transition("Test2", new Event(), new State(), new State());
        assertFalse(transition1.equals(transition2));
    }

    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        assertNotNull(transition.hashCode());
    }

    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        assertNotNull(transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        assertNotNull(transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        assertNotNull(transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        assertNotNull(transition.toString());
    }

    @Test
    public void testTrigger() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        Event event = new Event();
        IState outcomeState = transition.trigger(currentState, event);
        assertEquals(transition.getOutcomeState(), outcomeState);
    }

    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        Event event = new Event();
        assertTrue(transition.willTrigger(currentState, event));
    }
}