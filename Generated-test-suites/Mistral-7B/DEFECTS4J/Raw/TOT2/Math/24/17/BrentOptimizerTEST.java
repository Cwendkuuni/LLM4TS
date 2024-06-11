import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialization.Averaged;
import org.apache.commons.math3.optim.initialization.OptimizationPoint;
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0);
        UnivariateFunction function = new QuadraticFunction(-1.0, 1.0, 1.0);

        OptimizationPoint opt = optimizer.optimize(function, new Averaged(new OptimizationPoint(0.5)), GoalType.MINIMIZE);
        double expected = 0.0;
        assertEquals(expected, opt.getPoint(), 1e-8);
        assertEquals(expected, optimizer.getValue(opt), 1e-8);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0);
        UnivariateFunction function = new QuadraticFunction(-1.0, 1.0, -1.0);

        OptimizationPoint opt = optimizer.optimize(function, new Averaged(new OptimizationPoint(0.5)), GoalType.MAXIMIZE);
        double expected = 1.0;
        assertEquals(expected, opt.getPoint(), 1e-8);
        assertEquals(expected, optimizer.getValue(opt), -1e-8);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.0, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidRelativeThreshold() {
        new BrentOptimizer(0.0, 1e-10, null);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0.001);
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker(10);
        optimizer.setConvergenceChecker(checker);

        UnivariateFunction function = new QuadraticFunction(-1.0, 1.0, 1.0);
        OptimizationPoint opt = optimizer.optimize(function, new Averaged(new OptimizationPoint(0.5)), GoalType.MINIMIZE);

        assertTrue(checker.wasCalled());
    }
}

class QuadraticFunction implements UnivariateFunction {
    private final double a;
    private final double b;
    private final double c;

    public QuadraticFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double value(double x) {
        return a * x * x + b * x + c;
    }
}

class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
    private final int limit;

    public MyConvergenceChecker(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
        if (iteration > limit) {
            return true;
        }
        return false;
    }
}