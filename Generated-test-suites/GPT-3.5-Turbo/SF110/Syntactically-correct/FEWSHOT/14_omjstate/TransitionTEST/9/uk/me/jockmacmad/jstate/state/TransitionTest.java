package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.me.jockmacmad.jstate.predicate.IGuardCondition;
import uk.me.jockmacmad.jstate.state.Event;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

    @Mock
    private IState initialState;
    @Mock
    private IState outcomeState;
    @Mock
    private Event triggerEvent;
    private Transition transition;

    @Before
    public void setUp() {
        initialState = mock(IState.class);
        outcomeState = mock(IState.class);
        triggerEvent = mock(Event.class);

        transition = new Transition("testTransition", triggerEvent, initialState, outcomeState);
    }

    @Test
    public void testAddGuardConditionWithIPredicate() {
        IGuardCondition guardCondition = mock(IGuardCondition.class);
        transition.addGuardCondition(guardCondition);

        List<IGuardCondition> guardConditions = transition.getGuardConditions();

        assertNotNull(guardConditions);
        assertEquals(1, guardConditions.size());
        assertEquals(guardCondition, guardConditions.get(0));
    }

    @Test
    public void testAddGuardConditionWithIGuardCondition() {
        IGuardCondition guardCondition = mock(IGuardCondition.class);
        transition.addGuardCondition(guardCondition);

        List<IGuardCondition> guardConditions = transition.getGuardConditions();

        assertNotNull(guardConditions);
        assertEquals(1, guardConditions.size());
        assertEquals(guardCondition, guardConditions.get(0));
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
        Transition transition3 = new Transition("anotherTransition", triggerEvent, initialState, outcomeState);

        assertTrue(transition.equals(transition));
        assertTrue(transition.equals(transition1));
        assertTrue(transition1.equals(transition));
        assertTrue(transition1.equals(transition2));
        assertFalse(transition.equals(transition3));
        assertFalse(transition3.equals(transition));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("testTransition", triggerEvent, initialState, outcomeState);
        Transition transition2 = new Transition("testTransition", triggerEvent, initialState, outcomeState);

        assertEquals(transition.hashCode(), transition1.hashCode());
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = transition.getInitialState();

        assertNotNull(initialState);
        assertEquals(this.initialState, initialState);
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = transition.getOutcomeState();

        assertNotNull(outcomeState);
        assertEquals(this.outcomeState, outcomeState);
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = transition.getTriggerEvent();

        assertNotNull(triggerEvent);
        assertEquals(this.triggerEvent, triggerEvent);
    }

    @Test
    public void testToString() {
        String transitionString = transition.toString();

        assertNotNull(transitionString);
        assertFalse(transitionString.isEmpty());
    }

    @Test
    public void testTriggerWhenPassesGuards() {
        IState currentState = mock(IState.class);
        Event event = mock(Event.class);
        IGuardCondition guardCondition = mock(IGuardCondition.class);

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);

        transition.addGuardCondition(guardCondition);

        when(this.initialState.equals(currentState)).thenReturn(true);
        when(this.triggerEvent.equals(event)).thenReturn(true);
        when(guardCondition.evaluate(event)).thenReturn(true);
        when(this.outcomeState.equals(currentState)).thenReturn(false);

        IState outcomeState = transition.trigger(currentState, event);

        assertNotNull(outcomeState);
        assertEquals(this.outcomeState, outcomeState);
    }

    @Test
    public void testTriggerWhenDoesNotPassGuards() {
        IState currentState = mock(IState.class);
        Event event = mock(Event.class);
        IGuardCondition guardCondition = mock(IGuardCondition.class);

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);

        transition.addGuardCondition(guardCondition);

        when(this.initialState.equals(currentState)).thenReturn(true);
        when(this.triggerEvent.equals(event)).thenReturn(true);
        when(guardCondition.evaluate(event)).thenReturn(false);
        when(this.outcomeState.equals(currentState)).thenReturn(false);

        IState outcomeState = transition.trigger(currentState, event);

        assertNotNull(outcomeState);
        assertEquals(currentState, outcomeState);
    }

    @Test
    public void testWillTriggerWhenPassesGuards() {
        IState currentState = mock(IState.class);
        Event event = mock(Event.class);
        IGuardCondition guardCondition = mock(IGuardCondition.class);

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);

        transition.addGuardCondition(guardCondition);

        when(this.initialState.equals(currentState)).thenReturn(true);
        when(this.triggerEvent.equals(event)).thenReturn(true);
        when(guardCondition.evaluate(event)).thenReturn(true);

        boolean willTrigger = transition.willTrigger(currentState, event);

        assertTrue(willTrigger);
    }

    @Test
    public void testWillTriggerWhenDoesNotPassGuards() {
        IState currentState = mock(IState.class);
        Event event = mock(Event.class);
        IGuardCondition guardCondition = mock(IGuardCondition.class);

        List<IGuardCondition> guardConditions = new ArrayList<>();
        guardConditions.add(guardCondition);

        transition.addGuardCondition(guardCondition);

        when(this.initialState.equals(currentState)).thenReturn(true);
        when(this.triggerEvent.equals(event)).thenReturn(true);
        when(guardCondition.evaluate(event)).thenReturn(false);

        boolean willTrigger = transition.willTrigger(currentState, event);

        assertFalse(willTrigger);
    }
}