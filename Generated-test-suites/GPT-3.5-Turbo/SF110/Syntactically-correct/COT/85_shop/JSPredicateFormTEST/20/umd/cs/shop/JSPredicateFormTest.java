
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSParserError;
import umd.cs.shop.JSJshopVars;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSUtil;

import java.io.StringReader;
import java.util.stream.StreamTokenizer;


public class JSPredicateFormTest {

    private JSPredicateForm jspf;
    
    @Before
    public void setUp() {
        jspf = new JSPredicateForm();
    }
    
    @Test
    public void testJSPredicateForm() {
        String text = "(test)";
        JSPredicateForm jspf = new JSPredicateForm(text);
        assertEquals(text, jspf.toStr().toString());
        
        StreamTokenizer tokenizer = createTokenizer(text);
        jspf = new JSPredicateForm(tokenizer);
        assertEquals(text, jspf.toStr().toString());
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormNullTokenizer() {
        StreamTokenizer tokenizer = null;
        JSPredicateForm jspf = new JSPredicateForm(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedRightPar() {
        StreamTokenizer tokenizer = createTokenizer(")");
        jspf.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitUnexpectedOperatorName() {
        StreamTokenizer tokenizer = createTokenizer("! %%%");
        jspf.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitNotWithoutRightPar() {
        StreamTokenizer tokenizer = createTokenizer("not");
        jspf.JSPredicateFormInit(tokenizer);
    }
    
    @Test(expected = JSParserError.class)
    public void testJSPredicateFormInitCallWithoutLeftPar() {
        StreamTokenizer tokenizer = createTokenizer("call");
        jspf.JSPredicateFormInit(tokenizer);
    }
    
    @Test
    public void testPrint() {
        jspf.addElement("test");
        jspf.print();
    }
    
    @Test
    public void testToStr() {
        jspf.addElement("test");
        assertEquals("(test)", jspf.toStr().toString());
    }
    
    @Test
    public void testClonePF() {
        jspf.addElement("test");
        JSPredicateForm clone = jspf.clonePF();
        assertEquals(jspf.toStr().toString(), clone.toStr().toString());
        
        JSPredicateForm not = new JSPredicateForm("!test");
        clone = not.clonePF();
        assertEquals(not.toStr().toString(), clone.toStr().toString());
    }
    
    @Test
    public void testApplySubstitutionPF() {
        JSTerm term = new JSTerm("term");
        JSSubstitution substitution = new JSSubstitution();
        substitution.addElement(term, term);
        
        jspf.addElement("test");
        JSPredicateForm result = jspf.applySubstitutionPF(substitution);
        assertEquals(jspf.toStr().toString(), result.toStr().toString());
        
        JSPredicateForm not = new JSPredicateForm("!term");
        result = not.applySubstitutionPF(substitution);
        assertEquals(not.toStr().toString(), result.toStr().toString());
    }
    
    @Test
    public void testMatches() {
        JSPredicateForm t = new JSPredicateForm("test");
        JSSubstitution alpha = new JSSubstitution();
        
        assertTrue(jspf.matches(t).fail());
        assertTrue(jspf.matches(t, alpha).fail());
        
        assertFalse(jspf.matches(jspf));
        assertFalse(jspf.matches(jspf.clonePF()));
    }
    
    @Test
    public void testEquals() {
        JSPredicateForm t = new JSPredicateForm("test");
        
        assertFalse(jspf.equals(null));
        
        assertTrue(jspf.equals(jspf));
        assertTrue(jspf.equals(jspf.clonePF()));
        
        assertFalse(jspf.equals(t));
        assertFalse(t.equals(jspf));
    }
    
    @Test
    public void testStandarizerPredicateForm() {
        jspf.addElement("test");
        JSPredicateForm result = jspf.standarizerPredicateForm();
        assertEquals(jspf.toStr().toString(), result.toStr().toString());
        
        JSPredicateForm not = new JSPredicateForm("!term");
        result = not.standarizerPredicateForm();
        assertEquals(not.toStr().toString(), result.toStr().toString());
    }
    
    private StreamTokenizer createTokenizer(String text) {
        return new StreamTokenizer(new StringReader(text));
    }
}
