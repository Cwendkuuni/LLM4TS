package org.jsoup.select;

import org.jsoup.helper.ElementFilter;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("div");
        Evaluator evaluator2 = new ClassNameEvaluator("class1");
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2);

        Element root = Element.parse("<html><body><div class='class1'></div></body></html>");
        List<Element> matches = and.findAll(root, null);
        Assert.assertEquals(1, matches.size());
        Assert.assertTrue(matches.get(0) instanceof Element);

        root = Element.parse("<html><body><div></div><p class='class1'></p></body></html>");
        matches = and.findAll(root, null);
        Assert.assertEquals(1, matches.size());
        Assert.assertTrue(matches.get(0) instanceof Element);

        root = Element.parse("<html><body><div></div></body></html>");
        matches = and.findAll(root, null);
        Assert.assertEquals(0, matches.size());

        root = Element.parse("<html><body><div class='class2'></div></body></html>");
        matches = and.findAll(root, null);
        Assert.assertEquals(0, matches.size());
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("div");
        Evaluator evaluator2 = new ClassNameEvaluator("class1");
        Evaluator evaluator3 = new ClassNameEvaluator("class2");
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);

        Element root = Element.parse("<html><body><div class='class1'></div></body></html>");
        List<Element> matches = or.findAll(root, null);
        Assert.assertEquals(1, matches.size());
        Assert.assertTrue(matches.get(0) instanceof Element);

        root = Element.parse("<html><body><div class='class1'></div></body></html>");
        matches = or.findAll(root, null);
        Assert.assertEquals(1, matches.size());
        Assert.assertTrue(matches.get(0) instanceof Element);

        root = Element.parse("<html><body><div class='class2'></div></body></html>");
        matches = or.findAll(root, null);
        Assert.assertEquals(1, matches.size());
        Assert.assertTrue(matches.get(0) instanceof Element);

        root = Element.parse("<html><body><div></div></body></html>");
        matches = or.findAll(root, null);
        Assert.assertEquals(0, matches.size());
    }

    @Test
    public void testAddEvaluatorToOrEvaluator() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("div");
        CombiningEvaluator.Or or = new CombiningEvaluator.Or();
        or.add(evaluator1);

        Element root = Element.parse("<html><body><div class='class1'></div></body></html>");
        List<Element> matches = or.findAll(root, null);
        Assert.assertEquals(1, matches.size());
        Assert.assertTrue(matches.get(0) instanceof Element);

        root = Element.parse("<html><body><div></div></body></html>");
        matches = or.findAll(root, null);
        Assert.assertEquals(0, matches.size());

        CombiningEvaluator.And and = new CombiningEvaluator.And(new ElementNameEvaluator("html"), or);
        root = Element.parse("<html><body><div class='class1'></div></body></html>");
        matches = and.findAll(root, null);
        Assert.assertEquals(1, matches.size());
        Assert.assertTrue(matches.get(0) instanceof Element);

        root = Element.parse("<html><body><div></div></body></html>");
        matches = and.findAll(root, null);
        Assert.assertEquals(0, matches.size());
    }

    @Test
    public void testToString() {
        Evaluator evaluator1 = new ElementStartsWithTagNameEvaluator("div");
        Evaluator evaluator2 = new ClassNameEvaluator("class1");
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2);

        String expected = String.format("div and class:class1");
        Assert.assertEquals(expected, and.toString());

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2);
        expected = String.format(":or div :and class:class1");
        Assert.assertEquals(expected, or.toString());
    }
}