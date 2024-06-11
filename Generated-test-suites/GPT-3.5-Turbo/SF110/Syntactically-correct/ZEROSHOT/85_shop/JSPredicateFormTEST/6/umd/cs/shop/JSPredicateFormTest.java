import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.*;

public class JSPredicateFormTest {

    // Test JSPredicateForm constructor with no parameter
    @Test
    public void testJSPredicateFormNoParam() {
        JSPredicateForm form = new JSPredicateForm();
        
        // Test if the form is empty
        assertEquals(0, form.size());
    }
    
    // Test JSPredicateForm constructor with a text parameter
    @Test
    public void testJSPredicateFormWithTextParam() {
        String text = "(call (someTerm))";
        JSPredicateForm form = new JSPredicateForm(text);
        
        // Test if the form is parsed correctly
        assertEquals(3, form.size());
        assertEquals("call", form.elementAt(0));
        assertEquals("someTerm", ((JSTerm)form.elementAt(1)).toString());
    }
    
    // Test JSPredicateForm constructor with a StreamTokenizer parameter
    @Test
    public void testJSPredicateFormWithTokenizerParam() {
        String text = "(call (someTerm))";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        
        // Test if the form is parsed correctly
        assertEquals(3, form.size());
        assertEquals("call", form.elementAt(0));
        assertEquals("someTerm", ((JSTerm)form.elementAt(1)).toString());
    }
    
    // Test JSPredicateFormInit method
    @Test
    public void testJSPredicateFormInit() {
        String text = "(call (someTerm))";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        
        // Test if the form is initialized correctly
        assertEquals(3, form.size());
        assertEquals("call", form.elementAt(0));
        assertEquals("someTerm", ((JSTerm)form.elementAt(1)).toString());
    }
    
    // Test print method
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("(call (someTerm))");
        form.print();
        // Manually check the output of the print method
    }
    
    // Test toStr method
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm("(call (someTerm))");
        StringBuffer res = form.toStr();
        
        // Test if the toString output matches the expected string
        assertEquals("(call (someTerm))", res.toString());
    }
    
    // Test clonePF method
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm("(call (someTerm))");
        JSPredicateForm clone = form.clonePF();
        
        // Test if the clone form is equal to the original form
        assertTrue(form.equals(clone));
    }
    
    // Test applySubstitutionPF method
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm("(call (someTerm))");
        JSSubstitution alpha = new JSSubstitution();
        alpha.add("someTerm", "newTerm");
        JSPredicateForm newForm = form.applySubstitutionPF(alpha);
        
        // Test if the substitution is applied correctly
        assertEquals("newTerm", ((JSTerm)newForm.elementAt(1)).toString());
    }
    
    // Test matches method with matching forms
    @Test
    public void testMatchesMatch() {
        JSPredicateForm form1 = new JSPredicateForm("(call (someTerm))");
        JSPredicateForm form2 = new JSPredicateForm("(call (someTerm))");
        JSSubstitution result = form1.matches(form2);
        
        // Test if the result is successful
        assertFalse(result.fail());
    }
    
    // Test matches method with non-matching forms
    @Test
    public void testMatchesNoMatch() {
        JSPredicateForm form1 = new JSPredicateForm("(call (term1))");
        JSPredicateForm form2 = new JSPredicateForm("(call (term2))");
        JSSubstitution result = form1.matches(form2);
        
        // Test if the result is a failure
        assertTrue(result.fail());
    }
    
    // Test equals method with equal forms
    @Test
    public void testEqualsEqual() {
        JSPredicateForm form1 = new JSPredicateForm("(call (someTerm))");
        JSPredicateForm form2 = new JSPredicateForm("(call (someTerm))");
        boolean result = form1.equals(form2);
        
        // Test if the forms are equal
        assertTrue(result);
    }
    
    // Test equals method with non-equal forms
    @Test
    public void testEqualsNotEqual() {
        JSPredicateForm form1 = new JSPredicateForm("(call (term1))");
        JSPredicateForm form2 = new JSPredicateForm("(call (term2))");
        boolean result = form1.equals(form2);
        
        // Test if the forms are not equal
        assertFalse(result);
    }
    
    // Test standarizerPredicateForm method
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(call (someTerm [var]))");
        JSPredicateForm standarizedForm = form.standarizerPredicateForm();
        
        // Test if the variables are standarized correctly
        assertEquals("(call (someTerm [var0]))", standarizedForm.toStr().toString());
    }
}