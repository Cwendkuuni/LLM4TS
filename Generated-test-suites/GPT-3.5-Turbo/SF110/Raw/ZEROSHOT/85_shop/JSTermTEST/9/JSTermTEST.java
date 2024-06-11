import org.junit.Test;
import umd.cs.shop.JSTerm;
import java.io.*;

public class JSTermTest {
    
    @Test
    public void testJSTermConstructor() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("Term"));
        JSTerm term = new JSTerm(tokenizer);
    }

    @Test
    public void testJSTermParseList() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("List"));
        JSTerm term = new JSTerm();
        term.parseList(tokenizer);
    }

    @Test
    public void testJSTermPrint() {
        JSTerm term = new JSTerm();
        term.print();
    }

    @Test
    public void testJSTermCloneT() {
        JSTerm term = new JSTerm();
        term.cloneT();
    }

    @Test
    public void testJSTermApplySubstitutionT() {
        JSSubstitution alpha = new JSSubstitution();
        JSTerm term = new JSTerm();
        term.applySubstitutionT(alpha);
    }

    @Test
    public void testJSTermMatches() {
        JSTerm t = new JSTerm();
        JSTerm term = new JSTerm();
        term.matches(t);
    }

    @Test
    public void testJSTermEquals() {
        JSTerm t = new JSTerm();
        JSTerm term = new JSTerm();
        term.equals(t);
    }

    @Test
    public void testJSTermMakeFunction() {
        JSTerm term = new JSTerm();
        term.makeFunction();
    }

    @Test
    public void testJSTermMakeVariable() {
        JSTerm term = new JSTerm();
        term.makeVariable();
    }

    @Test
    public void testJSTermMakeConstant() {
        JSTerm term = new JSTerm();
        term.makeConstant();
    }

    @Test
    public void testJSTermMakeEval() {
        JSTerm term = new JSTerm();
        term.makeEval(true);
    }

    @Test
    public void testJSTermToStr() {
        JSTerm term = new JSTerm();
        term.toStr();
    }

    @Test
    public void testJSTermIsGround() {
        JSTerm term = new JSTerm();
        term.isGround();
    }

    @Test
    public void testJSTermStandardizerTerm() {
        JSTerm term = new JSTerm();
        term.standardizerTerm();
    }

    @Test
    public void testJSTermCall() {
        JSTerm term = new JSTerm();
        term.call();
    }
}