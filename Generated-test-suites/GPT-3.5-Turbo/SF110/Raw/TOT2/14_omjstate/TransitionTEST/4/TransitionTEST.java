import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        Event event = new Event("testEvent");
        IState initialState = new State("initialState");
        IState outcomeState = new State("outcomeState");
        transition = new Transition("testTransition", event, initialState, outcomeState);
    }

    @Test
    public void testAddGuardCondition_IPredicate() {
        IPredicate guard = new TruePredicate(); // Assuming TruePredicate is implemented
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        IGuardCondition guard = new TrueGuardCondition(); // Assuming TrueGuardCondition is implemented
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        Transition sameTransition = new Transition("testTransition", new Event("testEvent"), new State("initialState"), new State("outcomeState"));
        assertEquals(transition, sameTransition);
    }

    @Test
    public void testHashCode() {
        assertEquals(transition.hashCode(), transition.hashCode());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}