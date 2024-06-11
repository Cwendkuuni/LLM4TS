import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormString() {
        String text = "(not (equal x y))";
        JSPredicateForm form = new JSPredicateForm(text);
        assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormStreamTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (equal x y))"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertNotNull(form);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNullTokenizer() {
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(null);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("not (equal x y))"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitExpectedPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test
    public void testJSPredicateFormInitExclamation() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not equal)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals("!equal", form.elementAt(0));
    }

    @Test
    public void testJSPredicateFormInitNot() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (equal x y))"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals("not", form.elementAt(0));
        assertEquals("equal", ((JSPredicateForm)form.elementAt(1)).elementAt(0));
    }

    @Test
    public void testJSPredicateFormInitCall() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call (function x y))"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals("call", form.elementAt(0));
        assertEquals("function", ((JSTerm)form.elementAt(1)).elementAt(0));
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitInvalidToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(invalid (token)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitExpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(missing right par"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }

    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("equal");
        form.addElement("x");
        form.addElement("y");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        form.print();
        assertEquals("(equal x y ) ", baos.toString());
        System.setOut(System.out);
    }

    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("equal");
        form.addElement("x");
        form.addElement("y");
        assertEquals("(equal x y)", form.toStr().toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("equal");
        form.addElement("x");
        form.addElement("y");
        JSPredicateForm clone = form.clonePF();
        assertEquals(form.size(), clone.size());
        for (int i = 0; i < form.size(); i++) {
            assertEquals(form.elementAt(i), clone.elementAt(i));
        }
    }

    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.add("x", "y");
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("equal");
        form.addElement("x");
        form.addElement("y");
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertEquals(form.elementAt(0), result.elementAt(0));
        assertEquals(form.elementAt(1), result.elementAt(1));
        assertEquals(form.elementAt(2), result.elementAt(2));
        assertEquals("y", ((JSTerm)result.elementAt(1)).elementAt(0));
        assertEquals("y", ((JSTerm)result.elementAt(2)).elementAt(0));
    }

    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("equal");
        form1.addElement("x");
        form1.addElement("y");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("equal");
        form2.addElement("x");
        form2.addElement("z");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = form1.matches(form2, alpha);
        assertFalse(result.fail());
        assertEquals("y", result.get("z"));
        assertNull(result.get("x"));
    }

    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("equal");
        form1.addElement("x");
        form1.addElement("y");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("equal");
        form2.addElement("x");
        form2.addElement("y");
        assertTrue(form1.equals(form2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("equal");
        form.addElement("x");
        form.addElement("y");
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals(form.elementAt(0), result.elementAt(0));
        assertEquals(form.elementAt(1), result.elementAt(1));
        assertEquals(form.elementAt(2), result.elementAt(2));
        assertEquals("x1", ((JSTerm)result.elementAt(1)).elementAt(0));
        assertEquals("y1", ((JSTerm)result.elementAt(2)).elementAt(0));
    }

}