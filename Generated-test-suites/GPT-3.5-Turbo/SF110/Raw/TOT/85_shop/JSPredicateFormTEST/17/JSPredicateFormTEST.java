import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateFormDefault() {
        JSPredicateForm form = new JSPredicateForm();
        assertTrue(form.isEmpty());
    }
    
    @Test
    public void testJSPredicateFormString() {
        String text = "(not p)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals("(not p)", form.toStr().toString());
    }
    
    @Test
    public void testJSPredicateFormStreamTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call (+ a b))"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertEquals("(call (+ a b))", form.toStr().toString());
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNullTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingLeftPar() {
        String text = "not p)";
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingPredicate() {
        String text = "()";
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightPar() {
        String text = "(p))";
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingRightPar() {
        String text = "(not p";
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(new StreamTokenizer(new StringReader(text)));
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("(not p)");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        form.print();
        assertEquals("(not p ) ", outContent.toString());
    }
    
    @Test
    public void testToString() {
        JSPredicateForm form = new JSPredicateForm("(not p)");
        assertEquals("(not p)", form.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form1 = new JSPredicateForm("(not p)");
        JSPredicateForm form2 = form1.clonePF();
        assertEquals("(not p)", form2.toStr().toString());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form1 = new JSPredicateForm("(not p)");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm form2 = form1.applySubstitutionPF(alpha);
        assertEquals("(not p)", form2.toStr().toString());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm("(not p)");
        JSPredicateForm form2 = new JSPredicateForm("(not q)");
        JSSubstitution result = form1.matches(form2);
        assertTrue(result.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm("(not p)");
        JSPredicateForm form2 = new JSPredicateForm("(not q)");
        assertFalse(form1.equals(form2));
    }
    
    @Test
    public void testStandardizerPredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm("(not p)");
        JSPredicateForm form2 = form1.standarizerPredicateForm();
        assertEquals("(not p)", form2.toStr().toString());
    }
    
}