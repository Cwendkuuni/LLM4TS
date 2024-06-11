import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(
                new TestEvaluator(true),
                new TestEvaluator(true)
        ));

        assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And and = new CombiningEvaluator.And(Arrays.asList(
                new TestEvaluator(true),
                new TestEvaluator(false)
        ));

        assertFalse(and.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(
                new TestEvaluator(false),
                new TestEvaluator(true)
        ));

        assertTrue(or.matches(root, node));
    }

    @Test
    public void testOrNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(
                new TestEvaluator(false),
                new TestEvaluator(false)
        ));

        assertFalse(or.matches(root, node));
    }

    @Test
    public void testOrWithEmptyCollection() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList());

        assertFalse(or.matches(root, node));
    }

    private static class TestEvaluator extends Evaluator {
        private boolean result;

        TestEvaluator(boolean result) {
            this.result = result;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return result;
        }
    }
}