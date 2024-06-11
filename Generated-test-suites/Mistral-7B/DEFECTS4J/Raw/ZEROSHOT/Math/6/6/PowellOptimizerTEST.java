package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);

        // Test with a univariate function.
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * Math.pow(Math.E, x);
            }
        };

        double[] startPoint = {0.0};
        PointValuePair result = optimizer.optimize(f, startPoint);

        assertEquals(0.0, result.getPoint()[0], 1e-8);
        assertEquals(-2.449489742748192, result.getValue(), 1e-8);

        // Test with a multivariate function.
        MultivariateFunction g = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += Math.pow(x[i], 2);
                }
                return sum;
            }

            @Override
            public int getNumberOfVariables() {
                return x.length;
            }
        };

        double[] startPointM = {1.0, 2.0};
        PointValuePair resultM = optimizer.optimize(g, startPointM);

        assertEquals(3.0, resultM.getPoint()[0], 1e-8);
        assertEquals(11.0, resultM.getPoint()[1], 1e-8);
        assertEquals(14.0, resultM.getValue(), 1e-8);

        // Test with a custom convergence checker.
        ConvergenceChecker<PointValuePair> customChecker = (iter, previous, current) -> {
            double diff = FastMath.abs(current.getValue() - previous.getValue());
            return diff < 1e-8;
        };

        PowellOptimizer optimizerCustom = new PowellOptimizer(1e-6, 1e-6, customChecker);
        UnivariateFunction fCustom = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };

        double[] startPointCustom = {1.0};
        PointValuePair resultCustom = optimizerCustom.optimize(fCustom, startPointCustom);

        assertEquals(1.273205345519235, resultCustom.getPoint()[0], 1e-6);
        assertEquals(0.420356157556994, resultCustom.getValue(), 1e-6);
    }
}