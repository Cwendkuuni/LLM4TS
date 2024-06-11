package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm form1 = new JSPredicateForm("call (predicate)");
        assertEquals(4, form1.size());
        
        JSPredicateForm form2 = new JSPredicateForm("!predicate");
        assertEquals(2, form2.size());
        
        JSPredicateForm form3 = new JSPredicateForm("! true");
        assertEquals(2, form3.size());
        
        JSPredicateForm form4 = new JSPredicateForm("not true");
        assertEquals(3, form4.size());
        
        JSPredicateForm form5 = new JSPredicateForm("(predicate)");
        assertEquals(2, form5.size());
        
        JSPredicateForm form6 = new JSPredicateForm("(predicate1, predicate2)");
        assertEquals(3, form6.size());
    }
    
    @Test(expected=JSParserError.class)
    public void testJSPredicateFormInitNullTokenizerException() {
        JSPredicateForm form = new JSPredicateForm(null);
    }
    
    @Test(expected=JSParserError.class)
    public void testJSPredicateFormInitMissingLeftParException() {
        JSPredicateForm form = new JSPredicateForm("call predicate)");
    }
    
    @Test(expected=JSParserError.class)
    public void testJSPredicateFormInitMissingPredicateException() {
        JSPredicateForm form = new JSPredicateForm("()");
    }
    
    @Test(expected=JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightParException() {
        JSPredicateForm form = new JSPredicateForm("(predicate))");
    }
    
    @Test(expected=JSParserError.class)
    public void testJSPredicateFormInitExclamationExpectedException() {
        JSPredicateForm form = new JSPredicateForm("(predicate");
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm("(predicate1, predicate2)");
        form.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm("(predicate1, predicate2)");
        StringBuffer str = form.toStr();
        assertEquals("(predicate1 predicate2)", str.toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm form1 = new JSPredicateForm("(not (predicate1, predicate2))");
        JSPredicateForm cloneForm1 = form1.clonePF();
        
        JSPredicateForm form2 = new JSPredicateForm("(predicate1, predicate2)");
        JSPredicateForm cloneForm2 = form2.clonePF();
        
        assertTrue(form1.equals(cloneForm1));
        assertTrue(form2.equals(cloneForm2));
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form1 = new JSPredicateForm("predicate");
        JSSubstitution alpha1 = new JSSubstitution();
        alpha1.add("predicate", "newValue");
        JSPredicateForm newForm1 = form1.applySubstitutionPF(alpha1);
        
        JSPredicateForm form2 = new JSPredicateForm("(not predicate)");
        JSSubstitution alpha2 = new JSSubstitution();
        alpha2.add("predicate", "newValue");
        JSPredicateForm newForm2 = form2.applySubstitutionPF(alpha2);
        
        assertTrue(newForm1.elementAt(0).equals("newValue"));
        assertTrue(newForm2.elementAt(0).equals("not"));
        assertTrue(newForm2.elementAt(1).equals("newValue"));
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate1, predicate2, predicate3)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate1, newValue, predicate3)");
        JSSubstitution alpha = form1.matches(form2);
        
        assertFalse(alpha.fail());
        assertTrue(alpha.get("predicate2").equals("newValue"));
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm("(predicate1, predicate2)");
        JSPredicateForm form2 = new JSPredicateForm("(predicate1, predicate2)");
        
        assertTrue(form1.equals(form2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm("(predicate1, !predicate2, (predicate3))");
        JSPredicateForm standarizedForm = form.standarizerPredicateForm();
        
        assertTrue(standarizedForm.elementAt(0).equals("predicate1"));
        assertTrue(standarizedForm.elementAt(1).equals("!"));
        assertTrue(standarizedForm.elementAt(2).elementAt(0).equals("predicate2"));
        assertTrue(standarizedForm.elementAt(3).elementAt(0).equals("predicate3"));
    }
    
}