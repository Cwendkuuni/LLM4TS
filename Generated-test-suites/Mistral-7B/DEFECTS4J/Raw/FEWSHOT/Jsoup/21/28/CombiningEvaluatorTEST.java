package org.jsoup.select;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndEvaluator() {
        Evaluator eval1 = new ElementStartsWithEvaluator("div");
        Evaluator eval2 = new ElementClassMatchesEvaluator("highlight");

        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(eval1, eval2);

        Element root = Element.parse("<html><body><div class='highlight'></div></body></html>");
        Element node = root.selectFirst("div.highlight");

        Assert.assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluator_twoFalse() {
        Evaluator eval1 = new ElementStartsWithEvaluator("div");
        Evaluator eval2 = new ElementClassMatchesEvaluator("table");

        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(eval1, eval2);

        Element root = Element.parse("<html><body><div></div><table></table></body></html>");
        Element node = root.selectFirst("div");

        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEvaluator_oneFalse() {
        Evaluator eval1 = new ElementStartsWithEvaluator("div");
        Evaluator eval2 = new ElementClassMatchesEvaluator("table");

        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(eval1, eval2);

        Element root = Element.parse("<html><body><table></table></body></html>");
        Element node = root.selectFirst("div");

        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluator() {
        Evaluator eval1 = new ElementStartsWithEvaluator("div");
        Evaluator eval2 = new ElementClassMatchesEvaluator("table");
        Evaluator eval3 = new ElementClassMatchesEvaluator("highlight");

        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(Arrays.asList(eval1, eval2, eval3));

        Element root = Element.parse("<html><body><div class='highlight'></div></body></html>");
        Element node1 = root.selectFirst("div.highlight");
        Element node2 = root.selectFirst("table");
        Element node3 = root.selectFirst("div");

        Assert.assertTrue(orEvaluator.matches(root, node1));
        Assert.assertFalse(orEvaluator.matches(root, node2));
        Assert.assertFalse(orEvaluator.matches(root, node3));
    }

    @Test
    public void testOrEvaluator_oneTrue() {
        Evaluator eval1 = new ElementStartsWithEvaluator("div");
        Evaluator eval2 = new ElementClassMatchesEvaluator("table");
        Evaluator eval3 = new ElementClassMatchesEvaluator("highlight");

        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(Arrays.asList(eval1, eval2, eval3));

        Element root = Element.parse("<html><body><table></table></body></html>");
        Element node = root.selectFirst("table");

        orEvaluator.add(new ElementClassMatchesEvaluator("highlight"));

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEvaluator_allFalse() {
        Evaluator eval1 = new ElementStartsWithEvaluator("div");
        Evaluator eval2 = new ElementClassMatchesEvaluator("table");
        Evaluator eval3 = new ElementClassMatchesEvaluator("highlight");

        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(Collections.singletonList(eval1));

        Element root = Element.parse("<html><body><div></div></body></html>");
        Element node = root.selectFirst("div");

        orEvaluator.add(eval2);
        orEvaluator.add(eval3);

        Assert.assertFalse(orEvaluator.matches(root, node));
    }
}