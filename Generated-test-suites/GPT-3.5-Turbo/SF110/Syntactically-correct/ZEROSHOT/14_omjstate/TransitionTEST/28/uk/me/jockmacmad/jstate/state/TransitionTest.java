package uk.me.jockmacmad.jstate.state;

import org.junit.Test;
import org.junit.Assert;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("test", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("test", new Event(), new IState(), new IState());
        Assert.assertEquals(transition1, transition2);
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("test", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("test", new Event(), new IState(), new IState());
        Assert.assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        Transition transition = new Transition("test", new Event(), new IState(), new IState());
        Assert.assertNotNull(transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        Transition transition = new Transition("test", new Event(), new IState(), new IState());
        Assert.assertNotNull(transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Transition transition = new Transition("test", new Event(), new IState(), new IState());
        Assert.assertNotNull(transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        Transition transition = new Transition("test", new Event(), new IState(), new IState());
        Assert.assertNotNull(transition.toString());
    }

    @Test
    public void testTrigger() {
        Transition transition = new Transition("test", new Event(), new IState(), new IState());
        IState currentState = new IState();
        Event event = new Event();
        IState outcomeState = transition.trigger(currentState, event);
        Assert.assertNotNull(outcomeState);
    }

    @Test
    public void testWillTrigger() {
        Transition transition = new Transition("test", new Event(), new IState(), new IState());
        IState currentState = new IState();
        Event event = new Event();
        boolean willTrigger = transition.willTrigger(currentState, event);
        Assert.assertFalse(willTrigger);
    }
}