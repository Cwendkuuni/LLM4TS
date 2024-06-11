package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.io.*;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormWithString() {
        String text = "Testing JSPredicateForm";
        JSPredicateForm form = new JSPredicateForm(text);
        assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Testing JSPredicateForm"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertNotNull(form);
    }

    @Test
    public void testJSPredicateFormInitWithNullTokenizer() {
        StreamTokenizer tokenizer = null;
        try {
            JSPredicateForm form = new JSPredicateForm();
            form.JSPredicateFormInit(tokenizer);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            assertTrue(e instanceof JSParserError);
        }
    }
    
    @Test
    public void testJSPredicateFormInitWithLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals(form.elementAt(0), "predicate");
    }

    @Test
    public void testJSPredicateFormInitWithRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(")"));
        JSPredicateForm form = new JSPredicateForm();
        try {
            form.JSPredicateFormInit(tokenizer);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            assertTrue(e instanceof JSParserError);
        }
    }

    @Test
    public void testJSPredicateFormInitWithExclamation() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("!predicate"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals(form.elementAt(0), "!predicate");
    }

    @Test
    public void testJSPredicateFormInitWithNot() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("not (predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals(form.elementAt(0), "not");
        assertTrue(form.elementAt(1) instanceof JSPredicateForm);
    }

    @Test
    public void testJSPredicateFormInitWithCall() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("call (predicate)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals(form.elementAt(0), "call");
        assertTrue(form.elementAt(1) instanceof JSTerm);
    }

    @Test
    public void testJSPredicateFormInitWithInvalidToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("invalid token"));
        JSPredicateForm form = new JSPredicateForm();
        try {
            form.JSPredicateFormInit(tokenizer);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            assertTrue(e instanceof JSParserError);
        }
    }

    @Test
    public void testJSPredicateFormPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement("argument");
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        form.print();
        assertEquals(outContent.toString(), "(predicate argument ) ");
        System.setOut(originalOut);
    }

    @Test
    public void testJSPredicateFormToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("predicate");
        form.addElement("argument");
        StringBuffer buffer = form.toStr();
        assertEquals(buffer.toString(), "(predicateargument)");
    }

    @Test
    public void testJSPredicateFormClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        JSPredicateForm clone = form.clonePF();
        assertEquals(form, clone);
    }

    @Test
    public void testJSPredicateFormApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertEquals(form, result);
    }

    @Test
    public void testJSPredicateFormMatches() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate1");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate2");
        JSSubstitution result = form1.matches(form2);
        assertTrue(result.fail());        
    }

    @Test
    public void testJSPredicateFormMatchesWithAlpha() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate1");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate2");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = form1.matches(form2, alpha);
        assertTrue(result.fail());
    }

    @Test
    public void testJSPredicateFormEquals() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("predicate1");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate2");
        assertFalse(form1.equals(form2));
    }

    @Test
    public void testJSPredicateFormStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals(form, result);
    }

}