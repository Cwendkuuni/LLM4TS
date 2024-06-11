import static org.junit.Assert.*;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertNotNull(multiDirectional);
        assertEquals(2.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.5, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testCustomConstructor() {
        MultiDirectional multiDirectional = new MultiDirectional(3.0, 0.3);
        assertNotNull(multiDirectional);
        assertEquals(3.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.3, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testIterateSimplex() {
        // Test scenario where function evaluation exception is thrown
        // This is an edge case when the function cannot be evaluated at some point
        MultiDirectional multiDirectional = new MultiDirectional();
        assertThrows(FunctionEvaluationException.class, () -> {
            multiDirectional.iterateSimplex((a, b) -> 0);
        });

        // Test scenario where optimization exception is thrown
        // This is an edge case when the maximal number of evaluations is exceeded
        multiDirectional = new MultiDirectional();
        assertThrows(OptimizationException.class, () -> {
            multiDirectional.setMaxEvaluations(0);
            multiDirectional.iterateSimplex((a, b) -> 0);
        });
    }

    @Test
    public void testEvaluateNewSimplex() {
        // Test scenario where the linearly transformed simplex is correctly evaluated
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 2.0}, 0.0);
        try {
            RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 0.5, (a, b) -> 0);
            assertNotNull(result);
            assertEquals(1.0, result.getPointRef()[0], 0.0);
            assertEquals(1.0, result.getPointRef()[1], 0.0);
        } catch (FunctionEvaluationException | OptimizationException e) {
            fail("Exception thrown when not expected");
        }
    }
}