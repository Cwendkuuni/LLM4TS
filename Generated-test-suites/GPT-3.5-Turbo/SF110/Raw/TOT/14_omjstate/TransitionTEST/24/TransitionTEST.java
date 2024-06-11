import org.junit.Test;
import static org.junit.Assert.*;

public class TransitionTest {

    @Test
    public void testAddGuardCondition_withPredicate() {
        Transition transition = new Transition("TransitionTest", new Event(), new State(), new State());
        transition.addGuardCondition(new Predicate());
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testAddGuardCondition_withGuardCondition() {
        Transition transition = new Transition("TransitionTest", new Event(), new State(), new State());
        transition.addGuardCondition(new GuardCondition());
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testEquals_sameObject() {
        Transition transition = new Transition("TransitionTest", new Event(), new State(), new State());
        assertTrue(transition.equals(transition));
    }
    
    @Test
    public void testEquals_differentObjects() {
        Transition transition1 = new Transition("TransitionTest1", new Event(), new State(), new State());
        Transition transition2 = new Transition("TransitionTest2", new Event(), new State(), new State());
        assertFalse(transition1.equals(transition2));
    }
    
    @Test
    public void testHashCode_equalObjects() {
        Transition transition1 = new Transition("TransitionTest", new Event(), new State(), new State());
        Transition transition2 = new Transition("TransitionTest", new Event(), new State(), new State());
        assertEquals(transition1.hashCode(), transition2.hashCode());
    }
    
    @Test
    public void testHashCode_differentObjects() {
        Transition transition1 = new Transition("TransitionTest1", new Event(), new State(), new State());
        Transition transition2 = new Transition("TransitionTest2", new Event(), new State(), new State());
        assertNotEquals(transition1.hashCode(), transition2.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        State initialState = new State();
        Transition transition = new Transition("TransitionTest", new Event(), initialState, new State());
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        State outcomeState = new State();
        Transition transition = new Transition("TransitionTest", new Event(), new State(), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("TransitionTest", triggerEvent, new State(), new State());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testTrigger_passesGuards() {
        State initialState = new State();
        State outcomeState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("TransitionTest", triggerEvent, initialState, outcomeState);
        
        initialState.addTransition(transition);
        
        IState currentState = initialState;
        IState nextState = transition.trigger(currentState, triggerEvent);
        
        assertEquals(outcomeState, nextState);
    }
    
    @Test
    public void testTrigger_doesNotPassGuards() {
        State initialState = new State();
        State outcomeState = new State();
        Event triggerEvent = new Event();
        Predicate guardCondition = new Predicate();
        guardCondition.setEvaluateResult(false);
        
        Transition transition = new Transition("TransitionTest", triggerEvent, initialState, outcomeState);
        transition.addGuardCondition(guardCondition);
        
        initialState.addTransition(transition);
        
        IState currentState = initialState;
        IState nextState = transition.trigger(currentState, triggerEvent);
        
        assertEquals(initialState, nextState);
    }
    
    @Test
    public void testWillTrigger_passesGuards() {
        State initialState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("TransitionTest", triggerEvent, initialState, new State());
        Predicate guardCondition = new Predicate();
        guardCondition.setEvaluateResult(true);
        transition.addGuardCondition(guardCondition);
        
        assertTrue(transition.willTrigger(initialState, triggerEvent));
    }
    
    @Test
    public void testWillTrigger_doesNotPassGuards() {
        State initialState = new State();
        Event triggerEvent = new Event();
        Transition transition = new Transition("TransitionTest", triggerEvent, initialState, new State());
        Predicate guardCondition = new Predicate();
        guardCondition.setEvaluateResult(false);
        transition.addGuardCondition(guardCondition);
        
        assertFalse(transition.willTrigger(initialState, triggerEvent));
    }
}