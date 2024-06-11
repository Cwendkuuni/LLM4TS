import static org.junit.Assert.*;
import org.junit.Test;

public class TransitionTest {

    // Test case for addGuardCondition(IPredicate pGuard)
    @Test
    public void testAddGuardConditionWithPredicate() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        transition.addGuardCondition(new Predicate());
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    // Test case for addGuardCondition(IGuardCondition pGuard)
    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        transition.addGuardCondition(new GuardCondition());
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    // Test case for equals method
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test1", new Event("TestEvent1"), new State("InitialState1"), new State("OutcomeState1"));
        Transition transition2 = new Transition("Test2", new Event("TestEvent2"), new State("InitialState2"), new State("OutcomeState2"));
        assertFalse(transition1.equals(transition2));
    }
    
    // Test case for hashCode method
    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertNotNull(transition.hashCode());
    }
    
    // Test case for getInitialState method
    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("InitialState", transition.getInitialState().getName());
    }
    
    // Test case for getOutcomeState method
    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("OutcomeState", transition.getOutcomeState().getName());
    }
    
    // Test case for getTriggerEvent method
    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        assertEquals("TestEvent", transition.getTriggerEvent().getName());
    }
    
    // Test case for trigger method
    @Test
    public void testTrigger() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        State currentState = new State("InitialState");
        Event event = new Event("TestEvent");
        assertEquals("OutcomeState", transition.trigger(currentState, event).getName());
    }
    
    // Test case for willTrigger method
    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        State currentState = new State("InitialState");
        Event event = new Event("TestEvent");
        assertTrue(transition.willTrigger(currentState, event));
    }
}