import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {

    // Test the JSPredicateForm constructor with no argument
    @Test
    public void testConstructor1() {
        JSPredicateForm form = new JSPredicateForm();
        assertTrue(form.isEmpty());
    }

    // Test the JSPredicateForm constructor with argument
    @Test
    public void testConstructor2() {
        String text = "(not (test))";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(2, form.size());
        assertEquals("not", form.elementAt(0));
        assertEquals("test", form.elementAt(1));
    }

    // Test the JSPredicateForm constructor with StreamTokenizer argument
    @Test
    public void testConstructor3() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (test))"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertEquals(2, form.size());
        assertEquals("not", form.elementAt(0));
        assertEquals("test", form.elementAt(1));
    }

    // Test the JSPredicateFormInit method
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(not (test))"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals(2, form.size());
        assertEquals("not", form.elementAt(0));
        assertEquals("test", form.elementAt(1));
    }

    // Test the print method
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("(not (test))");
        form.print(); // Output should be "(not test)"
    }

    // Test the toStr method
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm("(not (test))");
        StringBuffer res = form.toStr();
        assertEquals("(not test)", res.toString());
    }

    // Test the clonePF method
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm("(not (test))");
        JSPredicateForm clone = form.clonePF();
        assertEquals(form.size(), clone.size());
        for (int i = 0; i < form.size(); i++) {
            assertEquals(form.elementAt(i), clone.elementAt(i));
            assertNotSame(form.elementAt(i), clone.elementAt(i));
        }
    }

    // Test the applySubstitutionPF method
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm("(not (test))");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertEquals(form.size(), result.size());
        for (int i = 0; i < form.size(); i++) {
            assertEquals(form.elementAt(i), result.elementAt(i));
            assertNotSame(form.elementAt(i), result.elementAt(i));
        }
    }

    // Test the matches method with only one argument
    @Test
    public void testMatches1() {
        JSPredicateForm form1 = new JSPredicateForm("(not (test))");
        JSPredicateForm form2 = new JSPredicateForm("(not (test))");
        JSSubstitution result = form1.matches(form2);
        assertFalse(result.fail());
    }

    // Test the matches method with two arguments
    @Test
    public void testMatches2() {
        JSPredicateForm form1 = new JSPredicateForm("(not (test))");
        JSPredicateForm form2 = new JSPredicateForm("(not (test))");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = form1.matches(form2, alpha);
        assertFalse(result.fail());
    }

    // Test the equals method
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm("(not (test))");
        JSPredicateForm form2 = new JSPredicateForm("(not (test))");
        assertTrue(form1.equals(form2));
    }
    
    // Test the standarizerPredicateForm method
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(not (test))");
        JSPredicateForm result = form.standarizerPredicateForm();
        assertEquals(form.size(), result.size());
        for (int i = 0; i < form.size(); i++) {
            assertEquals(form.elementAt(i), result.elementAt(i));
            assertNotSame(form.elementAt(i), result.elementAt(i));
        }
    }
}