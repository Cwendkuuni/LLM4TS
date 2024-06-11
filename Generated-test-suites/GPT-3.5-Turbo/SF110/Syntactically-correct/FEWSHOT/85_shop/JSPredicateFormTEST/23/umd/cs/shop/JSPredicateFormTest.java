
package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StreamTokenizer;
import java.io.StringReader;

import org.junit.Test;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm pf = new JSPredicateForm("(predicate a b)");
        assertEquals(3, pf.size());
        assertEquals("predicate", pf.get(0));
        assertEquals("a", pf.get(1));
        assertEquals("b", pf.get(2));
        
        pf = new JSPredicateForm("(predicate (nested) (terms))");
        assertEquals(3, pf.size());
        assertEquals("predicate", pf.get(0));
        assertEquals("(nested)", pf.get(1));
        assertEquals("(terms)", pf.get(2));
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_NullTokenizer() {
        new JSPredicateForm(null);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInit_InvalidToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("invalid"));
        tokenizer.nextToken();
        new JSPredicateForm(tokenizer);
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm pf = new JSPredicateForm("(predicate a)");
        pf.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm pf = new JSPredicateForm("(predicate a)");
        assertEquals("((predicate a))", pf.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm pf = new JSPredicateForm("(predicate a)");
        JSPredicateForm clone = pf.clonePF();
        assertTrue(clone.equals(pf));
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement("a", "b");
        
        JSPredicateForm pf = new JSPredicateForm("(predicate a)");
        JSPredicateForm result = pf.applySubstitutionPF(alpha);
        assertEquals("((predicate b))", result.toStr().toString());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm pf1 = new JSPredicateForm("(predicate a)");
        JSPredicateForm pf2 = new JSPredicateForm("(predicate a)");
        
        JSSubstitution alpha = pf1.matches(pf2);
        assertFalse(alpha.fail());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm pf1 = new JSPredicateForm("(predicate a)");
        JSPredicateForm pf2 = new JSPredicateForm("(predicate a)");
        
        assertTrue(pf1.equals(pf2));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm("(predicate (nested) (terms))");
        JSPredicateForm standardize = pf.standarizerPredicateForm();
        
        assertEquals("((predicate (nested) (terms)))", standardize.toStr().toString());
    }
}
