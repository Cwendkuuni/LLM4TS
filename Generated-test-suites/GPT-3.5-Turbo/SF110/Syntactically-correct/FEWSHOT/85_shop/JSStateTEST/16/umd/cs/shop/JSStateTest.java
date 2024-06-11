import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSPredicateForm element1 = new JSPredicateForm();
        JSPredicateForm element2 = new JSPredicateForm();
        s.addElement(element1);
        s.addElement(element2);
        
        state.addElementsToState(s);
        
        assertEquals(2, state.size());
        assertNotNull(state.elementAt(0));
        assertNotNull(state.elementAt(1));
    }
    
    @Test
    public void testApply() {
        JSState state = new JSState();
        JSPlan pl = new JSPlan();
        
        JSState result = state.apply(pl);
        
        assertNotNull(result);
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