package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.me.jockmacmad.jstate.predicate.IGuardCondition;
import uk.me.jockmacmad.jstate.predicate.IPredicate;
import uk.me.jockmacmad.jstate.state.Event;
import uk.me.jockmacmad.jstate.state.IState;
import uk.me.jockmacmad.jstate.state.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

    @Mock
    private IState initialStateMock;
    @Mock
    private IState outcomeStateMock;
    @Mock
    private Event triggerEventMock;
    @Mock
    private IGuardCondition guardConditionMock;
    @Mock
    private IPredicate predicateMock;

    private Transition transition;

    @Before
    public void setUp() {
        initialStateMock = mock(IState.class);
        outcomeStateMock = mock(IState.class);
        triggerEventMock = mock(Event.class);
        guardConditionMock = mock(IGuardCondition.class);
        predicateMock = mock(IPredicate.class);

        transition = new Transition("Test", triggerEventMock, initialStateMock, outcomeStateMock);
    }

    @Test
    public void testConstructor() {
        assertEquals("Test", transition.name);
        assertEquals(triggerEventMock, transition.triggerEvent);
        assertEquals(initialStateMock, transition.initialState);
        assertEquals(outcomeStateMock, transition.outcomeState);
    }

    @Test
    public void testAddGuardCondition() {
        transition.addGuardCondition(guardConditionMock);

        assertEquals(1, transition.guardConditions.size());
        assertEquals(guardConditionMock, transition.guardConditions.get(0));
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test", triggerEventMock, initialStateMock, outcomeStateMock);
        Transition transition2 = new Transition("Test", triggerEventMock, initialStateMock, outcomeStateMock);
        Transition transition3 = new Transition("Different", triggerEventMock, initialStateMock, outcomeStateMock);

        assertTrue(transition1.equals(transition1));
        assertTrue(transition1.equals(transition2));
        assertFalse(transition1.equals(transition3));
        assertFalse(transition1.equals(null));
        assertFalse(transition1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("Test", triggerEventMock, initialStateMock, outcomeStateMock);
        Transition transition2 = new Transition("Test", triggerEventMock, initialStateMock, outcomeStateMock);

        assertEquals(transition1.hashCode(), transition2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        assertEquals(initialStateMock, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        assertEquals(outcomeStateMock, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        assertEquals(triggerEventMock, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        assertEquals("Transition[name=Test, initialState=Mock for IState, outcomeState=Mock for IState, triggerEvent=Mock for Event]", transition.toString());
    }

    @Test
    public void testTrigger_passesGuards() {
        when(initialStateMock.equals(initialStateMock)).thenReturn(true);
        when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
        when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(true);

        IState result = transition.trigger(initialStateMock, triggerEventMock);

        assertEquals(outcomeStateMock, result);
    }

    @Test
    public void testTrigger_notPassesGuards() {
        when(initialStateMock.equals(initialStateMock)).thenReturn(true);
        when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
        when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(false);

        IState result = transition.trigger(initialStateMock, triggerEventMock);

        assertEquals(initialStateMock, result);
    }

    @Test
    public void testTrigger_notTriggerEvent() {
        when(initialStateMock.equals(initialStateMock)).thenReturn(true);
        when(triggerEventMock.equals(triggerEventMock)).thenReturn(false);

        IState result = transition.trigger(initialStateMock, triggerEventMock);

        assertEquals(initialStateMock, result);
    }

    @Test
    public void testTrigger_notInitialState() {
        when(initialStateMock.equals(initialStateMock)).thenReturn(false);

        IState result = transition.trigger(initialStateMock, triggerEventMock);

        assertEquals(initialStateMock, result);
    }

    @Test
    public void testWillTrigger_passesGuards() {
        when(initialStateMock.equals(initialStateMock)).thenReturn(true);
        when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
        when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(true);

        boolean result = transition.willTrigger(initialStateMock, triggerEventMock);

        assertTrue(result);
    }

    @Test
    public void testWillTrigger_notPassesGuards() {
        when(initialStateMock.equals(initialStateMock)).thenReturn(true);
        when(triggerEventMock.equals(triggerEventMock)).thenReturn(true);
        when(guardConditionMock.evaluate(triggerEventMock)).thenReturn(false);

        boolean result = transition.willTrigger(initialStateMock, triggerEventMock);

        assertFalse(result);
    }

    @Test
    public void testWillTrigger_notTriggerEvent() {
        when(initialStateMock.equals(initialStateMock)).thenReturn(true);
        when(triggerEventMock.equals(triggerEventMock)).thenReturn(false);

        boolean result = transition.willTrigger(initialStateMock, triggerEventMock);

        assertFalse(result);
    }

    @Test
    public void testWillTrigger_notInitialState() {
        when(initialStateMock.equals(initialStateMock)).thenReturn(false);

        boolean result = transition.willTrigger(initialStateMock, triggerEventMock);

        assertFalse(result);
    }
}