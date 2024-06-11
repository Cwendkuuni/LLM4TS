import org.junit.*;

import java.io.StreamTokenizer;

public class JSStateTest {
    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        s.addElement(new JSPredicateForm());
        
        JSState state = new JSState();
        state.addElementsToState(s);
        
        Assert.assertEquals(1, state.size());
        Assert.assertEquals(s.elementAt(0), state.elementAt(0));
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();
        JSState ns = state.apply(pl);
        
        // Assert some conditions about ns
        // ...
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        
        JSTState tState = state.applyOp(op, alpha, addL, delL);
        
        // Assert some conditions about tState
        // ...
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution subs = state.satisfies(conds, alpha, axioms);
        
        // Assert some conditions about subs
        // ...
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution subs = state.satisfiesAll(conds, alpha, axioms);
        
        // Assert some conditions about subs
        // ...
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution subs = state.satisfiesTAm(t, alpha);
        
        // Assert some conditions about subs
        // ...
    }
    
    // Add more test cases for other methods
    
    
}