package umd.cs.shop;

import org.junit.*;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        // Add test cases here for JSState.addElementsToState() method
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        // Add test cases here for JSState.apply() method
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        // Add test cases here for JSState.applyOp() method
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        // Add test cases here for JSState.satisfies() method
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        // Add test cases here for JSState.satisfiesAll() method
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        // Add test cases here for JSState.satisfiesTAm() method
    }
}