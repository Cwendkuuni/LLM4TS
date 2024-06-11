package umd.cs.shop;

import org.junit.Before;
import org.junit.Test;

import java.io.StreamTokenizer;

public class JSStateTest {

    private JSState jsState;

    @Before
    public void setUp() {
        jsState = new JSState();
    }

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSListLogicalAtoms element1 = new JSListLogicalAtoms();
        JSListLogicalAtoms element2 = new JSListLogicalAtoms();
        s.addElement(element1);
        s.addElement(element2);

        jsState.addElementsToState(s);
        
        // TODO: Add assertions
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();

        JSState result = jsState.apply(pl);

        // TODO: Add assertions
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTState result = jsState.applyOp(op, alpha, addL, delL);

        // TODO: Add assertions
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = jsState.satisfies(conds, alpha, axioms);

        // TODO: Add assertions
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = jsState.satisfiesAll(conds, alpha, axioms);

        // TODO: Add assertions
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = jsState.satisfiesTAm(t, alpha);

        // TODO: Add assertions
    }
}