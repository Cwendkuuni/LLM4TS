
package umd.cs.shop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSTermTest {

    private JSTerm jsTerm;

    @Before
    public void setUp() {
        jsTerm = new JSTerm();
    }

    @Test
    public void testIsVariable() {
        jsTerm.makeVariable();
        assertTrue(jsTerm.isVariable());
    }

    @Test
    public void testIsConstant() {
        jsTerm.makeConstant();
        assertTrue(jsTerm.isConstant());
    }

    @Test
    public void testIsFunction() {
        jsTerm.makeFunction();
        assertTrue(jsTerm.isFunction());
    }

    @Test
    public void testIsEval() {
        jsTerm.makeEval(true);
        assertTrue(jsTerm.isEval());
    }

    @Test
    public void testMakeFunction() {
        jsTerm.makeFunction();
        assertTrue(jsTerm.isFunction());
    }

    @Test
    public void testMakeVariable() {
        jsTerm.makeVariable();
        assertTrue(jsTerm.isVariable());
    }

    @Test
    public void testMakeConstant() {
        jsTerm.makeConstant();
        assertTrue(jsTerm.isConstant());
    }

    @Test
    public void testMakeEval() {
        jsTerm.makeEval(true);
        assertTrue(jsTerm.isEval());
    }

    @Test
    public void testCloneT() {
        JSTerm clone = jsTerm.cloneT();
        assertEquals(jsTerm, clone);
    }

    @Test
    public void testApplySubstitutionT() {
        JSSubstitution alpha = new JSSubstitution();
        JSTerm result = jsTerm.applySubstitutionT(alpha);
        assertEquals(jsTerm, result);
    }

    @Test
    public void testMatches() {
        JSTerm t = new JSTerm();
        JSSubstitution result = jsTerm.matches(t);
        assertFalse(result.fail());
    }

    @Test
    public void testMatchesWithAlpha() {
        JSTerm t = new JSTerm();
        JSSubstitution alpha = new JSSubstitution();
        JSSubstitution result = jsTerm.matches(t, alpha);
        assertFalse(result.fail());
    }

    @Test
    public void testEquals() {
        JSTerm t = new JSTerm();
        assertTrue(jsTerm.equals(t));
    }

    @Test
    public void testIsGround() {
        assertFalse(jsTerm.isGround());
    }

    @Test
    public void testStandardizerTerm() {
        JSTerm standard = jsTerm.standardizerTerm();
        assertEquals(jsTerm, standard);
    }

    @Test
    public void testCall() {
        JSTerm result = jsTerm.call();
        assertEquals(jsTerm, result);
    }
}
