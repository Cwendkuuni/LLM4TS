import static org.junit.Assert.*;
import org.junit.Test;

public class TransitionTest {

    @Test
    public void testAddGuardCondition_IPredicate() {
        // Test adding guard condition with IPredicate
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        transition.addGuardCondition(new Predicate());

        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        // Test adding guard condition with IGuardCondition
        Transition transition = new Transition("Test", new Event("TestEvent"), new State("InitialState"), new State("OutcomeState"));
        transition.addGuardCondition(new GuardCondition());

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

    // Add more test cases for other methods

}