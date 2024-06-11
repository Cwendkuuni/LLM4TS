
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    private Element root;
    private Element node;
    private List<CombiningEvaluator.Evaluator> evaluators;

    @Before
    public void setUp() {
        root = new Element("root");
        node = new Element("node");
        evaluators = new ArrayList<>();
        evaluators.add(new CombiningEvaluator.And());
        evaluators.add(new CombiningEvaluator.Or());
        evaluators.add(new CombiningEvaluator.And(evaluators));
        evaluators.add(new CombiningEvaluator.Or(evaluators));
    }

    @Test
    public void testAndMatches() {
        CombiningEvaluator.And and = new CombiningEvaluator.And();
        assertTrue(and.matches(root, node));
    }

    @Test
    public void testAndToString() {
        CombiningEvaluator.And and = new CombiningEvaluator.And();
        assertEquals("", and.toString());
    }

    @Test
    public void testOrMatches() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or();
        assertFalse(or.matches(root, node));
    }

    @Test
    public void testOrToString() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or();
        assertEquals(":or[]", or.toString());
    }

    @Test
    public void testOrAdd() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or();
        or.add(new CombiningEvaluator.And());
        assertEquals(1, or.evaluators.size());
    }

    @Test
    public void testAndWithEvaluatorsMatches() {
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        assertTrue(and.matches(root, node));
    }

    @Test
    public void testOrWithEvaluatorsMatches() {
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        assertFalse(or.matches(root, node));
    }
}
