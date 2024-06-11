package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator evaluatorA = new And(Arrays.asList(new ElementStarts("p"), new ElementHasClass("title")));
        List<Element> elements = Elements.parseHtml("<html><body><p class=\"title\">Test</p></body></html>").select(evaluatorA.toString());
        Assert.assertEquals(1, elements.size());

        elements = Elements.parseHtml("<html><body><p>Test</p></body></html>").select(evaluatorA.toString());
        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluatorA = new ElementStarts("p");
        Evaluator evaluatorB = new ElementHasClass("title");
        Evaluator andEvaluator = new And(Arrays.asList(evaluatorA, evaluatorB));

        Evaluator orEvaluator = new Or(Arrays.asList(evaluatorA, evaluatorB));
        List<Element> elements = Elements.parseHtml("<html><body><p class=\"title\">Test</p></body></html>").select(orEvaluator.toString());
        Assert.assertEquals(1, elements.size());

        elements = Elements.parseHtml("<html><body><p>Test</p></body></html>").select(orEvaluator.toString());
        Assert.assertEquals(1, elements.size()); // matches evaluator A

        elements = Elements.parseHtml("<html><body><p class=\"title\">Test</p></body></html>").select(orEvaluator.toString());
        Assert.assertEquals(1, elements.size()); // matches evaluator B

        elements = Elements.parseHtml("<html><body><p>Test</p></body></html>").select(orEvaluator.toString());
        Assert.assertEquals(1, elements.size()); // matches evaluator A
    }

    @Test
    public void testAddEvaluatorToOr() {
        Evaluator evaluatorA = new ElementStarts("p");
        Evaluator evaluatorB = new ElementHasClass("title");
        Evaluator orEvaluator = new Or();
        orEvaluator.add(evaluatorA);
        orEvaluator.add(evaluatorB);

        List<Element> elements = Elements.parseHtml("<html><body><p class=\"title\">Test</p></body></html>").select(orEvaluator.toString());
        Assert.assertEquals(1, elements.size());

        elements = Elements.parseHtml("<html><body><p>Test</p></body></html>").select(orEvaluator.toString());
        Assert.assertEquals(1, elements.size()); // matches evaluator A

        elements = Elements.parseHtml("<html><body><p class=\"title\">Test</p></body></html>").select(orEvaluator.toString());
        Assert.assertEquals(1, elements.size()); // matches evaluator B
    }
}