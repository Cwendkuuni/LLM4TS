import org.junit.Assert;
import org.junit.Test;

public class JSStateTest {
    
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm());
        JSState state = new JSState();
        state.addElementsToState(s);
        Assert.assertEquals(1, state.size());
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();
        JSState result = state.apply(pl);
        // verify that the result is not null
        Assert.assertNotNull(result);
        // verify that the result is an instance of JSState
        Assert.assertTrue(result instanceof JSState);
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSTState result = state.applyOp(op, alpha, addL, delL);
        // verify that the result is not null
        Assert.assertNotNull(result);
        // verify that the result is an instance of JSTState
        Assert.assertTrue(result instanceof JSTState);
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSSubstitution result = state.satisfies(conds, alpha, axioms);
        // verify that the result is not null
        Assert.assertNotNull(result);
        // verify that the result is an instance of JSSubstitution
        Assert.assertTrue(result instanceof JSSubstitution);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSListSubstitution result = state.satisfiesAll(conds, alpha, axioms);
        // verify that the result is not null
        Assert.assertNotNull(result);
        // verify that the result is an instance of JSListSubstitution
        Assert.assertTrue(result instanceof JSListSubstitution);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSListSubstitution result = state.satisfiesTAm(t, alpha);
        // verify that the result is not null
        Assert.assertNotNull(result);
        // verify that the result is an instance of JSListSubstitution
        Assert.assertTrue(result instanceof JSListSubstitution);
    }
}