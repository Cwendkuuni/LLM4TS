import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.StreamTokenizer;

public class JSStateTest {

    @Test
    public void testJSStateDefaultConstructor() {
        JSState state = new JSState();
        assertNotNull(state);
    }
    
    @Test
    public void testJSStateStreamTokenizerConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Testing"));
        JSState state = new JSState(tokenizer);
        assertNotNull(state);
    }
    
    @Test
    public void testAddElementsToState() {
        JSState state = new JSState();
        JSListLogicalAtoms list = new JSListLogicalAtoms();
        list.addElement(new JSPredicateForm());
        state.addElementsToState(list);
        assertEquals(1, state.size());
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
        JSOperator operator = new JSOperator();
        JSSubstitution alpha = new JSSubstitution();
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms deleteList = new JSListLogicalAtoms();
        
        JSTState newState = state.applyOp(operator, alpha, addList, deleteList);
        assertNotNull(newState);
    }
    
    @Test
    public void testSatisfies() {
        JSState state = new JSState();
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSSubstitution result = state.satisfies(conditions, alpha, axioms);
        assertNotNull(result);
    }
    
    @Test
    public void testSatisfiesAll() {
        JSState state = new JSState();
        JSListLogicalAtoms conditions = new JSListLogicalAtoms();
        JSSubstitution alpha = new JSSubstitution();
        JSListAxioms axioms = new JSListAxioms();
        
        JSListSubstitution results = state.satisfiesAll(conditions, alpha, axioms);
        assertNotNull(results);
    }
    
    @Test
    public void testSatisfiesTAm() {
        JSState state = new JSState();
        JSPredicateForm form = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        
        JSListSubstitution results = state.satisfiesTAm(form, alpha);
        assertNotNull(results);
    }
}