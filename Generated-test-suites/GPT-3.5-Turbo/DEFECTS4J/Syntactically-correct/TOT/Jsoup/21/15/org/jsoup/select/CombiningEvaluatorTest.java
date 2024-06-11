
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    // Test case for CombiningEvaluator.And.matches()
    @Test
    public void testAndMatches() {
        // Arrange
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        List<Evaluator> evaluators = Arrays.asList(mockEvaluator1, mockEvaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = andEvaluator.matches(null, null);

        // Assert
        assertFalse(result);
    }

    // Test case for CombiningEvaluator.And.toString()
    @Test
    public void testAndToString() {
        // Arrange
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        List<Evaluator> evaluators = Arrays.asList(mockEvaluator1, mockEvaluator2);
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        String result = andEvaluator.toString();

        // Assert
        assertEquals("[MockEvaluator1, MockEvaluator2]", result);
    }

    // Test case for CombiningEvaluator.Or.matches()
    @Test
    public void testOrMatches() {
        // Arrange
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        List<Evaluator> evaluators = Arrays.asList(mockEvaluator1, mockEvaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = orEvaluator.matches(null, null);

        // Assert
        assertTrue(result);
    }

    // Test case for CombiningEvaluator.Or.toString()
    @Test
    public void testOrToString() {
        // Arrange
        Evaluator mockEvaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator mockEvaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        List<Evaluator> evaluators = Arrays.asList(mockEvaluator1, mockEvaluator2);
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        String result = orEvaluator.toString();

        // Assert
        assertEquals(":or[MockEvaluator1, MockEvaluator2]", result);
    }
}
