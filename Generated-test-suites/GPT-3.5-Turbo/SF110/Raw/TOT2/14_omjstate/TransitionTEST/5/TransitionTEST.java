import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class TransitionTest {

    @Test
    public void testAddGuardCondition1() {
        Transition transition = new Transition("Test", new Event("test"), new State("Initial"), new State("Outcome"));
        transition.addGuardCondition(new Predicate());
        assertNotNull(transition);
    }
    
    @Test
    public void testAddGuardCondition2() {
        Transition transition = new Transition("Test", new Event("test"), new State("Initial"), new State("Outcome"));
        transition.addGuardCondition(new GuardCondition());
        assertNotNull(transition);
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test1", new Event("test"), new State("Initial"), new State("Outcome"));
        Transition transition2 = new Transition("Test2", new Event("test"), new State("Initial"), new State("Outcome"));
        assertNotEquals(transition1, transition2);
    }
    
    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test", new Event("test"), new State("Initial"), new State("Outcome"));
        assertNotNull(transition.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("Test", new Event("test"), new State("Initial"), new State("Outcome"));
        assertEquals("Initial", transition.getInitialState().getName());
    }
    
    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("Test", new Event("test"), new State("Initial"), new State("Outcome"));
        assertEquals("Outcome", transition.getOutcomeState().getName());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("Test", new Event("test"), new State("Initial"), new State("Outcome"));
        assertEquals("test", transition.getTriggerEvent().getName());
    }
    
    @Test
    public void testTrigger() {
        Transition transition = new Transition("Test", new Event("test"), new State("Initial"), new State("Outcome"));
        State currentState = new State("Initial");
        State nextState = new State("Outcome");
        Event event = new Event("test");
        assertEquals(nextState, transition.trigger(currentState, event));
    }
    
    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("Test", new Event("test"), new State("Initial"), new State("Outcome"));
        State currentState = new State("Initial");
        Event event = new Event("test");
        assertTrue(transition.willTrigger(currentState, event));
    }

}