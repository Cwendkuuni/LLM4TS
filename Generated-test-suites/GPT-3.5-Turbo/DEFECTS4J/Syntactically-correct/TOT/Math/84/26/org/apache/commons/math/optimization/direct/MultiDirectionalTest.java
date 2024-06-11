import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(2.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.5, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testParameterizedConstructor() {
        double khi = 1.5;
        double gamma = 0.75;
        MultiDirectional paramMultiDirectional = new MultiDirectional(khi, gamma);
        assertEquals(khi, paramMultiDirectional.getKhi(), 0.0);
        assertEquals(gamma, paramMultiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testIterateSimplex_ReflectionStep() throws FunctionEvaluationException, OptimizationException {
        // Set up the original simplex
        RealPointValuePair[] original = new RealPointValuePair[3];
        double[] point1 = {1.0, 2.0, 3.0};
        double[] point2 = {2.0, 4.0, 6.0};
        double[] point3 = {3.0, 6.0, 9.0};
        original[0] = new RealPointValuePair(point1, 10.0);
        original[1] = new RealPointValuePair(point2, 20.0);
        original[2] = new RealPointValuePair(point3, 30.0);

        multiDirectional.setSimplex(original);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        multiDirectional.iterateSimplex(comparator);

        // Check if reflected point is correct, edge case
        double[] reflectedExpected = {0.0, 0.0, 0.0};
        assertArrayEquals(reflectedExpected, multiDirectional.getSimplex()[1].getPointRef(), 0.0);
    }

    @Test
    public void testIterateSimplex_ContractionStep() throws FunctionEvaluationException, OptimizationException {
        // Set up the original simplex
        RealPointValuePair[] original = new RealPointValuePair[3];
        double[] point1 = {1.0, 2.0, 3.0};
        double[] point2 = {2.0, 4.0, 6.0};
        double[] point3 = {3.0, 6.0, 9.0};
        original[0] = new RealPointValuePair(point1, 10.0);
        original[1] = new RealPointValuePair(point2, 20.0);
        original[2] = new RealPointValuePair(point3, 30.0);

        multiDirectional.setSimplex(original);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        multiDirectional.iterateSimplex(comparator);

        // Check if contracted point is correct, typical use case
        double[] contractedExpected = {1.5, 3.0, 4.5};
        assertArrayEquals(contractedExpected, multiDirectional.getSimplex()[2].getPointRef(), 0.0);
    }

}