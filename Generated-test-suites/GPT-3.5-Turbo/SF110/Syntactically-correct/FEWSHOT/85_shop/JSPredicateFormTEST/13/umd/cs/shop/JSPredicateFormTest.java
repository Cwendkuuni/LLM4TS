package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSPredicateFormTest {

    @Test
    public void testJSPredicateFormInit() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("not(store(true))"));
        JSPredicateForm form = new JSPredicateForm(tokenizer);
        assertTrue(form.elementAt(0).equals("not"));
        JSPredicateForm nestedForm = (JSPredicateForm)form.elementAt(1);
        assertTrue(nestedForm.elementAt(0).equals("store"));
        JSTerm nestedTerm = (JSTerm)nestedForm.elementAt(1);
        assertTrue(nestedTerm.toString().equals("true"));

        tokenizer = new StreamTokenizer(new StringReader("(true false)"));
        form = new JSPredicateForm(tokenizer);
        assertTrue(form.elementAt(0).equals("true"));
        JSTerm nestedTerm1 = (JSTerm)form.elementAt(1);
        assertTrue(nestedTerm1.toString().equals("false"));
    }

    @Test
    public void testPrint() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("store");
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form.addElement(term);
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        form.print();
        System.setOut(originalOut);
        assertEquals("Expecting (store apple ) in output", "(store apple ) ", outputStream.toString());
    }

    @Test
    public void testToStr() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("store");
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form.addElement(term);
        assertEquals("Expecting (store apple) in result", "(store apple)", form.toStr().toString());
    }

    @Test
    public void testClonePF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("store");
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form.addElement(term);
        JSPredicateForm clonedForm = form.clonePF();
        JSTerm clonedTerm = (JSTerm)clonedForm.elementAt(1);
        assertEquals("Expecting store in cloned form", "store", clonedForm.elementAt(0));
        assertEquals("Expecting apple in cloned term", "apple", clonedTerm.toString());
    }

    @Test
    public void testApplySubstitutionPF() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("store");
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form.addElement(term);

        JSSubstitution alpha = new JSSubstitution();
        alpha.assign(new JSVariable("apple"), new JSVariable("orange"));

        JSPredicateForm substitutedForm = form.applySubstitutionPF(alpha);
        JSTerm substitutedTerm = (JSTerm)substitutedForm.elementAt(1);
        assertEquals("Expecting store in substituted form", "store", substitutedForm.elementAt(0));
        assertEquals("Expecting orange in substituted term", "orange", substitutedTerm.toString());
    }

    @Test
    public void testMatches() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("store");
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form1.addElement(term1);

        JSSubstitution alpha = new JSSubstitution();
        alpha.assign(new JSVariable("apple"), new JSVariable("orange"));

        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("store");
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("orange")));
        form2.addElement(term2);

        JSSubstitution matches = form1.matches(form2);
        assertEquals("Expecting a successful match", false, matches.fail());

        JSPredicateForm form3 = new JSPredicateForm();
        form3.addElement("store");
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form3.addElement(term3);

        JSSubstitution noMatch = form1.matches(form3);
        assertEquals("Expecting a failed match", true, noMatch.fail());
    }

    @Test
    public void testEquals() {
        JSPredicateForm form1 = new JSPredicateForm();
        form1.addElement("store");
        JSTerm term1 = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form1.addElement(term1);

        JSPredicateForm form2 = new JSPredicateForm();
        form2.addElement("store");
        JSTerm term2 = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form2.addElement(term2);

        assertTrue("Expecting form1 and form2 to be equal", form1.equals(form2));

        JSPredicateForm form3 = new JSPredicateForm();
        form3.addElement("store");
        JSTerm term3 = new JSTerm(new StreamTokenizer(new StringReader("orange")));
        form3.addElement(term3);

        assertFalse("Expecting form1 and form3 to be not equal", form1.equals(form3));

        JSPredicateForm form4 = new JSPredicateForm();
        form4.addElement("place");
        JSTerm term4 = new JSTerm(new StreamTokenizer(new StringReader("apple")));
        form4.addElement(term4);

        assertFalse("Expecting form1 and form4 to be not equal", form1.equals(form4));
    }

    @Test
    public void testStandarizerPredicateForm() {
        JSPredicateForm form = new JSPredicateForm();
        form.addElement("store");
        JSTerm term = new JSTerm(new StreamTokenizer(new StringReader("#apple")));
        form.addElement(term);

        JSPredicateForm standarizedForm = form.standarizerPredicateForm();
        JSTerm standarizedTerm = (JSTerm)standarizedForm.elementAt(1);
        assertEquals("Expecting store in standarized form", "store", standarizedForm.elementAt(0));
        assertEquals("Expecting apple in standarized term", "#independent", standarizedTerm.toString());
    }

}