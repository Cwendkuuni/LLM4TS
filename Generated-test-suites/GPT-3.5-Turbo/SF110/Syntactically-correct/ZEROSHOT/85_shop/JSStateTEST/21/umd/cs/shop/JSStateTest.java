package umd.cs.shop;

import org.junit.Test;

import java.io.StreamTokenizer;

import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm());
        JSState jsState = new JSState();
        jsState.addElementsToState(s);
        assertEquals(1, jsState.size());
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState jsState = new JSState();
        JSState result = jsState.apply(pl);
        assertNull(result); // replace with appropriate assertion
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState jsState = new JSState();
        JSTState result = jsState.applyOp(op, alpha, addL, delL);
        assertNull(result); // replace with appropriate assertion
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState jsState = new JSState();
        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);
        assertNotNull(result); // replace with appropriate assertion
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState jsState = new JSState();
        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);
        assertNotNull(result); // replace with appropriate assertion
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSState jsState = new JSState();
        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);
        assertNotNull(result); // replace with appropriate assertion
    }
}