
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CombiningEvaluatorTest {
    private Element mockRoot;
    private Element mockNode;

    @Before
    public void setUp() {
        mockRoot = mock(Element.class);
        mockNode = mock(Element.class);
    }

    @Test
    public void testAndMatches_WithTrueEvaluator_ReturnsTrue() {
        Evaluator trueEvaluator = (root, node) -> true;

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(trueEvaluator));
        boolean result = andEvaluator.matches(mockRoot, mockNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testAndMatches_WithFalseEvaluator_ReturnsFalse() {
        Evaluator falseEvaluator = (root, node) -> false;

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(falseEvaluator));
        boolean result = andEvaluator.matches(mockRoot, mockNode);

        Assert.assertFalse(result);
    }

    @Test
    public void testAndMatches_WithMultipleEvaluators_ReturnsTrueIfAllEvaluatorsReturnTrue() {
        Evaluator trueEvaluator = (root, node) -> true;
        Evaluator falseEvaluator = (root, node) -> false;

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(trueEvaluator, trueEvaluator, trueEvaluator));
        boolean result = andEvaluator.matches(mockRoot, mockNode);

        Assert.assertTrue(result);

        andEvaluator = new CombiningEvaluator.And(Arrays.asList(trueEvaluator, trueEvaluator, falseEvaluator));
        result = andEvaluator.matches(mockRoot, mockNode);

        Assert.assertFalse(result);
    }

    @Test
    public void testAndToString_ReturnsCorrectStringRepresentation() {
        Evaluator evaluator1 = (root, node) -> true;
        Evaluator evaluator2 = (root, node) -> false;

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        String result = andEvaluator.toString();

        Assert.assertEquals("[true, false]", result);
    }

    @Test
    public void testOrMatches_WithTrueEvaluator_ReturnsTrue() {
        Evaluator trueEvaluator = (root, node) -> true;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(trueEvaluator));
        boolean result = orEvaluator.matches(mockRoot, mockNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrMatches_WithFalseEvaluator_ReturnsFalse() {
        Evaluator falseEvaluator = (root, node) -> false;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(falseEvaluator));
        boolean result = orEvaluator.matches(mockRoot, mockNode);

        Assert.assertFalse(result);
    }

    @Test
    public void testOrMatches_WithMultipleEvaluators_ReturnsTrueIfAnyEvaluatorReturnsTrue() {
        Evaluator trueEvaluator = (root, node) -> true;
        Evaluator falseEvaluator = (root, node) -> false;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(trueEvaluator, trueEvaluator, trueEvaluator));
        boolean result = orEvaluator.matches(mockRoot, mockNode);

        Assert.assertTrue(result);

        orEvaluator = new CombiningEvaluator.Or(Arrays.asList(falseEvaluator, falseEvaluator, trueEvaluator));
        result = orEvaluator.matches(mockRoot, mockNode);

        Assert.assertTrue(result);

        orEvaluator = new CombiningEvaluator.Or(Arrays.asList(falseEvaluator, falseEvaluator, falseEvaluator));
        result = orEvaluator.matches(mockRoot, mockNode);

        Assert.assertFalse(result);
    }

    @Test
    public void testOrAdd_AddsEvaluatorToList() {
        Evaluator evaluator = (root, node) -> true;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());
        orEvaluator.add(evaluator);

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator);

        Assert.assertEquals(evaluators, orEvaluator.evaluators);
    }

    @Test
    public void testOrToString_ReturnsCorrectStringRepresentation() {
        Evaluator evaluator1 = (root, node) -> true;
        Evaluator evaluator2 = (root, node) -> false;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        String result = orEvaluator.toString();

        Assert.assertEquals(":or[true, false]", result);
    }
}
