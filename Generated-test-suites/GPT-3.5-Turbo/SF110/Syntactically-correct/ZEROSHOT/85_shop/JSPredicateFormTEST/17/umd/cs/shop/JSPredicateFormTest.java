package umd.cs.shop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {
    
    private JSPredicateForm form;
    
    @Before
    public void setUp() {
        form = new JSPredicateForm();
    }
    
    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("test"));
        form.JSPredicateFormInit(tokenizer);
        assertNotNull(form);
    }
    
    @Test
    public void testPrint() {
        form.print();
    }
    
    @Test
    public void testToStr() {
        StringBuffer res = form.toStr();
        assertNotNull(res);
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm clone = form.clonePF();
        assertNotNull(clone);
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm result = form.applySubstitutionPF(alpha);
        assertNotNull(result);
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = form.matches(t, alpha);
        assertNotNull(result);
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm t = new JSPredicateForm();
        boolean result = form.equals(t);
        assertFalse(result);
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm result = form.standarizerPredicateForm();
        assertNotNull(result);
    }
}