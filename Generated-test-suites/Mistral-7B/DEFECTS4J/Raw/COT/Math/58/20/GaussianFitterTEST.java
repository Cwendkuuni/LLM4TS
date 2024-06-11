import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.ForglassLagariSolver;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.points.RealVector;
import org.apache.commons.math3.optim.points.SimpleVector;
import org.apache.commons.math3.optim.points.NonSmoothPoint;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

public class GaussianFitterTest {

    @Test
    public void testFit() throws Exception {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
            new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
            new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
            new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
            new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
            new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
            new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
            new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
            new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
            new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
            new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
            new WeightedObservedPoint(4.08366408, 620014.0, 1.0),
            new WeightedObservedPoint(4.08888889, 500000.0, 1.0), // Edge case
            new WeightedObservedPoint(4.0, 0.0, 1.0), // Zero point
            new WeightedObservedPoint(null, Double.NaN, 1.0) // Null point
        };

        GaussianFitter fitter = new GaussianFitter(new ForglassLagariSolver());
        fitter.addObservedPoints(observations);
        try {
            double[] result = fitter.fit();
            fail("Expected exception: " + NotStrictlyPositiveException.class.getName());
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }

        fitter = new GaussianFitter(new ForglassLagariSolver());
        fitter.addObservedPoints(observations);
        double[] initialGuess = { 1.0, 4.0, 1.0 };
        try {
            double[] result = fitter.fit(initialGuess);
            assertEquals(11, result.length);
            assertEquals(1.054132245551013, result[0], 1e-8);
            assertEquals(4.027354143130972, result[1], 1e-8);
            assertEquals(0.162720410888357, result[2], 1e-8);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getClass().getName());
        }

        PointValuePair<RealVector, RealVector> initialGuessLP = new PointValuePair<>(
            new SimpleVector(new double[] { 0.1, 1.0, 1.0 }),
            new LinearObjectiveFunction(new SimpleVector(new double[] { 1.0, 1.0, 1.0 })) {
                public double value(RealVector x) {
                    try {
                        double v = new Gaussian.Parametric().value(4.0, new double[] { 0.1, 1.0, 1.0 });
                        return v * v;
                    } catch (NotStrictlyPositiveException e) {
                        return Double.POSITIVE_INFINITY;
                    }
                }

                public RealVector gradient(RealVector x) {
                    try {
                        double[] g = new Gaussian.Parametric().gradient(4.0, new double[] { 0.1, 1.0, 1.0 });
                        return new SimpleVector(g);
                    } catch (NotStrictlyPositiveException e) {
                        return new SimpleVector(new double[] { Double.POSITIVE_INFINITY,
                                                               Double.POSITIVE_INFINITY,
                                                               Double.POSITIVE_INFINITY });
                    }
                }
            });

        OptimizationProblem problem = new OptimizationProblem(initialGuessLP, new GoalType() {});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[] { 1.0, 1.0, 1.0 }, 0.0, ""));
        problem.setConstraints(constraints);
        problem.setMaxIterations(1000);
        problem.setMaxEvaluations(10000);
        try {
            OptimizationResult result = new NonSmoothPoint(new SimpleVector(new double[] { 1.054132245551013,
                                                                                      4.027354143130972,
                                                                                      0.162720410888357 }))
                                                            .optimize(problem, new ForglassLagariSolver());
            assertEquals(ResultType.SOLVED, result.getResult());
            assertArrayEquals(new double[] { 1.054132245551013, 4.027354143130972, 0.162720410888357 },
                             result.getPoint().toArray(), 1e-8);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getClass().getName());
        }
    }
}