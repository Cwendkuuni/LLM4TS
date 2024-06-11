package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFinder;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndConstructorWithCollection() {
        List<Evaluator> evaluators = new ArrayList<>();
        Evaluator e1 = ElementFinder.safeSelector("div");
        Evaluator e2 = ElementFinder.safeSelector("p");
        evaluators.add(e1);
        evaluators.add(e2);

        And and = new And(evaluators);

        Element root = ElementFinder.parse("<html><body><div><p>Test</p></div></body></html>");
        Element node = root.getElementsByTag("div").get(0);

        assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndConstructorWithEvaluators() {
        And and = new And(ElementFinder.safeSelector("div"), ElementFinder.safeSelector("p"));

        Element root = ElementFinder.parse("<html><body><div><p>Test</p></div></body></html>");
        Element node = root.getElementsByTag("div").get(0);

        assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndMatchesFalse() {
        List<Evaluator> evaluators = new ArrayList<>();
        Evaluator e1 = ElementFinder.safeSelector("div");
        Evaluator e2 = ElementFinder.safeSelector("p");
        evaluators.add(e1);
        evaluators.add(e2);

        And and = new And(evaluators);

        Element root = ElementFinder.parse("<html><body><p>Test</p></body></html>");
        Element node = root.getElementsByTag("div").get(0);

        assertFalse(and.matches(root, node));
    }

    @Test
    public void testAndToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        Evaluator e1 = ElementFinder.safeSelector("div");
        Evaluator e2 = ElementFinder.safeSelector("p");
        evaluators.add(e1);
        evaluators.add(e2);

        And and = new And(evaluators);

        String expected = StringUtil.join(evaluators, " ");
        String actual = and.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testOrConstructorWithCollection() {
        List<Evaluator> evaluators = new ArrayList<>();
        Evaluator e1 = ElementFinder.safeSelector("div");
        evaluators.add(e1);
        evaluators.add(ElementFinder.safeSelector("p"));

        Or or = new Or(evaluators);

        Element root = ElementFinder.parse("<html><body><div><p>Test</p></div></body></html>");
        Element node = root.getElementsByTag("div").get(0);

        assertFalse(or.matches(root, node));

        or.add(ElementFinder.safeSelector("body"));

        assertTrue(or.matches(root, node));

        assertTrue(or.matches(root, root.getElementsByTag("body").get(0)));

        node = root.getElementsByTag("p").get(0);

        assertFalse(or.matches(root, node));
    }

    @Test
    public void testOrConstructorWithSingleEvaluator() {
        Or or = new Or(ElementFinder.safeSelector("div"));

        Element root = ElementFinder.parse("<html><body><div><p>Test</p></div></body></html>");
        Element node = root.getElementsByTag("div").get(0);

        assertTrue(or.matches(root, node));

        node = root.getElementsByTag("p").get(0);
        assertFalse(or.matches(root, node));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrWithEmptyList() {
        new Or(new ArrayList<>());
    }

    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        Evaluator e1 = ElementFinder.safeSelector("div");
        evaluators.add(e1);
        evaluators.add(ElementFinder.safeSelector("p"));

        Or or = new Or(evaluators);

        String expected = ":or" + StringUtil.join(evaluators, "%s");
        String actual = or.toString();

        assertEquals(expected, actual);
    }
}