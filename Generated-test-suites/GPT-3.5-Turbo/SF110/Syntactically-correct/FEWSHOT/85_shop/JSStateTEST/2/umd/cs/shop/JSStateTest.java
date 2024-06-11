package umd.cs.shop;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSStateTest {

    private JSState state;

    @Before
    public void setUp() {
        state = new JSState();
    }

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();

        // Test when JSState is empty
        state.addElementsToState(s);
        assertEquals(0, state.size());

        // Test when s contains elements that are not already in JSState
        s.addElement("atom1");
        s.addElement("atom2");
        state.addElement("atom3");
        state.addElementsToState(s);
        assertEquals(3, state.size());
        assertEquals("atom1", state.elementAt(0));
        assertEquals("atom2", state.elementAt(1));
        assertEquals("atom3", state.elementAt(2));

        // Test when s contains elements that are already in JSState
        s.clear();
        s.addElement("atom2");
        s.addElement("atom3");
        state.addElementsToState(s);
        assertEquals(3, state.size());
        assertEquals("atom1", state.elementAt(0));
        assertEquals("atom2", state.elementAt(1));
        assertEquals("atom3", state.elementAt(2));
    }

    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState ns = state.apply(pl);

        // Test that a new JSState instance is created
        assertNotSame(state, ns);

        // Test that the new JSState instance is empty
        assertEquals(0, ns.size());

        // Test that <STATE>.apply(<JSPlan>) message is logged
        // TODO: Test the log message
    }

    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();

        JSTState result = state.applyOp(op, alpha, addL, delL);

        // Test that a new JSState instance is created
        assertNotSame(state, result.getState());

        // Test that all the elements in op.deleteList() are not present in result.getState()
        JSListLogicalAtoms opDelL = op.deleteList();
        for (int i = 0; i < opDelL.size(); i++) {
            assertFalse(result.getState().contains(opDelL.elementAt(i)));
        }

        // Test that all the elements in op.addList() are present in result.getState()
        JSListLogicalAtoms opAddL = op.addList();
        for (int i = 0; i < opAddL.size(); i++) {
            assertTrue(result.getState().contains(opAddL.elementAt(i)));
        }

        // Test that the elements in addL that are not present in op.deleteList()
        // are also present in result.getAddL()
        for (int i = 0; i < addL.size(); i++) {
            if (!opDelL.contains(addL.elementAt(i))) {
                assertTrue(result.getAddL().contains(addL.elementAt(i)));
            }
        }

        // Test that the elements in delL that are not present in op.addList()
        // are also present in result.getDelL()
        for (int i = 0; i < delL.size(); i++) {
            if (!opAddL.contains(delL.elementAt(i))) {
                assertTrue(result.getDelL().contains(delL.elementAt(i)));
            }
        }
    }

    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSSubstitution result = state.satisfies(conds, alpha, axioms);

        // Test that a new JSSubstitution instance is created
        assertNotSame(alpha, result);

        // Test that the returned JSSubstitution instance is empty
        assertEquals(0, result.size());

        // Test the condition when TheoremProver fails
        axioms.addAxioms(new JSAxiom("axiom1", true));
        result = state.satisfies(conds, alpha, axioms);
        assertTrue(result.fail());
    }

    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();

        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);

        // Test that a new JSListSubstitution instance is created
        assertNotSame(alpha, result);

        // Test that the returned JSListSubstitution instance is empty
        assertEquals(0, result.size());

        // Test the condition when findfirst is true
        result = state.satisfiesAll(conds, alpha, axioms);
        assertEquals(0, result.size());

        // Test the condition when findfirst is false
        conds.addElement("cond1");
        axioms.addAxioms(new JSAxiom("axiom1", true));

        result = state.satisfiesAll(conds, alpha, axioms);

        // Test that the returned JSListSubstitution instance contains substitutions
        assertNotEquals(0, result.size());

        // Test that each substitution in the result satisfies the conditions
        for (int i = 0; i < result.size(); i++) {
            JSSubstitution subs = result.elementAt(i);
            JSListLogicalAtoms atoms = subs.getAtoms();
            JSListLogicalAtoms subsAtoms = subs.substitute(atoms);

            assertTrue(subsAtoms.containsAll(conds));
        }
    }

    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();

        JSListSubstitution result = state.satisfiesTAm(t, alpha);

        // Test that a new JSListSubstitution instance is created
        assertNotSame(alpha, result);

        // Test that the returned JSListSubstitution instance is empty
        assertEquals(0, result.size());

        // TODO: Test the goal matches atom scenario
    }
}