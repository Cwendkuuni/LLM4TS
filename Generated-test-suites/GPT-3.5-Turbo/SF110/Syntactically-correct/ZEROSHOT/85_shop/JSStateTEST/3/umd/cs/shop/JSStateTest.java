import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class JSStateTest {

    private JSState state;
    private JSListLogicalAtoms atoms;
    private JSPlan plan;
    private JSOperator operator;
    private JSSubstitution substitution;
    private JSListAxioms axioms;

    @Before
    public void setup() {
        state = new JSState();
        atoms = new JSListLogicalAtoms();
        plan = new JSPlan();
        operator = new JSOperator();
        substitution = new JSSubstitution();
        axioms = new JSListAxioms();
    }

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm());

        state.addElementsToState(s);

        assertEquals(1, state.size());
        assertEquals(new JSPredicateForm(), state.elementAt(0));
    }

    @Test
    public void testApply() {
        JSState ns = state.apply(plan);

        // assert statements
    }

    @Test
    public void testApplyOp() {
        JSTState result = state.applyOp(operator, substitution, atoms, atoms);

        // assert statements
    }

    @Test
    public void testSatisfies() {
        JSSubstitution result = state.satisfies(atoms, substitution, axioms);

        // assert statements
    }

    @Test
    public void testSatisfiesAll() {
        JSListSubstitution result = state.satisfiesAll(atoms, substitution, axioms);

        // assert statements
    }

    @Test
    public void testSatisfiesTAm() {
        JSListSubstitution result = state.satisfiesTAm(new JSPredicateForm(), substitution);

        // assert statements
    }
}