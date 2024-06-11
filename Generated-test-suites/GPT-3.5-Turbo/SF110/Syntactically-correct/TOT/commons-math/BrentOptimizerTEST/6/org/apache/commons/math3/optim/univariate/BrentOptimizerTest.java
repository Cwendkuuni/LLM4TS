import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-10, 1e-12, new SimpleValueChecker());
    }

    @Test
    public void testOptimize() {
        UnivariateObjectiveFunction objective = new UnivariateObjectiveFunction(d -> Math.pow(d, 2));
        UnivariatePointValuePair result = optimizer.optimize(objective, null, -2, 2);
        assertEquals(0.0, result.getPoint(), Precision.EPSILON);
        assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    // Typical use case
    @Test
    public void testOptimizeTypical() {
        UnivariateObjectiveFunction objective = new UnivariateObjectiveFunction(d -> Math.pow((d - 2), 2));
        UnivariatePointValuePair result = optimizer.optimize(objective, null, 0, 5);
        assertEquals(2.0, result.getPoint(), Precision.EPSILON);
        assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    // Edge case: Minimum tolerance values
    @Test
    public void testOptimizeMinimumTolerance() {
        UnivariateObjectiveFunction objective = new UnivariateObjectiveFunction(d -> Math.pow((d - 2), 2));
        UnivariatePointValuePair result = optimizer.optimize(objective, null, 0, 5);
        assertEquals(2.0, result.getPoint(), Precision.EPSILON);
        assertEquals(0.0, result.getValue(), Precision.EPSILON);
    }

    // Error scenario: Null objective function
    @Test(expected = NullPointerException.class)
    public void testOptimizeNullObjective() {
        optimizer.optimize(null, null, -2, 2);
        fail("Expected exception NullPointerException not thrown");
    }

    // Error scenario: Invalid search interval
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidInterval() {
        UnivariateObjectiveFunction objective = new UnivariateObjectiveFunction(d -> Math.pow(d, 2));
        optimizer.optimize(objective, null, 2, -2);
        fail("Expected exception IllegalArgumentException not thrown");
    }

    // Error scenario: Invalid absolute threshold
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidAbsoluteThreshold() {
        optimizer = new BrentOptimizer(1e-10, -1e-12, new SimpleValueChecker());
        UnivariateObjectiveFunction objective = new UnivariateObjectiveFunction(d -> Math.pow(d, 2));
        optimizer.optimize(objective, null, -2, 2);
        fail("Expected exception IllegalArgumentException not thrown");
    }

    // Error scenario: Invalid relative threshold
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeInvalidRelativeThreshold() {
        optimizer = new BrentOptimizer(-1e-10, 1e-12, new SimpleValueChecker());
        UnivariateObjectiveFunction objective = new UnivariateObjectiveFunction(d -> Math.pow(d, 2));
        optimizer.optimize(objective, null, -2, 2);
        fail("Expected exception IllegalArgumentException not thrown");
    }

}