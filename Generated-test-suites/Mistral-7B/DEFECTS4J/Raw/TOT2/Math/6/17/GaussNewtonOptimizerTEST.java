import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.functions.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.functions.SimpleFunctions;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.initializers.RandomInitializer;
import org.apache.commons.math3.optim.initializers.StartPoint;
import org.apache.commons.math3.optim.nonlinear.gradient.Gradient;
import org.apache.commons.math3.optim.nonlinear.gradient.SimpleGradient;
import org.apache.commons.math3.optim.nonlinear.linear.GoalType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet.ConstraintType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction.ValueType;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.maximization.MaxEvalPointValuePair;
import org.apache.commons.math3.optim.maximization.Maximizer;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintSetFiller;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunctionFiller;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintSetFiller.ConstraintTypeAndDirection;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunctionFiller.ValueTypeAndDirection;
import org.apache.commons.math3.optim.nonlinear.vector.linear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.linear.GaussNewtonOptimizer.LinearConstraintSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test constructor with default settings
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Test optimize method with a simple linear function
        LinearFunction f = new LinearFunction(new double[]{-1.0, 1.0}, new double[]{1.0, -2.0});
        PointValuePair initialPoint = new PointValuePair(new double[]{3.0, 3.0}, 9.0);
        optimizer.optimize(f, initialPoint.getPoint(), null);
        double[] expectedSolution = new double[]{1.0, -1.0};
        assertArrayEquals(expectedSolution, optimizer.getPoint(), 0.001);

        // Test optimize method with a weight matrix
        double[] targetValues = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        optimizer = new GaussNewtonOptimizer();
        optimizer.setWeight(new DiagonalRealMatrix(weights));
        PointValuePair initialPoint2 = new PointValuePair(new double[]{1.0, 1.0, 1.0}, 3.0);
        optimizer.optimize(f, initialPoint2.getPoint(), null);
        assertArrayEquals(expectedSolution, optimizer.getPoint(), 0.001);

        // Test optimize method with a convergence checker
        ConvergenceChecker<PointValuePair> checker = new MaxIter(100);
        optimizer = new GaussNewtonOptimizer(checker);
        optimizer.optimize(f, initialPoint.getPoint(), checker);
        assertTrue(checker.isConverged());

        // Test optimize method with a custom start point and convergence checker
        StartPoint startPoint = new RandomInitializer(2).getInitialPoint(f);
        optimizer = new GaussNewtonOptimizer(checker);
        optimizer.setStartPoint(startPoint);
        optimizer.optimize(f, null, checker);
        assertTrue(checker.isConverged());

        // Test optimize method with a non-null gradient
        Gradient g = new SimpleGradient(f);
        optimizer = new GaussNewtonOptimizer(g);
        optimizer.optimize(f, initialPoint.getPoint(), null);
        assertArrayEquals(expectedSolution, optimizer.getPoint(), 0.001);

        // Test optimize method with linear constraints
        LinearObjectiveFunction f2 = new LinearObjectiveFunction(new double[]{-1.0, 1.0},
                new double[]{1.0, -2.0}, ValueType.MINIMIZE);

        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, 0.0}, 1.0, ConstraintType.EQUAL);
        LinearConstraint c2 = new LinearConstraint(new double[]{0.0, 1.0}, 1.0, ConstraintType.EQUAL);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.add(c1);
        constraints.add(c2);

        LinearConstraintSetFiller constraintFiller = new LinearConstraintSetFiller();
        LinearObjectiveFunction linearFunction = constraintFiller.fill(f2, constraints);
        LinearConstraintSet optimizerConstraints = optimizer.getConstraintSet();
        optimizerConstraints.add(c1);
        optimizerConstraints.add(c2);

        optimizer = new GaussNewtonOptimizer(linearFunction, optimizerConstraints);
        optimizer.optimize(linearFunction, initialPoint.getPoint(), null);
        assertArrayEquals(expectedSolution, optimizer.getPoint(), 0.001);

        // Test optimize method with valueType and direction
        LinearObjectiveFunctionFiller objectiveFunctionFiller = new LinearObjectiveFunctionFiller();
        LinearObjectiveFunction linearObjectiveFunction = objectiveFunctionFiller.fill(f2, ValueType.MINIMIZE, null);
        ValueTypeAndDirection valueTypeAndDirection = new ValueTypeAndDirection(ValueType.MINIMIZE, GoalType.MINIMIZE);
        optimizer = new GaussNewtonOptimizer(linearObjectiveFunction, valueTypeAndDirection);
        optimizer.optimize(linearObjectiveFunction, initialPoint.getPoint(), null);
        assertArrayEquals(expectedSolution, optimizer.getPoint(), 0.001);

        // Test optimize method with constraintType and direction
        LinearConstraintSetFiller constraintSetFiller = new LinearConstraintSetFiller();
        LinearConstraintSet linearConstraints = constraintSetFiller.fill(new LinearConstraintSet(), ConstraintType.EQUAL, null);
        ConstraintTypeAndDirection constraintTypeAndDirection = new ConstraintTypeAndDirection(ConstraintType.EQUAL, GoalType.MINIMIZE);
        optimizer = new GaussNewtonOptimizer(linearObjectiveFunction, linearConstraints, constraintTypeAndDirection);
        optimizer.optimize(linearObjectiveFunction, initialPoint.getPoint(), null);
        assertArrayEquals(expectedSolution, optimizer.getPoint(), 0.001);

        // Test the exception handling
        try {
            optimizer = new GaussNewtonOptimizer(null);
            optimizer.optimize(f, initialPoint.getPoint(), null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(null, new SimpleVectorValueChecker());
            optimizer.optimize(f, initialPoint.getPoint(), null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(new LinearObjectiveFunction(new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, ValueType.MAXIMIZE));
            optimizer.optimize(f, initialPoint.getPoint(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(f, initialPoint.getPoint(), null);
            optimizer.optimize(null, null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(f, initialPoint.getPoint(), null);
            optimizer.optimize(f, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(f, initialPoint.getPoint(), checker);
            optimizer.optimize(null, null, null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(f, initialPoint.getPoint(), checker);
            optimizer.optimize(f, initialPoint.getPoint(), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(f, initialPoint.getPoint(), checker);
            optimizer.optimize(f, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(f, initialPoint.getPoint(), checker);
            optimizer.optimize(f, initialPoint.getPoint(), null);
            optimizer.optimize(f, initialPoint.getPoint(), null);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        try {
            optimizer = new GaussNewtonOptimizer(f, initialPoint.getPoint(), checker);
            Maximizer<MaxEvalPointValuePair> maximizer = new BrentMaximizer(checker);
            MaxEval maxEval = new MaxEval(f, null, goalType);
            MaxEvalPointValuePair maxEvalPointValuePair = maximizer.optimize(maxEval);
            optimizer.optimize(maxEval, maxEvalPointValuePair.getPoint(), null);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizerWithSingularMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, -2.0}, ValueType.MINIMIZE);
        PointValuePair initialPoint = new PointValuePair(new double[]{0.0, 0.0}, 0.0);

        try {
            optimizer.optimize(f, initialPoint.getPoint(), null);
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }
}