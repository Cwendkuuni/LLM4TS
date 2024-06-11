import org.junit.Test;
import org.junit.Assert;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("transition1", new Event(), new IState(), new IState());
        IPredicate guard = new IPredicate();
        transition.addGuardCondition(guard);
        Assert.assertEquals(1, transition.guardConditions.size());
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("transition1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("transition1", new Event(), new IState(), new IState());
        Assert.assertTrue(transition1.equals(transition2));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("transition1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("transition1", new Event(), new IState(), new IState());
        Assert.assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition transition = new Transition("transition1", new Event(), initialState, new IState());
        Assert.assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition transition = new Transition("transition1", new Event(), new IState(), outcomeState);
        Assert.assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition1", triggerEvent, new IState(), new IState());
        Assert.assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        Transition transition = new Transition("transition1", new Event(), new IState(), new IState());
        Assert.assertNotNull(transition.toString());
    }

    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition1", triggerEvent, initialState, outcomeState);
        IState currentState = initialState;
        Event event = triggerEvent;
        IState result = transition.trigger(currentState, event);
        Assert.assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition1", triggerEvent, initialState, new IState());
        IState currentState = initialState;
        Event event = triggerEvent;
        boolean result = transition.willTrigger(currentState, event);
        Assert.assertTrue(result);
    }
}