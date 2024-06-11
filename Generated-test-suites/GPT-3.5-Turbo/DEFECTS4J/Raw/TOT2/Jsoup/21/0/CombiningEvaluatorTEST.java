import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        // Typical use case - Both evaluators match
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("a"),
                new Evaluator.Tag("div")
        );
        Element root = new Element("div");
        Element node = new Element("a");
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndNotMatches() {
        // Typical use case - One evaluator does not match
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("a"),
                new Evaluator.Tag("div")
        );
        Element root = new Element("div");
        Element node = new Element("span");
        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndEmptyList() {
        // Edge case - Empty list of evaluators
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        Element root = new Element("div");
        Element node = new Element("a");
        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        // Typical use case - One evaluator matches
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("a"),
                new Evaluator.Tag("div")
        );
        Element root = new Element("div");
        Element node = new Element("span");
        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrNotMatches() {
        // Typical use case - None of the evaluators match
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("a"),
                new Evaluator.Tag("div")
        );
        Element root = new Element("span");
        Element node = new Element("span");
        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrEmptyList() {
        // Edge case - Empty list of evaluators
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        Element root = new Element("div");
        Element node = new Element("a");
        assertFalse(orEvaluator.matches(root, node));
    }

}