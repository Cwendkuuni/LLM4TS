import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;
import uk.me.jockmacmad.jstate.predicate.*;

public class TransitionTest {
    
    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("transition", new Event(), new IState(), new IState());
        IPredicate guard = new IPredicate();
        transition.addGuardCondition(guard);
        assertEquals(1, transition.guardConditions.size());
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("transition1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("transition2", new Event(), new IState(), new IState());
        assertFalse(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        Transition transition = new Transition("transition", new Event(), new IState(), new IState());
        int hashCode = transition.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        Transition transition = new Transition("transition", new Event(), new IState(), new IState());
        transition.evaluateGuardConditions();
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition transition = new Transition("transition", new Event(), initialState, new IState());
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition transition = new Transition("transition", new Event(), new IState(), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, new IState(), new IState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("transition", new Event(), new IState(), new IState());
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, outcomeState);
        
        IState result = transition.trigger(initialState, triggerEvent);
        assertEquals(outcomeState, result);
    }
    
    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, initialState, new IState());
        
        boolean result = transition.willTrigger(initialState, triggerEvent);
        assertTrue(result);
    }
}