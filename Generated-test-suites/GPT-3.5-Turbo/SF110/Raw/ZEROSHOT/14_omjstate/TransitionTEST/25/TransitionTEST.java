

import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;
import uk.me.jockmacmad.jstate.predicate.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        Transition t1 = new Transition("t1", new Event(), new IState(), new IState());
        Transition t2 = new Transition("t1", new Event(), new IState(), new IState());

        assertEquals(t1, t2);
    }

    @Test
    public void testHashCode() {
        Transition t1 = new Transition("t1", new Event(), new IState(), new IState());
        Transition t2 = new Transition("t1", new Event(), new IState(), new IState());

        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition transition = new Transition("t1", new Event(), initialState, new IState());

        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition transition = new Transition("t1", new Event(), new IState(), outcomeState);

        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("t1", triggerEvent, new IState(), new IState());

        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        Transition transition = new Transition("t1", new Event(), new IState(), new IState());

        assertNotNull(transition.toString());
    }

    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("t1", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;
        Event event = triggerEvent;
        IState result = transition.trigger(currentState, event);

        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("t1", triggerEvent, initialState, new IState());

        IState currentState = initialState;
        Event event = triggerEvent;
        boolean result = transition.willTrigger(currentState, event);

        assertTrue(result);
    }

    @Test
    public void testAddGuardConditionWithIPredicate() {
        Transition transition = new Transition("t1", new Event(), new IState(), new IState());

        transition.addGuardCondition(new IPredicate());

        assertFalse(transition.guardConditions.isEmpty());
    }

    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        Transition transition = new Transition("t1", new Event(), new IState(), new IState());

        transition.addGuardCondition(new IGuardCondition());

        assertFalse(transition.guardConditions.isEmpty());
    }

    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition("t1", new Event(), new IState(), new IState());

        transition.evaluateGuardConditions();
    }
}
