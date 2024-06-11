package umd.cs.shop;

import org.junit.Test;
import org.junit.Assert;

import java.io.StreamTokenizer;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        // Test case 1
        JSListLogicalAtoms s1 = new JSListLogicalAtoms();
        JSListLogicalAtoms s2 = new JSListLogicalAtoms();
        JSListLogicalAtoms expected = new JSListLogicalAtoms();
        JSState state = new JSState();
        
        state.addElementsToState(s1);
        Assert.assertEquals(expected, state);
        
        // Test case 2
        JSLogicalAtom atom1 = new JSLogicalAtom("atom1");
        JSLogicalAtom atom2 = new JSLogicalAtom("atom2");
        JSLogicalAtom atom3 = new JSLogicalAtom("atom3");
        
        s1.addElement(atom1);
        s1.addElement(atom2);
        s1.addElement(atom3);
        
        expected.addElement(atom1);
        expected.addElement(atom2);
        expected.addElement(atom3);
        
        state.addElementsToState(s1);
        Assert.assertEquals(expected, state);
        
        // Test case 3
        JSLogicalAtom atom4 = new JSLogicalAtom("atom4");
        JSLogicalAtom atom5 = new JSLogicalAtom("atom5");
        
        s2.addElement(atom4);
        s2.addElement(atom5);
        
        expected.addElement(atom4);
        expected.addElement(atom5);
        
        state.addElementsToState(s2);
        Assert.assertEquals(expected, state);
    }

    @Test
    public void testApply() {
        // Test case 1
        JSPlan plan = new JSPlan();
        JSState expected = new JSState();
        JSState state = new JSState();
        
        JSState result = state.apply(plan);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testApplyOp() {
        // Test case 1
        JSOperator op = new JSOperator();
        op.addAtom("atom1");
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState expected = new JSTState();
        JSState state = new JSState();
        
        JSTState result = state.applyOp(op, alpha, addL, delL);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSatisfies() {
        // Test case 1
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution expected = new JSSubstitution();
        JSState state = new JSState();

        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSatisfiesAll() {
        // Test case 1
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution expected = new JSListSubstitution();
        JSState state = new JSState();

        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSatisfiesTAm() {
        // Test case 1
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution expected = new JSListSubstitution();
        JSState state = new JSState();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        Assert.assertEquals(expected, result);
    }
}