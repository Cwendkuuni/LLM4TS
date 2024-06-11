import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest extends SimpleOptimizationTest {

    @Test
    public void testMultiDirectional() throws Exception {
        MultiDirectional md = new MultiDirectional();

        // Test with a simple quadratic function
        ObjectiveFunction f = new QuadraticFunction(2.0, -1.0, 2.0);
        RealConvergenceChecker convergenceChecker = new RealConvergenceChecker(1.0e-6, 100);
        PointValuePair solution = md.optimize(f, new PointValuePair(new double[]{-1.0, -1.0}, 4.0), GoalType.MINIMIZE, null, convergenceChecker);
        assertEquals(-1.0, solution.getPoint()[0], 1e-6);
        assertEquals(-1.0, solution.getPoint()[1], 1e-6);

        // Test with a linear function
        ObjectiveFunction g = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-2.0, -3.0});
        PointValuePair linearSolution = md.optimize(g, new PointValuePair(new double[]{0.0, 0.0}, 0.0), GoalType.MINIMIZE, null, convergenceChecker);
        assertEquals(-2.0 / (1.0 + 2.0), linearSolution.getPoint()[0], 1e-6);
        assertEquals(-3.0 / (1.0 + 2.0), linearSolution.getPoint()[1], 1e-6);

        // Test with a linear function with constraints
        ObjectiveFunction h = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{3.0, 2.0});
        LinearConstraintSet constraints = new LinearConstraintSet(new LinearConstraint[] {
                new LinearConstraint(new double[]{1.0, 1.0}, Relationship.LEQ, 1.0),
                new LinearConstraint(new double[]{2.0, 1.0}, Relationship.LEQ, 1.0),
                new LinearConstraint(new double[]{1.0, 2.0}, Relationship.GEQ, 0.0)
        });
        PointValuePair linearWithConstraintsSolution = md.optimize(h, new PointValuePair(new double[]{0.0, 0.0}, 0.0), GoalType.MINIMIZE, constraints, convergenceChecker);
        assertEquals(0.0, linearWithConstraintsSolution.getPoint()[0], 1e-6);
        assertEquals(0.0, linearWithConstraintsSolution.getPoint()[1], 1e-6);
    }

}

class QuadraticFunction implements ObjectiveFunction {

    private final double a;
    private final double b;
    private final double c;

    public QuadraticFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double value(double[] point) {
        return a * Math.pow(point[0], 2) + b * point[0] * point[1] + c;
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

}