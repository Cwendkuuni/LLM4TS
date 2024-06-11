import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {
    
    @Test
    public void testJSTermConstructor() {
        // Typical use case
        JSTerm jt = new JSTerm();
        assertNotNull(jt);

        // Edge case: parsing term with null tokenizer
        try {
            StreamTokenizer tokenizer = null;
            JSTerm jt2 = new JSTerm(tokenizer);
            fail("Expected JSParserError to be thrown");
        } catch (JSParserError e) {
            // Expected
        }
    }

    @Test
    public void testParseList() {
        // Typical use case
        StreamTokenizer tokenizer = new StreamTokenizer();
        JSTerm jt = new JSTerm(tokenizer);
        assertNotNull(jt.parseList(tokenizer));

        // Edge case: empty list parsing
        JSTerm emptyList = new JSTerm();
        assertEquals("nil", emptyList.parseList(tokenizer).elementAt(0));
    }

    @Test
    public void testPrint() {
        // Typical use case
        JSTerm jt = new JSTerm();
        jt.addElement("test");
        jt.addElement("example");
        assertEquals("(test example )", jt.print());

        // Edge case: printing empty term
        JSTerm emptyTerm = new JSTerm();
        assertEquals("", emptyTerm.print());
    }

    @Test
    public void testCloneT() {
        // Typical use case
        JSTerm jt = new JSTerm();
        jt.addElement("test");
        JSTerm clone = jt.cloneT();
        assertEquals(jt.elementAt(0), clone.elementAt(0));

        // Edge case: cloning an empty term
        JSTerm emptyTerm = new JSTerm();
        JSTerm cloneEmpty = emptyTerm.cloneT();
        assertEquals(0, cloneEmpty.size());
    }

    @Test
    public void testApplySubstitutionT() {
        // Typical use case
        JSTerm jt = new JSTerm();
        jt.addElement("example");
        JSSubstitution alpha = new JSSubstitution();
        JSTerm result = jt.applySubstitutionT(alpha);
        assertEquals(jt.elementAt(0), result.elementAt(0));

        // Edge case: applying substitution on an empty term
        JSTerm emptyTerm = new JSTerm();
        JSSubstitution emptySub = new JSSubstitution();
        JSTerm resultEmpty = emptyTerm.applySubstitutionT(emptySub);
        assertEquals(0, resultEmpty.size());
    }

    @Test
    public void testMatches() {
        // Typical use case
        JSTerm jt1 = new JSTerm();
        jt1.addElement("test");

        JSTerm jt2 = new JSTerm();
        jt2.addElement("test");

        JSSubstitution result = jt1.matches(jt2);
        assertFalse(result.fail());

        // Edge case: matching with different terms
        JSTerm diffTerm = new JSTerm();
        diffTerm.addElement("different");
        JSSubstitution resultDiff = jt1.matches(diffTerm);
        assertTrue(resultDiff.fail());
    }

    @Test
    public void testEquals() {
        // Typical use case
        JSTerm jt1 = new JSTerm();
        jt1.addElement("test");

        JSTerm jt2 = new JSTerm();
        jt2.addElement("test");

        assertTrue(jt1.equals(jt2));

        // Edge case: comparing with a different term
        JSTerm diffTerm = new JSTerm();
        diffTerm.addElement("different");
        assertFalse(jt1.equals(diffTerm));
    }

    @Test
    public void testStandardizerTerm() {
        // Typical use case
        JSTerm jt = new JSTerm();
        jt.addElement("test");
        JSTerm stdTerm = jt.standardizerTerm();
        assertEquals(jt.elementAt(0) + "0", stdTerm.elementAt(0));

        // Edge case: standardizing an empty term
        JSTerm emptyTerm = new JSTerm();
        JSTerm stdEmpty = emptyTerm.standardizerTerm();
        assertEquals(0, stdEmpty.size());
    }

    @Test
    public void testCall() {
        // Typical use case
        JSTerm jt = new JSTerm();
        jt.addElement("+");
        jt.addElement("2");
        jt.addElement("3");
        JSTerm result = jt.call();
        assertEquals("5", result.elementAt(0));

        // Edge case: calling on an empty term
        JSTerm emptyTerm = new JSTerm();
        JSTerm emptyResult = emptyTerm.call();
        assertEquals(0, emptyResult.size());
    }
}