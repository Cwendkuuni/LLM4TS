package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.Vector;

import org.junit.Test;

public class JSPredicateFormTest {
    
    @Test
    public void testJSPredicateForm() {
        JSPredicateForm pf1 = new JSPredicateForm();
        assertTrue(pf1.isEmpty());
        
        String text = "(test)";
        JSPredicateForm pf2 = new JSPredicateForm(text);
        assertTrue(pf2.contains("test"));
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(text));
        JSPredicateForm pf3 = new JSPredicateForm(tokenizer);
        assertTrue(pf3.contains("test"));

        StreamTokenizer nullTokenizer = null;
        try {
            new JSPredicateForm(nullTokenizer);
        } catch (JSParserError e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testJSPredicateFormInit() {
        Vector<Object> elements = new Vector<>();
        
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(test1 test2)"));
        tokenizer.nextToken();
        JSPredicateForm jpf = new JSPredicateForm();
        try {
            jpf.JSPredicateFormInit(tokenizer);
            elements.addElement("test1");
            elements.addElement("test2");
            for (int i = 0; i < elements.size(); i++) {
                assertEquals(elements.elementAt(i), jpf.elementAt(i));
            }
        } catch (JSParserError e) {
            assertFalse(true);
        }
        
        tokenizer = new StreamTokenizer(new StringReader("(test1 (call (test2)))"));
        tokenizer.nextToken();
        jpf = new JSPredicateForm();
        try {
            jpf.JSPredicateFormInit(tokenizer);
            elements.addElement("test1");
            elements.addElement("call");
            elements.addElement(new JSPredicateForm("(test2)"));
            for (int i = 0; i < elements.size(); i++) {
                assertEquals(elements.elementAt(i), jpf.elementAt(i));
            }
        } catch (JSParserError e) {
            assertFalse(true);
        }
        
        tokenizer = new StreamTokenizer(new StringReader("(test1 (not (test2)))"));
        tokenizer.nextToken();
        jpf = new JSPredicateForm();
        try {
            jpf.JSPredicateFormInit(tokenizer);
            elements.addElement("test1");
            elements.addElement("not");
            elements.addElement(new JSPredicateForm("(test2)"));
            for (int i = 0; i < elements.size(); i++) {
                assertEquals(elements.elementAt(i), jpf.elementAt(i));
            }
        } catch (JSParserError e) {
            assertFalse(true);
        }
        
        tokenizer = new StreamTokenizer(new StringReader("(test1)"));
        tokenizer.nextToken();
        jpf = new JSPredicateForm();
        try {
            jpf.JSPredicateFormInit(tokenizer);
            elements.addElement("test1");
            for (int i = 0; i < elements.size(); i++) {
                assertEquals(elements.elementAt(i), jpf.elementAt(i));
            }
        } catch (JSParserError e) {
            assertFalse(true);
        }
        
        tokenizer = new StreamTokenizer(new StringReader("())"));
        tokenizer.nextToken();
        try {
            new JSPredicateForm().JSPredicateFormInit(tokenizer);
        } catch (JSParserError e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testPrint() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.add("test");
        pf.print();
        
        JSPredicateForm pf2 = new JSPredicateForm("(not (test1))");
        pf2.print();
    }
    
    @Test
    public void testToStr() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.add("test");
        String expected = "(test)";
        assertEquals(expected, pf.toStr().toString());
        
        JSPredicateForm pf2 = new JSPredicateForm("(not (test1))");
        String expected2 = "(not (test1))";
        assertEquals(expected2, pf2.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.addElement("test1");
        
        JSPredicateForm pfClone = pf.clonePF();
        assertEquals(pf.elementAt(0), pfClone.elementAt(0));
        
        JSPredicateForm pf2 = new JSPredicateForm("(not (test1))");
        JSPredicateForm pfClone2 = pf2.clonePF();
        assertEquals(pf2.elementAt(0), pfClone2.elementAt(0));
        assertEquals(pf2.elementAt(1).toString(), pfClone2.elementAt(1).toString());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm pf = new JSPredicateForm();
        pf.addElement("test1");
        
        JSSubstitution alpha = new JSSubstitution();
        
        JSPredicateForm newPF = pf.applySubstitutionPF(alpha);
        assertEquals(pf.elementAt(0), newPF.elementAt(0));
        
        JSPredicateForm pf2 = new JSPredicateForm("(not (test1))");
        JSSubstitution alpha2 = new JSSubstitution();
        
        JSPredicateForm newPF2 = pf.applySubstitutionPF(alpha2);
        assertEquals(pf.elementAt(0), newPF2.elementAt(0));
        assertEquals(pf.elementAt(1).toString(), newPF2.elementAt(1).toString());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm pf = new JSPredicateForm("(test)");
        JSPredicateForm pf2 = new JSPredicateForm("(test)");
        JSSubstitution alpha = new JSSubstitution();
        
        JSSubstitution beta = pf.matches(pf2, alpha);
        assertTrue(beta.isUnassigned());
        
        JSPredicateForm pf3 = new JSPredicateForm("(not (test))");
        JSSubstitution beta2 = pf3.matches(pf, alpha);
        assertFalse(beta2.isUnassigned());
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm pf = new JSPredicateForm("(test)");
        JSPredicateForm pf2 = new JSPredicateForm("(test)");
        
        assertTrue(pf.equals(pf2));
        
        JSPredicateForm pf3 = new JSPredicateForm("(not (test))");
        assertFalse(pf3.equals(pf));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm pf = new JSPredicateForm("(test)");
        JSPredicateForm newPF = pf.standarizerPredicateForm();
        
        assertEquals(pf.elementAt(0), newPF.elementAt(0));
        
        JSPredicateForm pf2 = new JSPredicateForm("(not (test))");
        JSPredicateForm newPF2 = pf2.standarizerPredicateForm();
        
        assertEquals(pf2.elementAt(0), newPF2.elementAt(0));
        assertEquals(pf2.elementAt(1).toString(), newPF2.elementAt(1).toString());
    }
}