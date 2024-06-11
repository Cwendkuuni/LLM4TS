import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TransitionTest {

    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("Test", new Event(), new IState(), new IState());
        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };
        transition.addGuardCondition(guard);
        Assert.assertEquals(1, transition.getGuardConditions().size());
    }

    @Test
    public void testEquals() {
        Transition transition1 = new Transition("Test1", new Event(), new IState(), new IState());
        Transition transition2 = new Transition("Test2", new Event(), new IState(), new IState());
        Transition transition3 = transition1;
        Assert.assertTrue(transition1.equals(transition3));
        Assert.assertFalse(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode() {
        Transition transition = new Transition("Test", new Event(), new IState(), new IState());
        Assert.assertEquals(transition.hashCode(), transition.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        Transition transition = new Transition("Test", new Event(), initialState, new IState());
        Assert.assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        Transition transition = new Transition("Test", new Event(), new IState(), outcomeState);
        Assert.assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, new IState(), new IState());
        Assert.assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        String toString = "Transition[name=Test, initialState=IState@1, outcomeState=IState@2, triggerEvent=Event@3]";
        Transition transition = new Transition("Test", new Event(), new IState(), new IState());
        Assert.assertEquals(toString, transition.toString());
    }
    
    @Test
    public void testTrigger_PassesGuards() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);
        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };
        transition.addGuardCondition(guard);
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState result = transition.trigger(currentState, event);
        
        Assert.assertEquals(outcomeState, result);
    }
        
    @Test
    public void testTrigger_FailsGuards() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, outcomeState);
        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        };
        transition.addGuardCondition(guard);
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState result = transition.trigger(currentState, event);
        
        Assert.assertEquals(currentState, result);
    }
    
    @Test
    public void testWillTrigger_PassesGuards() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, new IState());
        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                return true;
            }
        };
        transition.addGuardCondition(guard);
        IState currentState = initialState;
        Event event = triggerEvent;
        
        boolean result = transition.willTrigger(currentState, event);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testWillTrigger_FailsGuards() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        Transition transition = new Transition("Test", triggerEvent, initialState, new IState());
        IPredicate guard = new IPredicate() {
            @Override
            public boolean evaluate(Event event) {
                return false;
            }
        };
        transition.addGuardCondition(guard);
        IState currentState = initialState;
        Event event = triggerEvent;
        
        boolean result = transition.willTrigger(currentState, event);
        
        Assert.assertFalse(result);
    }
}

