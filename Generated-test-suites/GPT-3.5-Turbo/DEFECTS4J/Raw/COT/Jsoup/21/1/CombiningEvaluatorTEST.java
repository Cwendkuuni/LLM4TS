import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator.And;
import org.jsoup.select.CombiningEvaluator.Or;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Create some sample evaluators
        Evaluator evaluator1 = new MockEvaluator(true);
        Evaluator evaluator2 = new MockEvaluator(true);
        Evaluator evaluator3 = new MockEvaluator(false);

        // Create an 'and' evaluator with the sample evaluators
        CombiningEvaluator.And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2, evaluator3));

        // Check if all evaluators in 'and' evaluator return true, so the 'and' evaluator should return true
        Assert.assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Create some sample evaluators
        Evaluator evaluator1 = new MockEvaluator(true);
        Evaluator evaluator2 = new MockEvaluator(false);

        // Create an 'and' evaluator with the sample evaluators
        CombiningEvaluator.And andEvaluator = new And(Arrays.asList(evaluator1, evaluator2));

        // Check if any evaluator in 'and' evaluator returns false, so the 'and' evaluator should return false
        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Create some sample evaluators
        Evaluator evaluator1 = new MockEvaluator(true);
        Evaluator evaluator2 = new MockEvaluator(false);
        Evaluator evaluator3 = new MockEvaluator(false);

        // Create an 'or' evaluator with the sample evaluators
        CombiningEvaluator.Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2, evaluator3));

        // Check if any evaluator in 'or' evaluator returns true, so the 'or' evaluator should return true
        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Create some sample evaluators
        Evaluator evaluator1 = new MockEvaluator(false);
        Evaluator evaluator2 = new MockEvaluator(false);
        Evaluator evaluator3 = new MockEvaluator(false);

        // Create an 'or' evaluator with the sample evaluators
        CombiningEvaluator.Or orEvaluator = new Or(Arrays.asList(evaluator1, evaluator2, evaluator3));

        // Check if all evaluators in 'or' evaluator return false, so the 'or' evaluator should return false
        Assert.assertFalse(orEvaluator.matches(root, node));
    }

    private static class MockEvaluator extends Evaluator {
        private final boolean match;

        public MockEvaluator(boolean match) {
            this.match = match;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return match;
        }
    }
}