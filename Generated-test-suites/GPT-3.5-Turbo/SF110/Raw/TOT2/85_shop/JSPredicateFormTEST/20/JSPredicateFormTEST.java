import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormConstructorWithString() {
        String text = "(test)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertNotNull(form);
        assertEquals(1, form.size());
        assertEquals("test", form.elementAt(0));
    }

    @Test
    public void testJSPredicateFormConstructorWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(test)"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertNotNull(form);
        assertEquals(1, form.size());
        assertEquals("test", form.elementAt(0));
    }
    
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(A B)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals(2, form.size());
        assertEquals("A", form.elementAt(0));
        assertEquals("B", form.elementAt(1));
    }
    
    @Test
    public void testPrintMethod() {
        JSPredicateForm form = new JSPredicateForm("(test)");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        form.print();
        assertEquals("(test )", outContent.toString().trim());
    }
    
    @Test
    public void testToStrMethod() {
        JSPredicateForm form = new JSPredicateForm("(test)");
        StringBuffer str = form.toStr();
        assertNotNull(str);
        assertEquals("(test)", str.toString());
    }
    
    @Test
    public void testClonePFMethod() {
        JSPredicateForm form = new JSPredicateForm("(test)");
        JSPredicateForm clone = form.clonePF();
        assertNotNull(clone);
        assertNotSame(form, clone);
        assertEquals(form.elementAt(0), clone.elementAt(0));
    }
    
    @Test
    public void testApplySubstitutionPFMethod() {
        JSPredicateForm form = new JSPredicateForm("(test)");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertNotNull(result);
        assertEquals(form.elementAt(0), result.elementAt(0));
    }
    
    @Test
    public void testMatchesMethod() {
        JSPredicateForm form1 = new JSPredicateForm("(test)");
        JSPredicateForm form2 = new JSPredicateForm("(test)");
        JSSubstitution beta = form1.matches(form2);
        assertNotNull(beta);
        assertFalse(beta.fail());
    }
    
    @Test
    public void testEqualsMethod() {
        JSPredicateForm form1 = new JSPredicateForm("(test)");
        JSPredicateForm form2 = new JSPredicateForm("(test)");
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateFormMethod() {
        JSPredicateForm form = new JSPredicateForm("(test)");
        JSPredicateForm stanForm = form.standarizerPredicateForm();
        assertNotNull(stanForm);
        assertEquals(form.elementAt(0), stanForm.elementAt(0));
    }
    
    @Test(expected = NullPointerException.class)
    public void testJSPredicateFormConstructorWithNullString() {
        String text = null;
        JSPredicateForm form = new JSPredicateForm(text);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

}