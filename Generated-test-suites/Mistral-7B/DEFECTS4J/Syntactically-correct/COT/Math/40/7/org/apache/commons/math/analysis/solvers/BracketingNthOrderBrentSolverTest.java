import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 3);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-6, 3);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracyAndFunctionValueAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 1e-6, 1e-7, 3);
        assertEquals(1e-5, solver.getRelativeAccuracy(), 0);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0);
        assertEquals(1e-7, solver.getFunctionValueAccuracy(), 0);
        assertEquals(3, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithMaximalOrderLowerThan2() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testGetMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolveWithMinMaxAndFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(50, function, -2, 2);
        assertEquals(0, root, 0.0001);
    }

    @Test
    public void testSolveWithMinMaxAndStartValueAndFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double root = solver.solve(50, function, 0, 2, 1);
        assertEquals(1, root, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateFunction function = null;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(50, function, 0, 0);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketingFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 1.5;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(50, function, -2, 1.5);
    }

}