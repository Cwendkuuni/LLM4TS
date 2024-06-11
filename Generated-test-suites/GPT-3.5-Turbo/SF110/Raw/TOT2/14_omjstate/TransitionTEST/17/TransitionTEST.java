import static org.junit.Assert.*;
import org.junit.Test;

public class TransitionTest {
    
    @Test
    public void testAddGuardCondition_IPredicate() {
        // Test adding guard condition with IPredicate
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        transition.addGuardCondition(new Predicate("TestPredicate"));
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testAddGuardCondition_IGuardCondition() {
        // Test adding guard condition with IGuardCondition
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        transition.addGuardCondition(new GuardCondition("TestCondition"));
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testEquals() {
        // Test equals method
        Transition transition1 = new Transition("Test1", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState1"));
        Transition transition2 = new Transition("Test2", new Event("TestEvent2"), new State("InitialState2"), new State("OutcomeState2"));
        assertFalse(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        // Test hashCode method
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertNotNull(transition.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        // Test getInitialState method
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("InitialState", transition.getInitialState().getName());
    }
    
    @Test
    public void testGetOutcomeState() {
        // Test getOutcomeState method
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("OutcomeState", transition.getOutcomeState().getName());
    }
    
    @Test
    public void testGetTriggerEvent() {
        // Test getTriggerEvent method
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("TestEvent", transition.getTriggerEvent().getName());
    }
    
    @Test
    public void testToString() {
        // Test toString method
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTrigger() {
        // Test trigger method
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        State initialState = new State("InitialState");
        State outcomeState = transition.trigger(initialState, new Event("TestEvent"));
        assertEquals("OutcomeState", outcomeState.getName());
    }
    
    @Test
    public void testWillTrigger() {
        // Test willTrigger method
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        State initialState = new State("InitialState");
        assertTrue(transition.willTrigger(initialState, new Event("TestEvent")));
    }
}