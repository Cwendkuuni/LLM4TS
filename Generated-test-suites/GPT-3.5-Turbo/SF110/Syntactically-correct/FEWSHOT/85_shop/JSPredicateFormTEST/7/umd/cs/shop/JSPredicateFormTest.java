package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.stream.StreamTokenizer;

import org.junit.Test;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm();
        assertEquals(0, form1.size());
        
        String text = "(not (call func))";
        JSPredicateForm form2 = new JSPredicateForm(text);
        assertEquals(3, form2.size());
        assertEquals("not", form2.elementAt(0));
        assertEquals("call", form2.elementAt(1));
        assertEquals("func", form2.elementAt(2));
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm form3 = new JSPredicateForm(tokenizer);
        assertEquals(3, form3.size());
        assertEquals("not", form3.elementAt(0));
        assertEquals("call", form3.elementAt(1));
        assertEquals("func", form3.elementAt(2));
        
        Form form4 = new JSPredicateForm(null);
        assertEquals(0, form4.size());
    }
    
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(call func)"));
        JSPredicateForm form1 = new JSPredicateForm();
        form1.JSPredicateFormInit(tokenizer);
        assertEquals(3, form1.size());
        assertEquals("call", form1.elementAt(0));
        assertEquals("func", form1.elementAt(1));
        
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(not (call func))"));
        JSPredicateForm form2 = new JSPredicateForm();
        form2.JSPredicateFormInit(tokenizer2);
        assertEquals(3, form2.size());
        assertEquals("not", form2.elementAt(0));
        assertEquals("call", form2.elementAt(1));
        assertEquals("func", form2.elementAt(2));
        
        StreamTokenizer tokenizer3 = new StreamTokenizer(new StringReader("(not (call func))"));
        JSPredicateForm form3 = new JSPredicateForm();
        form3.JSPredicateFormInit(tokenizer3);
        assertEquals(3, form3.size());
        assertEquals("not", form3.elementAt(0));
        assertEquals("call", form3.elementAt(1));
        assertEquals("func", form3.elementAt(2));
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_Exception() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("func");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement("call");
        form2.addElement(form1);
        
        form2.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("func");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement("call");
        form2.addElement(form1);
        
        form2.toStr();
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("func");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement(form1);
        
        JSPredicateForm form3 = form2.clonePF();
        assertEquals(2, form3.size());
        assertEquals("not", form3.elementAt(0));
        JSPredicateForm form4 = (JSPredicateForm) form3.elementAt(1);
        assertEquals(2, form4.size());
        assertEquals("call", form4.elementAt(0));
        assertEquals("func", form4.elementAt(1));
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("func");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement(form1);
        
        JSPredicateForm form3 = form2.applySubstitutionPF(alpha);
        assertEquals(2, form3.size());
        assertEquals("not", form3.elementAt(0));
        JSPredicateForm form4 = (JSPredicateForm) form3.elementAt(1);
        assertEquals(2, form4.size());
        assertEquals("call", form4.elementAt(0));
        assertEquals("func", form4.elementAt(1));
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("func1");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement("call");
        form2.addElement(form1);
        
        JSPredicateForm form3 = new JSPredicateForm();
        form3.addElement("not");
        form3.addElement("call");
        form3.addElement(form1);
        
        JSSubstitution alpha = form2.matches(form3);
        assertFalse(alpha.fail());
    }
    
    @Test
    public void testMatches_Failure() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("func1");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement("call");
        form2.addElement(form1);
        
        JSPredicateForm form3 = new JSPredicateForm();
        form3.addElement("not");
        form3.addElement("call");
        
        JSSubstitution alpha = form2.matches(form3);
        assertTrue(alpha.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("func1");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement("call");
        form2.addElement(form1);
        
        JSPredicateForm form3 = new JSPredicateForm();
        form3.addElement("not");
        form3.addElement("call");
        form3.addElement(form1);
        
        assertTrue(form2.equals(form3));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("func1");
        
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("not");
        form2.addElement("call");
        form2.addElement(form1);
        
        form2.standarizerPredicateForm();
    }
}