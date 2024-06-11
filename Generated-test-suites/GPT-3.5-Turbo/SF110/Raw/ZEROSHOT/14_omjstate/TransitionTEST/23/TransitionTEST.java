import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {
    
    @Test
    public void testAddGuardCondition_IPredicate() {
        Transition transition = new Transition("Test Transition", new Event(), new IState(), new IState());
        IPredicate predicate = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        };
        
        transition.addGuardCondition(predicate);
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testAddGuardCondition_IGuardCondition() {
        Transition transition = new Transition("Test Transition", new Event(), new IState(), new IState());
        IGuardCondition condition = new IGuardCondition() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        };
        
        transition.addGuardCondition(condition);
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test Transition 1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("Test Transition 2", new Event(), new IState(), new IState());
        assertFalse(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test Transition", new Event(), new IState(), new IState());
        assertTrue(transition.hashCode() > 0);
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
        assertNotNull(transition.toString());
    }
    
    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        
        Transition transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState result = transition.trigger(currentState, event);
        
        assertEquals(outcomeState, result);
    }
    
    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        
        Transition transition = new Transition("Test Transition", triggerEvent, initialState, new IState());
        IState currentState = initialState;
        Event event = triggerEvent;
        
        boolean result = transition.willTrigger(currentState, event);
        
        assertTrue(result);
    }
}