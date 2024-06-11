import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {

    private final double RELATIVE_THRESHOLD = 1e-6;
    private final double ABSOLUTE_THRESHOLD = 1e-8;

    // Constructor tests

    @Test
    public void testConstructorWithValidArguments() {
        try {
            ConvergenceChecker<UnivariatePointValuePair> checker = null;
            BrentOptimizer brentOptimizer = new BrentOptimizer(RELATIVE_THRESHOLD, ABSOLUTE_THRESHOLD, checker);
        } catch (Exception e) {
            fail("Failed to create BrentOptimizer with valid arguments");
        }
    }

    @Test
    public void testConstructorWithRelTooSmall() {
        try {
            ConvergenceChecker<UnivariatePointValuePair> checker = null;
            BrentOptimizer brentOptimizer = new BrentOptimizer(BrentOptimizer.MIN_RELATIVE_TOLERANCE / 2, ABSOLUTE_THRESHOLD, checker);
            fail("Failed to throw NumberIsTooSmallException when rel is too small");
        } catch (NumberIsTooSmallException e) {
            assertTrue(true); // Exception is expected
        } catch (Exception e) {
            fail("Failed to throw NumberIsTooSmallException when rel is too small");
        }
    }

    @Test
    public void testConstructorWithNegativeAbs() {
        try {
            ConvergenceChecker<UnivariatePointValuePair> checker = null;
            BrentOptimizer brentOptimizer = new BrentOptimizer(RELATIVE_THRESHOLD, -ABSOLUTE_THRESHOLD, checker);
            fail("Failed to throw NotStrictlyPositiveException when abs is negative");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true); // Exception is expected
        } catch (Exception e) {
            fail("Failed to throw NotStrictlyPositiveException when abs is negative");
        }
    }

    // Optimization tests

    @Test
    public void testOptimizeOnQuadraticFunction() {
        double rel = 1e-6;
        double abs = 1e-8;
        double[] coefficients = {1, -2, 1}; // Quadratic function: x^2 - 2x + 1
        UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> {
            double result = 0;
            for (int i = 0; i < coefficients.length; i++) {
                result += coefficients[i] * Math.pow(x, i);
            }
            return result;
        });

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, -1, 2);
        assertEquals(1.0, result.getPoint(), abs);
    }

    @Test
    public void testOptimizeOnCubicFunction() {
        double rel = 1e-6;
        double abs = 1e-8;
        double[] coefficients = {1, -3, 3, -1}; // Cubic function: x^3 - 3x^2 + 3x - 1
        UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> {
            double result = 0;
            for (int i = 0; i < coefficients.length; i++) {
                result += coefficients[i] * Math.pow(x, i);
            }
            return result;
        });

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, 1, 3);
        assertEquals(1.0, result.getPoint(), abs);
    }

    @Test
    public void testOptimizeOnLinearFunction() {
        double rel = 1e-6;
        double abs = 1e-8;
        double[] coefficients = {2, -3}; // Linear function: 2x - 3
        UnivariateObjectiveFunction objectiveFunction = new UnivariateObjectiveFunction(x -> {
            double result = 0;
            for (int i = 0; i < coefficients.length; i++) {
                result += coefficients[i] * Math.pow(x, i);
            }
            return result;
        });

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, 0, 5);
        assertEquals(1.5, result.getPoint(), abs);
    }

    // Edge case tests

    @Test
    public void testOptimizeOnEmptyInterval() {
        double rel = 1e-6;
        double abs = 1e-8;
        UnivariateObjectiveFunction objectiveFunction = x -> 0;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, 0, 0);
        assertEquals(0.0, result.getPoint(), abs);
    }

    @Test
    public void testOptimizeOnNegativeInfinityToPositiveInfinity() {
        double rel = 1e-6;
        double abs = 1e-8;
        UnivariateObjectiveFunction objectiveFunction = x -> x;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        UnivariatePointValuePair result = optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(0.0, result.getPoint(), abs);
    }

    // Error scenario tests

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithMaxLessThanMin() {
        double rel = 1e-6;
        double abs = 1e-8;
        UnivariateObjectiveFunction objectiveFunction = x -> x;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.optimize(objectiveFunction, GoalType.MINIMIZE, 1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidGoalType() {
        double rel = 1e-6;
        double abs = 1e-8;
        UnivariateObjectiveFunction objectiveFunction = x -> x;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.optimize(objectiveFunction, null, 1, 2);
    }

}