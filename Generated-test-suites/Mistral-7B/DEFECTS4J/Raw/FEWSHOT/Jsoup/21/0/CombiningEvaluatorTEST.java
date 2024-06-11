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
        Evaluator evaluator1 = new And(Arrays.asList(new ElementStarts("a"), new ElementHasClass("class1")));
        Element element = new Element("a");
        element.getClassList().add("class1");

        Assert.assertTrue(evaluator1.matches(null, element));

        Element element2 = new Element("b");
        Assert.assertFalse(evaluator1.matches(null, element2));

        Element element3 = new Element("a");
        element3.getClassList().clear();
        Assert.assertFalse(evaluator1.matches(null, element3));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator evaluator1 = new ElementStarts("a");
        Evaluator evaluator2 = new ElementStarts("b");
        Evaluator andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

        Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2));

        Element aElement = new Element("a");
        Element bElement = new Element("b");
        Element cElement = new Element("c");

        List<Element> elements = new ArrayList<>();
        elements.add(aElement);
        elements.add(bElement);
        elements.add(cElement);

        for (Element element : elements) {
            Assert.assertTrue(andEvaluator.matches(null, element));
            Assert.assertTrue(orEvaluator.matches(null, element));
        }

        Element dElement = new Element("d");

        Assert.assertFalse(andEvaluator.matches(null, dElement));
        Assert.assertTrue(orEvaluator.matches(null, dElement));
    }

    @Test
    public void testAndEvaluatorWithMultipleElements() {
        Evaluator evaluator1 = new ElementStarts("a");
        Evaluator evaluator2 = new ElementHasText("text");
        Evaluator andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

        Element element = new Element("a").text("text");

        Assert.assertTrue(andEvaluator.matches(null, element));

        Element element2 = new Element("a").text("otherText");
        Assert.assertFalse(andEvaluator.matches(null, element2));

        Element element3 = new Element("b").text("text");
        Assert.assertFalse(andEvaluator.matches(null, element3));
    }

    @Test
    public void testOrEvaluatorWithMultipleEvaluators() {
        Evaluator evaluator1 = new ElementStarts("a");
        Evaluator evaluator2 = new ElementStarts("b");
        Evaluator evaluator3 = new ElementHasClass("class1");

        Or orEvaluator = new Or();
        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);
        orEvaluator.add(evaluator3);

        Element aElement = new Element("a");
        Element bElement = new Element("b");
        Element cElement = new Element("c");
        Element class1Element = new Element("div").addClass("class1");

        List<Element> elements = new ArrayList<>();
        elements.add(aElement);
        elements.add(bElement);
        elements.add(cElement);
        elements.add(class1Element);

        for (Element element : elements) {
            Assert.assertTrue(orEvaluator.matches(null, element));
        }

        Element dElement = new Element("d");
        Assert.assertFalse(orEvaluator.matches(null, dElement));
    }
}