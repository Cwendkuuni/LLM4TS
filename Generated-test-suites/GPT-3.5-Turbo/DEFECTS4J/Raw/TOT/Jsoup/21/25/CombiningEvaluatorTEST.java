import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatchesPositive() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Create two evaluators that match
        MyEvaluator evaluator1 = new MyEvaluator(true);
        MyEvaluator evaluator2 = new MyEvaluator(true);
        CombiningEvaluator.And combiningAnd = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        boolean result = combiningAnd.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testAndMatchesNegative() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Create two evaluators, one matches and the other does not
        MyEvaluator evaluator1 = new MyEvaluator(true);
        MyEvaluator evaluator2 = new MyEvaluator(false);
        CombiningEvaluator.And combiningAnd = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        boolean result = combiningAnd.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testAndMatchesEmptyEvaluators() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And combiningAnd = new CombiningEvaluator.And();

        boolean result = combiningAnd.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrMatchesPositive() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Create two evaluators, one matches and the other does not
        MyEvaluator evaluator1 = new MyEvaluator(true);
        MyEvaluator evaluator2 = new MyEvaluator(false);
        CombiningEvaluator.Or combiningOr = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        boolean result = combiningOr.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrMatchesNegative() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Create two evaluators that do not match
        MyEvaluator evaluator1 = new MyEvaluator(false);
        MyEvaluator evaluator2 = new MyEvaluator(false);
        CombiningEvaluator.Or combiningOr = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        boolean result = combiningOr.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testOrMatchesEmptyEvaluators() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or combiningOr = new CombiningEvaluator.Or();

        boolean result = combiningOr.matches(root, node);

        Assert.assertFalse(result);
    }

    private static class MyEvaluator extends CombiningEvaluator {
        private final boolean matches;

        public MyEvaluator(boolean matches) {
            this.matches = matches;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return matches;
        }
    }
}