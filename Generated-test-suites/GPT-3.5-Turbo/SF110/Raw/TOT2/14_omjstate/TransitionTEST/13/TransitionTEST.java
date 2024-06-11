
import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        IPredicate predicate = new Predicate(true);
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardConditionNull() {
        Transition transition = new Transition("Test", new Event("Test"), new State("Initial"), new State("Outcome"));
        transition.addGuardCondition(null);
        assertEquals(0, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardConditionEmptyEvent() {
        Transition transition = new Transition("Test", new Event(""), new State("Initial"), new State("Outcome"));
        IPredicate predicate = new Predicate(true);
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
    }

}

