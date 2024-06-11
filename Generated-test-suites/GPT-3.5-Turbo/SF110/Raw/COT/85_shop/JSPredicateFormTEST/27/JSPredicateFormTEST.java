package umd.cs.shop;

import org.junit.Test;

import java.io.StreamTokenizer;
import java.io.StringReader;

import static org.junit.Assert.*;

public class JSPredicateFormTest {

    @Test
    public void testConstructorEmptyText() {
        JSPredicateForm form = new JSPredicateForm("");
        assertEquals(0, form.size());
    }

    @Test
    public void testConstructorNullText() {
        JSPredicateForm form = new JSPredicateForm(null);
        assertEquals(0, form.size());
    }

    @Test
    public void testConstructorWithText() {
        String text = "(not (call (term)))";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(3, form.size());
        assertEquals("not", form.elementAt(0));
        assertEquals("call", form.elementAt(1));
        assertTrue(form.elementAt(2) instanceof JSTerm);
    }

    @Test
    public void testConstructorWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (call (term)))"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertEquals(3, form.size());
        assertEquals("not", form.elementAt(0));
        assertEquals("call", form.elementAt(1));
        assertTrue(form.elementAt(2) instanceof JSTerm);
    }

    @Test(expected = JSParserError.class)
    public void testConstructorInvalidTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        new JSPredicateForm(tokenizer);
    }

    @Test
    public void testPrintEmptyPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals("", form.toStr().toString());
    }

    @Test
    public void testPrintPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(not (call (term)))");
        assertEquals("(not (call term))", form.toStr().toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm("(not (call (term)))");
        JSPredicateForm clonedForm = form.clonePF();
        assertEquals(form.size(), clonedForm.size());
        assertEquals(form.elementAt(0), clonedForm.elementAt(0));
        assertEquals(form.elementAt(1), clonedForm.elementAt(1));
        assertTrue(clonedForm.elementAt(1) instanceof JSPredicateForm);
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution substitution = new JSSubstitution();
        JSPredicateForm form = new JSPredicateForm("(not (call (term)))");
        JSPredicateForm newForm = form.applySubstitutionPF(substitution);
        assertEquals(form.size(), newForm.size());
        assertEquals(form.elementAt(0), newForm.elementAt(0));
        assertEquals(form.elementAt(1), newForm.elementAt(1));
        assertTrue(newForm.elementAt(1) instanceof JSPredicateForm);
    }

    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm("(not (call (term)))");
        JSPredicateForm form2 = new JSPredicateForm("(not (call (term)))");
        JSSubstitution substitution = form1.matches(form2);
        assertFalse(substitution.fail());
        assertEquals(0, substitution.size());
    }

    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm("(not (call (term)))");
        JSPredicateForm form2 = new JSPredicateForm("(not (call (term)))");
        assertTrue(form1.equals(form2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(not (call (term)))");
        JSPredicateForm standardForm = form.standarizerPredicateForm();
        assertEquals(form.size(), standardForm.size());
        assertEquals(form.elementAt(0), standardForm.elementAt(0));
        assertEquals(form.elementAt(1), standardForm.elementAt(1));
        assertTrue(standardForm.elementAt(1) instanceof JSPredicateForm);
    }

}