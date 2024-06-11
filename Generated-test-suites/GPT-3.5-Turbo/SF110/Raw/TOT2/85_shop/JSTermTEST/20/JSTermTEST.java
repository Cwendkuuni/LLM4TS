import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermConstructor() {
        JSTerm term = new JSTerm();
        assertNotNull(term);
    }

    @Test
    public void testParseList() {
        JSTerm term = new JSTerm();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(1 2 3)"));
        
        JSTerm result = term.parseList(tokenizer);
        assertEquals("(1 2 3)", result.toString());
    }

    @Test
    public void testPrint() {
        JSTerm term = new JSTerm();
        term.addElement("test");
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        term.print();
        assertEquals("test ", outContent.toString());
    }

    @Test
    public void testCloneT() {
        JSTerm term = new JSTerm();
        term.addElement("test");
        
        JSTerm clone = term.cloneT();
        assertNotNull(clone);
        assertEquals(term.toString(), clone.toString());
    }

    @Test
    public void testApplySubstitutionT() {
        JSTerm term = new JSTerm();
        term.addElement("test");
        
        JSSubstitution alpha = new JSSubstitution();
        
        JSTerm result = term.applySubstitutionT(alpha);
        assertNotNull(result);
    }

    @Test
    public void testMatches() {
        JSTerm term1 = new JSTerm();
        term1.addElement("test");
        
        JSTerm term2 = new JSTerm();
        term2.addElement("test");
        
        JSSubstitution result = term1.matches(term2);
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        JSTerm term1 = new JSTerm();
        term1.addElement("test");
        
        JSTerm term2 = new JSTerm();
        term2.addElement("test");
        
        assertTrue(term1.equals(term2));
    }

    // Add more test cases for other methods as needed

}