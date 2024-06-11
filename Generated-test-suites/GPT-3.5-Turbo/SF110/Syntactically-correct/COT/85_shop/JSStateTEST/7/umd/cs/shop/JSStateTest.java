
package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms atoms = new JSListLogicalAtoms();
        JSListLogicalAtoms expected = new JSListLogicalAtoms();

        atoms.addElement(new JSPredicateForm("atom1"));
        atoms.addElement(new JSPredicateForm("atom2"));
        atoms.addElement(new JSPredicateForm("atom3"));

        state.addElementsToState(atoms);

        expected.addElement(new JSPredicateForm("atom1"));
        expected.addElement(new JSPredicateForm("atom2"));
        expected.addElement(new JSPredicateForm("atom3"));

        assertEquals(expected, state);
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();

        JSState expected = new JSState();

        JSState result = state.apply(plan);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTState expected = new JSTState();

        JSTState result = state.applyOp(op, alpha, addL, delL);

        assertEquals(expected, result);
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution expected = new JSSubstitution();

        JSSubstitution result = state.satisfies(conds, alpha, axioms);

        assertEquals(expected, result);
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution expected = new JSListSubstitution();

        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);

        assertEquals(expected, result);
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution expected = new JSListSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);

        assertEquals(expected, result);
    }
    
}

