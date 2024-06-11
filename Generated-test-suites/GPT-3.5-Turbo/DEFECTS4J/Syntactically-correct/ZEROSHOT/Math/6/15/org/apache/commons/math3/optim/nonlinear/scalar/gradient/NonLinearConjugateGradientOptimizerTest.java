import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;


public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testSetInitialStep() {
        double initialStep = 0.5;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setInitialStep(initialStep);
        assertEquals(initialStep, optimizer.getInitialStep(), 0);
    }

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
        optimizer.setConvergenceChecker(new SimpleValueChecker());
        optimizer.setMaxEvaluations(1000);

        double[] startPoint = {2.0, 2.0};
        double[] expectedPoint = {1.0, 1.0};
        GoalType goal = GoalType.MINIMIZE;

        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(goal);

        PointValuePair result = optimizer.doOptimize();

        double[] actualPoint = result.getPoint();
        for (int i = 0; i < actualPoint.length; i++) {
            assertEquals(expectedPoint[i], actualPoint[i], 0.001);
        }
    }

    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4.0;
            }
        };

        double a = 0.0;
        double h = 1.0;

        double upperBound = optimizer.findUpperBound(f, a, h);
        assertEquals(3.0, upperBound, 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundThrowsMathIllegalStateException() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x + 4.0;
            }
        };

        double a = 0.0;
        double h = 1.0;

        optimizer.findUpperBound(f, a, h);
    }
}