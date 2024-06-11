import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CombiningEvaluatorTest {

    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;
    private Evaluator evaluator_1;
    private Evaluator evaluator_2;
    private Evaluator evaluator_3;
    private Element root;
    private Element node;

    @Before
    public void setUp() {
        root = mock(Element.class);
        node = mock(Element.class);
        evaluator_1 = mock(Evaluator.class);
        evaluator_2 = mock(Evaluator.class);
        evaluator_3 = mock(Evaluator.class);
        andEvaluator = new CombiningEvaluator.And(evaluator_1, evaluator_2);
        orEvaluator = new CombiningEvaluator.Or(evaluator_1, evaluator_2);
    }

    @Test
    public void testAndMatches_True() {
        when(evaluator_1.matches(root, node)).thenReturn(true);
        when(evaluator_2.matches(root, node)).thenReturn(true);

        boolean result = andEvaluator.matches(root, node);

        assertEquals(true, result);
    }

    @Test
    public void testAndMatches_False() {
        when(evaluator_1.matches(root, node)).thenReturn(true);
        when(evaluator_2.matches(root, node)).thenReturn(false);

        boolean result = andEvaluator.matches(root, node);

        assertEquals(false, result);
    }

    @Test
    public void testAndToString() {
        String expected = "evaluator1 evaluator2";

        String result = andEvaluator.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testOrMatches_True() {
        when(evaluator_1.matches(root, node)).thenReturn(false);
        when(evaluator_2.matches(root, node)).thenReturn(true);

        boolean result = orEvaluator.matches(root, node);

        assertEquals(true, result);
    }

    @Test
    public void testOrMatches_False() {
        when(evaluator_1.matches(root, node)).thenReturn(false);
        when(evaluator_2.matches(root, node)).thenReturn(false);

        boolean result = orEvaluator.matches(root, node);

        assertEquals(false, result);
    }

    @Test
    public void testOrToString() {
        String expected = ":or[evaluator1, evaluator2]";

        String result = orEvaluator.toString();

        assertEquals(expected, result);
    }
}