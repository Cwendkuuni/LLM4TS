package org.jsoup.select.test;

import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CombiningEvaluator.
 */
public class CombiningEvaluatorTest {

    @Test
    public void testConstructor() {
        CombiningEvaluator ce = new CombiningEvaluator();
        assertFalse(ce.isEmpty());
    }

    @Test
    public void testAndConstruction() {
        Evaluator e1 = new ElementStartsWithTagNameEvaluator("div");
        Evaluator e2 = new ElementHasClassEvaluator("class1");
        And and = new And(Arrays.asList(e1, e2));

        Element element = new Element("div");
        element.classes().add("class1");

        assertTrue(and.matches(new Element("html"), element));
    }

    @Test
    public void testAndMatches() {
        Evaluator e1 = new ElementStartsWithTagNameEvaluator("div");
        Evaluator e2 = new ElementHasClassEvaluator("class1");
        And and = new And(Arrays.asList(e1, e2));

        Element element = new Element("div");
        element.classes().add("class1");

        assertTrue(and.matches(new Element("html"), element));

        element = new Element("p");
        assertFalse(and.matches(new Element("html"), element));
    }

    @Test
    public void testOrConstruction() {
        Evaluator e1 = new ElementStartsWithTagNameEvaluator("div");
        Evaluator e2 = new ElementHasClassEvaluator("class1");
        Or or = new Or(Arrays.asList(e1, e2));

        Element element = new Element("div");
        element.classes().add("class1");

        assertTrue(or.matches(new Element("html"), element));

        element = new Element("p");
        assertFalse(or.matches(new Element("html"), element));

        evaluateOrWithMoreThanOneMatcher(or, element);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrEmptyEvaluatorList() {
        Or or = new Or(new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrNullEvaluator() {
        Or or = new Or(null);
    }

    private void evaluateOrWithMoreThanOneMatcher(Or or, Element element) {
        Evaluator e3 = new ElementHasTagNameEvaluator("span");
        or.add(e3);

        element = new Element("div");
        element.classes().add("class1");
        element.appendElement(new Element("span"));

        assertTrue(or.matches(new Element("html"), element));

        element = new Element("p");
        assertFalse(or.matches(new Element("html"), element));
    }

    @Test
    public void testToString() {
        Evaluator e1 = new ElementStartsWithTagNameEvaluator("div");
        Evaluator e2 = new ElementHasClassEvaluator("class1");
        And and = new And(Arrays.asList(e1, e2));

        String actual = and.toString();
        String expected = "div:has(.class1)";

        assertTrue(actual.equals(expected));

        Evaluator or = new Or(Arrays.asList(and, new ElementHasTextEvaluator("Test")));
        String actualOr = or.toString();

        String expectedOr = "div:has(.class1):hasText(Test)";

        assertTrue(actualOr.equals(expectedOr));
    }
}