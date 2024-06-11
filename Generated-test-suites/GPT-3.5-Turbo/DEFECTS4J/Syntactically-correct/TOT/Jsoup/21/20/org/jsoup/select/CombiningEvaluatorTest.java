import org.jsoup.nodes.Element;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches_WhenAllEvaluatorsMatch_ReturnsTrue() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        EvaluatorMock evaluator1 = new EvaluatorMock(true);
        EvaluatorMock evaluator2 = new EvaluatorMock(true);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        // Act
        boolean result = andEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
        assertEquals(1, evaluator1.getCalls());
        assertEquals(1, evaluator2.getCalls());
    }

    @Test
    public void testAndMatches_WhenAnyEvaluatorDoesNotMatch_ReturnsFalse() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        EvaluatorMock evaluator1 = new EvaluatorMock(true);
        EvaluatorMock evaluator2 = new EvaluatorMock(false);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        // Act
        boolean result = andEvaluator.matches(root, node);

        // Assert
        assertFalse(result);
        assertEquals(1, evaluator1.getCalls());
        assertEquals(1, evaluator2.getCalls());
    }

    @Test
    public void testAndToString_ReturnsStringRepresentation() {
        // Arrange
        EvaluatorMock evaluator1 = new EvaluatorMock(true);
        EvaluatorMock evaluator2 = new EvaluatorMock(true);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        // Act
        String result = andEvaluator.toString();

        // Assert
        assertEquals("evaluator1 evaluator2", result);
    }

    @Test
    public void testOrMatches_WhenAnyEvaluatorMatches_ReturnsTrue() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        EvaluatorMock evaluator1 = new EvaluatorMock(true);
        EvaluatorMock evaluator2 = new EvaluatorMock(false);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        // Act
        boolean result = orEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
        assertEquals(1, evaluator1.getCalls());
        assertEquals(1, evaluator2.getCalls());
    }

    @Test
    public void testOrMatches_WhenNoEvaluatorMatches_ReturnsFalse() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");
        EvaluatorMock evaluator1 = new EvaluatorMock(false);
        EvaluatorMock evaluator2 = new EvaluatorMock(false);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        // Act
        boolean result = orEvaluator.matches(root, node);

        // Assert
        assertFalse(result);
        assertEquals(1, evaluator1.getCalls());
        assertEquals(1, evaluator2.getCalls());
    }

    @Test
    public void testOrToString_ReturnsStringRepresentation() {
        // Arrange
        EvaluatorMock evaluator1 = new EvaluatorMock(true);
        EvaluatorMock evaluator2 = new EvaluatorMock(false);
        EvaluatorMock evaluator3 = new EvaluatorMock(true);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2, evaluator3));

        // Act
        String result = orEvaluator.toString();

        // Assert
        assertEquals(":or[evaluator1, evaluator2, evaluator3]", result);
    }

    private static class EvaluatorMock implements Evaluator {
        private boolean matchesCalled;
        private int calls;

        public EvaluatorMock(boolean matchesCalled) {
            this.matchesCalled = matchesCalled;
            this.calls = 0;
        }

        @Override
        public boolean matches(Element root, Element node) {
            calls++;
            return matchesCalled;
        }

        public int getCalls() {
            return calls;
        }
    }
}