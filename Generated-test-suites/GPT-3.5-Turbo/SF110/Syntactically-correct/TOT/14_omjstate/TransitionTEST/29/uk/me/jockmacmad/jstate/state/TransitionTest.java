import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Mockito;
import uk.me.jockmacmad.jstate.state.*;

public class TransitionTest {
    
    private Transition transition;
    private IState initialState;
    private IState outcomeState;
    private Event triggerEvent;
    
    @Before
    public void setUp() {
        initialState = Mockito.mock(IState.class);
        outcomeState = Mockito.mock(IState.class);
        triggerEvent = Mockito.mock(Event.class);
        transition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
    }
    
    @Test
    public void testAddGuardCondition_withPredicate() {
        IPredicate guard = Mockito.mock(IPredicate.class);
        transition.addGuardCondition(guard);
        assertTrue(transition.getGuardConditions().contains(guard));
    }
    
    @Test
    public void testAddGuardCondition_withGuardCondition() {
        IGuardCondition guard = Mockito.mock(IGuardCondition.class);
        transition.addGuardCondition(guard);
        assertTrue(transition.getGuardConditions().contains(guard));
    }
    
    @Test
    public void testEquals_withEqualTransition() {
        Transition equalTransition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        assertTrue(transition.equals(equalTransition));
    }
    
    @Test
    public void testEquals_withDifferentTransition() {
        Transition differentTransition = new Transition("Different Transition", triggerEvent, initialState, outcomeState);
        assertFalse(transition.equals(differentTransition));
    }
    
    @Test
    public void testHashCode() {
        Transition equalTransition = new Transition("Test Transition", triggerEvent, initialState, outcomeState);
        assertEquals(transition.hashCode(), equalTransition.hashCode());
    }
    
    @Test
    public void testGetInitialState() {
        assertEquals(initialState, transition.getInitialState());
    }
    
    @Test
    public void testGetOutcomeState() {
        assertEquals(outcomeState, transition.getOutcomeState());
    }
    
    @Test
    public void testGetTriggerEvent() {
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }
    
    @Test
    public void testToString() {
        String expectedString = "Transition[name=Test Transition, initialState=initialState, outcomeState=outcomeState, triggerEvent=triggerEvent]";
        assertEquals(expectedString, transition.toString());
    }
    
    @Test
    public void testTrigger_withPassesGuards() {
        IPredicate guard1 = Mockito.mock(IPredicate.class);
        IGuardCondition guard2 = Mockito.mock(IGuardCondition.class);
        Mockito.when(guard1.evaluate(triggerEvent)).thenReturn(true);
        Mockito.when(guard2.evaluate(triggerEvent)).thenReturn(true);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        
        IState current = initialState;
        IState expectedOutcome = outcomeState;
        Event event = triggerEvent;
        IState outcome = transition.trigger(current, event);
        
        assertEquals(expectedOutcome, outcome);
    }
    
    @Test
    public void testTrigger_withFailsGuards() {
        IPredicate guard1 = Mockito.mock(IPredicate.class);
        IGuardCondition guard2 = Mockito.mock(IGuardCondition.class);
        Mockito.when(guard1.evaluate(triggerEvent)).thenReturn(true);
        Mockito.when(guard2.evaluate(triggerEvent)).thenReturn(false);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        
        IState current = initialState;
        IState expectedOutcome = initialState;
        Event event = triggerEvent;
        IState outcome = transition.trigger(current, event);
        
        assertEquals(expectedOutcome, outcome);
    }
    
    @Test
    public void testWillTrigger_withPassesGuards() {
        IPredicate guard1 = Mockito.mock(IPredicate.class);
        IGuardCondition guard2 = Mockito.mock(IGuardCondition.class);
        Mockito.when(guard1.evaluate(triggerEvent)).thenReturn(true);
        Mockito.when(guard2.evaluate(triggerEvent)).thenReturn(true);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        
        IState current = initialState;
        Event event = triggerEvent;
        boolean willTrigger = transition.willTrigger(current, event);
        
        assertTrue(willTrigger);
    }
    
    @Test
    public void testWillTrigger_withFailsGuards() {
        IPredicate guard1 = Mockito.mock(IPredicate.class);
        IGuardCondition guard2 = Mockito.mock(IGuardCondition.class);
        Mockito.when(guard1.evaluate(triggerEvent)).thenReturn(true);
        Mockito.when(guard2.evaluate(triggerEvent)).thenReturn(false);
        transition.addGuardCondition(guard1);
        transition.addGuardCondition(guard2);
        
        IState current = initialState;
        Event event = triggerEvent;
        boolean willTrigger = transition.willTrigger(current, event);
        
        assertFalse(willTrigger);
    }
}