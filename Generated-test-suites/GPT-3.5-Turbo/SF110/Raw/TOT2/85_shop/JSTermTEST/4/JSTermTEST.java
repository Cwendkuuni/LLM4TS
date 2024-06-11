import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;

public class JSTermTest {

    @Test
    public void testJSTermConstructor1() {
        JSTerm jsTerm = new JSTerm();
        assertNotNull(jsTerm);
        assertFalse(jsTerm.isVariable());
        assertFalse(jsTerm.isConstant());
        assertFalse(jsTerm.isFunction());
        assertFalse(jsTerm.isEval());
    }

    @Test
    public void testJSTermConstructor2() {
        StringReader reader = new StringReader("(list 1 2 3)");
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        JSTerm jsTerm = new JSTerm(tokenizer);
        assertNotNull(jsTerm);
        assertTrue(jsTerm.isFunction());
    }

    @Test
    public void testJSTermParseList() {
        StringReader reader = new StringReader("(list 1 2 3)");
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        JSTerm jsTerm = new JSTerm();
        JSTerm list = jsTerm.parseList(tokenizer);
        assertNotNull(list);
        assertTrue(list.isFunction());
    }

    @Test
    public void testJSTermPrint() {
        JSTerm jsTerm = new JSTerm();
        jsTerm.addElement("test");
        jsTerm.makeConstant();
        jsTerm.print();
        // Check output in console
    }

    @Test
    public void testJSTermCloneT() {
        JSTerm jsTerm = new JSTerm();
        jsTerm.addElement("test");
        JSTerm clonedTerm = jsTerm.cloneT();
        assertNotNull(clonedTerm);
        assertEquals(jsTerm.elementAt(0), clonedTerm.elementAt(0));
    }

    @Test
    public void testJSTermApplySubstitutionT() {
        JSTerm jsTerm = new JSTerm();
        jsTerm.addElement("test");
        JSSubstitution alpha = new JSSubstitution();
        JSTerm substitutedTerm = jsTerm.applySubstitutionT(alpha);
        assertNotNull(substitutedTerm);
        assertEquals(jsTerm, substitutedTerm);
    }

    @Test
    public void testJSTermMatches() {
        JSTerm jsTerm1 = new JSTerm();
        JSTerm jsTerm2 = new JSTerm();
        JSSubstitution alpha = jsTerm1.matches(jsTerm2);
        assertNotNull(alpha);
        assertTrue(alpha.isEmpty());
    }

    @Test
    public void testJSTermEquals() {
        JSTerm jsTerm1 = new JSTerm();
        jsTerm1.addElement("test");
        JSTerm jsTerm2 = new JSTerm();
        jsTerm2.addElement("test");
        assertTrue(jsTerm1.equals(jsTerm2));
    }

}