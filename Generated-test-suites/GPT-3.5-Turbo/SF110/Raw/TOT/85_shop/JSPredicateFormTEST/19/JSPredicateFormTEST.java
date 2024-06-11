import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateForm() {
        // Typical use case - passing a valid text
        JSPredicateForm jpf1 = new JSPredicateForm("(call (add 2 3))");
        assertNotNull(jpf1);
        
        // Edge case - passing an empty text
        JSPredicateForm jpf2 = new JSPredicateForm("");
        assertNotNull(jpf2);
        assertEquals(0, jpf2.size());
        
        // Error scenario - passing a null text
        JSPredicateForm jpf3 = new JSPredicateForm(null);
        assertNotNull(jpf3);
        assertEquals(0, jpf3.size());
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm jpf = new JSPredicateForm("(call (add 2 3))");
        
        // Typical use case - printing a valid predicate form
        jpf.print();
        
        // Edge case - printing an empty predicate form
        JSPredicateForm jpf2 = new JSPredicateForm();
        jpf2.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm jpf = new JSPredicateForm("(call (add 2 3))");
        
        // Typical use case - converting a valid predicate form to a string
        StringBuffer str = jpf.toStr();
        assertNotNull(str);
        assertTrue(str.toString().contains("call"));
        
        // Edge case - converting an empty predicate form to a string
        JSPredicateForm jpf2 = new JSPredicateForm();
        StringBuffer str2 = jpf2.toStr();
        assertNotNull(str2);
        assertTrue(str2.toString().isEmpty());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm jpf = new JSPredicateForm("(call (add 2 3))");
        
        // Typical use case - cloning a valid predicate form
        JSPredicateForm clone = jpf.clonePF();
        assertNotNull(clone);
        assertNotSame(jpf, clone);
        assertEquals(jpf.size(), clone.size());
        
        // Edge case - cloning an empty predicate form
        JSPredicateForm jpf2 = new JSPredicateForm();
        JSPredicateForm clone2 = jpf2.clonePF();
        assertNotNull(clone2);
        assertNotSame(jpf2, clone2);
        assertEquals(0, clone2.size());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm jpf = new JSPredicateForm("(call (add 2 3))");
        JSSubstitution alpha = new JSSubstitution();
        
        // Typical use case - applying a substitution to a valid predicate form
        JSPredicateForm newPF = jpf.applySubstitutionPF(alpha);
        assertNotNull(newPF);
        assertNotSame(jpf, newPF);
        assertEquals(jpf.size(), newPF.size());
        
        // Edge case - applying a substitution to an empty predicate form
        JSPredicateForm jpf2 = new JSPredicateForm();
        JSPredicateForm newPF2 = jpf2.applySubstitutionPF(alpha);
        assertNotNull(newPF2);
        assertNotSame(jpf2, newPF2);
        assertEquals(0, newPF2.size());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm jpf1 = new JSPredicateForm("(call (add 2 3))");
        JSPredicateForm jpf2 = new JSPredicateForm("(call (add 2 (mul 3 4)))");
        
        // Typical use case - matching two valid predicate forms
        JSSubstitution result = jpf1.matches(jpf2);
        assertNotNull(result);
        assertFalse(result.fail());
        
        // Edge case - matching an empty predicate form with a non-empty predicate form
        JSPredicateForm empty = new JSPredicateForm();
        result = empty.matches(jpf1);
        assertNotNull(result);
        assertFalse(result.fail());
        
        // Error scenario - matching two predicate forms with different sizes
        JSPredicateForm jpf3 = new JSPredicateForm("(call (add 2 3))");
        JSPredicateForm jpf4 = new JSPredicateForm("(call (add 2 3) 5)");
        result = jpf3.matches(jpf4);
        assertNotNull(result);
        assertTrue(result.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm jpf1 = new JSPredicateForm("(call (add 2 3))");
        JSPredicateForm jpf2 = new JSPredicateForm("(call (add 2 (mul 3 4)))");
        
        // Typical use case - comparing two valid predicate forms
        boolean isEqual = jpf1.equals(jpf2);
        assertFalse(isEqual);
        
        // Edge case - comparing an empty predicate form with a non-empty predicate form
        JSPredicateForm empty = new JSPredicateForm();
        isEqual = empty.equals(jpf1);
        assertFalse(isEqual);
        
        // Error scenario - comparing two predicate forms with different sizes
        JSPredicateForm jpf3 = new JSPredicateForm("(call (add 2 3))");
        JSPredicateForm jpf4 = new JSPredicateForm("(call (add 2 3) 5)");
        isEqual = jpf3.equals(jpf4);
        assertFalse(isEqual);
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm jpf = new JSPredicateForm("(call (add 2 3))");
        
        // Typical use case - standardizing a valid predicate form
        JSPredicateForm standardized = jpf.standarizerPredicateForm();
        assertNotNull(standardized);
        assertNotSame(jpf, standardized);
        
        // Edge case - standardizing an empty predicate form
        JSPredicateForm empty = new JSPredicateForm();
        JSPredicateForm standardized2 = empty.standarizerPredicateForm();
        assertNotNull(standardized2);
        assertNotSame(empty, standardized2);
        assertEquals(0, standardized2.size());
    }
}