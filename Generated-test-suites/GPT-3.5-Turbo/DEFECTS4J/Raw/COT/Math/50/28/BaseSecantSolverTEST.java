
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver baseSecantSolver;

    @Before
    public void setup() {
        baseSecantSolver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {

            @Override
            protected double computeObjectiveValue(double x) {
                return 0;
            }

            @Override
            protected void verifyBracketing(double lower, double upper) throws NoBracketingException {
                if (lower >= upper) {
                    throw new NoBracketingException(lower, upper, lower >= upper);
                }
            }
        };
    }

    @Test
    public void testSolve() {
        double result = baseSecantSolver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double v) {
                return v;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        Assert.assertEquals(0, result, 0.0001d);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketingException() {
        baseSecantSolver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double v) {
                return v;
            }
        }, 10, -10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testSolveWithMathInternalError() {
        baseSecantSolver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double v) {
                return v;
            }
        }, 0, 0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }
}
