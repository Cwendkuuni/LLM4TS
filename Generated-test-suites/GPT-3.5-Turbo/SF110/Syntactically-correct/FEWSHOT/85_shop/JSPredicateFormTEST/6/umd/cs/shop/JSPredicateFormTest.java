package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.StringReader;
import java.util.*;

public class JSPredicateFormTest {
    
    private static JSPredicateForm pf;
    
    @BeforeClass
    public static void setUp() {
        String text = "(JSPredicateFormTest (JSTerm1) (JSTerm2))";
        pf = new JSPredicateForm(text);
    }
    
    @Test
    public void testJSPredicateForm() {
        JSPredicateForm pf1 = new JSPredicateForm();
        assertEquals(0, pf1.size());
    }
    
    @Test
    public void testJSPredicateFormWithText() {
        String text = "(JSPredicateFormTest (JSTerm1) (JSTerm2))";
        JSPredicateForm pf1 = new JSPredicateForm(text);
        assertEquals(3, pf1.size());
        assertEquals("JSPredicateFormTest", pf1.elementAt(0));
        assertEquals("JSTerm1", pf1.elementAt(1));
        assertEquals("JSTerm2", pf1.elementAt(2));
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormWithInvalidText() {
        String text = "(JSPredicateFormTest (JSTerm1) (JSTerm2)";
        JSPredicateForm pf1 = new JSPredicateForm(text);
    }
    
    @Test
    public void testJSPredicateFormInitWithTokenizer() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(JSPredicateFormTest (JSTerm1) (JSTerm2))"));
        pf.JSPredicateFormInit(tokenizer);
        assertEquals(3, pf.size());
        assertEquals("JSPredicateFormTest", pf.elementAt(0));
        assertEquals("JSTerm1", pf.elementAt(1));
        assertEquals("JSTerm2", pf.elementAt(2));
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithNullTokenizer() {
        StreamTokenizer tokenizer = null;
        pf.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitWithInvalidToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(JSPredicateFormTest (JSTerm1) (JSTerm2)"));
        pf.JSPredicateFormInit(tokenizer);
    }
    
    @Test
    public void testPrint() {
        pf.print();
    }
    
    @Test
    public void testToStr() {
        StringBuffer res = pf.toStr();
        assertEquals("(JSPredicateFormTest (JSTerm1) (JSTerm2))", res.toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm cloned = pf.clonePF();
        assertEquals(pf.size(), cloned.size());
        for (int i = 0; i < pf.size(); i++) {
            assertEquals(pf.elementAt(i), cloned.elementAt(i));
        }
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSSubstitution alpha = new JSSubstitution();
        alpha.addSubstitution("JSTerm1", "value1");
        alpha.addSubstitution("JSTerm2", "value2");
        
        JSPredicateForm applied = pf.applySubstitutionPF(alpha);
        assertEquals(pf.size(), applied.size());
        assertEquals("JSPredicateFormTest", applied.elementAt(0));
        assertEquals("value1", applied.elementAt(1));
        assertEquals("value2", applied.elementAt(2));
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm t = new JSPredicateForm("(JSPredicateFormTest (JSTerm1) (JSTerm2))");
        JSSubstitution alpha = pf.matches(t);
        assertFalse(alpha.fail());
        assertEquals(0, alpha.size());
        
        JSSubstitution beta = pf.matches(null);
        assertTrue(beta.fail());
        assertEquals(0, beta.size());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm t = new JSPredicateForm("(JSPredicateFormTest (JSTerm1) (JSTerm2))");
        assertTrue(pf.equals(t));
        
        JSPredicateForm t1 = new JSPredicateForm("(JSPredicateFormTest (JSTerm1) (JSTerm3))");
        assertFalse(pf.equals(t1));
        
        assertFalse(pf.equals(null));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm standarized = pf.standarizerPredicateForm();
        assertEquals(pf.size(), standarized.size());
        for (int i = 0; i < pf.size(); i++) {
            assertEquals(pf.elementAt(i), standarized.elementAt(i));
        }
    }
}