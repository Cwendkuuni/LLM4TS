package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.StringReader;
import java.util.Vector;

public class JSPredicateFormTest {
    @Test
    public void testJSPredicateFormInit() {
        String text = "(predicate)";
        JSPredicateForm form = new JSPredicateForm(text);
        assertEquals(form.size(), 1);
        assertEquals(form.elementAt(0), "predicate");

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(predicate)"));
        JSPredicateForm form2 = new JSPredicateForm(tokenizer);
        assertEquals(form2.size(), 1);
        assertEquals(form2.elementAt(0), "predicate");

        StreamTokenizer tokenizerNull = null;
        try {
            JSPredicateForm form3 = new JSPredicateForm(tokenizerNull);
            assertFalse(true);
        }
        catch (JSParserError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        form.print();
        assertTrue(true);
    }

    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        StringBuffer str = new StringBuffer("(not predicate)");
        assertEquals(form.toStr(), str);
    }

    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        JSPredicateForm clone = form.clonePF();
        assertEquals(form.size(), clone.size());
        for (int i = 0; i < form.size(); i++) {
            assertEquals(form.elementAt(i), clone.elementAt(i));
        }
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        JSSubstitution alpha = new JSSubstitution();
        JSPredicateForm applySubstitution = form.applySubstitutionPF(alpha);
        assertEquals(form.size(), applySubstitution.size());
        for (int i = 0; i < form.size(); i++) {
            assertEquals(form.elementAt(i), applySubstitution.elementAt(i));
        }
    }

    @Test
    public void testMatches() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("not");
        t.addElement("predicate");
        JSSubstitution matches = form.matches(t);
        assertFalse(matches.fail());

        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate");
        JSSubstitution matches2 = form.matches(form2);
        assertTrue(matches2.fail());
    }

    @Test
    public void testEquals() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        JSPredicateForm t = new JSPredicateForm();
        t.addElement("not");
        t.addElement("predicate");
        assertTrue(form.equals(t));

        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("predicate");
        assertFalse(form.equals(form2));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("not");
        form.addElement("predicate");
        JSPredicateForm standarizer = form.standarizerPredicateForm();
        assertEquals(form.size(), standarizer.size());
        for (int i = 0; i < form.size(); i++) {
            assertEquals(form.elementAt(i), standarizer.elementAt(i));
        }
    }
}