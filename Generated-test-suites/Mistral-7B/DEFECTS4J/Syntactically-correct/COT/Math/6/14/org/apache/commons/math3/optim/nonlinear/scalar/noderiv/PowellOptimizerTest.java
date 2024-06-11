import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinConstraintViolationException;
import org.apache.commons.math3.optim.nonlinear.scalar.multivariate.AbstractMultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.multivariate.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.multivariate.NonNegativeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.multivariate.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.multivariate.SimpleMultivariateObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.multivariate.SimpleMultivariateValueChecker;
import org.apache.commons.math3.optim.points.PointValuePairList;
import org.apache.commons.math3.optim.points.RealVector;
import org.junit.Test;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithRelativeAndAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0E-6, 1.0E-6);
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithCustomConvergenceChecker() {
        SimpleMultivariateValueChecker checker = new SimpleMultivariateValueChecker(1.0E-6, 1.0E-6);
        PowellOptimizer optimizer = new PowellOptimizer(1.0E-6, 1.0E-6, checker);
        assertNotNull(optimizer);
        assertSame(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeBasic() throws OptimaException {
        MultivariateFunction function = new SimpleMultivariateObjectiveFunction(new double[][]{{-1.0, -1.0}, {0.0, 0.0}}, 1.0);
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair solution = optimizer.optimize(function);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() throws OptimaException {
        SimpleMultivariateValueChecker checker = new SimpleMultivariateValueChecker(1.0E-6, 1.0E-6);
        MultivariateFunction function = new SimpleMultivariateObjectiveFunction(new double[][]{{-1.0, -1.0}, {0.0, 0.0}}, 1.0);
        PowellOptimizer optimizer = new PowellOptimizer(1.0E-6, 1.0E-6, checker);
        PointValuePair solution = optimizer.optimize(function);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithLinearConstraints() throws OptimaException, LinConstraintViolationException {
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, 1.0, 1.0, Relationship.LEQ));
        constraints.addConstraint(new LinearConstraint(1.0, 2.0, 1.0, Relationship.LEQ));

        MultivariateFunction function = new SimpleMultivariateObjectiveFunction(new double[][]{{-1.0, -1.0}, {0.0, 0.0}}, 1.0);
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair solution = optimizer.optimize(function, constraints);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertTrue(constraints.isSatisfied(solution.getPoint()));
    }

    @Test
    public void testOptimizeWithCustomConvergenceCheckerAndLinearConstraints() throws OptimaException, LinConstraintViolationException {
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(1.0, 1.0, 1.0, Relationship.LEQ));
        constraints.addConstraint(new LinearConstraint(1.0, 2.0, 1.0, Relationship.LEQ));

        SimpleMultivariateValueChecker checker = new SimpleMultivariateValueChecker(1.0E-6, 1.0E-6);
        MultivariateFunction function = new SimpleMultivariateObjectiveFunction(new double[][]{{-1.0, -1.0}, {0.0, 0.0}}, 1.0);
        PowellOptimizer optimizer = new PowellOptimizer(1.0E-6, 1.0E-6, checker);
        PointValuePair solution = optimizer.optimize(function, constraints);
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 1e-6);
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertTrue(constraints.isSatisfied(solution.getPoint()));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0E-6, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.0, 1.0E-6);
    }

    @Test
    public void testGetGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer();
        GoalType goalType = optimizer.getGoalType();
        assertEquals(GoalType.MINIMIZE, goalType);
    }

    @Test
    public void testGetStartPoint() {
        PowellOptimizer optimizer = new PowellOptimizer();
        double[] startPoint = optimizer.getStartPoint();
        assertNotNull(startPoint);
    }

    @Test
    public void testComputeObjectiveValue() throws OptimaException {
        MultivariateFunction function = new SimpleMultivariateObjectiveFunction(new double[][]{{-1.0, -1.0}, {0.0, 0.0}}, 1.0);
        PowellOptimizer optimizer = new PowellOptimizer();
        double value = optimizer.computeObjectiveValue(new double[]{0.0, 0.0}, function);
        assertEquals(1.0, value, 1e-6);
    }

    @Test
    public void testNewPointAndDirection() {
        double[] p = new double[]{0.0, 0.0};
        double[] d = new double[]{1.0, 1.0};
        double optimum = 1.0;
        double[][] newPointAndDirection = optimizer.newPointAndDirection(p, d, optimum);
        double[] newPoint = newPointAndDirection[0];
        double[] newDirection = newPointAndDirection[1];
        assertEquals(new double[]{1.0, 1.0}, newPoint, 1e-6);
        assertEquals(new double[]{2.0, 2.0}, newDirection, 1e-6);
    }
}