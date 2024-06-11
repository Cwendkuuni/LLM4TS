import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CombiningEvaluatorTest {
    
    @Mock
    private Element rootElement;
    
    @Mock
    private Element nodeElement;

    @Test
    public void testMatchesAnd() {
        // Arrange
        CombiningEvaluator.And evaluatorAnd = new CombiningEvaluator.And();

        // Act

        // Assert
        assertFalse(evaluatorAnd.matches(rootElement, nodeElement));
    }

    @Test
    public void testMatchesOr() {
        // Arrange
        CombiningEvaluator.Or evaluatorOr = new CombiningEvaluator.Or();

        // Act

        // Assert
        assertFalse(evaluatorOr.matches(rootElement, nodeElement));
    }

    @Test
    public void testToStringAnd() {
        // Arrange
        CombiningEvaluator.And evaluatorAnd = new CombiningEvaluator.And();

        // Act

        // Assert
        assertEquals("", evaluatorAnd.toString());
    }

    @Test
    public void testToStringOr() {
        // Arrange
        CombiningEvaluator.Or evaluatorOr = new CombiningEvaluator.Or();

        // Act

        // Assert
        assertEquals(":or[]", evaluatorOr.toString());
    }

    @Test
    public void testAndMatches() {
        // Arrange
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        List<Evaluator> evaluators = Arrays.asList(evaluator);
        CombiningEvaluator.And evaluatorAnd = new CombiningEvaluator.And(evaluators);

        // Act

        // Assert
        assertTrue(evaluatorAnd.matches(rootElement, nodeElement));
    }

    @Test
    public void testOrMatches() {
        // Arrange
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        List<Evaluator> evaluators = Arrays.asList(evaluator);
        CombiningEvaluator.Or evaluatorOr = new CombiningEvaluator.Or(evaluators);

        // Act

        // Assert
        assertTrue(evaluatorOr.matches(rootElement, nodeElement));
    }

    @Test
    public void testOrAdd() {
        // Arrange
        CombiningEvaluator.Or evaluatorOr = new CombiningEvaluator.Or();
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        // Act
        evaluatorOr.add(evaluator);

        // Assert
        assertTrue(evaluatorOr.matches(rootElement, nodeElement));
    }

    @Test
    public void testOrAddMultiple() {
        // Arrange
        CombiningEvaluator.Or evaluatorOr = new CombiningEvaluator.Or();
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        List<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        // Act
        evaluatorOr.add(evaluators);

        // Assert
        assertTrue(evaluatorOr.matches(rootElement, nodeElement));
    }
}