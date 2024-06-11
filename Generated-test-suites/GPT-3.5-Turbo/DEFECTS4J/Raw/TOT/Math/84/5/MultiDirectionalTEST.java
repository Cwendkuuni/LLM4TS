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
    public void testCustomConstructor() {
        MultiDirectional customMultiDirectional = new MultiDirectional(1.0, 0.2);
        assertEquals(1.0, customMultiDirectional.getKhi(), 0.0);
        assertEquals(0.2, customMultiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testIterateSimplexWithReflection() {
        try {
            multiDirectional.iterateSimplex();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testIterateSimplexWithReflectionAndContraction() {
        try {
            multiDirectional.setIterationsCounter(100);
            multiDirectional.iterateSimplex();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexWithException() throws FunctionEvaluationException, OptimizationException {
        multiDirectional.setIterationsCounter(200);
        multiDirectional.iterateSimplex();
    }

    @Test
    public void testEvaluateNewSimplex() {
        double[] point = {1.0, 2.0};
        RealPointValuePair[] original = new RealPointValuePair[3];
        double value = 2.5;
        boolean isMinimized = false;
        for (int i = 0; i < original.length; i++) {
            original[i] = new RealPointValuePair(point, value, isMinimized);
        }
        try {
            RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 0.5, null);
            assertEquals(point[0], result.getPointRef()[0], 0.0);
            assertEquals(point[1], result.getPointRef()[1], 0.0);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

}