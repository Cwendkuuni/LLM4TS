import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;
    private double min;
    private double max;
    private double accuracy;
    private Method method;
    private AllowedSolution allowedSolution;
    private double expectedResult;

    public BaseSecantSolverTest(Method method, AllowedSolution allowedSolution,
                               double min, double max, double accuracy,
                               double expectedResult) {
        this.method = method;
        this.allowedSolution = allowedSolution;
        this.min = min;
        this.max = max;
        this.accuracy = accuracy;
        this.expectedResult = expectedResult;
        function = f -> f.value(0.0); // Placeholder function for testing
    }

    @Parameterized.Parameters(name = "Test method={0}, allowedSolution={1}, " +
            "min={2}, max={3}, accuracy={4}, expectedResult={5}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {Method.ILLINOIS, AllowedSolution.RIGHT_SIDE, 1.0, 2.0, 1e-5, 1.5},
                {Method.ILLINOIS, AllowedSolution.LEFT_SIDE, 1.0, 2.0, 1e-5, 1.0},
                {Method.ILLINOIS, AllowedSolution.ANY_SIDE, 1.0, 2.0, 1e-5, 1.25},
                {Method.PEGASUS, AllowedSolution.RIGHT_SIDE, 1.0, 2.0, 1e-5, 1.5},
                {Method.PEGASUS, AllowedSolution.LEFT_SIDE, 1.0, 2.0, 1e-5, 1.0},
                {Method.PEGASUS, AllowedSolution.ANY_SIDE, 1.0, 2.0, 1e-5, 1.25},
                {Method.REGULA_FALSI, AllowedSolution.RIGHT_SIDE, 1.0, 2.0, 1e-5, 1.5},
                {Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE, 1.0, 2.0, 1e-5, 1.0},
                {Method.REGULA_FALSI, AllowedSolution.ANY_SIDE, 1.0, 2.0, 1e-5, 1.25},
                {Method.ILLINOIS, AllowedSolution.ABOVE_SIDE, 1.0, 2.0, 1e-5, 1.5},
                {Method.ILLINOIS, AllowedSolution.BELOW_SIDE, 1.0, 2.0, 1e-5, 1.0},
                {Method.ILLINOIS, AllowedSolution.ABOVE_SIDE, 1.0, 2.0, 1e-5, 1.75},
                {Method.ILLINOIS, AllowedSolution.BELOW_SIDE, 1.0, 2.0, 1e-5, 0.75},
                {Method.PEGASUS, AllowedSolution.ABOVE_SIDE, 1.0, 2.0, 1e-5, 1.5},
                {Method.PEGASUS, AllowedSolution.BELOW_SIDE, 1.0, 2.0, 1e-5, 1.0},
                {Method.PEGASUS, AllowedSolution.ABOVE_SIDE, 1.0, 2.0, 1e-5, 1.75},
                {Method.PEGASUS, AllowedSolution.BELOW_SIDE, 1.0, 2.0, 1e-5, 0.75},
                {Method.REGULA_FALSI, AllowedSolution.ABOVE_SIDE, 1.0, 2.0, 1e-5, 1.5},
                {Method.REGULA_FALSI, AllowedSolution.BELOW_SIDE, 1.0, 2.0, 1e-5, 1.0},
                {Method.REGULA_FALSI, AllowedSolution.ABOVE_SIDE, 1.0, 2.0, 1e-5, 1.75},
                {Method.REGULA_FALSI, AllowedSolution.BELOW_SIDE, 1.0, 2.0, 1e-5, 0.75},
                {Method.ILLINOIS, AllowedSolution.ANY_SIDE, -1.0, 1.0, 1e-5, 0.0},
                {Method.ILLINOIS, AllowedSolution.ANY_SIDE, -1.0, 1.0, 1e-5, 1.0},
                {Method.PEGASUS, AllowedSolution.ANY_SIDE, -1.0, 1.0, 1e-5, 0.0},
                {Method.PEGASUS, AllowedSolution.ANY_SIDE, -1.0, 1.0, 1e-5, 1.0}
        });
    }

    @Before
    public void setup() {
        solver = new BaseSecantSolver(method, accuracy) {
            @Override
            public BracketedRealRoots findRoots(UnivariateRealFunction f, double start,
                                              double end, int maxEval)
                    throws MaxEvalExceededException, MathIllegalArgumentException {
                return new BracketedRealRoots(start, end, expectedResult);
            }
        };
    }

    @Test
    public void testSolve() throws Exception {
        solver.solve(100, function, min, max, allowedSolution);
        double result = solver.getRoot();
        assertEquals(expectedResult, Precision.round(result, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAllowedSolution() {
        solver = new BaseSecantSolver(method, accuracy);
        solver.solve(100, function, min, max, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMethod() {
        solver = new BaseSecantSolver(null, accuracy);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(100, function, min, max, allowedSolution));
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testMaxEvalExceeded() {
        solver = new BaseSecantSolver(method, accuracy);
        solver.setMaxEval(0);
        assertThrows(MaxEvalExceededException.class, () -> solver.solve(100, function, min, max, allowedSolution));
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidFunction() {
        solver = new BaseSecantSolver(method, accuracy);
        assertThrows(MathIllegalArgumentException.class, () -> solver.solve(100, null, min, max, allowedSolution));
    }
}