package uk.me.jockmacmad.jstate.state;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import uk.me.jockmacmad.jstate.predicate.*;
import org.apache.commons.lang.builder.*;
import java.util.*;

public class TransitionTest {
  
  IState initialState;
  IState outcomeState;
  List<IGuardCondition> guardConditions;
  Event triggerEvent;
  String name;
  
  @Before
  public void setUp() {
    initialState = new IState();
    outcomeState = new IState();
    guardConditions = new ArrayList<IGuardCondition>();
    triggerEvent = new Event();
    name = "Transition Test";
  }

  @Test
  public void testTransitionConstructor() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    
    assertEquals(name, transition.getName());
    assertEquals(triggerEvent, transition.getTriggerEvent());
    assertEquals(initialState, transition.getInitialState());
    assertEquals(outcomeState, transition.getOutcomeState());
    
    assertTrue(transition.getGuardConditions().isEmpty());
  }
  
  @Test
  public void testAddGuardCondition_withIPredicate() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    
    IPredicate pGuard = new IPredicate();
    transition.addGuardCondition(pGuard);
    
    assertFalse(transition.getGuardConditions().isEmpty());
    assertEquals(pGuard, transition.getGuardConditions().get(0));
  }
  
  @Test
  public void testAddGuardCondition_withIGuardCondition() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    
    IGuardCondition pGuard = new IGuardCondition();
    transition.addGuardCondition(pGuard);
    
    assertFalse(transition.getGuardConditions().isEmpty());
    assertEquals(pGuard, transition.getGuardConditions().get(0));
  }
  
  @Test
  public void testEquals() {
    Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
    Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
    
    assertTrue(transition1.equals(transition2));
  }
  
  @Test
  public void testHashCode() {
    Transition transition1 = new Transition(name, triggerEvent, initialState, outcomeState);
    Transition transition2 = new Transition(name, triggerEvent, initialState, outcomeState);
    
    assertEquals(transition1.hashCode(), transition2.hashCode());
  }
  
  @Test
  public void testEvaluateGuardConditions() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    
    // Test evaluateGuardConditions method
  }
  
  @Test
  public void testGetInitialState() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    
    assertEquals(initialState, transition.getInitialState());
  }
  
  @Test
  public void testGetOutcomeState() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    
    assertEquals(outcomeState, transition.getOutcomeState());
  }
  
  @Test
  public void testGetTriggerEvent() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    
    assertEquals(triggerEvent, transition.getTriggerEvent());
  }
  
  @Test
  public void testToString() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    
    assertEquals("Transition[name=Transition Test, triggerEvent=Event@<address>, initialState=IState@<address>, outcomeState=IState@<address>]", transition.toString());
  }
  
  @Test
  public void testTrigger() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    IState currentState = new IState();
    Event event = new Event();
    
    IState expectedOutcomeState = transition.getOutcomeState();
    IState actualOutcomeState = transition.trigger(currentState, event);
    
    assertEquals(expectedOutcomeState, actualOutcomeState);
  }
  
  @Test
  public void testWillTrigger() {
    Transition transition = new Transition(name, triggerEvent, initialState, outcomeState);
    IState currentState = new IState();
    Event event = new Event();
    
    boolean expectedTrigger = true;
    boolean actualTrigger = transition.willTrigger(currentState, event);
    
    assertEquals(expectedTrigger, actualTrigger);
  }
}