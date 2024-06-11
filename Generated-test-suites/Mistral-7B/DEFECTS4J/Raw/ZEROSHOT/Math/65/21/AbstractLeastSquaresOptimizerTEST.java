import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleConstraints;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.VectorConstraintSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        Assert.assertArrayEquals(new int[]{100}, optimizer.getMaxIterations(), "Default max iterations");
        Assert.assertNull(optimizer.getConvergenceChecker(), "Default convergence checker");
        optimizer.setMaxIterations(5);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        Assert.assertEquals(5, optimizer.getMaxIterations(), "Set max iterations");
        Assert.assertNotNull(optimizer.getConvergenceChecker(), "Set convergence checker");

        double[] expectedPoint = {1.0, 2.0, 3.0};
        double[] expectedObjective = {1.5, 3.1, 4.2};
        double[] expectedResiduals = {0.5, 1.2, 1.8};
        double[] expectedWeights = {1.0, 2.0, 3.0};

        double[] startPoint = {0.0, 0.0, 0.0};

        DifferentiableMultivariateVectorialFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
            }

            @Override
            public double[] gradient(double[] x) {
                return new double[]{2.0 * x[0], 2.0 * x[1], 2.0 * x[2]};
            }
        };

        PointValuePair result = optimizer.optimize(function, expectedObjective, expectedWeights, startPoint);

        Assert.assertNotNull(result, "Optimization result should not be null");
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), "Expected point not matched");
        Assert.assertArrayEquals(expectedObjective, result.getValue(), "Expected objective not matched");

        optimizer.reset();

        // Test with constraints
        MultivariateFunction constraintFunction = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2) - 1;
            }
        };

        LinearConstraintSet linearConstraints = new VectorConstraintSet();
        LinearConstraint xConstraint = new LinearConstraint(1.0, 0);
        LinearConstraint yConstraint = new LinearConstraint(1.0, 1);
        linearConstraints.add(xConstraint);
        linearConstraints.add(yConstraint);

        LinearObjectiveFunction lof = new LinearObjectiveFunction(expectedObjective);

        ObjectiveFunction objectiveFunction = new LinearObjectiveFunction(expectedObjective);
        objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) throws Exception {
                return objectiveFunction.value(x) + Math.sqrt(100.0);
            }
        };

        SimpleConstraints simpleConstraints = new SimpleConstraints();
        simpleConstraints.add(new LinearConstraint(0.0, 0));
        simpleConstraints.add(new LinearConstraint(0.0, 1));
        simpleConstraints.add(new LinearConstraint(0.0, 2));

        PointValuePair resultWithConstraints = optimizer.optimize(objectiveFunction, lof, expectedWeights, startPoint,
                linearConstraints, simpleConstraints, GoalType.MINIMIZE);

        Assert.assertNotNull(resultWithConstraints, "Optimization result with constraints should not be null");
        Assert.assertArrayEquals(expectedPoint, resultWithConstraints.getPoint(), "Expected point with constraints not matched");
        Assert.assertArrayEquals(expectedObjective, resultWithConstraints.getValue(), "Expected objective with constraints not matched");
    }

}