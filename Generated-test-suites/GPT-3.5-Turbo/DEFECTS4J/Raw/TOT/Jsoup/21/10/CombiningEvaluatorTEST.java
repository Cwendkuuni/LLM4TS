
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class CombiningEvaluatorTest {

    @Test
    public void andMatches_AllEvaluatorsReturnTrue_ReturnsTrue() {
        // Arrange
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(createMockEvaluators(true, true, true));

        // Act
        boolean result = evaluator.matches(null, null);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void andMatches_AtLeastOneEvaluatorReturnsFalse_ReturnsFalse() {
        // Arrange
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(createMockEvaluators(true, true, false));

        // Act
        boolean result = evaluator.matches(null, null);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void andMatches_NoEvaluators_ReturnsTrue() {
        // Arrange
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And();

        // Act
        boolean result = evaluator.matches(null, null);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void andToString_ReturnsEvaluatorsJoinedBySpace() {
        // Arrange
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(createMockEvaluators(true, false, true));

        // Act
        String result = evaluator.toString();

        // Assert
        Assert.assertEquals("true false true", result);
    }

    @Test
    public void orMatches_AllEvaluatorsReturnFalse_ReturnsFalse() {
        // Arrange
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(createMockEvaluators(false, false, false));

        // Act
        boolean result = evaluator.matches(null, null);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void orMatches_AtLeastOneEvaluatorReturnsTrue_ReturnsTrue() {
        // Arrange
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(createMockEvaluators(false, true, false));

        // Act
        boolean result = evaluator.matches(null, null);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void orMatches_NoEvaluators_ReturnsFalse() {
        // Arrange
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or();

        // Act
        boolean result = evaluator.matches(null, null);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void orToString_ReturnsEvaluatorsWrappedInOR() {
        // Arrange
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(createMockEvaluators(true, false, true));

        // Act
        String result = evaluator.toString();

        // Assert
        Assert.assertEquals(":or[true, false, true]", result);
    }

    private List<Evaluator> createMockEvaluators(boolean... results) {
        List<Evaluator> evaluators = new ArrayList<>();
        for (boolean result : results) {
            evaluators.add(new Evaluator() {
                @Override
                public boolean matches(Element root, Element node) {
                    return result;
                }
            });
        }
        return evaluators;
    }
}
