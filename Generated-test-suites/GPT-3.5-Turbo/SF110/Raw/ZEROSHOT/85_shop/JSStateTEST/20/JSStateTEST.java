import org.junit.Assert;
import org.junit.Test;
import umd.cs.shop.*;

public class JSStateTest {

    @Test
    public void testAddElementsToState() {
        JSListLogicalAtoms s = new JSListLogicalAtoms();
        JSState state = new JSState();
        
        // Populate s with elements
        // ...
        
        state.addElementsToState(s);
        
        // Assert statements
        // ...
    }
    
    @Test
    public void testApply() {
        JSPlan pl = new JSPlan();
        JSState state = new JSState();
        
        // Populate pl
        // ...
        
        JSState ns = state.apply(pl);
        
        // Assert statements
        // ...
    }
    
    @Test
    public void testApplyOp() {
        JSOperator op = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addL = new JSListLogicalAtoms();
        JSListLogicalAtoms delL = new JSListLogicalAtoms();
        JSState state = new JSState();
        
        // Populate op, alpha, addL, delL
        // ...
        
        JSTState ns = state.applyOp(op, alpha, addL, delL);
        
        // Assert statements
        // ...
    }
    
    @Test
    public void testSatisfies() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        
        // Populate conds, alpha, axioms
        // ...
        
        JSSubstitution answer = state.satisfies(conds, alpha, axioms);
        
        // Assert statements
        // ...
    }
    
    @Test
    public void testSatisfiesAll() {
        JSListLogicalAtoms conds = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        JSState state = new JSState();
        
        // Populate conds, alpha, axioms
        // ...
        
        JSListSubstitution answers = state.satisfiesAll(conds, alpha, axioms);
        
        // Assert statements
        // ...
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSState state = new JSState();
        
        // Populate t, alpha
        // ...
        
        JSListSubstitution answers = state.satisfiesTAm(t, alpha);
        
        // Assert statements
        // ...
    }
}