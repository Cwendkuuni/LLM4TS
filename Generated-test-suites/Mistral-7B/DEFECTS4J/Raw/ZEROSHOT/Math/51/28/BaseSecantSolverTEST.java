import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        // Test with default constructor
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);

        // Test with constructor with absolute accuracy
        BaseSecantSolver solverAbs = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solverAbs);
        assertEquals(0.001, solverAbs.getAbsoluteAccuracy(), 0.0);

        // Test with constructor with relative accuracy
        BaseSecantSolver solverRel = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solverRel);
        assertEquals(0.01, solverRel.getRelativeAccuracy(), 0.0);

        // Test with constructor with all arguments
        BaseSecantSolver solverAll = new BaseSecantSolver(0.01, 0.001, 1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solverAll);
        assertEquals(0.01, solverAll.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, solverAll.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-6, solverAll.getFunctionValueAccuracy(), 0.0);

        // Test with different methods
        BaseSecantSolver solverRegulaFalsi = new BaseSecantSolver(0.01, BaseSecantSolver.Method.REGULA_FALSI);
        BaseSecantSolver solverIllinois = new BaseSecantSolver(0.01, BaseSecantSolver.Method.ILLINOIS);
        BaseSecantSolver solverPegasus = new BaseSecantSolver(0.01, BaseSecantSolver.Method.PEGASUS);

        // Test solve method with default bounds and function
        UnivariateRealFunction f = x -> Math.pow(x, 2) - 4;
        try {
            double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0, root, 0.001);
        } catch (NoBracketingException e) {
            fail("Method should find solution within bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        // Test solve method with different bounds and function
        try {
            double root = solver.solve(10, f, -5.0, 1.0, AllowedSolution.LEFT_SIDE);
            fail("Method should not find solution within given bounds, as the function is always positive in this interval");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            double root = solver.solve(10, f, 1.0, 5.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(3.0, root, 0.001);
        } catch (NoBracketingException e) {
            fail("Method should find solution within given bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        // Test allowed solutions
        try {
            double root = solverRel.solve(10, f, -2.0, 2.0, AllowedSolution.BELOW_SIDE);
            assertEquals(2.0, root, 0.001);
        } catch (NoBracketingException e) {
            fail("Method should find solution within given bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        try {
            double root = solverRel.solve(10, f, -2.0, 2.0, AllowedSolution.ABOVE_SIDE);
            fail("Method should not find solution within given bounds, as the function is always negative in this interval");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test different methods
        try {
            double rootRegulaFalsi = solverRegulaFalsi.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0, rootRegulaFalsi, 0.001);
        } catch (NoBracketingException e) {
            fail("Method should find solution within bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        try {
            double rootIllinois = solverIllinois.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0, rootIllinois, 0.001);
        } catch (NoBracketingException e) {
            fail("Method should find solution within bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        try {
            double rootPegasus = solverPegasus.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0, rootPegasus, 0.001);
        } catch (NoBracketingException e) {
            fail("Method should find solution within bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        // Test with very small relative accuracy
        BaseSecantSolver solverSmallRel = new BaseSecantSolver(1e-10, BaseSecantSolver.Method.ILLINOIS);
        try {
            double rootSmallRel = solverSmallRel.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0, rootSmallRel, 1e-10);
        } catch (NoBracketingException e) {
            fail("Method should find solution within bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        // Test with very large absolute accuracy
        BaseSecantSolver solverLargeAbs = new BaseSecantSolver(10000.0, BaseSecantSolver.Method.ILLINOIS);
        try {
            double rootLargeAbs = solverLargeAbs.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0, rootLargeAbs, 10000.0);
        } catch (NoBracketingException e) {
            fail("Method should find solution within bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        // Test with very large function value accuracy
        BaseSecantSolver solverLargeFVal = new BaseSecantSolver(0.01, 10000.0, BaseSecantSolver.Method.ILLINOIS);
        try {
            double rootLargeFVal = solverLargeFVal.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0, rootLargeFVal, 10000.0);
        } catch (NoBracketingException e) {
            fail("Method should find solution within bounds");
        } catch (MaxEvalException e) {
            fail("Method should find solution within given number of evaluations");
        }

        // Test with very large max evaluations
        BaseSecantSolver solverLargeMaxEval = new BaseSecantSolver(0.01, 0.001, 100000, BaseSecantSolver.Method.ILLINOIS);
        try {
            double rootLargeMaxEval = solverLargeMaxEval.solve(100000, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(1.0, rootLargeMaxEval, 0.001);
        } catch (NoBracketingException e) {
            fail("Method should find solution within bounds");
        } catch (MaxEvalException e) {
            fail("Method should not throw MaxEvalException as it was able to find the solution within given number of evaluations");
        }

        // Test with very small min and max bounds
        BaseSecantSolver solverSmallBounds = new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.ILLINOIS);
        try {
            double rootSmallBounds = solverSmallBounds.solve(10, f, -1e-6, 1e-6, AllowedSolution.ANY_SIDE);
            fail("Method should throw NoBracketingException as the bounds do not bracket the root");
        } catch (NoBracketingException e) {
            // Expected
        }

        // Test with very large min bound
        BaseSecantSolver solverLargeMin = new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.ILLINOIS);
        try {
            double rootLargeMin = solverLargeMin.solve(10, f, Double.POSITIVE_INFINITY, 2.0, AllowedSolution.ANY_SIDE);
            fail("Method should throw IllegalArgumentException as the minimum bound cannot be positive infinity");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with very large max bound
        BaseSecantSolver solverLargeMax = new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.ILLINOIS);
        try {
            double rootLargeMax = solverLargeMax.solve(10, f, -2.0, Double.POSITIVE_INFINITY, AllowedSolution.ANY_SIDE);
            fail("Method should throw IllegalArgumentException as the maximum bound cannot be positive infinity");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}