import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition1() {
        Transition transition = new Transition("transition", new Event(), new IState(), new IState());
        IPredicate predicate = new IPredicate();
        transition.addGuardCondition(predicate);
        assertTrue(transition.getGuardConditions().contains(predicate));
    }

    @Test
    public void testAddGuardCondition2() {
        Transition transition = new Transition("transition", new Event(), new IState(), new IState());
        IGuardCondition guardCondition = new IGuardCondition();
        transition.addGuardCondition(guardCondition);
        assertTrue(transition.getGuardConditions().contains(guardCondition));
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("transition", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("transition", new Event(), new IState(), new IState());
        assertTrue(transition1.equals(transition2));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("transition", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("transition", new Event(), new IState(), new IState());
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        IState currentState = new IState();
        Event event = new Event();
        IState result = transition.trigger(currentState, event);
        assertEquals(initialState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, new IState());
        IState currentState = new IState();
        Event event = new Event();
        boolean result = transition.willTrigger(currentState, event);
        assertFalse(result);
    }
}