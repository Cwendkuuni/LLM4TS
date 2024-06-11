package umd.cs.shop;

import org.junit.Test;

import java.io.StreamTokenizer;

import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSListLogicalAtoms element1 = new JSListLogicalAtoms();
        JSListLogicalAtoms element2 = new JSListLogicalAtoms();
        JSListLogicalAtoms element3 = new JSListLogicalAtoms();

        JSState jsState = new JSState();
        jsState.addElementsToState(s);

        assertEquals(0, jsState.size());

        element1.addElement(new JSPredicateForm());
        s.addElement(element1);

        jsState.addElementsToState(s);
        assertEquals(1, jsState.size());

        element2.addElement(new JSPredicateForm());
        s.addElement(element2);

        jsState.addElementsToState(s);
        assertEquals(2, jsState.size());

        element3.addElement(new JSPredicateForm());
        s.addElement(element3);

        jsState.addElementsToState(s);
        assertEquals(3, jsState.size());
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState jsState = new JSState();
        JSState ns = jsState.apply(pl);

        assertNotNull(ns);
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSState jsState = new JSState();
        JSTState nState = jsState.applyOp(op, alpha, addL, delL);

        assertNotNull(nState);
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSState jsState = new JSState();
        JSSubstitution answer = jsState.satisfies(conds, alpha, axioms);

        assertNotNull(answer);
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSState jsState = new JSState();
        JSListSubstitution answers = jsState.satisfiesAll(conds, alpha, axioms);

        assertNotNull(answers);
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();

        JSState jsState = new JSState();
        JSListSubstitution answers = jsState.satisfiesTAm(t, alpha);

        assertNotNull(answers);
    }
}