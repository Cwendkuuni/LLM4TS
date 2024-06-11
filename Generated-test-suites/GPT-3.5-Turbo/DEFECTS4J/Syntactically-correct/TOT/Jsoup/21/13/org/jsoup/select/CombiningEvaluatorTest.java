import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        MockEvaluator evaluator1 = new MockEvaluator(true);
        MockEvaluator evaluator2 = new MockEvaluator(true);
        MockEvaluator evaluator3 = new MockEvaluator(false);

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2, evaluator3);
        Assert.assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndFails() {
        Element root = new Element("root");
        Element node = new Element("node");

        MockEvaluator evaluator1 = new MockEvaluator(true);
        MockEvaluator evaluator2 = new MockEvaluator(true);
        MockEvaluator evaluator3 = new MockEvaluator(false);

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluator1, evaluator2, evaluator3);
        Assert.assertFalse(and.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        MockEvaluator evaluator1 = new MockEvaluator(false);
        MockEvaluator evaluator2 = new MockEvaluator(false);
        MockEvaluator evaluator3 = new MockEvaluator(true);

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);
        Assert.assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrFails() {
        Element root = new Element("root");
        Element node = new Element("node");

        MockEvaluator evaluator1 = new MockEvaluator(false);
        MockEvaluator evaluator2 = new MockEvaluator(false);
        MockEvaluator evaluator3 = new MockEvaluator(false);

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);
        Assert.assertFalse(or.matches(root, node));
    }

    @Test
    public void testOrAddEvaluator() {
        Element root = new Element("root");
        Element node = new Element("node");

        MockEvaluator evaluator1 = new MockEvaluator(true);
        MockEvaluator evaluator2 = new MockEvaluator(true);
        MockEvaluator evaluator3 = new MockEvaluator(false);

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluator1, evaluator2);
        or.add(evaluator3);
        Assert.assertTrue(or.matches(root, node));
    }

    private static class MockEvaluator extends Evaluator {
        private boolean result;

        public MockEvaluator(boolean result) {
            this.result = result;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return result;
        }
    }
}