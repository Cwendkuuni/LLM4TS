import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setup() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new TestEvaluator());
        evaluators.add(new TestEvaluator());
        evaluators.add(new TestEvaluator());
        andEvaluator = new CombiningEvaluator.And(evaluators);

        List<Evaluator> orEvaluators = new ArrayList<>();
        orEvaluators.add(new TestEvaluator());
        orEvaluator = new CombiningEvaluator.Or(orEvaluators);
    }

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        andEvaluator.evaluators.get(1).setMatches(false);

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");

        orEvaluator.evaluators.get(0).setMatches(false);

        assertFalse(orEvaluator.matches(root, node));
    }

    private class TestEvaluator extends CombiningEvaluator.Evaluator {
        private boolean matches = true;

        @Override
        public boolean matches(Element root, Element node) {
            return matches;
        }

        public void setMatches(boolean matches) {
            this.matches = matches;
        }
    }
}