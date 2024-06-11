import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {
    
    private Transition transition;
    
    @Before
    public void setUp() {
        transition = new Transition("transition", new Event(), new State(), new State());
    }
    
    @Test
    public void testAddGuardCondition() {
        IPredicate guard = new Predicate();
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testEquals() {
        Transition transition2 = new Transition("transition", new Event(), new State(), new State());
        assertEquals(transition, transition2);
    }
    
    @Test
    public void testHashCode() {
        Transition transition2 = new Transition("transition", new Event(), new State(), new State());
        assertEquals(transition.hashCode(), transition2.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        assertEquals(new State(), transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        assertEquals(new State(), transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        assertEquals(new Event(), transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTrigger() {
        IState currentState = new State();
        Event event = new Event();
        assertEquals(currentState, transition.trigger(currentState, event));
    }
    
    @Test
    public void testWillTrigger() {
        IState currentState = new State();
        Event event = new Event();
        assertFalse(transition.willTrigger(currentState, event));
    }
}