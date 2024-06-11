import org.junit.*;
import static org.junit.Assert.*;
import uk.me.jockmacmad.jstate.state.*;
import uk.me.jockmacmad.jstate.predicate.*;

public class TransitionTest {

    private Transition transition;

    @Before
    public void setUp() {
        transition = new Transition("transition1", new Event(), new IState(), new IState());
    }

    @Test
    public void testAddGuardCondition_IPredicate() {
        transition.addGuardCondition(new IPredicate());
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(IPredicate.class, transition.getGuardConditions().get(0).getClass());
    }

    @Test
    public void testAddGuardCondition_IGuardCondition() {
        transition.addGuardCondition(new IGuardCondition());
        assertEquals(1, transition.getGuardConditions().size());
        assertEquals(IGuardCondition.class, transition.getGuardConditions().get(0).getClass());
    }

    @Test
    public void testEquals() {
        Transition transition2 = new Transition("transition1", new Event(), new IState(), new IState());
        assertTrue(transition.equals(transition2));
    }

    @Test
    public void testHashCode() {
        Transition transition2 = new Transition("transition1", new Event(), new IState(), new IState());
        assertEquals(transition.hashCode(), transition2.hashCode());
    }

    @Test
    public void testGetInitialState() {
        IState initialState = new IState();
        transition = new Transition("transition1", new Event(), initialState, new IState());
        assertEquals(initialState, transition.getInitialState());
    }

    @Test
    public void testGetOutcomeState() {
        IState outcomeState = new IState();
        transition = new Transition("transition1", new Event(), new IState(), outcomeState);
        assertEquals(outcomeState, transition.getOutcomeState());
    }

    @Test
    public void testGetTriggerEvent() {
        Event triggerEvent = new Event();
        transition = new Transition("transition1", triggerEvent, new IState(), new IState());
        assertEquals(triggerEvent, transition.getTriggerEvent());
    }

    @Test
    public void testToString() {
        assertEquals("Transition[name=transition1, initialState=null, outcomeState=null, triggerEvent=null, guardConditions=[]]", transition.toString());
    }

    @Test
    public void testTrigger() {
        IState initialState = new IState();
        IState outcomeState = new IState();
        Event triggerEvent = new Event();
        transition = new Transition("transition1", triggerEvent, initialState, outcomeState);
        IState currentState = initialState;
        Event event = triggerEvent;
        IState result = transition.trigger(currentState, event);
        assertEquals(outcomeState, result);
    }

    @Test
    public void testWillTrigger() {
        IState initialState = new IState();
        Event triggerEvent = new Event();
        transition = new Transition("transition1", triggerEvent, initialState, new IState());
        IState currentState = initialState;
        Event event = triggerEvent;
        boolean result = transition.willTrigger(currentState, event);
        assertTrue(result);
    }

}