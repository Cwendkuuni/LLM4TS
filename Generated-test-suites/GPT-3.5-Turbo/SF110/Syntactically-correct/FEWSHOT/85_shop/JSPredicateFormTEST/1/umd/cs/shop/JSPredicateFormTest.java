import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit_StreamTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(Not A B)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);

        assertNotNull(form);
        assertEquals(3, form.size());
        assertEquals("Not", form.elementAt(0));
        assertEquals("A", form.elementAt(1));
        assertEquals("B", form.elementAt(2));
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_NullTokenizer() {
        JSPredicateForm form = new JSPredicateForm(null);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_InvalidToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("A B)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
    }
    
    @Test
    public void testPrint_EmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.print();
    }

    @Test
    public void testPrint_NotForm() {
        JSPredicateForm form = new JSPredicateForm("(Not A B)");
        form.print();
    }

    @Test
    public void testToStr_EmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        StringBuffer expected = new StringBuffer("()");

        StringBuffer result = form.toStr();

        assertNotNull(result);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testToStr_NotForm() {
        JSPredicateForm form = new JSPredicateForm("(Not A B)");
        StringBuffer expected = new StringBuffer("(Not (A B))");

        StringBuffer result = form.toStr();

        assertNotNull(result);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testClonePF_EmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm clone = form.clonePF();

        assertNotNull(clone);
        assertNotEquals(form, clone);
    }

    @Test
    public void testClonePF_NotForm() {
        JSPredicateForm form = new JSPredicateForm("(Not A B)");
        JSPredicateForm clone = form.clonePF();

        assertNotNull(clone);
        assertNotEquals(form, clone);
    }

    @Test
    public void testApplySubstitutionPF_EmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);

        assertNotNull(result);
        assertEquals(form, result);        
    }

    @Test
    public void testApplySubstitutionPF_NotForm() {
        JSPredicateForm form = new JSPredicateForm("(Not A B)");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);

        assertNotNull(result);
        assertEquals(form, result);        
    }

    @Test
    public void testMatches_SameForm() {
        JSPredicateForm form1 = new JSPredicateForm("(Not A B)");
        JSPredicateForm form2 = new JSPredicateForm("(Not A B)");
        JSSubstitution result = form1.matches(form2);
        
        assertNotNull(result);
        assertFalse(result.fail());      
    }

    @Test
    public void testMatches_DifferentForm() {
        JSPredicateForm form1 = new JSPredicateForm("(Not A B)");
        JSPredicateForm form2 = new JSPredicateForm("(Not A C)");
        JSSubstitution result = form1.matches(form2);

        assertNotNull(result);
        assertFalse(result.fail());         
    }

    @Test
    public void testMatches_DifferentSizeForm() {
        JSPredicateForm form1 = new JSPredicateForm("(Not A B)");
        JSPredicateForm form2 = new JSPredicateForm("(Not A)");
        JSSubstitution result = form1.matches(form2);

        assertNotNull(result);
        assertTrue(result.fail());         
    }

    @Test
    public void testEquals_SameForm() {
        JSPredicateForm form1 = new JSPredicateForm("(Not A B)");
        JSPredicateForm form2 = new JSPredicateForm("(Not A B)");
        boolean result = form1.equals(form2);

        assertTrue(result);     
    }

    @Test
    public void testEquals_DifferentForm() {
        JSPredicateForm form1 = new JSPredicateForm("(Not A B)");
        JSPredicateForm form2 = new JSPredicateForm("(Not A C)");
        boolean result = form1.equals(form2);

        assertFalse(result);         
    }

    @Test
    public void testStandarizerPredicateForm_EmptyForm() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm result = form.standarizerPredicateForm();

        assertNotNull(result);
        assertEquals(form, result);     
    }

    @Test
    public void testStandarizerPredicateForm_NotForm() {
        JSPredicateForm form = new JSPredicateForm("(Not A B)");
        JSPredicateForm result = form.standarizerPredicateForm();

        assertNotNull(result);
        assertEquals(form, result);       
    }
}