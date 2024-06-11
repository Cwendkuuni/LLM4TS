import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setUp() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new HasClassSelector("class1"));
        evaluators.add(new HasAttributeSelector("attr1"));

        andEvaluator = new CombiningEvaluator.And(evaluators);

        orEvaluator = new CombiningEvaluator.Or(evaluators);
    }

    @After
    public void tearDown() {
        andEvaluator = null;
        orEvaluator = null;
    }

    @Test
    public void testAndEvaluatorMatches() {
        Element root = new Element("div");
        Element node = new Element("span");

        boolean result = andEvaluator.matches(root, node);

        assertEquals(true, result);
    }

    @Test
    public void testAndEvaluatorDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("a");

        boolean result = andEvaluator.matches(root, node);

        assertEquals(false, result);
    }

    @Test
    public void testOrEvaluatorMatches() {
        Element root = new Element("div");
        Element node = new Element("a");

        boolean result = orEvaluator.matches(root, node);

        assertEquals(true, result);
    }

    @Test
    public void testOrEvaluatorDoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("span");

        boolean result = orEvaluator.matches(root, node);

        assertEquals(false, result);
    }
}