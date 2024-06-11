
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void andMatches_AllEvaluatorsMatch_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (root1, node1) -> true;
        Evaluator evaluator2 = (root1, node1) -> true;

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        boolean result = andEvaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void andMatches_OneEvaluatorDoesNotMatch_ReturnsFalse() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (root1, node1) -> true;
        Evaluator evaluator2 = (root1, node1) -> false;

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        boolean result = andEvaluator.matches(root, node);

        assertFalse(result);
    }

    @Test
    public void andMatches_NoEvaluators_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        boolean result = andEvaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void orMatches_AllEvaluatorsMatch_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (root1, node1) -> true;
        Evaluator evaluator2 = (root1, node1) -> true;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        boolean result = orEvaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void orMatches_OneEvaluatorMatches_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = (root1, node1) -> false;
        Evaluator evaluator2 = (root1, node1) -> true;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        boolean result = orEvaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void orMatches_NoEvaluators_ReturnsFalse() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        boolean result = orEvaluator.matches(root, node);

        assertFalse(result);
    }

    @Test
    public void orToString_ReturnsProperStringRepresentation() {
        Evaluator evaluator1 = (root1, node1) -> true;
        Evaluator evaluator2 = (root1, node1) -> true;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        String result = orEvaluator.toString();

        assertEquals(":or[" + evaluator1.toString() + ", " + evaluator2.toString() + "]", result);
    }
}
