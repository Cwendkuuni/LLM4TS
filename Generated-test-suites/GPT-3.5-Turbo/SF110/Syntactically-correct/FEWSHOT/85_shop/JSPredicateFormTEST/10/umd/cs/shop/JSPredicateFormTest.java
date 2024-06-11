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
        assertEquals(0, pf1.size());
        
        String text = "(pred1 arg1 arg2)";
        JSPredicateForm pf2 = new JSPredicateForm(text);
        assertEquals(4, pf2.size());
        
        StringReader reader = new StringReader(text);
        JSPredicateForm pf3 = new JSPredicateForm(new StreamTokenizer(reader));
        assertEquals(4, pf3.size());
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitTokenizerNull() {
        JSPredicateForm pf = new JSPredicateForm(null);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitTokenizeExpectLeftPar() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(""));
        JSPredicateForm pf = new JSPredicateForm(tokenizer);
    }
    
    // The remaining test cases are to be implemented based on the methods provided in the class.
    
    @Test
    public void testPrint() {
        
    }
    
    @Test
    public void testToStr() {
        
    }
    
    @Test
    public void testClonePF() {
        
    }
    
    @Test
    public void testApplySubstitutionPF() {
        
    }
    
    @Test
    public void testMatches() {
        
    }
    
    @Test
    public void testEquals() {
        
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        
    }
}