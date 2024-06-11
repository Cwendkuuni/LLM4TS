import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches_AllEvaluatorsReturnTrue_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Mock evaluators that always return true
        Evaluator evaluator1 = (root, node) -> true;
        Evaluator evaluator2 = (root, node) -> true;
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        boolean result = and.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testAndMatches_OneEvaluatorReturnsFalse_ReturnsFalse() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Mock evaluators
        Evaluator evaluator1 = (root, node) -> true; // Always returns true
        Evaluator evaluator2 = (root, node) -> false; // Always returns false
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        boolean result = and.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testAndMatches_EmptyEvaluators_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Empty evaluators
        List<Evaluator> evaluators = Collections.emptyList();

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        boolean result = and.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrMatches_OneEvaluatorReturnsTrue_ReturnsTrue() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Mock evaluators
        Evaluator evaluator1 = (root, node) -> false; // Always returns false
        Evaluator evaluator2 = (root, node) -> true; // Always returns true
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        boolean result = or.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrMatches_AllEvaluatorsReturnFalse_ReturnsFalse() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Mock evaluators that always return false
        Evaluator evaluator1 = (root, node) -> false;
        Evaluator evaluator2 = (root, node) -> false;
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        boolean result = or.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testOrMatches_EmptyEvaluators_ReturnsFalse() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Empty evaluators
        List<Evaluator> evaluators = Collections.emptyList();

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        boolean result = or.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testOrAdd_AddEvaluator_EvaluatorAddedSuccessfully() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Mock evaluators
        Evaluator evaluator1 = (root, node) -> false;
        Evaluator evaluator2 = (root, node) -> true;
        
        List<Evaluator> initialEvaluators = Collections.singletonList(evaluator1);
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(initialEvaluators);

        // Add new evaluator
        or.add(evaluator2);

        boolean result = or.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrAdd_AddNullEvaluator_EvaluatorNotAdded() {
        Element root = new Element("root");
        Element node = new Element("node");

        // Mock evaluators
        Evaluator evaluator1 = (root, node) -> true;
        
        List<Evaluator> initialEvaluators = Collections.singletonList(evaluator1);
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(initialEvaluators);

        // Add null evaluator
        or.add(null);

        boolean result = or.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrToString_ReturnsCorrectStringRepresentation() {
        // Mock evaluators
        Evaluator evaluator1 = (root, node) -> true;
        Evaluator evaluator2 = (root, node) -> false;
        
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        String result = or.toString();

        Assert.assertEquals(":or[" + evaluators.toString() + "]", result);
    }
}