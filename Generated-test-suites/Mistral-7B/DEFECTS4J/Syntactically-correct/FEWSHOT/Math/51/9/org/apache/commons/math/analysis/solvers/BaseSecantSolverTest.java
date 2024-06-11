package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MaxEvalExceededException;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1.0e-6);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 1.0e-6);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }
    }

    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 1.0e-6);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }
    }

    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new PegasusSolver(1.0e-6);
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 1.0e-6);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }
    }

    @Test
    public void testSolve_MinMaxOutOfOrder() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, 3.0, -3.0, AllowedSolution.ANY_SIDE);
            fail("Expected MathIllegalArgumentException but got result: " + root);
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_RootInInterval() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 1.0e-6);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }
    }

    @Test
    public void testSolve_RootOutsideInterval() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, -5.0, 1.0, AllowedSolution.ANY_SIDE);
            fail("Expected MathIllegalArgumentException but got result: " + root);
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_FunctionValueBelowZero() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x + 2);

        try {
            double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError but got result: " + root);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            // Expected
        }
    }

    @Test
    public void testSolve_FunctionValueAboveZero() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x + 2);

        try {
            double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
            fail("Expected MathInternalError but got result: " + root);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            // Expected
        }
    }

    @Test
    public void testSolve_FunctionValueZero() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
            assertEquals(0.0, root, 1.0e-6);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }

        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(0.0, root, 1.0e-6);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }
    }

    @Test
    public void testSolve_FunctionValueZero_WithPrecision() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
            double expected = Precision.round(Math.sqrt(2.0), 6);
            assertEquals(expected, root, 1.0e-6);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }

        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
            double expected = Precision.round(Math.sqrt(2.0), 6);
            assertEquals(expected, root, 1.0e-6);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }
    }

    @Test
    public void testSolve_FunctionValueZero_WithCustomAccuracy() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-8);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.LEFT_SIDE);
            double expected = Precision.round(Math.sqrt(2.0), 8);
            assertEquals(expected, root, 1.0e-8);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }

        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.RIGHT_SIDE);
            double expected = Precision.round(Math.sqrt(2.0), 8);
            assertEquals(expected, root, 1.0e-8);
        } catch (MaxEvalExceededException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }
    }

    @Test
    public void testSolve_MaxEvalExceeded() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);

        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        try {
            solver.solve(1, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            fail("Expected MaxEvalExceededException but got no exception");
        } catch (MaxEvalExceededException e) {
            // Expected
        } catch (MathIllegalArgumentException e) {
            fail("Illegal argument: " + e.getMessage());
        } catch (MathInternalError e) {
            fail("Internal error: " + e.getMessage());
        }
    }

    @Test
    public void testAllowedSolution_AnySide() {
        BaseSecantSolver solver = new IllinoisSolver(1.0e-6);
        assertSame(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testAllowedSolution_LeftSide() {
        IllinoisSolver solver = new IllinoisSolver(1.0e-6, AllowedSolution.LEFT_SIDE);
        assertEquals(AllowedSolution.LEFT_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testAllowedSolution_RightSide() {
        IllinoisSolver solver = new IllinoisSolver(1.0e-6, AllowedSolution.RIGHT_SIDE);
        assertEquals(AllowedSolution.RIGHT_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testAllowedSolution_BelowSide() {
        IllinoisSolver solver = new IllinoisSolver(1.0e-6, AllowedSolution.BELOW_SIDE);
        assertEquals(AllowedSolution.BELOW_SIDE, solver.getAllowedSolution());
    }

    @Test
    public void testAllowedSolution_AboveSide() {
        IllinoisSolver solver = new IllinoisSolver(1.0e-6, AllowedSolution.ABOVE_SIDE);
        assertEquals(AllowedSolution.ABOVE_SIDE, solver.getAllowedSolution());
    }
}