package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JSStateTest {
    @Test
    public void testConstructor() {
        JSState state = new JSState();
        assertNotNull(state);
    }

    @Test
    public void testConstructorWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new InputStreamReader(System.in));
        JSState state = new JSState(tokenizer);
        assertNotNull(state);
    }

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();
        state.addElementsToState(s);
        assertEquals(0, state.size());
        
        s.addElement(new JSPredicateForm());
        state.addElementsToState(s);
        assertEquals(1, state.size());
        
        s.addElement(new JSPredicateForm());
        state.addElementsToState(s);
        assertEquals(2, state.size());
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();
        JSState ns = state.apply(pl);
        assertNotNull(ns);
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState state = new JSState();
        JSTState tState = state.applyOp(op, alpha, addL, delL);
        assertNotNull(tState);
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        JSSubstitution satisfies = state.satisfies(conds, alpha, axioms);
        assertNotNull(satisfies);
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        JSListSubstitution satisfiesAll = state.satisfiesAll(conds, alpha, axioms);
        assertNotNull(satisfiesAll);
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();
        JSListSubstitution satisfiesTAm = state.satisfiesTAm(t, alpha);
        assertNotNull(satisfiesTAm);
    }
}