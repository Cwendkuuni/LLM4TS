import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TransitionTest {
    
    @Test
    public void testAddGuardCondition() {
        Transition transition = new Transition("transition", new Event(), new State(), new State());
        IPredicate guard = new GuardCondition();
        transition.addGuardCondition(guard);
        assertEquals(1, transition.getGuardConditions().size());
    }
    
    @Test
    public void testEquals() {
        Transition transition1 = new Transition("transition1", new Event(), new State(), new State());
        Transition transition2 = new Transition("transition2", new Event(), new State(), new State());
        Transition transition3 = new Transition("transition1", new Event(), new State(), new State());
        
        assertEquals(false, transition1.equals(transition2));
        assertEquals(true, transition1.equals(transition3));
    }
    
    @Test
    public void testHashCode() {
        Transition transition1 = new Transition("transition1", new Event(), new State(), new State());
        Transition transition2 = new Transition("transition2", new Event(), new State(), new State());
        
        assertEquals(false, transition1.hashCode() == transition2.hashCode());
        assertEquals(true, transition1.hashCode() == transition1.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        IState initialState = new State();
        Transition transition = new Transition("transition", new Event(), initialState, new State());
        
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new State();
        Transition transition = new Transition("transition", new Event(), new State(), outcomeState);
        
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        Transition transition = new Transition("transition", triggerEvent, new State(), new State());
        
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        Transition transition = new Transition("transition", new Event(), new State(), new State());
        
        assertEquals("Transition[name=transition, initialState=State@hashcode, outcomeState=State@hashcode, triggerEvent=Event@hashcode, guardConditions=[]]", transition.toString());
    }
    
    @Test
    public void testTrigger_noGuards() {
        IState pCurrentState = new State();
        Event pEvent = new Event();
        IState outcomeState = new State();
        Transition transition = new Transition("transition", pEvent, pCurrentState, outcomeState);
        
        IState actualOutcomeState = transition.trigger(pCurrentState, pEvent);
        
        assertEquals(outcomeState, actualOutcomeState);
    }
    
    @Test
    public void testTrigger_withGuards() {
        IState pCurrentState = new State();
        Event pEvent = new Event();
        IState outcomeState = new State();
        Transition transition = new Transition("transition", pEvent, pCurrentState, outcomeState);
        transition.addGuardCondition(new GuardCondition());
        
        IState actualOutcomeState = transition.trigger(pCurrentState, pEvent);
        
        assertEquals(outcomeState, actualOutcomeState);
    }
    
    @Test
    public void testWillTrigger_false() {
        IState pCurrentState = new State();
        Event pEvent = new Event();
        Transition transition = new Transition("transition", new Event(), new State(), new State());
        
        boolean willTrigger = transition.willTrigger(pCurrentState, pEvent);
        
        assertEquals(false, willTrigger);
    }
    
    @Test
    public void testWillTrigger_true() {
        IState pCurrentState = new State();
        Event pEvent = new Event();
        Transition transition = new Transition("transition", pEvent, pCurrentState, new State());
        
        boolean willTrigger = transition.willTrigger(pCurrentState, pEvent);
        
        assertEquals(true, willTrigger);
    }
    
}