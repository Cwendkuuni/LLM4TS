import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Test computeObjectiveValue() method with typical inputs and a valid output
    @Test
    public void testComputeObjectiveValue_typicalInputs_validOutput() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        double result = optimizer.computeObjectiveValue(5.0);

        assertEquals(25.0, result, 0.0001);
    }

    // Test computeObjectiveValue() method with minimum boundary inputs and a valid output
    @Test
    public void testComputeObjectiveValue_minBoundaryInputs_validOutput() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        double result = optimizer.computeObjectiveValue(Double.MIN_VALUE);

        assertEquals(Double.MIN_VALUE * Double.MIN_VALUE, result, 0.0001);
    }

    // Test computeObjectiveValue() method with maximum boundary inputs and a valid output
    @Test
    public void testComputeObjectiveValue_maxBoundaryInputs_validOutput() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        double result = optimizer.computeObjectiveValue(Double.MAX_VALUE);

        assertEquals(Double.MAX_VALUE * Double.MAX_VALUE, result, 0.0001);
    }

    // Test computeObjectiveValue() method with an error scenario and an expected exception
    @Test(expected = IllegalArgumentException.class)
    public void testComputeObjectiveValue_errorScenario_expectedException() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        optimizer.computeObjectiveValue(Double.NaN);
    }

    // Test doOptimize() method with typical inputs and a valid output
    @Test
    public void testDoOptimize_typicalInputs_validOutput() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= optimizer.getMax());
        assertNotNull(result.getValue());
        assertEquals(optimizer.isMinimize(), result.getValue() < 0);
    }

    // Test doOptimize() method with minimum boundary inputs and a valid output
    @Test
    public void testDoOptimize_minBoundaryInputs_validOutput() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        optimizer.setMin(Double.MIN_VALUE);
        optimizer.setMax(0.0);
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= optimizer.getMax());
        assertNotNull(result.getValue());
        assertEquals(optimizer.isMinimize(), result.getValue() < 0);
    }

    // Test doOptimize() method with maximum boundary inputs and a valid output
    @Test
    public void testDoOptimize_maxBoundaryInputs_validOutput() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        optimizer.setMin(0.0);
        optimizer.setMax(Double.MAX_VALUE);
        UnivariatePointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertTrue(result.getPoint() >= optimizer.getMin());
        assertTrue(result.getPoint() <= optimizer.getMax());
        assertNotNull(result.getValue());
        assertEquals(optimizer.isMinimize(), result.getValue() < 0);
    }

    // Test doOptimize() method with an error scenario and an expected exception
    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimize_errorScenario_expectedException() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.0001);
        optimizer.setMin(Double.NaN);
        optimizer.setMax(Double.POSITIVE_INFINITY);
        optimizer.doOptimize();
    }

}