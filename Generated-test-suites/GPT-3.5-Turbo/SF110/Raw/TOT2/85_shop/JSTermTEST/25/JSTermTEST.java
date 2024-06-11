import static org.junit.Assert.*;
import org.junit.Test;

public class JSTermTest {

    @Test
    public void testJSTermConstructor() {
        JSTerm jst = new JSTerm();
        assertNotNull(jst);
    }

    @Test
    public void testJSTermParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(a b c)"));
        JSTerm jst = new JSTerm(tokenizer);
        assertEquals("(a b c)", jst.toString());
    }

    @Test
    public void testJSTermPrint() {
        JSTerm jst = new JSTerm();
        jst.addElement("test");
        assertEquals("test ", jst.toString());
    }

    @Test
    public void testJSTermCloneT() {
        JSTerm jst = new JSTerm();
        jst.addElement("test");
        JSTerm clone = jst.cloneT();
        assertEquals("test ", clone.toString());
    }

    @Test
    public void testJSTermApplySubstitutionT() {
        JSTerm jst = new JSTerm();
        jst.addElement("x");
        JSSubstitution alpha = new JSSubstitution();
        alpha.addElement(new JSPairVarTerm(new JSTerm("x"), new JSTerm("y")));
        JSTerm newJst = jst.applySubstitutionT(alpha);
        assertEquals("y ", newJst.toString());
    }

    @Test
    public void testJSTermMatches() {
        JSTerm jst1 = new JSTerm();
        jst1.addElement("test");
        JSTerm jst2 = new JSTerm();
        jst2.addElement("test");
        JSSubstitution result = jst1.matches(jst2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testJSTermEquals() {
        JSTerm jst1 = new JSTerm();
        jst1.addElement("test");
        JSTerm jst2 = new JSTerm();
        jst2.addElement("test");
        assertTrue(jst1.equals(jst2));
    }

}