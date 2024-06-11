package org.apache.commons.math.optimization.general;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.initializers.ArrayInitializer;
import org.apache.commons.math3.optim.nonlinear.functions.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.functions.Pow;
import org.apache.commons.math3.optim.nonlinear.functions.ScaledBroydenFletcherGoldfarbShanno;
import org.apache.commons.math3.optim.nonlinear.gradient.JacobianMatrixFunction;
import org.apache.commons.math3.optim.nonlinear.vector.dense.DenseVector;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("Method doOptimize should not be called in tests.");
                return null;
            }
        };

        // Test default settings
        optimizer.setMaxIterations(DEFAULT_MAX_ITERATIONS);
        optimizer.setMaxEvaluations(Integer.MAX_VALUE);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());

        // Test optimization of a simple linear function
        LinearFunction f = new LinearFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        double[] target = new double[]{5.0, 6.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] startPoint = new double[]{1.0, 1.0};

        PointValuePair result = optimizer.optimize(f, target, weights, startPoint);
        assertEquals(5.0, result.getPoint()[0], 1e-9);
        assertEquals(5.0, result.getValue(), 1e-9);
        assertEquals(6.0, result.getPoint()[1], 1e-9);
        assertEquals(6.0, result.getValue(), 1e-9);

        // Test optimization of a polynomial function
        PolynomialFunction p = new PolynomialFunction(new double[]{1.0, -2.0, 3.0}, 3);
        double[] targetPolynomial = new double[]{1.0, 1.0, 1.0};
        double[] weightsPolynomial = new double[]{1.0, 1.0, 1.0};
        double[] startPointPolynomial = new double[]{0.0, 0.0, 0.0};

        PointValuePair resultPolynomial = optimizer.optimize(p, targetPolynomial, weightsPolynomial, startPointPolynomial);
        assertEquals(1.0, resultPolynomial.getPoint()[0], 1e-9);
        assertEquals(-0.6666666666666666, resultPolynomial.getValue(), 1e-6);
        assertEquals(1.3333333333333335, resultPolynomial.getPoint()[1], 1e-6);
        assertEquals(-1.3333333333333335, resultPolynomial.getPoint()[2], 1e-6);
        assertEquals(1.0, resultPolynomial.getValue(), 1e-6);

        // Test optimization with custom convergence checker
        VectorialConvergenceChecker customChecker = new VectorialConvergenceChecker() {
            @Override
            public boolean isConverged(double[] point, double[] objective, double[] jacobian, double tolerance) {
                boolean converged = false;
                for (int i = 0; i < point.length; i++) {
                    if (Math.abs(point[i] - 1.0) > 1e-6) {
                        converged = false;
                        break;
                    } else {
                        converged = true;
                    }
                }
                return converged;
            }
        };
        optimizer.setConvergenceChecker(customChecker);

        // Test optimization of a function with non-linear behavior near optimum
        Pow powFunction = new Pow(new Pow(new Pow(new LinearFunction(new double[]{1.0}, new double[]{1.0}), 2.0), 2.0));
        double[] targetPow = new double[]{1.0};
        double[] weightsPow = new double[]{1.0};
        double[] startPointPow = new double[]{0.5};

        PointValuePair resultPow = optimizer.optimize(powFunction, targetPow, weightsPow, startPointPow);
        assertEquals(1.0, resultPow.getPoint()[0], 1e-6);
        assertEquals(0.0, resultPow.getValue(), 1e-6);

        // Test optimization with custom jacobian evaluation function
        JacobianMatrixFunction jF = new JacobianMatrixFunction() {
            @Override
            public double[][] value(double[] point) {
                double[][] jacobian = new double[1][2];
                jacobian[0][0] = 3.0 * Math.sin(point[0]);
                jacobian[0][1] = 2.0 * Math.cos(point[0]);
                return jacobian;
            }
        };
        LinearFunction fWithCustomJacobian = new LinearFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}) {
            @Override
            public MultivariateMatrixFunction jacobian() {
                return jF;
            }
        };
        double[] targetWithCustomJacobian = new double[]{5.0, 6.0};
        double[] weightsWithCustomJacobian = new double[]{1.0, 1.0};
        double[] startPointWithCustomJacobian = new double[]{1.0, 1.0};

        PointValuePair resultWithCustomJacobian = optimizer.optimize(fWithCustomJacobian, targetWithCustomJacobian, weightsWithCustomJacobian, startPointWithCustomJacobian);
        assertEquals(5.0, resultWithCustomJacobian.getPoint()[0], 1e-6);
        assertEquals(5.0, resultWithCustomJacobian.getValue(), 1e-6);
        assertEquals(6.0, resultWithCustomJacobian.getPoint()[1], 1e-6);
        assertEquals(6.0, resultWithCustomJacobian.getValue(), 1e-6);

        // Test optimization with custom initialization
        AbstractLeastSquaresOptimizer optimizerWithCustomInitialization = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                fail("Method doOptimize should not be called in tests.");
                return null;
            }

            @Override
            public VectorialPointValuePair optimize(final DifferentiableMultivariateVectorialFunction f,
                                                   final double[] target, final double[] weights,
                                                   final double[] startPoint)
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                return new ScaledBroydenFletcherGoldfarbShanno(optimizer, new ArrayInitializer(startPoint))
                        .optimize(f, target, weights);
            }
        };
        LinearFunction fWithCustomInitialization = new LinearFunction(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
        double[] targetWithCustomInitialization = new double[]{5.0, 6.0};
        double[] weightsWithCustomInitialization = new double[]{1.0, 1.0};
        double[] startPointWithCustomInitialization = new double[]{2.0, 2.0};

        PointValuePair resultWithCustomInitialization = optimizerWithCustomInitialization.optimize(fWithCustomInitialization, targetWithCustomInitialization, weightsWithCustomInitialization, startPointWithCustomInitialization);
        assertEquals(5.0, resultWithCustomInitialization.getPoint()[0], 1e-6);
        assertEquals(5.0, resultWithCustomInitialization.getValue(), 1e-6);
        assertEquals(6.0, resultWithCustomInitialization.getPoint()[1], 1e-6);
        assertEquals(6.0, resultWithCustomInitialization.getValue(), 1e-6);
    }

}