package com.google.javascript.jscomp.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  
  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter cai = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    cai.setJSTypeRegistry(typeRegistry);
    
    ObjectType type = typeRegistry.createObjectType();
    JSType result = cai.restrictToArrayVisitor.visit(type);
    assertNotNull(result);
    assertEquals(typeRegistry.getNativeType(JSTypeNative.ARRAY_TYPE), result);
    
    FunctionType functionType = typeRegistry.createFunctionType();
    result = cai.restrictToArrayVisitor.visit(functionType);
    assertNull(result);
  }
  
  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter cai = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    cai.setJSTypeRegistry(typeRegistry);
    
    ObjectType type = typeRegistry.createObjectType();
    JSType result = cai.restrictToNotArrayVisitor.visit(type);
    assertNotNull(result);
    assertEquals(type, result);
    
    FunctionType functionType = typeRegistry.createFunctionType();
    result = cai.restrictToNotArrayVisitor.visit(functionType);
    assertNotNull(result);
    assertEquals(functionType, result);
  }
  
  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter cai = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    cai.setJSTypeRegistry(typeRegistry);
    
    ObjectType type = typeRegistry.createObjectType();
    JSType result = cai.restrictToObjectVisitor.visit(type);
    assertNotNull(result);
    assertEquals(type, result);
    
    FunctionType functionType = typeRegistry.createFunctionType();
    result = cai.restrictToObjectVisitor.visit(functionType);
    assertNotNull(result);
    assertEquals(functionType, result);
  }
  
  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter cai = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    cai.setJSTypeRegistry(typeRegistry);
    
    ObjectType type = typeRegistry.createObjectType();
    JSType result = cai.restrictToNotObjectVisitor.visit(type);
    assertNull(result);
    
    FunctionType functionType = typeRegistry.createFunctionType();
    result = cai.restrictToNotObjectVisitor.visit(functionType);
    assertNull(result);
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter cai = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    cai.setJSTypeRegistry(typeRegistry);
    
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = cai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
    // perform more assertions based on expected behavior
    
    outcome = false;
    result = cai.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(result);
    // perform more assertions based on expected behavior
  }
  
  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter cai = new ClosureReverseAbstractInterpreter();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    cai.setJSTypeRegistry(typeRegistry);
    
    Node parameter = new Node();
    JSType type = typeRegistry.createType();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    
    FlowScope result = cai.restrictParameter(parameter, type, blindScope, null, outcome);
    assertNotNull(result);
    // perform more assertions based on expected behavior
    
    outcome = false;
    result = cai.restrictParameter(parameter, type, blindScope, null, outcome);
    assertNotNull(result);
    // perform more assertions based on expected behavior
  }
  
  @Test
  public void testTypeRestriction() {
    TypeRestriction restriction = new TypeRestriction(null, true);
    assertNotNull(restriction);
    // perform more assertions based on expected behavior
    
    restriction = new TypeRestriction(null, false);
    assertNotNull(restriction);
    // perform more assertions based on expected behavior
  }
}