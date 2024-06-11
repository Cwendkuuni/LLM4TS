import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertEquals(0, jspf.size());
        
        jspf = new JSPredicateForm("(not (call hello))");
        assertEquals(3, jspf.size());
        assertEquals("not", jspf.elementAt(0));
        assertEquals("call", jspf.elementAt(1));
        assertTrue(jspf.elementAt(2) instanceof JSPredicateForm);
        
        jspf = new JSPredicateForm("(test 1 2 3)");
        assertEquals(4, jspf.size());
        assertEquals("test", jspf.elementAt(0));
        assertEquals(1, jspf.elementAt(1));
        assertEquals(2, jspf.elementAt(2));
        assertEquals(3, jspf.elementAt(3));
    }

    @Test
    public void testPrint() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (call hello))");
        jspf1.print(); // expected output: (not (call hello))
        
        JSPredicateForm jspf2 = new JSPredicateForm("(test 1 2 3)");
        jspf2.print(); // expected output: (test 1 2 3)
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (call hello))");
        assertEquals("(not (call hello))", jspf1.toStr().toString());
        
        JSPredicateForm jspf2 = new JSPredicateForm("(test 1 2 3)");
        assertEquals("(test 1 2 3)", jspf2.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (call hello))");
        JSPredicateForm clone1 = jspf1.clonePF();
        assertEquals(jspf1.size(), clone1.size());
        
        JSPredicateForm jspf2 = new JSPredicateForm("(test 1 2 3)");
        JSPredicateForm clone2 = jspf2.clonePF();
        assertEquals(jspf2.size(), clone2.size());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.put("x", 1);
        alpha.put("y", 2);
        alpha.put("z", 3);
        
        JSPredicateForm jspf1 = new JSPredicateForm("(not (call hello))");
        JSPredicateForm result1 = jspf1.applySubstitutionPF(alpha);
        assertEquals(jspf1.size(), result1.size());
        
        JSPredicateForm jspf2 = new JSPredicateForm("(test x y z)");
        JSPredicateForm result2 = jspf2.applySubstitutionPF(alpha);
        assertEquals(jspf2.size(), result2.size());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (call hello))");
        JSPredicateForm jspf2 = new JSPredicateForm("(not (call world))");
        JSSubstitution result1 = jspf1.matches(jspf2);
        assertNull(result1);
        
        JSPredicateForm jspf3 = new JSPredicateForm("(test 1 2 3)");
        JSPredicateForm jspf4 = new JSPredicateForm("(test x y z)");
        JSSubstitution result2 = jspf3.matches(jspf4);
        assertNull(result2);
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (call hello))");
        JSPredicateForm jspf2 = new JSPredicateForm("(not (call world))");
        assertEquals(false, jspf1.equals(jspf2));
        
        JSPredicateForm jspf3 = new JSPredicateForm("(test 1 2 3)");
        JSPredicateForm jspf4 = new JSPredicateForm("(test 1 2 3)");
        assertEquals(true, jspf3.equals(jspf4));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm jspf1 = new JSPredicateForm("(not (call hello))");
        JSPredicateForm result1 = jspf1.standarizerPredicateForm();
        assertEquals(jspf1.size(), result1.size());
        
        JSPredicateForm jspf2 = new JSPredicateForm("(test 1 2 3)");
        JSPredicateForm result2 = jspf2.standarizerPredicateForm();
        assertEquals(jspf2.size(), result2.size());
    }
}