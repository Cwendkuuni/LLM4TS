
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatches() {
        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(true);

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        assertTrue(evaluator.matches(root, node));

        Mockito.verify(evaluator1).matches(root, node);
        Mockito.verify(evaluator2).matches(root, node);
    }

    @Test
    public void testAndDoesNotMatch() {
        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(false);

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        assertFalse(evaluator.matches(root, node));

        Mockito.verify(evaluator1).matches(root, node);
        Mockito.verify(evaluator2).matches(root, node);
    }

    @Test
    public void testOrMatches() {
        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Mockito.when(evaluator1.matches(root, node)).thenReturn(true);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(false);

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        assertTrue(evaluator.matches(root, node));

        Mockito.verify(evaluator1).matches(root, node);
        Mockito.verify(evaluator2).matches(root, node);
    }

    @Test
    public void testOrDoesNotMatch() {
        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Mockito.when(evaluator1.matches(root, node)).thenReturn(false);
        Mockito.when(evaluator2.matches(root, node)).thenReturn(false);

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        assertFalse(evaluator.matches(root, node));

        Mockito.verify(evaluator1).matches(root, node);
        Mockito.verify(evaluator2).matches(root, node);
    }

    @Test
    public void testOrWithSingleEvaluatorMatches() {
        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Evaluator evaluator = Mockito.mock(Evaluator.class);

        Mockito.when(evaluator.matches(root, node)).thenReturn(true);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator));

        assertTrue(orEvaluator.matches(root, node));

        Mockito.verify(evaluator).matches(root, node);
    }

    @Test
    public void testOrWithSingleEvaluatorDoesNotMatch() {
        Element root = Mockito.mock(Element.class);
        Element node = Mockito.mock(Element.class);

        Evaluator evaluator = Mockito.mock(Evaluator.class);

        Mockito.when(evaluator.matches(root, node)).thenReturn(false);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator));

        assertFalse(orEvaluator.matches(root, node));

        Mockito.verify(evaluator).matches(root, node);
    }
}
