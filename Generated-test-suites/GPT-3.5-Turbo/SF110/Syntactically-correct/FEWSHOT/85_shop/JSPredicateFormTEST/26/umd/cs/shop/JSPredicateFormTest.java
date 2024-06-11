package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm();
        assertTrue(form1.isEmpty());
        
        String text = "(not (p x y))";
        JSPredicateForm form2 = new JSPredicateForm(text);
        assertFalse(form2.isEmpty());
        assertEquals(3, form2.size());
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm form3 = new JSPredicateForm(tokenizer);
        assertFalse(form3.isEmpty());
        assertEquals(3, form3.size());
        
        StreamTokenizer tokenizerNull = null;
        try {
            JSPredicateForm form4 = new JSPredicateForm(tokenizerNull);
        } catch (JSParserError e) {
            
        }
        
        StreamTokenizer tokenizerIOStream = new StreamTokenizer(new FileReader("inputfile.txt"));
        try {
            JSPredicateForm form5 = new JSPredicateForm(tokenizerIOStream);
        } catch (JSParserError e) {
            
        }
    }
    
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(p x y)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertFalse(form.isEmpty());
        assertEquals(3, form.size());
        
        StreamTokenizer tokenizerNull = null;
        try {
            form.JSPredicateFormInit(tokenizerNull);
        } catch (JSParserError e) {
            
        }
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("p");
        form.addElement("x");
        form.addElement("y");
        
        // Redirect println to a different output stream for testing
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        form.print();
        assertEquals("(p x y ) ", outContent.toString());
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("p");
        form.addElement("x");
        form.addElement("y");
        
        assertEquals("(p x y)", form.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("p");
        form.addElement("x");
        form.addElement("y");
        
        JSPredicateForm clonedForm = form.clonePF();
        assertFalse(clonedForm.isEmpty());
        assertEquals(3, clonedForm.size());
        assertTrue(clonedForm.equals(form));
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("p");
        form.addElement("x");
        form.addElement("y");
        
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        assertTrue(result.equals(form));
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("p");
        form1.addElement("x");
        form1.addElement("y");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("p");
        form2.addElement("x");
        form2.addElement("y");
        
        JSSubstitution alpha = new JSSubstitution();
        
        JSSubstitution result1 = form1.matches(form2);
        assertTrue(result1.isEmpty());
        
        JSSubstitution result2 = form1.matches(form2, alpha);
        assertTrue(result2.isEmpty());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("p");
        form1.addElement("x");
        form1.addElement("y");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("p");
        form2.addElement("x");
        form2.addElement("y");
        
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("p");
        form.addElement("x");
        form.addElement("y");
        
        JSPredicateForm result = form.standarizerPredicateForm();
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        assertTrue(result.equals(form));
    }
}