import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSState;
import umd.cs.shop.JSSubstitution;
import umd.cs.shop.JSListSubstitution;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSListAxioms;

import java.io.StringReader;
import java.io.StreamTokenizer;

public class JSStateTest extends TestCase {
  
  private JSState state;
  private JSListLogicalAtoms atoms;
  private JSPredicateForm predicate;
  private JSListLogicalAtoms addList;
  private JSListLogicalAtoms deleteList;
  private JSListLogicalAtoms conditionList;
  private JSSubstitution substitution;
  private JSListAxioms axioms;
  
  @Before
  public void setUp() {
    state = new JSState();
    atoms = new JSListLogicalAtoms();
    predicate = new JSPredicateForm();
    addList = new JSListLogicalAtoms();
    deleteList = new JSListLogicalAtoms();
    conditionList = new JSListLogicalAtoms();
    substitution = new JSSubstitution();
    axioms = new JSListAxioms();
  }
  
  @Test
  public void testAddElementsToState() {
    // Typical use case
    JSListLogicalAtoms typicalAtoms = new JSListLogicalAtoms();
    typicalAtoms.addElement(new JSPredicateForm());
    typicalAtoms.addElement(new JSPredicateForm());
    state.addElementsToState(typicalAtoms);
    
    assertEquals(2, state.size());
    
    // Edge case: s is empty
    JSListLogicalAtoms emptyAtoms = new JSListLogicalAtoms();
    state.addElementsToState(emptyAtoms);
    
    assertEquals(2, state.size());
    
    // Error scenario: s contains duplicate elements
    JSListLogicalAtoms duplicateAtoms = new JSListLogicalAtoms();
    duplicateAtoms.addElement(new JSPredicateForm());
    duplicateAtoms.addElement(new JSPredicateForm());
    state.addElementsToState(duplicateAtoms);
    
    assertEquals(2, state.size());
  }
  
  @Test
  public void testApply() {
    // Typical use case
    JSPlan typicalPlan = new JSPlan();
    JSState newState = state.apply(typicalPlan);
    
    assertNotNull(newState);
    
    // Edge case: <STATE>.apply(<JSPlan>) not implemented yet
    // This test case does not provide a clear criteria for success, so it is not testable.
  }
  
  @Test
  public void testApplyOp() {
    // Typical use case
    JSOperator typicalOperator = new JSOperator();
    JSSubstitution typicalSubstitution = new JSSubstitution();
    JSState typicalAddList = new JSState();
    JSState typicalDeleteList = new JSState();
    JSTState newState = state.applyOp(typicalOperator, typicalSubstitution, typicalAddList, typicalDeleteList);
    
    assertNotNull(newState);
    
    // Edge case: this.size() is 0
    state = new JSState();
    newState = state.applyOp(typicalOperator, typicalSubstitution, typicalAddList, typicalDeleteList);
    
    assertNotNull(newState);
    
    // Error scenario: opDelL and opAddL contain duplicate elements
    typicalOperator.addList().addElement(new JSPredicateForm());
    typicalOperator.deleteList().addElement(new JSPredicateForm());
    typicalAddList.addElement(new JSPredicateForm());
    typicalAddList.addElement(new JSPredicateForm());
    typicalDeleteList.addElement(new JSPredicateForm());
    typicalDeleteList.addElement(new JSPredicateForm());
    newState = state.applyOp(typicalOperator, typicalSubstitution, typicalAddList, typicalDeleteList);
    
    assertNotNull(newState);
  }
  
  @Test
  public void testSatisfies() {
    // Typical use case
    JSListLogicalAtoms typicalConds = new JSListLogicalAtoms();
    typicalConds.addElement(new JSPredicateForm());
    JSSubstitution typicalAlpha = new JSSubstitution();
    JSListAxioms typicalAxioms = new JSListAxioms();
    JSSubstitution result = state.satisfies(typicalConds, typicalAlpha, typicalAxioms);
    
    assertNotNull(result);
    
    // Edge case: satisfiers.fail() is true
    axioms.add(new JSListAxioms());
    result = state.satisfies(typicalConds, typicalAlpha, axioms);
    
    assertEquals(true, result.fail());
  }
  
  @Test
  public void testSatisfiesAll() {
    // Typical use case
    JSListLogicalAtoms typicalConds = new JSListLogicalAtoms();
    typicalConds.addElement(new JSPredicateForm());
    JSSubstitution typicalAlpha = new JSSubstitution();
    JSListAxioms typicalAxioms = new JSListAxioms();
    JSListSubstitution result = state.satisfiesAll(typicalConds, typicalAlpha, typicalAxioms);
    
    assertNotNull(result);
    
    // Edge case: findfirst is true
    conditionalList.addElement(new JSPredicateForm());
    result = state.satisfiesAll(typicalConds, typicalAlpha, typicalAxioms);
    
    assertNotNull(result);
  }
  
  @Test
  public void testSatisfiesTAm() {
    // Typical use case
    JSPredicateForm typicalPredicate = new JSPredicateForm();
    JSSubstitution typicalAlpha = new JSSubstitution();
    JSListSubstitution result = state.satisfiesTAm(typicalPredicate, typicalAlpha);
    
    assertNotNull(result);
    
    // Edge case: this.size() is 0
    state = new JSState();
    result = state.satisfiesTAm(typicalPredicate, typicalAlpha);
    
    assertNotNull(result);
  }
}