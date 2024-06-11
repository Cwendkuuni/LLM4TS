package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.JSPredicateFormInit(null);
        assertTrue(pf.isEmpty());
        
        pf = new JSPredicateForm("");
        assertTrue(pf.isEmpty());
        
        pf = new JSPredicateForm("(");
        assertTrue(pf.isEmpty());
        
        pf = new JSPredicateForm(")");
        assertTrue(pf.isEmpty());
        
        pf = new JSPredicateForm("%%%");
        assertTrue(pf.isEmpty());
        
        pf = new JSPredicateForm("!");
        assertTrue(pf.isEmpty());
        
        pf = new JSPredicateForm("!predicate");
        assertFalse(pf.isEmpty());
        
        pf = new JSPredicateForm("not");
        assertTrue(pf.isEmpty());
        
        pf = new JSPredicateForm("call");
        assertTrue(pf.isEmpty());
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.print();
        
        pf.addElement("not");
        pf.addElement(new JSPredicateForm());
        pf.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm pf = new JSPredicateForm();
        assertEquals(new StringBuffer().toString(), pf.toStr().toString());
        
        pf.addElement("not");
        pf.addElement(new JSPredicateForm());
        assertFalse(new StringBuffer().toString().equals(pf.toStr().toString()));
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm pf = new JSPredicateForm();
        JSPredicateForm clonedPF = pf.clonePF();
        assertTrue(pf.equals(clonedPF));
        
        pf.addElement("not");
        pf.addElement(new JSPredicateForm());
        clonedPF = pf.clonePF();
        assertTrue(pf.equals(clonedPF));
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm pf = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm newPF = pf.applySubstitutionPF(alpha);
        assertTrue(pf.equals(newPF));
        
        pf.addElement("not");
        pf.addElement(new JSPredicateForm());
        newPF = pf.applySubstitutionPF(alpha);
        assertTrue(pf.equals(newPF));
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm pf = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution beta = pf.matches(t, alpha);
        assertTrue(beta.fail());
        
        pf.addElement("not");
        pf.addElement(new JSPredicateForm());
        beta = pf.matches(t, alpha);
        assertTrue(beta.fail());
        
        t.addElement("not");
        t.addElement(new JSPredicateForm());
        beta = pf.matches(t, alpha);
        assertFalse(beta.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm pf = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        assertFalse(pf.equals(t));
        
        pf.addElement("not");
        assertFalse(pf.equals(t));
        
        t.addElement("not");
        assertFalse(pf.equals(t));
        
        pf.addElement(new JSPredicateForm());
        t.addElement(new JSPredicateForm());
        assertTrue(pf.equals(t));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm();
        JSPredicateForm newPF = pf.standarizerPredicateForm();
        assertTrue(pf.equals(newPF));
        
        pf.addElement("not");
        pf.addElement(new JSPredicateForm());
        newPF = pf.standarizerPredicateForm();
        assertTrue(pf.equals(newPF));
    }
}