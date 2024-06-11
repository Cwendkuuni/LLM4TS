package umd.cs.shop;

import org.junit.*;
import static org.junit.Assert.*;

public class JSStateTest {
  
  @Test
  public void testAddElementsToState() {
    // Test case 1
    JSListLogicalAtoms s1 = new JSListLogicalAtoms();
    JSState state1 = new JSState();
    state1.addElementsToState(s1);
    assertEquals(0, state1.size());
    
    // Test case 2
    JSListLogicalAtoms s2 = new JSListLogicalAtoms();
    s2.addElement(new JSPredicateForm("predicate1"));
    s2.addElement(new JSPredicateForm("predicate2"));
    JSState state2 = new JSState();
    state2.addElement(new JSPredicateForm("predicate1"));
    state2.addElementsToState(s2);
    assertEquals(2, state2.size());
    assertEquals("predicate1", state2.elementAt(0).toString());
    assertEquals("predicate2", state2.elementAt(1).toString());
  }
  
  @Test
  public void testApply() {
    // Test case 1
    JSPlan pl1 = new JSPlan();
    JSState state1 = new JSState();
    JSState ns1 = state1.apply(pl1);
    // Assertion for ns1
    
    // Test case 2
    JSPlan pl2 = new JSPlan();
    JSState state2 = new JSState();
    JSState ns2 = state2.apply(pl2);
    // Assertion for ns2
  }
  
  @Test
  public void testApplyOp() {
    // Test case 1
    JSOperator op1 = new JSOperator();
    JSSubstitution alpha1 = new JSSubstitution();
    JSListLogicalAtoms addL1 = new JSListLogicalAtoms();
    JSListLogicalAtoms delL1 = new JSListLogicalAtoms();
    JSTState result1 = state1.applyOp(op1, alpha1, addL1, delL1);
    // Assertion for result1
    
    // Test case 2
    JSOperator op2 = new JSOperator();
    JSSubstitution alpha2 = new JSSubstitution();
    JSListLogicalAtoms addL2 = new JSListLogicalAtoms();
    JSListLogicalAtoms delL2 = new JSListLogicalAtoms();
    JSTState result2 = state2.applyOp(op2, alpha2, addL2, delL2);
    // Assertion for result2
  }
  
  @Test
  public void testSatisfies() {
    // Test case 1
    JSListLogicalAtoms conds1 = new JSListLogicalAtoms();
    JSSubstitution alpha1 = new JSSubstitution();
    JSListAxioms axioms1 = new JSListAxioms();
    JSSubstitution result1 = state1.satisfies(conds1, alpha1, axioms1);
    // Assertion for result1
    
    // Test case 2
    JSListLogicalAtoms conds2 = new JSListLogicalAtoms();
    JSSubstitution alpha2 = new JSSubstitution();
    JSListAxioms axioms2 = new JSListAxioms();
    JSSubstitution result2 = state2.satisfies(conds2, alpha2, axioms2);
    // Assertion for result2
  }
  
  @Test
  public void testSatisfiesAll() {
    // Test case 1
    JSListLogicalAtoms conds1 = new JSListLogicalAtoms();
    JSSubstitution alpha1 = new JSSubstitution();
    JSListAxioms axioms1 = new JSListAxioms();
    JSListSubstitution result1 = state1.satisfiesAll(conds1, alpha1, axioms1);
    // Assertion for result1
    
    // Test case 2
    JSListLogicalAtoms conds2 = new JSListLogicalAtoms();
    JSSubstitution alpha2 = new JSSubstitution();
    JSListAxioms axioms2 = new JSListAxioms();
    JSListSubstitution result2 = state2.satisfiesAll(conds2, alpha2, axioms2);
    // Assertion for result2
  }
  
  @Test
  public void testSatisfiesTAm() {
    // Test case 1
    JSPredicateForm t1 = new JSPredicateForm("predicate1");
    JSSubstitution alpha1 = new JSSubstitution();
    JSListSubstitution result1 = state1.satisfiesTAm(t1, alpha1);
    // Assertion for result1
    
    // Test case 2
    JSPredicateForm t2 = new JSPredicateForm("predicate2");
    JSSubstitution alpha2 = new JSSubstitution();
    JSListSubstitution result2 = state2.satisfiesTAm(t2, alpha2);
    // Assertion for result2
  }
}