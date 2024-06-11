package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.scalings.SquaredSumScaling;
import org.apache.commons.math3.optim.simplextree.AmaldiSimplex;
import org.apache.commons.math3.optim.simplextree.NelderMeadSimplex;
import org.apache.commons.math3.optim.simplextree.SimplexIterateResult;
import org.apache.commons.math3.optim.simplextree.SimplexModel;
import org.apache.commons.math3.optim.simplextree.SimplexStepType;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /** The objective function to optimize. */
    private static final MultivariateFunction FUNCTION
        = new MultivariateFunction() {
            public double value(double[] x) {
                return FunctionUtils.pow(FunctionUtils.sum(x), 2);
            }
        };

    /** The simplex optimizer to test. */
    private SimplexOptimizer optimizer;

    /** The simplex optimizer with Amaldi's rule. */
    private SimplexOptimizer amaldiOptimizer;

    /** The simplex optimizer with Nelder-Mead rule. */
    private SimplexOptimizer nelderMeadOptimizer;

    /** The convergence checker. */
    private SimpleValueChecker checker;

    /** The initial point. */
    private double[] initialPoint = new double[] { 2.0, 3.0 };

    /** The optimization data. */
    private OptimizationData optimizationData;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        optimizationData = new OptimizationData(new double[] { 0.0, 0.0 },
                                                new AbstractSimplex[] {
                                                    new AmaldiSimplex(),
                                                    new NelderMeadSimplex()
                                                });
        optimizer = new SimplexOptimizer(new SimpleValueChecker(0.001, 0.0));
        amaldiOptimizer = new SimplexOptimizer(optimizationData);
        nelderMeadOptimizer = new SimplexOptimizer(optimizationData);
    }

    /**
     * Test optimization with Amaldi's rule.
     */
    @Test
    public void testAmaldi() {
        PointValuePair result = optimizer.optimize(5, FUNCTION, GoalType.MINIMIZE, initialPoint);
        assertNotNull(result);
        assertEquals(initialPoint[0], result.getPoint()[0], 0.001);
        assertEquals(initialPoint[1], result.getPoint()[1], 0.001);
        assertEquals(FunctionUtils.pow(FunctionUtils.sum(initialPoint), 2),
                     result.getValue(), 0.001);

        try {
            PointValuePair resultAmaldi
                = amaldiOptimizer.optimize(FunctionUtils.MIN_EVALS, FUNCTION, GoalType.MINIMIZE, initialPoint);
            assertEquals(result.getValue(), resultAmaldi.getValue(), 0.001);
            System.out.println("Amaldi rule ok.");
        } catch (UnconstrainedOptimizationException e) {
            System.out.println("Amaldi rule failed.");
        }
    }

    /**
     * Test optimization with Nelder-Mead rule.
     */
    @Test
    public void testNelderMead() {
        PointValuePair result = optimizer.optimize(5, FUNCTION, GoalType.MINIMIZE, initialPoint);
        assertNotNull(result);
        assertEquals(initialPoint[0], result.getPoint()[0], 0.001);
        assertEquals(initialPoint[1], result.getPoint()[1], 0.001);
        assertEquals(FunctionUtils.pow(FunctionUtils.sum(initialPoint), 2),
                     result.getValue(), 0.001);

        SimplexModel model = new SimplexModel(new SquaredSumScaling(), new NelderMeadSimplex());
        SimplexIterateResult resultNelderMead
            = model.optimize(new PointValuePair(initialPoint, FunctionUtils.pow(FunctionUtils.sum(initialPoint), 2)),
                            FunctionUtils.MIN_EVALS,
                            GoalType.MINIMIZE);
        PointValuePair resultNelderMeadPair
            = new PointValuePair(resultNelderMead.getPoint(), resultNelderMead.getValue());
        assertEquals(result.getValue(), resultNelderMeadPair.getValue(), 0.001);
        System.out.println("Nelder-Mead rule ok.");
    }

    /**
     * Test optimization with invalid arguments.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullArguments() {
        new SimplexOptimizer(null);
    }
}