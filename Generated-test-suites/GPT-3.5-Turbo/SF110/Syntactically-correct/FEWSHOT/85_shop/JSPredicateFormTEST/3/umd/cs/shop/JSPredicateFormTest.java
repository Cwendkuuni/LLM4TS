package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm();
        assertNotNull(pf);
    }
    
    @Test
    public void testJSPredicateFormString() {
        JSPredicateForm pf = new JSPredicateForm("");
        assertNotNull(pf);
        
        pf = new JSPredicateForm(null);
        assertNotNull(pf);
    }
    
    @Test
    public void testJSPredicateFormStreamTokenizer() {
        JSPredicateForm pf = new JSPredicateForm(null);
        assertNotNull(pf);
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        pf = new JSPredicateForm(tokenizer);
        assertNotNull(pf);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitThrowsJSParserError() {
        StreamTokenizer tokenizer = new StreamTokenizer(null);
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitThrowsJSParserError() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.addElement("");
        pf.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm pf = new JSPredicateForm();
        StringBuffer str = pf.toStr();
        assertNotNull(str);
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm pf = new JSPredicateForm();
        JSPredicateForm clone = pf.clonePF();
        assertNotSame(pf, clone);
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm pf = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm newPF = pf.applySubstitutionPF(alpha);
        assertNotSame(pf, newPF);
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm pf1 = new JSPredicateForm();
        JSPredicateForm pf2 = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = pf1.matches(pf2, alpha);
        assertNotNull(beta);
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm pf1 = new JSPredicateForm();
        JSPredicateForm pf2 = new JSPredicateForm();
        assertFalse(pf1.equals(null));
        assertTrue(pf1.equals(pf2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm();
        JSPredicateForm newPF = pf.standarizerPredicateForm();
        assertNotSame(pf, newPF);
    }
}