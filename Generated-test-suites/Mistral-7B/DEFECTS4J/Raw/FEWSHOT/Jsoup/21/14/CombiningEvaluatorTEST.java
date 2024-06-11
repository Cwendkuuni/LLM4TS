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
    public void testAndCombiningEvaluator() {
        Evaluator evaluator1 = new And(Arrays.asList(new ElementStarts("div"), new ElementHasClass("class1")));
        Evaluator evaluator2 = new And(Arrays.asList(new ElementHasText("text1"), new ElementHasClass("class2")));

        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        Element root = Element.parse("<div class=\"class1 class2\"><div class=\"class1\">text1</div></div>");
        Elements elements = root.selectNodes(andEvaluator.toString());

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(root.selectNode("div:class(class1):has(.class2):hasText(text1)"), elements.get(0));
    }

    @Test
    public void testOrCombiningEvaluator() {
        Evaluator evaluator1 = new ElementStarts("div");
        Evaluator evaluator2 = new ElementHasClass("class1");
        Evaluator evaluator3 = new ElementHasClass("class2");

        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2, evaluator3));

        Element root = Element.parse("<div class=\"class1\"><div class=\"class2\">Content 1</div></div>" +
                "<div class=\"class1 class2\">Content 2</div>" +
                "<div class=\"class2\">Content 3</div>");
        Elements elements = root.selectNodes(orEvaluator.toString());

        Assert.assertEquals(3, elements.size());
        Assert.assertTrue(elements.contains(root.selectNode("div:class(class1)")));
        Assert.assertTrue(elements.contains(root.selectNode("div:class(class1 class2)")));
        Assert.assertTrue(elements.contains(root.selectNode("div:class(class2)")));
    }

    @Test
    public void testAndCombiningEvaluatorWithNullEvaluator() {
        Evaluator evaluator1 = new And(Arrays.asList(new ElementStarts("div"), new ElementHasClass("class1")));
        Evaluator nullEvaluator = null;

        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, nullEvaluator));

        Element root = Element.parse("<div class=\"class1\"><div class=\"class1\">Content</div></div>");
        Elements elements = root.selectNodes(andEvaluator.toString());

        Assert.assertEquals(0, elements.size());
    }

    @Test
    public void testOrCombiningEvaluatorWithEmptyList() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());

        Element root = Element.parse("<div class=\"class1\"><div class=\"class2\">Content 1</div></div>" +
                "<div class=\"class1 class2\">Content 2</div>" +
                "<div class=\"class2\">Content 3</div>");
        Elements elements = root.selectNodes(orEvaluator.toString());

        Assert.assertEquals(3, elements.size());
        // Or evaluator doesn't have any specific behavior when an empty list is passed
    }
}