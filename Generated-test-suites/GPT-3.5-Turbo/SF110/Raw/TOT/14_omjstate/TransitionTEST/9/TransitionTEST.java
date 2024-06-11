import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TransitionTest {
    
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    
    @Before
    public void setUp() {
        initialState = new IStateImpl("Initial");
        outcomeState = new IStateImpl("Outcome");
        triggerEvent = new Event("Trigger");
        transition = new Transition("Transition1", triggerEvent, initialState, outcomeState);
    }
    
    @Test
    public void testAddGuardCondition() {
        IPredicate guard1 = new IPredicateImpl();
        IGuardCondition guard2 = new IGuardConditionImpl();
        
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        
        Assert.assertEquals(2, transition.guardConditions.size());
    }
    
    @Test
    public void testEquals() {
        Transition transition2 = new Transition("Transition2", triggerEvent, initialState, outcomeState);
        
        boolean result = transition.equals(transition2);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testHashCode() {
        int hashCode = transition.hashCode();
        
        Assert.assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testEvaluateGuardConditions() {
        // TODO: Implement test for evaluateGuardConditions()
    }
    
    @Test
    public void testGetInitialState() {
        IState initial = transition.getInitialState();
        
        Assert.assertEquals(initialState, initial);
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcome = transition.getOutcomeState();
        
        Assert.assertEquals(outcomeState, outcome);
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event trigger = transition.getTriggerEvent();
        
        Assert.assertEquals(triggerEvent, trigger);
    }
    
    @Test
    public void testToString() {
        String string = transition.toString();
        
        Assert.assertNotNull(string);
    }
    
    @Test
    public void testTrigger_noGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IState outcome = transition.trigger(currentState, event);
        
        Assert.assertEquals(outcomeState, outcome);
    }
    
    @Test
    public void testTrigger_withGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IPredicate guard1 = new IPredicateImpl(true);
        IPredicate guard2 = new IPredicateImpl(false);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        
        IState outcome = transition.trigger(currentState, event);
        
        Assert.assertEquals(initialState, outcome);
    }
    
    @Test
    public void testWillTrigger_noGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        Assert.assertTrue(willTrigger);
    }
    
    @Test
    public void testWillTrigger_withGuardConditions() {
        IState currentState = initialState;
        Event event = triggerEvent;
        
        IGuardCondition guard1 = new IGuardConditionImpl(true);
        IGuardCondition guard2 = new IGuardConditionImpl(false);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        
        boolean willTrigger = transition.willTrigger(currentState, event);
        
        Assert.assertFalse(willTrigger);
    }
}