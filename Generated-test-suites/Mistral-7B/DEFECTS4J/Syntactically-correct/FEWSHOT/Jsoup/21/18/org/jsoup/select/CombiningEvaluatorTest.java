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
        List<Evaluator> evaluators = Arrays.asList(
                new ElementStartsEvaluator("p"),
                new ElementClassEvaluator("class", "selected")
        );
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(evaluators);

        Element root = Element.parse("<html><body><p class='selected'>Hello</p></body></html>");
        Elements elements = root.select("p.selected");
        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(andEvaluator.matches(root, elements.get(0)));

        root = Element.parse("<html><body><p class='Hello'>Hello</p></body></html>");
        elements = root.select("p.selected");
        Assert.assertEquals(0, elements.size());
        Assert.assertFalse(andEvaluator.matches(root, null));
    }

    @Test
    public void testOrEvaluator() {
        List<Evaluator> firstClause = Arrays.asList(
                new ElementStartsEvaluator("p"),
                new ElementClassEvaluator("class", "selected")
        );

        CombiningEvaluator firstClauseCombiningEvaluator = new CombiningEvaluator.And(firstClause);

        List<Evaluator> secondClause = Arrays.asList(
                new ElementClassEvaluator("id", "content")
        );

        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(firstClauseCombiningEvaluator, secondClause);

        Element root = Element.parse("<html><body><p class='selected'>Hello</p></body></html>");
        Elements elements = root.select("p.selected");
        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(orEvaluator.matches(root, elements.get(0)));

        root = Element.parse("<html><body><p id='content'>Hello</p></body></html>");
        elements = root.select("p.selected");
        Assert.assertEquals(0, elements.size());
        Assert.assertTrue(orEvaluator.matches(root, null));

        root = Element.parse("<html><body><p class='selected' id='content'>Hello</p></body></html>");
        elements = root.select("p.selected");
        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(orEvaluator.matches(root, elements.get(0)));
    }

    @Test
    public void testAddEvaluatorToOrEvaluator() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or();
        orEvaluator.add(new ElementStartsEvaluator("p"));
        orEvaluator.add(new ElementClassEvaluator("class", "selected"));

        Element root = Element.parse("<html><body><p class='selected'>Hello</p></body></html>");
        Elements elements = root.select("p.selected");
        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(orEvaluator.matches(root, elements.get(0)));

        root = Element.parse("<html><body><p class='Hello'>Hello</p></body></html>");
        elements = root.select("p.selected");
        Assert.assertEquals(0, elements.size());
        Assert.assertFalse(orEvaluator.matches(root, null));
    }
}