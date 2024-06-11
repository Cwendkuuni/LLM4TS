import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.predicate.*;
import java.util.*;

public class TransitionTest {
    
    // Public Methods
    
    @Test
    public void testAddGuardCondition_withIPredicate() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IPredicate guard = new Predicate();
        transition.addGuardCondition(guard);
        List<IGuardCondition> expectedConditions = new ArrayList<>();
        expectedConditions.add(guard);
        assertEquals(expectedConditions, transition.getGuardConditions());
    }
    
    @Test
    public void testAddGuardCondition_withIGuardCondition() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IGuardCondition guard = new GuardCondition();
        transition.addGuardCondition(guard);
        List<IGuardCondition> expectedConditions = new ArrayList<>();
        expectedConditions.add(guard);
        assertEquals(expectedConditions, transition.getGuardConditions());
    }
    
    @Test
    public void testEquals() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        Transition sameTransition = new Transition("Test", new Event(), new State(), new State());
        Transition differentTransition = new Transition("Different", new Event(), new State(), new State());
        assertTrue(transition.equals(sameTransition));
        assertFalse(transition.equals(differentTransition));
    }
    
    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        Transition sameTransition = new Transition("Test", new Event(), new State(), new State());
        Transition differentTransition = new Transition("Different", new Event(), new State(), new State());
        assertEquals(transition.hashCode(), sameTransition.hashCode());
        assertNotEquals(transition.hashCode(), differentTransition.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new State();
        Transition transition = new Transition("Test", new Event(), initialState, new State());
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new State();
        Transition transition = new Transition("Test", new Event(), new State(), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, new State(), new State());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        String expectedString = "Transition[name=Test, initialState=null, outcomeState=null, triggerEvent=null, guardConditions=[]]";
        assertEquals(expectedString, transition.toString());
    }
    
    @Test
    public void testTrigger_noGuardConditions() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        assertEquals(transition.getOutcomeState(), outcomeState);
    }
    
    @Test
    public void testTrigger_withGuardConditions_passesGuards() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        transition.addGuardCondition(new Predicate(true)); // Guard condition that always passes
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        assertEquals(transition.getOutcomeState(), outcomeState);
    }
    
    @Test
    public void testTrigger_withGuardConditions_failsGuards() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        transition.addGuardCondition(new Predicate(false)); // Guard condition that always fails
        IState outcomeState = transition.trigger(currentState, triggerEvent);
        assertEquals(currentState, outcomeState);
    }
    
    @Test
    public void testWillTrigger_noGuardConditions() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        assertTrue(willTrigger);
    }
    
    @Test
    public void testWillTrigger_withGuardConditions_passesGuards() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        transition.addGuardCondition(new Predicate(true)); // Guard condition that always passes
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        assertTrue(willTrigger);
    }
    
    @Test
    public void testWillTrigger_withGuardConditions_failsGuards() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        Event triggerEvent = new Event();
        transition.addGuardCondition(new Predicate(false)); // Guard condition that always fails
        boolean willTrigger = transition.willTrigger(currentState, triggerEvent);
        assertFalse(willTrigger);
    }

    // Edge Cases and Exception Handling
    
    @Test
    public void testAddGuardCondition_withNull() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        try {
            transition.addGuardCondition(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testEquals_withDifferentObject() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        assertFalse(transition.equals(new Object()));
    }
    
    @Test
    public void testEquals_withNull() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        assertFalse(transition.equals(null));
    }
    
    @Test
    public void testTrigger_withNullCurrentState() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        Event triggerEvent = new Event();
        IState outcomeState = transition.trigger(null, triggerEvent);
        assertNull(outcomeState);
    }
    
    @Test
    public void testTrigger_withNullTriggerEvent() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        IState outcomeState = transition.trigger(currentState, null);
        assertNull(outcomeState);
    }
    
    @Test
    public void testWillTrigger_withNullCurrentState() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        Event triggerEvent = new Event();
        boolean willTrigger = transition.willTrigger(null, triggerEvent);
        assertFalse(willTrigger);
    }
    
    @Test
    public void testWillTrigger_withNullTriggerEvent() {
        Transition transition = new Transition("Test", new Event(), new State(), new State());
        IState currentState = new State();
        boolean willTrigger = transition.willTrigger(currentState, null);
        assertFalse(willTrigger);
    }
}


