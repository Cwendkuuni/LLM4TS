import static org.junit.Assert.*;
import org.junit.Test;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();
        state.addElementsToState(s);
        // add test assertions here
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();
        JSState result = state.apply(pl);
        // add test assertions here
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState state = new JSState();
        JSTState result = state.applyOp(op, alpha, addL, delL);
        // add test assertions here
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        // add test assertions here
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        // add test assertions here
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        // add test assertions here
    }
}