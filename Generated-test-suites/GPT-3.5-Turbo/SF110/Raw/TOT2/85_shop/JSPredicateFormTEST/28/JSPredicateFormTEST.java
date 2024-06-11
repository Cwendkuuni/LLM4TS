import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateFormInit_NullTokenizer_ThrowsException() {
        JSPredicateForm form = new JSPredicateForm();
        StreamTokenizer tokenizer = null;
        assertThrows(JSParserError.class, () -> form.JSPredicateFormInit(tokenizer));
    }
    
    @Test
    public void testJSPredicateFormInit_InvalidLeftPar_ThrowsException() {
        JSPredicateForm form = new JSPredicateForm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("abc"));
        assertThrows(JSParserError.class, () -> form.JSPredicateFormInit(tokenizer));
    }
    
    @Test
    public void testJSPredicateFormInit_NotOperatorName_ThrowsException() {
        JSPredicateForm form = new JSPredicateForm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("! %%%"));
        assertThrows(JSParserError.class, () -> form.JSPredicateFormInit(tokenizer));
    }
    
    @Test
    public void testPrint_EmptyForm_PrintsErrorMessage() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals("Predicateform.print(): 0 elements", form.print());
    }
    
    @Test
    public void testMatches_DifferentSizeForms_ReturnsFailureSubstitution() {
        JSPredicateForm form1 = new JSPredicateForm();
        JSPredicateForm form2 = new JSPredicateForm();
        JSSubstitution result = form1.matches(form2);
        assertTrue(result.fail());
    }
    
    @Test
    public void testEquals_NotEqualForms_ReturnsFalse() {
        JSPredicateForm form1 = new JSPredicateForm("not test");
        JSPredicateForm form2 = new JSPredicateForm("not different");
        assertFalse(form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateForm_NotOperator_StanderizesForm() {
        JSPredicateForm form = new JSPredicateForm("not test");
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals("not test", result.toString());
    }
}