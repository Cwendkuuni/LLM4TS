import org.junit.Test;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testAddGuardConditionWithIPredicate() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        transition.addGuardCondition(new Predicate("Test"));
        assertTrue(transition.getGuardConditions().size() == 1);
    }
    
    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        transition.addGuardCondition(new GuardCondition("Test"));
        assertTrue(transition.getGuardConditions().size() == 1);
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        Transition transition2 = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        assertTrue(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        assertNotNull(transition.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        assertEquals("Initial", transition.getInitialState().getName());
    }
    
    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        assertEquals("Outcome", transition.getOutcomeState().getName());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        assertEquals("Event", transition.getTriggerEvent().getName());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTrigger() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        State currentState = new State("Initial");
        Event event = new Event("Event");
        assertEquals("Outcome", transition.trigger(currentState, event).getName());
    }
    
    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        State currentState = new State("Initial");
        Event event = new Event("Event");
        assertTrue(transition.willTrigger(currentState, event));
    }

}