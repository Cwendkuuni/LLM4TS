
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws Exception {
        Comparator<RealPointValuePair> comparator = Mockito.mock(Comparator.class);

        multiDirectional.iterateSimplex(comparator);

        // Verify that incrementIterationsCounter() is called
        assertEquals(1, multiDirectional.getIterations());

        // Verify that evaluateNewSimplex() and evaluateSimplex() are called
        verify(comparator, times(3)).compare(any(RealPointValuePair.class), any(RealPointValuePair.class));
    }

    @Test
    public void testEvaluateNewSimplex() throws Exception {
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{3.0, 4.0}, 0.0);

        Comparator<RealPointValuePair> comparator = Mockito.mock(Comparator.class);

        RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 0.5, comparator);

        // Verify that original simplex is preserved
        assertEquals(original[0], result);

        // Verify that xSmallest is used in the transformation
        double[] expectedPoint = {0.5, 1.0};
        verify(comparator).compare(any(RealPointValuePair.class), eq(original[0]));
        verify(comparator).compare(any(RealPointValuePair.class), eq(original[1]));
        assertEquals(expectedPoint[0], result.getPointRef()[0], 0.0001);
        assertEquals(expectedPoint[1], result.getPointRef()[1], 0.0001);
    }
}
