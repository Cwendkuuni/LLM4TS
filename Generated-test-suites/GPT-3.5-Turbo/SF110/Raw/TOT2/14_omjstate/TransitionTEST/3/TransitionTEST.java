import static org.junit.Assert.*;
import org.junit.Test;

public class TransitionTest {

    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("Test", new Event("Event"), new State("Initial"), new State("Outcome"));
        assertEquals(0, transition.getGuardConditions().size());
        transition.addGuardCondition(new PredicateGuardCondition(true));
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test1", new Event("Event1"), new State("Initial1"), new State("Outcome1"));
        Transition transition2 = new Transition("Test2", new Event("Event2"), new State("Initial2"), new State("Outcome2"));
        assertTrue(transition1.equals(transition1));
        assertFalse(transition1.equals(transition2));
    }

    // Additional test cases for other methods...

}