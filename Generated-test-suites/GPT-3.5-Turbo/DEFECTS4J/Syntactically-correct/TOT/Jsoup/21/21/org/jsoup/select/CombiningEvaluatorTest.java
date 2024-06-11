
package org.jsoup.select;

import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class CombiningEvaluatorTest {

    @Test
    public void andEvaluator_Matches_AllEvaluatorsSucceed_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (r, n) -> true;
        Evaluator evaluator2 = (r, n) -> true;
        Evaluator evaluator3 = (r, n) -> true;

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2, evaluator3));

        Assert.assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void andEvaluator_OneEvaluatorFails_ReturnsFalse() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (r, n) -> false;
        Evaluator evaluator2 = (r, n) -> true;
        Evaluator evaluator3 = (r, n) -> true;

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2, evaluator3));

        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void orEvaluator_OneEvaluatorSucceeds_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (r, n) -> true;
        Evaluator evaluator2 = (r, n) -> false;
        Evaluator evaluator3 = (r, n) -> false;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2, evaluator3));

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void orEvaluator_AllEvaluatorsFail_ReturnsFalse() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (r, n) -> false;
        Evaluator evaluator2 = (r, n) -> false;
        Evaluator evaluator3 = (r, n) -> false;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2, evaluator3));

        Assert.assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void orEvaluator_AddingEvaluator_AffectsEvaluation() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (r, n) -> true;
        Evaluator evaluator2 = (r, n) -> false;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Collections.singletonList(evaluator1));

        Assert.assertTrue(orEvaluator.matches(root, node));

        orEvaluator.add(evaluator2);

        Assert.assertFalse(orEvaluator.matches(root, node));
    }

}
