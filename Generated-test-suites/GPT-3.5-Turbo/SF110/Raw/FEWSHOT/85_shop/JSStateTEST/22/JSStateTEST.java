
package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StreamTokenizer;
import java.util.ArrayList;

import org.apache.commons.cli.Util;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSStateTest {

    @BeforeClass
    public static void setUp() {
        // Initialization
    }

    @Test
    public void testConstructor() {
        JSState state = new JSState();
        assertNotNull(state);
    }

    @Test
    public void testConstructorWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        JSState state = new JSState(tokenizer);
        assertNotNull(state);
    }

    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        state.addElementsToState(s);
        assertEquals(0, state.size());

        JSPredicateForm atom1 = new JSPredicateForm();
        JSPredicateForm atom2 = new JSPredicateForm();
        JSListLogicalAtoms s2 = new JSListLogicalAtoms();
        s2.addElement(atom1);
        s2.addElement(atom2);

        state.addElementsToState(s2);
        assertEquals(2, state.size());
        assertTrue(state.contains(atom1));
        assertTrue(state.contains(atom2));
    }

    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan plan = new JSPlan();
        JSState newState = state.apply(plan);
        assertNotNull(newState);
    }

    @Test
    public void testApplyOp() {
        JSState state = new JSState();
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTState result = state.applyOp(op, alpha, addL, delL);
        assertNotNull(result);
    }

    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        assertNotNull(result);
    }

    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        assertNotNull(result);
    }

    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        assertNotNull(result);
    }
}
