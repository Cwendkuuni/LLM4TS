package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import uk.me.jockmacmad.jstate.predicate.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("test", new Event(), new IState(), new IState());
        IPredicate predicate = new IPredicate();
        IGuardCondition guardCondition = new IGuardCondition();

        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());

        transition.addGuardCondition(guardCondition);
        assertEquals(2, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("test", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("test", new Event(), new IState(), new IState());
        assertTrue(transition1.equals(transition2));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("test", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("test", new Event(), new IState(), new IState());
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition transition = new Transition("test", new Event(), initialState, new IState());
        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition transition = new Transition("test", new Event(), new IState(), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("test", triggerEvent, new IState(), new IState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        Transition transition = new Transition("test", new Event(), new IState(), new IState());
        String expected = "Transition{name='test', initialState=IState@hashcode, outcomeState=IState@hashcode, triggerEvent=Event@hashcode, guardConditions=[]}";
        assertEquals(expected, transition.toString());
    }

    @Test
    public void testTrigger_passesGuards() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        IGuardCondition guardCondition1 = new IGuardCondition();
        IGuardCondition guardCondition2 = new IGuardCondition();

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition1);
        guardConditions.add(guardCondition2);

        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);
        transition.setGuardConditions(guardConditions);

        IState currentState = initialState;

        assertTrue(transition.willTrigger(currentState, triggerEvent));

        currentState = transition.trigger(currentState, triggerEvent);

        assertEquals(outcomeState, currentState);
    }

    @Test
    public void testTrigger_passesNoGuards() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();

        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);

        IState currentState = initialState;

        assertTrue(transition.willTrigger(currentState, triggerEvent));

        currentState = transition.trigger(currentState, triggerEvent);

        assertEquals(outcomeState, currentState);
    }

    @Test
    public void testTrigger_doesNotPassGuards() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        IGuardCondition guardCondition1 = new IGuardCondition();
        IGuardCondition guardCondition2 = new IGuardCondition();

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition1);
        guardConditions.add(guardCondition2);

        Transition transition = new Transition("test", triggerEvent, initialState, outcomeState);
        transition.setGuardConditions(guardConditions);

        IState currentState = initialState;

        assertTrue(transition.willTrigger(currentState, triggerEvent));

        currentState = transition.trigger(currentState, triggerEvent);

        assertEquals(initialState, currentState);
    }

    @Test
    public void testWillTrigger_passesGuards() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        IGuardCondition guardCondition1 = new IGuardCondition();
        IGuardCondition guardCondition2 = new IGuardCondition();

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition1);
        guardConditions.add(guardCondition2);

        Transition transition = new Transition("test", triggerEvent, initialState, new IState());
        transition.setGuardConditions(guardConditions);

        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }

    @Test
    public void testWillTrigger_doesNotPassGuards() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        IGuardCondition guardCondition1 = new IGuardCondition();
        IGuardCondition guardCondition2 = new IGuardCondition();

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition1);
        guardConditions.add(guardCondition2);

        Transition transition = new Transition("test", triggerEvent, initialState, new IState());
        transition.setGuardConditions(guardConditions);

        assertFalse(transition.willTrigger(initialState, triggerEvent));
    }
}