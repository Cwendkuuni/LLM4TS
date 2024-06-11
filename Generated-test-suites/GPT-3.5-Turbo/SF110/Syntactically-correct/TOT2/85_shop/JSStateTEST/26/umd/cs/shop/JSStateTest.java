package umd.cs.shop;
import static org.junit.Assert.*;
import org.junit.Test;

public class JSStateTest {

    @Test
    public void testConstructor() {
        JSState state = new JSState();
        assertNotNull(state);
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
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms delList = new JSListLogicalAtoms();
        JSTState newState = state.applyOp(op, alpha, addList, delList);
        assertNotNull(newState);
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