import static org.junit.Assert.*;
import org.junit.*;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateFormInit() {
        // Typical use case
        JSPredicateForm form = new JSPredicateForm("(call (term))");
        assertEquals("(call (term))", form.toStr().toString());
        
        // Edge case - empty form
        form = new JSPredicateForm();
        assertEquals("Predicateform.print(): 0 elements", form.toStr().toString());
        
        // Error scenario - invalid input
        try {
            form = new JSPredicateForm("(call term))");
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            assertEquals("expected '('", e.getMessage());
        }
    }
    
    @Test
    public void testPrint() {
        // Typical use case - not predicate form
        JSPredicateForm form = new JSPredicateForm("(not (term))");
        assertEquals("(not (term))", form.toStr().toString());
        
        // Typical use case - regular predicate form
        form = new JSPredicateForm("(call (term))");
        assertEquals("(call (term))", form.toStr().toString());
        
        // Edge case - empty form
        form = new JSPredicateForm();
        assertEquals("Predicateform.print(): 0 elements", form.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        // Typical use case - not predicate form
        JSPredicateForm form = new JSPredicateForm("(not (term))");
        JSPredicateForm clone = form.clonePF();
        assertEquals("(not (term))", clone.toStr().toString());
        
        // Typical use case - regular predicate form
        form = new JSPredicateForm("(call (term))");
        clone = form.clonePF();
        assertEquals("(call (term))", clone.toStr().toString());
        
        // Edge case - empty form
        form = new JSPredicateForm();
        clone = form.clonePF();
        assertEquals("Predicateform.print(): 0 elements", clone.toStr().toString());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        // Typical use case - not predicate form
        JSSubstitution alpha = new JSSubstitution();
        alpha.add("term", "value");
        
        JSPredicateForm form = new JSPredicateForm("(not (term))");
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertEquals("(not (value))", result.toStr().toString());
        
        // Typical use case - regular predicate form
        form = new JSPredicateForm("(call (term))");
        result = form.applySubstitutionPF(alpha);
        assertEquals("(call (value))", result.toStr().toString());
        
        // Edge case - empty form
        form = new JSPredicateForm();
        result = form.applySubstitutionPF(alpha);
        assertEquals("Predicateform.print(): 0 elements", result.toStr().toString());
    }
    
    @Test
    public void testMatches() {
        // Typical use case - not predicate form
        JSPredicateForm form1 = new JSPredicateForm("(not (term))");
        JSPredicateForm form2 = new JSPredicateForm("(not (term))");
        JSSubstitution alpha = form1.matches(form2);
        assertFalse(alpha.fail());
        
        // Typical use case - regular predicate form with matching terms
        form1 = new JSPredicateForm("(call (term))");
        form2 = new JSPredicateForm("(call (term))");
        alpha = form1.matches(form2);
        assertFalse(alpha.fail());
        
        // Typical use case - regular predicate form with non-matching terms
        form1 = new JSPredicateForm("(call (term1))");
        form2 = new JSPredicateForm("(call (term2))");
        alpha = form1.matches(form2);
        assertTrue(alpha.fail());
        
        // Edge case - empty forms
        form1 = new JSPredicateForm();
        form2 = new JSPredicateForm();
        alpha = form1.matches(form2);
        assertFalse(alpha.fail());
    }
    
    @Test
    public void testEquals() {
        // Typical use case - not predicate form
        JSPredicateForm form1 = new JSPredicateForm("(not (term))");
        JSPredicateForm form2 = new JSPredicateForm("(not (term))");
        assertTrue(form1.equals(form2));
        
        // Typical use case - regular predicate form with matching terms
        form1 = new JSPredicateForm("(call (term))");
        form2 = new JSPredicateForm("(call (term))");
        assertTrue(form1.equals(form2));
        
        // Typical use case - regular predicate form with non-matching terms
        form1 = new JSPredicateForm("(call (term1))");
        form2 = new JSPredicateForm("(call (term2))");
        assertFalse(form1.equals(form2));
        
        // Edge case - empty forms
        form1 = new JSPredicateForm();
        form2 = new JSPredicateForm();
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        // Typical use case - not predicate form
        JSPredicateForm form = new JSPredicateForm("(not (term))");
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals("(not (term))", result.toStr().toString());
        
        // Typical use case - regular predicate form
        form = new JSPredicateForm("(call (term))");
        result = form.standarizerPredicateForm();
        assertEquals("(call (term))", result.toStr().toString());
        
        // Edge case - empty form
        form = new JSPredicateForm();
        result = form.standarizerPredicateForm();
        assertEquals("Predicateform.print(): 0 elements", result.toStr().toString());
    }
    
}