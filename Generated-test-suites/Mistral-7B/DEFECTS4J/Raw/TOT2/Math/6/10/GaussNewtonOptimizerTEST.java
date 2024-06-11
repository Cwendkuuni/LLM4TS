import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.GradientDescentOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer_SimpleLinearFunction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        ObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{3.0, 1.0});
        InitialGuess initialGuess = new InitialGuess(new double[]{1.0, 1.0});
        double[] expectedResult = new double[]{2.0, 1.0};
        MaxEval maxEval = new MaxEval(1000);
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});

        PointValuePair result = optimizer.optimize(function, initialGuess, maxEval, bounds, GoalType.MINIMIZE);
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(function.value(expectedResult), result.getValue(), 0.001);
    }

    @Test
    public void testGaussNewtonOptimizer_SingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                RealMatrix a = new BlockRealMatrix(new double[][]{
                        {0.0, 1.0},
                        {1.0, 0.0}
                });
                RealMatrix b = new ArrayRealVector(new double[]{0.0, 0.0});
                try {
                    optimizer.optimize(function, new InitialGuess(new double[]{0.0}), new MaxEval(1), new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0}), GoalType.MINIMIZE);
                } catch (ConvergenceException e) {
                    // Expected
                }
                return 0.0;
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{0.0});
        MaxEval maxEval = new MaxEval(1);
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});

        try {
            optimizer.optimize(function, initialGuess, maxEval, bounds, GoalType.MINIMIZE);
            fail("Should have thrown SingularMatrixException");
        } catch (ConvergenceException e) {
            try {
                e.getCause();
                fail("Should have been a SingularMatrixException");
            } catch (SingularMatrixException sme) {
                // Expected
            }
        }
    }

    @Test
    public void testGaussNewtonOptimizer_GradientDescentOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2.0) + Math.pow(Math.sin(Math.PI * x[1]), 2.0);
            }

            @Override
            public double[] valueGradient(double[] x) {
                double[] grad = new double[2];
                grad[0] = -2.0 * Math.sin(Math.PI * x[0]) * Math.cos(Math.PI * x[0]);
                grad[1] = -2.0 * Math.sin(Math.PI * x[1]) * Math.cos(Math.PI * x[1]);
                return grad;
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{0.5, 0.5});
        double[] expectedResult = new double[]{Math.PI / 4.0, Math.PI / 4.0};
        MaxEval maxEval = new MaxEval(1000);
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});

        PointValuePair result = optimizer.optimize(function, initialGuess, maxEval, bounds, GoalType.MINIMIZE);
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(function.value(expectedResult), result.getValue(), 0.001);

        Optimizer otherOptimizer = new GradientDescentOptimizer(0.1);
        result = otherOptimizer.optimize(function, initialGuess, maxEval, bounds, GoalType.MINIMIZE);
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
        assertEquals(function.value(expectedResult), result.getValue(), 0.001);
    }

    @Test
    public void testGaussNewtonOptimizer_LinearConstraints() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        ObjectiveFunction function = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0);
            }

            @Override
            public double[] valueGradient(double[] x) {
                return new double[]{2.0 * (x[0] - 1.0), 2.0 * (x[1] - 1.0)};
            }
        };
        InitialGuess initialGuess = new InitialGuess(new double[]{1.5, 1.5});
        MaxEval maxEval = new MaxEval(1000);
        SimpleBounds bounds = new SimpleBounds(new double[]{-5.0, -5.0}, new double[]{5.0, 5.0});

        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, 1.0, 0.0));
        constraints.addConstraint(new LinearConstraint(1.0, 2.0, 0.0));

        try {
            optimizer.optimize(function, initialGuess, maxEval, bounds, constraints, GoalType.MINIMIZE);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}