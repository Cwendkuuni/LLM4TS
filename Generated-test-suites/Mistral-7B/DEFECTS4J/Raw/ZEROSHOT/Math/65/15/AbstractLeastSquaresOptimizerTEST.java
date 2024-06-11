import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairImpl;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction testFunction;
    private double[] testTargetValues;
    private double[] testWeights;
    private double[] testStartPoint;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                updateResidualsAndCost();
                incrementIterationsCounter();
                return new PointValuePairImpl<>(point, objective);
            }
        };

        testFunction = new MultivariateVectorFunction() {
            @Override
            public double value(double[] x) {
                // Implement the test function here
                return 0;
            }

            @Override
            public double[] gradient(double[] x) {
                // Implement the test function gradient here
                return new double[0];
            }
        };

        testTargetValues = new double[]{1.0, 2.0, 3.0};
        testWeights = new double[]{1.0, 1.0, 1.0};
        testStartPoint = new double[]{0.0, 0.0, 0.0};
    }

    @Test
    public void testOptimize() throws Exception {
        ObjectiveFunction objectiveFunction = new LinearObjectiveFunction(testFunction, testTargetValues);
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair solution = optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);

        LinearConstraint[] linearConstraints = new LinearConstraint[0];
        FibonacciSearch fibonacciSearch = new FibonacciSearch(objectiveFunction, linearConstraints, goalType);
        PointValuePair fibonacciSolution = fibonacciSearch.findMinimum();

        Assert.assertArrayEquals(solution.getPoint(), fibonacciSolution.getPoint(), 0.001);
        Assert.assertEquals(solution.getFunValue(), fibonacciSolution.getFunValue(), 0.001);
    }

    @Test(expected = UnboundedDomainException.class)
    public void testOptimizeWithUnboundedDomain() throws Exception {
        testFunction = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 1.0 / x[0];
            }
        };
        optimizer.optimize(testFunction, new double[0], new double[0], new double[]{0.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidStartPoint() throws Exception {
        testFunction = new MultivariateVectorFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[0];
            }
        };
        testStartPoint = new double[1];
        optimizer.optimize(testFunction, testTargetValues, testWeights, testStartPoint);
    }

}