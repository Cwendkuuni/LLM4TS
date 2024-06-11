import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {
    
    @Test
    public void testJSTermConstructor() {
        // Typical use case
        JSTerm term = new JSTerm();
        assertNotNull(term);
        
        // Edge case
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Example"));
        JSTerm term2 = new JSTerm(tokenizer);
        assertNotNull(term2);
    }
    
    @Test
    public void testJSTermParseList() {
        // Typical use case
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(A . (B . C))"));
        JSTerm term = new JSTerm(tokenizer);
        JSTerm parsedList = term.parseList(tokenizer);
        assertNotNull(parsedList);
        
        // Edge case
        StreamTokenizer tokenizer2 = new StreamTokenizer(new StringReader("(nil)"));
        JSTerm term2 = new JSTerm(tokenizer2);
        JSTerm parsedList2 = term2.parseList(tokenizer2);
        assertNotNull(parsedList2);
    }
    
    @Test
    public void testJSTermPrintList() {
        // Typical use case
        JSTerm term = new JSTerm();
        term.addElement("A");
        term.addElement(new JSTerm());
        term.addElement("B");
        term.addElement(new JSTerm());
        term.addElement("C");
        term.makeFunction();
        term.printList(term);
        
        // Edge case
        JSTerm term2 = new JSTerm();
        term2.addElement("nil");
        term2.makeConstant();
        term2.printList(term2);
    }
    
    @Test
    public void testJSTermEquals() {
        // Typical use case
        JSTerm term1 = new JSTerm();
        term1.addElement("A");
        term1.addElement("B");
        term1.makeFunction();
        
        JSTerm term2 = new JSTerm();
        term2.addElement("A");
        term2.addElement("B");
        term2.makeFunction();
        
        assertTrue(term1.equals(term2));
        
        // Edge case
        JSTerm term3 = new JSTerm();
        term3.addElement("A");
        term3.makeConstant();
        assertFalse(term1.equals(term3));
    }
    
    @Test
    public void testJSTermApplySubstitutionT() {
        // Typical use case
        JSTerm term = new JSTerm();
        term.addElement("A");
        term.addElement("B");
        term.makeFunction();
        
        JSSubstitution alpha = new JSSubstitution();
        
        JSTerm result = term.applySubstitutionT(alpha);
        assertNotNull(result);
        
        // Edge case
        JSTerm term2 = new JSTerm();
        term2.addElement("A");
        term2.makeConstant();
        
        JSTerm result2 = term2.applySubstitutionT(alpha);
        assertNotNull(result2);
    }
    
    @Test
    public void testJSTermMatches() {
        // Typical use case
        JSTerm term1 = new JSTerm();
        term1.addElement("A");
        term1.addElement("B");
        term1.makeFunction();
        
        JSTerm term2 = new JSTerm();
        term2.addElement("A");
        term2.addElement("B");
        term2.makeFunction();
        
        JSSubstitution result = term1.matches(term2);
        assertNotNull(result);
        
        // Edge case
        JSTerm term3 = new JSTerm();
        term3.addElement("A");
        term3.makeConstant();
        
        JSSubstitution result2 = term1.matches(term3);
        assertNotNull(result2);
    }
    
}