package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.function.ParametricUnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NoDerivativeOptimizer;
import org.apache.commons.math3.optim.points.PointList;
import org.apache.commons.math3.optim.points.WeightedObservedPoint;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class GaussianFitterTest {
    /**
     * The optimizer to use for testing.
     */
    private UnivariateOptimizer optimizer;

    /**
     * Prepares the test environment.
     */
    @Before
    public void setUp() {
        this.optimizer = new NoDerivativeOptimizer(new SimpleBounds(0.0, Double.POSITIVE_INFINITY), MaxEval.EVEN_ODD);
    }

    /**
     * Tests the fitting of a Gaussian function to the specified points.
     */
    @Test
    public void testFit() {
        double[] xData = new double[] { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] yData = new double[] { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < observations.length; i++) {
            observations[i] = new WeightedObservedPoint(xData[i], yData[i]);
        }

        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(observations);

        double[] params = fitter.fit();

        ParametricUnivariateRealFunction gaussian = new Gaussian.Parametric();
        PointValuePair result = gaussian.findRoot(new ObjectiveFunction() {
            private final double[] params;

            public GaussianFitterTest(double[] params) {
                this.params = params;
            }

            public double value(double x) {
                gaussian.setParameters(params);
                return Math.pow(gaussian.value(x), 2) - 1;
            }

            public InitialGuess getInitialGuess() {
                return null;
            }
        }, new InitialGuess() {
            public double getInitialX() {
                return 0.0;
            }
        }, new SimpleBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), GoalType.MILP, 1e-6);

        assertEquals("The fitted Gaussian function should have the same root as the initial Gaussian function.", 0.0, result.getPoint().getX(), 1e-6);

        double[] expectedParams = new double[] { params[0], params[1], params[2] };
        assertEquals("The fitted Gaussian function should have the correct parameters.", Arrays.toString(expectedParams), Arrays.toString(params));

        PointList points = new PointList(xData.length);
        for (int i = 0; i < xData.length; i++) {
            points.add(xData[i]);
            points.setValue(i, gaussian.value(xData[i], params));
        }

        double[] expectedYData = new double[] { 532173.418222155, 990759.365837814, 1895515.541684435, 2689452.389121366, 3463094.17674767, 3582374.193050198, 3438339.113253119, 2877094.780404875, 2175396.795508373, 1446707.339428325, 716622.958606183, 620060.886320206 };
        for (int i = 0; i < xData.length; i++) {
            assertEquals("The fitted Gaussian function should match the observed data.", expectedYData[i], points.getValue(i), 1e-6);
        }
    }
}