import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import uk.me.jockmacmad.jstate.predicate.*;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    private IState initialState;
    private IState outcomeState;
    private List<IGuardCondition> guardConditions;
    private Event triggerEvent;
    private Transition transition;

    @Before
    public void setUp() {
        initialState = new State("Initial");
        outcomeState = new State("Outcome");
        guardConditions = new ArrayList<>();
        triggerEvent = new Event("Test");
        transition = new Transition("TestTransition", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testAddGuardCondition_IPredicate() {
        IPredicate guard = new CustomPredicate();
        transition.addGuardCondition(guard);
        assertTrue(transition.getGuardConditions().contains(guard));
    }

    // More test cases for other public methods can be added here

}