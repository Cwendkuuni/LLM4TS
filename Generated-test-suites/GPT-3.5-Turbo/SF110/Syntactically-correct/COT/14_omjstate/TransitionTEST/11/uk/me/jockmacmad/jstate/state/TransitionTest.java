import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("Test Transition", new Event(), new IState(), new IState());
        IPredicate guard = new IPredicateImpl();
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testAddGuardCondition_IGuardCondition() {
        Transition transition = new Transition("Test Transition", new Event(), new IState(), new IState());
        IGuardCondition guard = new IGuardConditionImpl();
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Transition 1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("Transition 1", new Event(), new IState(), new IState());
        assertTrue(transition1.equals(transition1));
        assertFalse(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("Transition 1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("Transition 1", new Event(), new IState(), new IState());
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        // Unable to test as it is a protected method
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition transition = new Transition("Test Transition", new Event(), initialState, new IState());
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition transition = new Transition("Test Transition", new Event(), new IState(), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test Transition", triggerEvent, new IState(), new IState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("Test Transition", new Event(), new IState(), new IState());
        assertEquals("Test Transition", transition.toString());
    }
    
    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        IState currentState = initialState;
        IState result = transition.trigger(currentState, triggerEvent);
        assertEquals(outcomeState, result);
    }
    
    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test Transition", triggerEvent, initialState, new IState());
        IState currentState = initialState;
        boolean result = transition.willTrigger(currentState, triggerEvent);
        assertTrue(result);
    }
}