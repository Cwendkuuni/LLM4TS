package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleLevenbergMarquardtOptimizer;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

import java.util.Arrays;

public class GaussianFitterTest {

    @Test
    public void testFit() throws NotStrictlyPositiveException {
        double[] dataX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408, 4.08614332};
        double[] dataY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0, 572125.0};

        GaussianFitter fitter = new GaussianFitter(new SimpleLevenbergMarquardtOptimizer());
        for (int i = 0; i < dataX.length; i++) {
            fitter.addObservedPoint(dataX[i], dataY[i]);
        }
        double[] parameters = fitter.fit();

        ParametricUnivariateRealFunction gaussianFunction = new Gaussian.Parametric();
        double xTest = 4.05;
        double expectedY = gaussianFunction.value(xTest, parameters);

        UnivariateFunction testFunction = new ParametricUnivariateRealFunction() {
            private final ParametricUnivariateRealFunction g = gaussianFunction;

            public double value(double x, double[] p) {
                return g.value(x, p);
            }
        };

        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            public double value(PointValuePair point) {
                return Math.pow(testFunction.value(point.getPoint()) - expectedY, 2);
            }
        };

        SimpleLevenbergMarquardtOptimizer optimizer = new SimpleLevenbergMarquardtOptimizer();
        optimizer.setMaxIterations(100);
        optimizer.setAbsoluteTolerance(1e-12);
        optimizer.setRelativeTolerance(1e-12);

        InitialGuess initialGuess = new InitialGuess(parameters);
        PointValuePair solution = optimizer.optimize(objectiveFunction, initialGuess, GoalType.MINIMIZE, null, null, new SimpleBounds(0.0, null), null);

        assertArrayEquals(parameters, solution.getPoint(), 0.001);
    }
}