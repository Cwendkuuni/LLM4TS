
import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("Test", new Event("testEvent"), new State("initialState"), new State("outcomeState"));
        IPredicate predicate = new Predicate();
        IGuardCondition guardCondition = new GuardCondition(predicate);

        transition.addGuardCondition(predicate);
        assertTrue(transition.getGuardConditions().contains(predicate));

        transition.addGuardCondition(guardCondition);
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

}
