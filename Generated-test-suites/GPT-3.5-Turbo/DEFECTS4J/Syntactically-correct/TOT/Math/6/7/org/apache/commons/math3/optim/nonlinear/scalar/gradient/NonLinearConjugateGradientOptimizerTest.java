import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NonLinearConjugateGradientOptimizerTest {

    private static final double EPSILON = 1e-6;

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(
                ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(),
                new BrentSolver(),
                new IdentityPreconditioner()
        );
    }

    @Test
    public void testDoOptimize_minimize() {
        double[] startPoint = {1, 1};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        PointValuePair optimum = optimizer.optimize();

        assertNotNull(optimum);
        assertEquals(0, optimum.getPoint()[0], EPSILON);
        assertEquals(0, optimum.getPoint()[1], EPSILON);
        assertEquals(0, optimum.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_maximize() {
        double[] startPoint = {1, 1};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        PointValuePair optimum = optimizer.optimize();

        assertNotNull(optimum);
        assertEquals(0, optimum.getPoint()[0], EPSILON);
        assertEquals(0, optimum.getPoint()[1], EPSILON);
        assertEquals(0, optimum.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_converged() {
        double[] startPoint = {1e-5, 1e-5};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-8, 1e-8);
        optimizer.setConvergenceChecker(checker);

        PointValuePair optimum = optimizer.optimize();

        assertNotNull(optimum);
        assertEquals(0, optimum.getPoint()[0], EPSILON);
        assertEquals(0, optimum.getPoint()[1], EPSILON);
        assertEquals(0, optimum.getValue(), EPSILON);
    }

    @Test
    public void testDoOptimize_maxEvaluations() {
        double[] startPoint = {1, 1};
        optimizer.setStartPoint(startPoint);
        optimizer.setMaxEvaluations(10);
        optimizer.setGoalType(GoalType.MINIMIZE);

        try {
            optimizer.optimize();
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.OPTIMIZATION_FAILED, e.getSpecifier());
        }
    }

    @Test
    public void testDoOptimize_lineSearchFailed() {
        double[] startPoint = {1, 1};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariateSolver lineSearchSolver = new UnivariateSolver() {
            public double solve(int maxEval, UnivariateFunction f, double min, double max) {
                throw new MathIllegalStateException(LocalizedFormats.UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH);
            }

            public int getEvaluations() {
                return 0;
            }
        };
        optimizer.setLineSearchSolver(lineSearchSolver);

        try {
            optimizer.optimize();
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.UNABLE_TO_BRACKET_OPTIMUM_IN_LINE_SEARCH, e.getSpecifier());
        }
    }

    @Test
    public void testFindUpperBound() {
        UnivariateFunction f = x -> x * x - 4;

        double a = 1;
        double h = 2;
        double upperBound = optimizer.findUpperBound(f, a, h);

        assertTrue(f.value(a) * f.value(upperBound) < 0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBound_noBracket() {
        UnivariateFunction f = Math::exp;

        double a = 1;
        double h = 0.1;
        optimizer.findUpperBound(f, a, h);
    }

}