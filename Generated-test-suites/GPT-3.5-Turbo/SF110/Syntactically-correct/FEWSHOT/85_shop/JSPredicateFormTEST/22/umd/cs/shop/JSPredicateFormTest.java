package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateFormConstructor() {
        JSPredicateForm form = new JSPredicateForm();
        assertNotNull(form);
        
        String text = "example text";
        form = new JSPredicateForm(text);
        assertNotNull(form);
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        form = new JSPredicateForm(tokenizer);
        assertNotNull(form);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNullTokenizer() {
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(null);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test)"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingPredicate() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("()"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate) )"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitMissingRightPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate"));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.print();
        
        form.addElement("not");
        form.addElement("test");
        form.print();
        
        form.addElement("term");
        form.addElement(new JSTerm());
        form.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        StringBuffer res = form.toStr();
        assertEquals("", res.toString());
        
        form.addElement("not");
        form.addElement("test");
        res = form.toStr();
        assertEquals("(not test)", res.toString());
        
        form.addElement("term");
        form.addElement(new JSTerm());
        res = form.toStr();
        assertEquals("(not test term )", res.toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm clone = form.clonePF();
        assertEquals(form.size(), clone.size());
        
        form.addElement("not");
        form.addElement("test");
        clone = form.clonePF();
        assertEquals(form.size(), clone.size());
        
        form.addElement("term");
        form.addElement(new JSTerm());
        clone = form.clonePF();
        assertEquals(form.size(), clone.size());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm newForm = form.applySubstitutionPF(alpha);
        assertEquals(form.size(), newForm.size());
        
        form.addElement("not");
        form.addElement("test");
        newForm = form.applySubstitutionPF(alpha);
        assertEquals(form.size(), newForm.size());
        
        form.addElement("term");
        form.addElement(new JSTerm());
        newForm = form.applySubstitutionPF(alpha);
        assertEquals(form.size(), newForm.size());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm();
        JSPredicateForm form2 = new JSPredicateForm();
        JSSubstitution alpha = form1.matches(form2);
        assertFalse(alpha.fail());
        
        form1.addElement("not");
        form1.addElement("test");
        form2.addElement("not");
        form2.addElement("test");
        alpha = form1.matches(form2);
        assertFalse(alpha.fail());
        
        form1.addElement("term");
        form1.addElement(new JSTerm());
        form2.addElement("term");
        form2.addElement(new JSTerm());
        alpha = form1.matches(form2);
        assertFalse(alpha.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm();
        JSPredicateForm form2 = null;
        boolean result = form1.equals(form2);
        assertFalse(result);
        
        form2 = new JSPredicateForm();
        result = form1.equals(form2);
        assertTrue(result);
        
        form1.addElement("not");
        form1.addElement("test");
        form2.addElement("not");
        form2.addElement("test");
        result = form1.equals(form2);
        assertTrue(result);
        
        form1.addElement("term");
        form1.addElement(new JSTerm());
        form2.addElement("term");
        form2.addElement(new JSTerm());
        result = form1.equals(form2);
        assertTrue(result);
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        JSPredicateForm nTA = form.standarizerPredicateForm();
        assertEquals(form.size(), nTA.size());
        
        form.addElement("not");
        form.addElement("test");
        nTA = form.standarizerPredicateForm();
        assertEquals(form.size(), nTA.size());
        
        form.addElement("term");
        form.addElement(new JSTerm());
        nTA = form.standarizerPredicateForm();
        assertEquals(form.size(), nTA.size());
    }
}