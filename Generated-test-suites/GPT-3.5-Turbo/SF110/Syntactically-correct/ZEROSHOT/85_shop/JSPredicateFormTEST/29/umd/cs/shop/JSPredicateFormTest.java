import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Vector;

import static org.junit.Assert.*;

public class JSPredicateFormTest {
    private JSPredicateForm jsPredicateForm;

    @Before
    public void setUp() {
        jsPredicateForm = new JSPredicateForm();
    }

    @Test
    public void testJSPredicateForm() {
        JSPredicateForm jsPredicateForm1 = new JSPredicateForm();
        assertNotNull(jsPredicateForm1);
    }

    @Test
    public void testJSPredicateFormWithText() {
        String text = "not call (term)";
        JSPredicateForm jsPredicateForm1 = new JSPredicateForm(text);
        assertNotNull(jsPredicateForm1);
    }

    @Test
    public void testJSPredicateFormWithTokenizer() {
        StringReader stringReader = new StringReader("not call (term)");
        StreamTokenizer tokenizer = new StreamTokenizer(stringReader);
        JSPredicateForm jsPredicateForm1 = new JSPredicateForm(tokenizer);
        assertNotNull(jsPredicateForm1);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        jsPredicateForm.JSPredicateFormInit(null);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithWrongTokenType() {
        StringReader stringReader = new StringReader("not call (term))");
        StreamTokenizer tokenizer = new StreamTokenizer(stringReader);
        jsPredicateForm.JSPredicateFormInit(tokenizer);
    }

    @Test
    public void testPrintWithEmptyPredicateForm() {
        jsPredicateForm.print();
        // No assertion, just checking for no exceptions
    }

    @Test
    public void testPrintWithNotPredicateForm() {
        jsPredicateForm.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        jsPredicateForm.addElement(innerForm);
        jsPredicateForm.print();
        // No assertion, just checking for no exceptions
    }

    @Test
    public void testPrintWithPredicateForm() {
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);
        jsPredicateForm.print();
        // No assertion, just checking for no exceptions
    }

    @Test
    public void testToStrWithEmptyPredicateForm() {
        StringBuffer res = jsPredicateForm.toStr();
        assertNotNull(res);
        assertEquals("()", res.toString());
    }

    @Test
    public void testToStrWithNotPredicateForm() {
        jsPredicateForm.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        jsPredicateForm.addElement(innerForm);
        StringBuffer res = jsPredicateForm.toStr();
        assertNotNull(res);
        assertEquals("(not ())", res.toString());
    }

    @Test
    public void testToStrWithPredicateForm() {
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);
        StringBuffer res = jsPredicateForm.toStr();
        assertNotNull(res);
        assertEquals("(call )", res.toString());
    }

    @Test
    public void testClonePFWithNotPredicateForm() {
        jsPredicateForm.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        jsPredicateForm.addElement(innerForm);
        JSPredicateForm clonedForm = jsPredicateForm.clonePF();
        assertNotNull(clonedForm);
        assertFalse(jsPredicateForm == clonedForm);
        assertEquals(jsPredicateForm.elementAt(0), clonedForm.elementAt(0));
        assertFalse(jsPredicateForm.elementAt(1) == clonedForm.elementAt(1));
    }

    @Test
    public void testClonePFWithPredicateForm() {
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);
        JSPredicateForm clonedForm = jsPredicateForm.clonePF();
        assertNotNull(clonedForm);
        assertFalse(jsPredicateForm == clonedForm);
        assertEquals(jsPredicateForm.elementAt(0), clonedForm.elementAt(0));
        assertTrue(((JSTerm) jsPredicateForm.elementAt(1)).equals((JSTerm) clonedForm.elementAt(1)));
    }

    @Test
    public void testApplySubstitutionPFWithNotPredicateForm() {
        JSSubstitution alpha = new JSSubstitution();
        jsPredicateForm.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        jsPredicateForm.addElement(innerForm);
        JSPredicateForm result = jsPredicateForm.applySubstitutionPF(alpha);
        assertNotNull(result);
        assertFalse(jsPredicateForm == result);
        assertEquals(jsPredicateForm.elementAt(0), result.elementAt(0));
        assertFalse(jsPredicateForm.elementAt(1) == result.elementAt(1));
    }

    @Test
    public void testApplySubstitutionPFWithPredicateForm() {
        JSSubstitution alpha = new JSSubstitution();
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);
        JSPredicateForm result = jsPredicateForm.applySubstitutionPF(alpha);
        assertNotNull(result);
        assertFalse(jsPredicateForm == result);
        assertEquals(jsPredicateForm.elementAt(0), result.elementAt(0));
        assertTrue(((JSTerm) jsPredicateForm.elementAt(1)).equals((JSTerm) result.elementAt(1)));
    }

    @Test
    public void testMatchesWithIdenticalPredicateForm() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution result = jsPredicateForm.matches(t);
        assertNotNull(result);
        assertFalse(result.fail());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMatchesWithDifferentSizePredicateForm() {
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution result = jsPredicateForm.matches(t);
        assertNotNull(result);
        assertTrue(result.fail());
    }

    @Test
    public void testMatchesWithDifferentFunctor() {
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        t.addElement(innerForm);
        JSSubstitution result = jsPredicateForm.matches(t);
        assertNotNull(result);
        assertTrue(result.fail());
    }

    @Test
    public void testMatchesWithNotPredicateForm() {
        jsPredicateForm.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        jsPredicateForm.addElement(innerForm);
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("not");
        JSPredicateForm innerForm2 = new JSPredicateForm();
        t.addElement(innerForm2);
        JSSubstitution result = jsPredicateForm.matches(t);
        assertNotNull(result);
        assertFalse(result.fail());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMatchesWithDifferentTerms() {
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);

        JSPredicateForm t = new JSPredicateForm();
        t.addElement("call");
        JSTerm differentTerm = new JSTerm();
        t.addElement(differentTerm);

        JSSubstitution result = jsPredicateForm.matches(t);
        assertNotNull(result);
        assertFalse(result.fail());
        assertFalse(result.isEmpty());
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(jsPredicateForm.equals(null));
    }

    @Test
    public void testEqualsWithDifferentSize() {
        jsPredicateForm.addElement("call");
        assertFalse(jsPredicateForm.equals(new JSPredicateForm()));
    }

    @Test
    public void testEqualsWithDifferentFunctor() {
        jsPredicateForm.addElement("call");
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("not");
        assertFalse(jsPredicateForm.equals(t));
    }

    @Test
    public void testEqualsWithNotPredicateForm() {
        jsPredicateForm.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        jsPredicateForm.addElement(innerForm);
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("not");
        JSPredicateForm innerForm2 = new JSPredicateForm();
        t.addElement(innerForm2);
        assertTrue(jsPredicateForm.equals(t));
    }

    @Test
    public void testEqualsWithDifferentTerms() {
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);

        JSPredicateForm t = new JSPredicateForm();
        t.addElement("call");
        JSTerm differentTerm = new JSTerm();
        t.addElement(differentTerm);

        assertFalse(jsPredicateForm.equals(t));
    }

    @Test
    public void testStandarizerPredicateFormWithNotPredicateForm() {
        jsPredicateForm.addElement("not");
        JSPredicateForm innerForm = new JSPredicateForm();
        jsPredicateForm.addElement(innerForm);
        JSPredicateForm result = jsPredicateForm.standarizerPredicateForm();
        assertNotNull(result);
        assertFalse(jsPredicateForm == result);
        assertEquals(jsPredicateForm.elementAt(0), result.elementAt(0));
        assertFalse(jsPredicateForm.elementAt(1) == result.elementAt(1));
    }

    @Test
    public void testStandarizerPredicateFormWithPredicateForm() {
        jsPredicateForm.addElement("call");
        JSTerm term = new JSTerm();
        jsPredicateForm.addElement(term);
        JSPredicateForm result = jsPredicateForm.standarizerPredicateForm();
        assertNotNull(result);
        assertFalse(jsPredicateForm == result);
        assertEquals(jsPredicateForm.elementAt(0), result.elementAt(0));
        assertTrue(((JSTerm) jsPredicateForm.elementAt(1)).equals((JSTerm) result.elementAt(1)));
    }
}