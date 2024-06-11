import org.jsoup.nodes.Element;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);

        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(true);

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = andEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
        Mockito.verify(evaluator1, Mockito.times(1)).matches(root, node);
        Mockito.verify(evaluator2, Mockito.times(1)).matches(root, node);
    }

    @Test
    public void testAndDoesNotMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);

        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(false);

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        // Act
        boolean result = andEvaluator.matches(root, node);

        // Assert
        assertFalse(result);
        Mockito.verify(evaluator1, Mockito.times(1)).matches(root, node);
        Mockito.verify(evaluator2, Mockito.times(1)).matches(root, node);
    }

    @Test
    public void testAndWithNoEvaluators() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();

        // Act
        boolean result = andEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testOrMatches() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);

        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(false);

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = orEvaluator.matches(root, node);

        // Assert
        assertTrue(result);
        Mockito.verify(evaluator1, Mockito.times(1)).matches(root, node);
        Mockito.verify(evaluator2, Mockito.times(1)).matches(root, node);
    }

    @Test
    public void testOrDoesNotMatch() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Mockito.when(evaluator1.matches(root, node)).thenReturn(false);

        Evaluator evaluator2 = Mockito.mock(Evaluator.class);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(false);

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        // Act
        boolean result = orEvaluator.matches(root, node);

        // Assert
        assertFalse(result);
        Mockito.verify(evaluator1, Mockito.times(1)).matches(root, node);
        Mockito.verify(evaluator2, Mockito.times(1)).matches(root, node);
    }

    @Test
    public void testOrWithNoEvaluators() {
        // Arrange
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();

        // Act
        boolean result = orEvaluator.matches(root, node);

        // Assert
        assertFalse(result);
    }
}