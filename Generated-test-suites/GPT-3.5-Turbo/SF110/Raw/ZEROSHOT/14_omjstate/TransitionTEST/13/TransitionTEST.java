import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class TransitionTest {

  @Test
  public void testEquals() {
    Transition t1 = new Transition("Transition1", new Event(), new IState(), new IState());
    Transition t2 = new Transition("Transition2", new Event(), new IState(), new IState());
    assertFalse(t1.equals(t2));
  }
  
  @Test
  public void testHashCode() {
    Transition t = new Transition("Transition", new Event(), new IState(), new IState());
    int hash = t.hashCode();
    assertEquals(hash, t.hashCode());
  }
  
  @Test
  public void testAddGuardCondition() {
    Transition t = new Transition("Transition", new Event(), new IState(), new IState());
    IPredicate guard = new IPredicate();
    t.addGuardCondition(guard);
    assertEquals(1, t.getGuardConditions().size());
  }
  
  @Test
  public void testGetInitialState() {
    IState initialState = new IState();
    Transition t = new Transition("Transition", new Event(), initialState, new IState());
    assertEquals(initialState, t.getInitialState());
  }
  
  @Test
  public void testGetOutcomeState() {
    IState outcomeState = new IState();
    Transition t = new Transition("Transition", new Event(), new IState(), outcomeState);
    assertEquals(outcomeState, t.getOutcomeState());
  }
  
  @Test
  public void testGetTriggerEvent() {
    Event triggerEvent = new Event();
    Transition t = new Transition("Transition", triggerEvent, new IState(), new IState());
    assertEquals(triggerEvent, t.getTriggerEvent());
  }
  
  @Test
  public void testToString() {
    Transition t = new Transition("Transition", new Event(), new IState(), new IState());
    String str = t.toString();
    assertNotNull(str);
  }
  
  @Test
  public void testTrigger() {
    IState initialState = new IState();
    IState outcomeState = new IState();
    Event triggerEvent = new Event();
    Transition t = new Transition("Transition", triggerEvent, initialState, outcomeState);
    IState currentState = initialState;
    assertTrue(t.trigger(currentState, triggerEvent) == outcomeState);
  }
  
  @Test
  public void testWillTrigger() {
    IState initialState = new IState();
    Event triggerEvent = new Event();
    Transition t = new Transition("Transition", triggerEvent, initialState, new IState());
    IState currentState = initialState;
    assertTrue(t.willTrigger(currentState, triggerEvent));
  }
}