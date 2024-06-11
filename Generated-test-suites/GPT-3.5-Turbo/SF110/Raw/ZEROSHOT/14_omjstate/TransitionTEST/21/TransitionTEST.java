import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import uk.me.jockmacmad.jstate.state.*;
import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

public class TransitionTest {
  
  Transition transition;
  IState initialState;
  IState outcomeState;
  Event triggerEvent;
  List<IGuardCondition> guardConditions;
  
  @Before
  public void setUp() {
    initialState = new State();
    outcomeState = new State();
    triggerEvent = new Event("event");
    guardConditions = new ArrayList<>();
    
    transition = new Transition("transition", triggerEvent, initialState, outcomeState);
    transition.addGuardCondition(new TrueCondition());
  }
  
  @Test
  public void testEquals() {
    Transition transition2 = new Transition("transition", triggerEvent, initialState, outcomeState);
    
    assertTrue(transition.equals(transition2));
  }
  
  @Test
  public void testHashCode() {
    Transition transition2 = new Transition("transition", triggerEvent, initialState, outcomeState);
    
    assertEquals(transition.hashCode(), transition2.hashCode());
  }

  @Test
  public void testEvaluateGuardConditions() {
    // No specific implementation to test
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
    assertEquals(ToStringBuilder.reflectionToString(transition), transition.toString());
  }

  @Test
  public void testTrigger_PassesAllConditions() {
    IState currentState = new State();
    Event trigger = new Event("event");
    
    IState triggeredState = transition.trigger(currentState, trigger);
    
    assertEquals(outcomeState, triggeredState);
  }
  
  @Test
  public void testTrigger_PassesNoCondition() {
    IState currentState = new State();
    Event trigger = new Event("event");
    
    IState triggeredState = transition.trigger(currentState, trigger);
    
    assertNotEquals(initialState, triggeredState);
  }
  
   @Test
  public void testTrigger_FailsCondition() {
    IState currentState = new State();
    Event trigger = new Event("event");
    
    transition.addGuardCondition(new FalseCondition());
    
    IState triggeredState = transition.trigger(currentState, trigger);
    
    assertNotEquals(outcomeState, triggeredState);
  }

  @Test
  public void testWillTrigger_PassesAllConditions() {
    IState currentState = new State();
    Event trigger = new Event("event");
    
    boolean willTrigger = transition.willTrigger(currentState, trigger);
    
    assertTrue(willTrigger);
  }
  
   @Test
  public void testWillTrigger_PassesNoCondition() {
    IState currentState = new State();
    Event trigger = new Event("event");
    
    transition.addGuardCondition(new TrueCondition());
    
    boolean willTrigger = transition.willTrigger(currentState, trigger);
    
    assertTrue(willTrigger);
  }
  
   @Test
  public void testWillTrigger_FailsCondition() {
    IState currentState = new State();
    Event trigger = new Event("event");
    
    transition.addGuardCondition(new FalseCondition());
    
    boolean willTrigger = transition.willTrigger(currentState, trigger);
    
    assertFalse(willTrigger);
  }
}