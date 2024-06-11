package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        assertEquals(0, form.size());
    }
    
    @Test
    public void testJSPredicateFormString() {
        String text = "(call (term1) (term2))";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(5, form.size());
        assertEquals("(", form.elementAt(0));
        assertEquals("call", form.elementAt(1));
        assertEquals("(", form.elementAt(2));
        assertEquals("term1", form.elementAt(3));
        assertEquals("term2", form.elementAt(4));
    }
    
    @Test
    public void testJSPredicateFormTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(null);
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertEquals(0, form.size());
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormNullTokenizer() {
        JSPredicateForm form = new JSPredicateForm(null);
        assertEquals(0, form.size());
    }
    
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(null);
        JSPredicateForm form = new JSPredicateForm();
        form.JSPredicateFormInit(tokenizer);
        assertEquals(0, form.size());
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("(");
        form.addElement("call");
        form.addElement("(");
        form.addElement("term1");
        form.addElement("term2");
        form.addElement(")");
        form.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("(");
        form.addElement("call");
        form.addElement("(");
        form.addElement("term1");
        form.addElement("term2");
        form.addElement(")");
        StringBuffer res = form.toStr();
        assertEquals("(call(term1term2))", res.toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("call(term1)"));
        JSPredicateForm clonedForm = form.clonePF();
        assertTrue(form.equals(clonedForm));
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("call(term1)"));
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm appliedForm = form.applySubstitutionPF(alpha);
        assertTrue(form.equals(appliedForm));
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("term1");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("call");
        form2.addElement("term1");
        JSSubstitution beta = form1.matches(form2);
        assertFalse(beta.fail());
    }
    
    @Test
    public void testMatchesWithAlpha() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("term1");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("call");
        form2.addElement("term1");
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = form1.matches(form2, alpha);
        assertFalse(beta.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("call");
        form1.addElement("term1");
        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("call");
        form2.addElement("term1");
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement(new JSPredicateForm("call(term1)"));
        JSPredicateForm standarizedForm = form.standarizerPredicateForm();
        assertTrue(form.equals(standarizedForm));
    }
}