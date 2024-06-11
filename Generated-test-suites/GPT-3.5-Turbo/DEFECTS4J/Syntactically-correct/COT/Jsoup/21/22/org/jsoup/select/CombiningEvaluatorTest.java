
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = new Evaluator.Tag("node");
        Evaluator evaluator2 = new Evaluator.Tag("anotherTag");
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        // Test when both evaluators match
        boolean result1 = andEvaluator.matches(root, node);
        // Assert that the result should be true
        assert result1;

        // Test when only one evaluator matches
        Element nonMatchingNode = new Element("nonMatching");
        boolean result2 = andEvaluator.matches(root, nonMatchingNode);
        // Assert that the result should be false
        assert !result2;
    }

    @Test
    public void testAndToString() {
        Evaluator evaluator1 = new Evaluator.Tag("node");
        Evaluator evaluator2 = new Evaluator.Tag("anotherTag");
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        String expected = "node anotherTag";
        String actual = andEvaluator.toString();

        // Assert that the actual toString() representation matches the expected value
        assert actual.equals(expected);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = new Evaluator.Tag("node");
        Evaluator evaluator2 = new Evaluator.Tag("anotherTag");
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        // Test when both evaluators match
        boolean result1 = orEvaluator.matches(root, node);
        // Assert that the result should be true
        assert result1;

        // Test when no evaluators match
        Element nonMatchingNode = new Element("nonMatching");
        boolean result2 = orEvaluator.matches(root, nonMatchingNode);
        // Assert that the result should be false
        assert !result2;
    }

    @Test
    public void testOrToString() {
        Evaluator evaluator1 = new Evaluator.Tag("node");
        Evaluator evaluator2 = new Evaluator.Tag("anotherTag");
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        String expected = ":or[node, anotherTag]";
        String actual = orEvaluator.toString();

        // Assert that the actual toString() representation matches the expected value
        assert actual.equals(expected);
    }

    @Test
    public void testOrAdd() {
        Evaluator evaluator1 = new Evaluator.Tag("node");
        Evaluator evaluator2 = new Evaluator.Tag("anotherTag");
        Evaluator evaluator3 = new Evaluator.Tag("thirdTag");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        orEvaluator.add(evaluator3);

        // Assert that the evaluators list contains all three evaluators
        assert orEvaluator.evaluators.size() == 3;
        assert orEvaluator.evaluators.contains(evaluator1);
        assert orEvaluator.evaluators.contains(evaluator2);
        assert orEvaluator.evaluators.contains(evaluator3);
    }
}
