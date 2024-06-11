import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Assert;
import org.junit.Test;

public class PowellOptimizerTest {
    @Test
    public void testConstructor() {
        double rel = 1e-6;
        double abs = 1e-12;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < abs;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, checker);

        Assert.assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        Assert.assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelTooSmall() {
        double rel = 1e-20;
        double abs = 1e-12;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithAbsNotStrictlyPositive() {
        double rel = 1e-6;
        double abs = 0;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
    }
    
    @Test
    public void testConstructorWithLineTolerances() {
        double rel = 1e-6;
        double abs = 1e-12;
        double lineRel = 1e-4;
        double lineAbs = 1e-8;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < abs;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, lineRel, lineAbs, checker);

        Assert.assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        Assert.assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
        Assert.assertEquals(lineRel, optimizer.getLine().getRelativeThreshold(), 0);
        Assert.assertEquals(lineAbs, optimizer.getLine().getAbsoluteThreshold(), 0);
    }
    
    @Test
    public void testDefaultConstructor() {
        double rel = 1e-6;
        double abs = 1e-12;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);

        Assert.assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        Assert.assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
    }
    
    @Test
    public void testDoOptimize() {
        double rel = 1e-6;
        double abs = 1e-12;
        double lineRel = 1e-4;
        double lineAbs = 1e-8;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return Math.abs(current.getValue() - previous.getValue()) < abs;
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, lineRel, lineAbs, checker);

        GoalType goal = GoalType.MINIMIZE;
        double[] startPoint = {0, 0, 0};
        PointValuePair result = optimizer.optimize(goal, new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1] + x[2] * x[2];
            }
        }, goal, startPoint);

        Assert.assertEquals(0, result.getPoint()[0], 0);
        Assert.assertEquals(0, result.getPoint()[1], 0);
        Assert.assertEquals(0, result.getPoint()[2], 0);
        Assert.assertEquals(0, result.getValue(), 0);
    }
}