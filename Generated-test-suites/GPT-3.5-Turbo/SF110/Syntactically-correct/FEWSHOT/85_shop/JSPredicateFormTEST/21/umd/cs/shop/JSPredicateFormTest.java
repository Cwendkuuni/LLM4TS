package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StreamTokenizer;
import java.io.StringReader;

import org.junit.Test;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateFormInit() {
        JSPredicateForm jspf = new JSPredicateForm();
        assertTrue(jspf.isEmpty());

        jspf = new JSPredicateForm("()");
        assertTrue(jspf.isEmpty());

        jspf = new JSPredicateForm("(not ()");
        assertFalse(jspf.isEmpty());
        assertEquals(jspf.elementAt(0), "not");
    }

    @Test
    public void testPrint() {
        JSPredicateForm jspf = new JSPredicateForm();
        jspf.print();
        assertTrue(true);
    }

    @Test
    public void testToStr() {
        JSPredicateForm jspf = new JSPredicateForm();
        StringBuilder expected = new StringBuilder();
        expected.append("()");
        assertEquals(expected.toString(), jspf.toStr().toString());

        jspf = new JSPredicateForm("(not ())");
        expected = new StringBuilder();
        expected.append("(not ())");
        assertEquals(expected.toString(), jspf.toStr().toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm jspf = new JSPredicateForm();
        JSPredicateForm cloned = jspf.clonePF();
        assertEquals(jspf.size(), cloned.size());
        assertTrue(jspf != cloned);

        jspf = new JSPredicateForm("(not ())");
        cloned = jspf.clonePF();
        assertEquals(jspf.size(), cloned.size());
        assertEquals(jspf.elementAt(0), cloned.elementAt(0));
        assertTrue(jspf.elementAt(0) != cloned.elementAt(0));
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm jspf = new JSPredicateForm();
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm applied = jspf.applySubstitutionPF(alpha);
        assertEquals(jspf.size(), applied.size());
        assertTrue(jspf != applied);

        jspf = new JSPredicateForm("(not ())");
        applied = jspf.applySubstitutionPF(alpha);
        assertEquals(jspf.size(), applied.size());
        assertEquals(jspf.elementAt(0), applied.elementAt(0));
        assertTrue(jspf.elementAt(0) != applied.elementAt(0));

        jspf = new JSPredicateForm("(not call())");
        JSSubstitution newAlpha = new JSSubstitution();
        JSPredicateForm applied2 = jspf.applySubstitutionPF(newAlpha);
        assertEquals(jspf.size(), applied2.size());
        assertEquals(jspf.elementAt(0), applied2.elementAt(0));
        assertTrue(jspf.elementAt(0) != applied2.elementAt(0));
    }

    @Test
    public void testMatches() {
        JSPredicateForm jspf = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        JSSubstitution result = jspf.matches(t);
        assertTrue(result.fail());

        jspf = new JSPredicateForm("(not ())");
        t = new JSPredicateForm("(not ())");
        result = jspf.matches(t);
        assertFalse(result.fail());

        jspf = new JSPredicateForm("(not call())");
        t = new JSPredicateForm("(not call())");
        result = jspf.matches(t);
        assertFalse(result.fail());

        jspf = new JSPredicateForm("(not call())");
        t = new JSPredicateForm("(not ())");
        result = jspf.matches(t);
        assertTrue(result.fail());
    }

    @Test
    public void testEquals() {
        JSPredicateForm jspf = new JSPredicateForm();
        JSPredicateForm t = new JSPredicateForm();
        assertFalse(jspf.equals(t));
        assertFalse(jspf.equals(null));

        jspf = new JSPredicateForm("(not ())");
        t = new JSPredicateForm("(not ())");
        assertTrue(jspf.equals(t));

        jspf = new JSPredicateForm("(not call())");
        t = new JSPredicateForm("(not call())");
        assertTrue(jspf.equals(t));

        jspf = new JSPredicateForm("(not call())");
        t = new JSPredicateForm("(not ())");
        assertFalse(jspf.equals(t));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm jspf = new JSPredicateForm();
        JSPredicateForm standard = jspf.standarizerPredicateForm();
        assertEquals(jspf.size(), standard.size());
        assertTrue(jspf != standard);

        jspf = new JSPredicateForm("(not ())");
        standard = jspf.standarizerPredicateForm();
        assertEquals(jspf.size(), standard.size());
        assertEquals(jspf.elementAt(0), standard.elementAt(0));
        assertTrue(jspf.elementAt(0) != standard.elementAt(0));

        jspf = new JSPredicateForm("(not call())");
        standard = jspf.standarizerPredicateForm();
        assertEquals(jspf.size(), standard.size());
        assertEquals(jspf.elementAt(0), standard.elementAt(0));
        assertTrue(jspf.elementAt(0) != standard.elementAt(0));
    }
}