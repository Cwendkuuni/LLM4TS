package uk.me.jockmacmad.jstate.state;

import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        transition = new Transition("Test", new Event(), new IState(), new IState());
    }

    @Test
    public void testAddGuardCondition_Predicate() {
        IPredicate predicate = null; // TODO: Initialize predicate
        transition.addGuardCondition(predicate);
        // TODO: Assert statement
    }

    @Test
    public void testAddGuardCondition_GuardCondition() {
        IGuardCondition guardCondition = null; // TODO: Initialize guardCondition
        transition.addGuardCondition(guardCondition);
        // TODO: Assert statement
    }

    @Test
    public void testEquals() {
        Object pTransition = null; // TODO: Initialize pTransition
        boolean result = transition.equals(pTransition);
        // TODO: Assert statement
    }

    @Test
    public void testHashCode() {
        int result = transition.hashCode();
        // TODO: Assert statement
    }

    @Test
    public void testEvaluateGuardConditions() {
        transition.evaluateGuardConditions();
        // TODO: Assert statement
    }

    @Test
    public void testGetInitialState() {
        IState result = transition.getInitialState();
        // TODO: Assert statement
    }

    @Test
    public void testGetOutcomeState() {
        IState result = transition.getOutcomeState();
        // TODO: Assert statement
    }

    @Test
    public void testGetTriggerEvent() {
        Event result = transition.getTriggerEvent();
        // TODO: Assert statement
    }

    @Test
    public void testToString() {
        String result = transition.toString();
        // TODO: Assert statement
    }

    @Test
    public void testTrigger() {
        IState pCurrentState = null; // TODO: Initialize pCurrentState
        Event pEvent = null; // TODO: Initialize pEvent
        IState result = transition.trigger(pCurrentState, pEvent);
        // TODO: Assert statement
    }

    @Test
    public void testWillTrigger() {
        IState pCurrentState = null; // TODO: Initialize pCurrentState
        Event pEvent = null; // TODO: Initialize pEvent
        boolean result = transition.willTrigger(pCurrentState, pEvent);
        // TODO: Assert statement
    }
}