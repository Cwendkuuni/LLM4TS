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
        Evaluator evaluator1 = new And(Arrays.asList(
                new ElementStarts("p"),
                new ElementClass("red")
        ));
        Evaluator evaluator2 = new And(Arrays.asList(
                new ElementStarts("p"),
                new ElementAttribute("id", "test")
        ));

        Elements elements = new Elements(Elements.parser().parse("<html><body><p id='test' class='red'></p></body></html>"));

        List<Element> matchedElements = new ArrayList<>();
        for (Element element : elements) {
            if (evaluator1.matches(null, element)) {
                matchedElements.add(element);
            }
        }

        Assert.assertEquals(1, matchedElements.size());

        List<Element> unmatchedElements = new ArrayList<>();
        for (Element element : elements) {
            if (!evaluator2.matches(null, element)) {
                unmatchedElements.add(element);
            }
        }

        Assert.assertEquals(1, unmatchedElements.size());
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluator1 = new ElementStarts("p");
        Evaluator evaluator2 = new ElementClass("red");
        Evaluator andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

        Or orEvaluator = new Or(Arrays.asList(evaluator1, andEvaluator));

        Elements elements = new Elements(Elements.parser().parse("<html><body><p class='red'></p><p class='blue'></p></body></html>"));

        List<Element> matchedElements = new ArrayList<>();
        for (Element element : elements) {
            if (orEvaluator.matches(null, element)) {
                matchedElements.add(element);
            }
        }

        Assert.assertEquals(2, matchedElements.size());
    }

    @Test
    public void testAddEvaluatorToOrEvaluator() {
        Or orEvaluator = new Or();

        Evaluator evaluator1 = new ElementStarts("p");
        Evaluator evaluator2 = new ElementClass("red");
        Evaluator evaluator3 = new ElementAttribute("id", "test");

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);
        orEvaluator.add(evaluator3);

        Elements elements = new Elements(Elements.parser().parse("<html><body><p id='test' class='red'></p></body></html>"));

        List<Element> matchedElements = new ArrayList<>();
        for (Element element : elements) {
            if (orEvaluator.matches(null, element)) {
                matchedElements.add(element);
            }
        }

        Assert.assertEquals(1, matchedElements.size());
    }
}