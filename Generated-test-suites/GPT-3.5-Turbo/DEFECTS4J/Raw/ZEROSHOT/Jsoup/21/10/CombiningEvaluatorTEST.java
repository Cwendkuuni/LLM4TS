import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    private Element root;
    private Element node;

    @Before
    public void setUp() {
        root = new Element();
        node = new Element();
    }

    @Test
    public void testAndMatches() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new DummyEvaluator(true));
        evaluators.add(new DummyEvaluator(true));
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new DummyEvaluator(true));
        evaluators.add(new DummyEvaluator(false));
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new DummyEvaluator(false));
        evaluators.add(new DummyEvaluator(true));
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        List<Evaluator> evaluators = new ArrayList<Evaluator>();
        evaluators.add(new DummyEvaluator(false));
        evaluators.add(new DummyEvaluator(false));
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertFalse(orEvaluator.matches(root, node));
    }

    private class DummyEvaluator extends Evaluator {
        private boolean result;

        public DummyEvaluator(boolean result) {
            this.result = result;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return result;
        }
    }
}