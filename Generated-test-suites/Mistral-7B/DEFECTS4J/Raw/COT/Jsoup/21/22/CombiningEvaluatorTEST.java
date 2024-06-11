package org.jsoup.select.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.Elements;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMethod() {
        Element root = Elements.parse("<html><body><div id='a'></div><div id='b'></div></body></html>").first();
        Evaluator evaluator1 = new And(Arrays.asList(
                new ElementStartsWithTagNameEvaluator("div"),
                new AttributeEqualsValueEvaluator("id", "a")
        ));
        Evaluator evaluator2 = new And(
                new ElementStartsWithTagNameEvaluator("div"),
                new AttributeEqualsValueEvaluator("id", "b")
        );
        And and = new And(evaluator1, evaluator2);
        assertTrue(and.matches(root, root));
        assertFalse(and.matches(root, Elements.parse("#c").first()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAndWithNullEvaluator() {
        new And(null);
    }

    @Test
    public void testAndWithEmptyList() {
        And and = new And();
        assertFalse(and.matches(null, null));
        assertFalse(and.matches(Elements.parse("<html><body></body></html>").first(), null));
        assertFalse(and.matches(null, Elements.parse("<html><body></body></html>").first()));
    }

    @Test
    public void testOrMethod() {
        Element root = Elements.parse("<html><body><div id='a'></div><div id='b'></div></body></html>").first();
        Evaluator evaluator1 = new And(
                new ElementStartsWithTagNameEvaluator("div"),
                new AttributeEqualsValueEvaluator("id", "a")
        );
        Evaluator evaluator2 = new And(
                new ElementStartsWithTagNameEvaluator("div"),
                new AttributeEqualsValueEvaluator("id", "b")
        );
        Or or = new Or(Arrays.asList(evaluator1, evaluator2));
        assertTrue(or.matches(root, root));
        assertTrue(or.matches(root, Elements.parse("#a").first()));
        assertTrue(or.matches(root, Elements.parse("#b").first()));
        assertFalse(or.matches(root, Elements.parse("#c").first()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrWithNullEvaluator() {
        new Or(null);
    }

    @Test
    public void testOrWithEmptyList() {
        Or or = new Or();
        assertFalse(or.matches(null, null));
        assertFalse(or.matches(Elements.parse("<html><body></body></html>").first(), null));
        assertFalse(or.matches(null, Elements.parse("<html><body></body></html>").first()));
    }

    @Test
    public void testAddMethodToOr() {
        Element root = Elements.parse("<html><body><div id='a'></div><div id='b'></div><div id='c'></div></body></html>").first();
        Or or = new Or();
        or.add(new And(
                new ElementStartsWithTagNameEvaluator("div"),
                new AttributeEqualsValueEvaluator("id", "a")
        ));
        or.add(new And(
                new ElementStartsWithTagNameEvaluator("div"),
                new AttributeEqualsValueEvaluator("id", "b")
        ));
        or.add(new And(
                new ElementStartsWithTagNameEvaluator("div"),
                new AttributeEqualsValueEvaluator("id", "c")
        ));
        assertTrue(or.matches(root, root));
        assertTrue(or.matches(root, Elements.parse("#a").first()));
        assertTrue(or.matches(root, Elements.parse("#b").first()));
        assertTrue(or.matches(root, Elements.parse("#c").first()));
    }

    @Test
    public void testCombiningEvaluatorConstructor() {
        List<Evaluator> evaluators = new ArrayList<>();
        CombiningEvaluator ce = new CombiningEvaluator(evaluators);
        assertNotNull(ce);
        assertTrue(ce instanceof CombiningEvaluator);
        assertTrue(ce.evaluators.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testCombiningEvaluatorWithNullCollection() {
        new CombiningEvaluator(null);
    }
}