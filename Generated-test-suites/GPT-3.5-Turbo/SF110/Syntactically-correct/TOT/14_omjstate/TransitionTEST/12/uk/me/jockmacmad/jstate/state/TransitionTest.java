import static org.junit.Assert.*;
import org.junit.Test;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition_IPredicate() {
        Transition transition = new Transition("test", new Event("event"), new State("initial"), new State("outcome"));
        IPredicate guard = new Predicate();
        transition.addGuardCondition(guard);
        assertTrue(transition.getGuardConditions().contains(guard));
    }
    
    @Test
    public void testAddGuardCondition_IGuardCondition() {
        Transition transition = new Transition("test", new Event("event"), new State("initial"), new State("outcome"));
        IGuardCondition guard = new GuardCondition();
        transition.addGuardCondition(guard);
        assertTrue(transition.getGuardConditions().contains(guard));
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("test", new Event("event"), new State("initial"), new State("outcome"));
        Transition transition2 = new Transition("test", new Event("event"), new State("initial"), new State("outcome"));
        assertTrue(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        Transition transition = new Transition("test", new Event("event"), new State("initial"), new State("outcome"));
        assertNotNull(transition.hashCode());
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition("test", new Event("event"), new State("initial"), new State("outcome"));
        // Test when guardConditions is empty
        transition.evaluateGuardConditions();
        // Test when guardConditions is not empty
        IPredicate guard1 = new Predicate();
        IPredicate guard2 = new Predicate();
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        transition.evaluateGuardConditions();
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new State("initial");
        Transition transition = new Transition("test", new Event("event"), initialState, new State("outcome"));
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new State("outcome");
        Transition transition = new Transition("test", new Event("event"), new State("initial"), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event("event");
        Transition transition = new Transition("test", triggerEvent, new State("initial"), new State("outcome"));
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("test", new Event("event"), new State("initial"), new State("outcome"));
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTrigger() {
        IState currentState = new State("initial");
        IState outcomeState = new State("outcome");
        Event triggerEvent = new Event("event");
        
        // Test when guardConditions is empty and currentState and triggerEvent match
        Transition transition1 = new Transition("test", triggerEvent, currentState, outcomeState);
        assertEquals(outcomeState, transition1.trigger(currentState, triggerEvent));
        
        // Test when guardConditions is not empty and currentState and triggerEvent match, and passesGuards is true
        IPredicate guard = new Predicate();
        transition1.addGuardCondition(guard);
        assertEquals(outcomeState, transition1.trigger(currentState, triggerEvent));
        
        // Test when guardConditions is not empty and currentState and triggerEvent match, and passesGuards is false
        IGuardCondition falseGuard = new FalseGuardCondition();
        transition1.addGuardCondition(falseGuard);
        assertEquals(currentState, transition1.trigger(currentState, triggerEvent));
        
        // Test when currentState and triggerEvent does not match
        Transition transition2 = new Transition("test", triggerEvent, new State("other"), outcomeState);
        assertEquals(currentState, transition2.trigger(currentState, triggerEvent));
    }
    
    @Test
    public void testWillTrigger() {
        IState currentState = new State("initial");
        Event triggerEvent = new Event("event");
        
        // Test when guardConditions is empty and currentState and triggerEvent match
        Transition transition1 = new Transition("test", triggerEvent, currentState, new State("outcome"));
        assertTrue(transition1.willTrigger(currentState, triggerEvent));
        
        // Test when guardConditions is not empty and currentState and triggerEvent match, and passesGuards is true
        IPredicate guard = new Predicate();
        transition1.addGuardCondition(guard);
        assertTrue(transition1.willTrigger(currentState, triggerEvent));
        
        // Test when guardConditions is not empty and currentState and triggerEvent match, and passesGuards is false
        IGuardCondition falseGuard = new FalseGuardCondition();
        transition1.addGuardCondition(falseGuard);
        assertFalse(transition1.willTrigger(currentState, triggerEvent));
        
        // Test when currentState and triggerEvent does not match
        Transition transition2 = new Transition("test", triggerEvent, new State("other"), new State("outcome"));
        assertFalse(transition2.willTrigger(currentState, triggerEvent));
    }
}