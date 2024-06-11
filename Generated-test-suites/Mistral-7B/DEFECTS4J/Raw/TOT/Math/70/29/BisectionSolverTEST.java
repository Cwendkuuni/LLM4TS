package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.function.Add;
import org.apache.commons.math3.analysis.function.Constant;
import org.apache.commons.math3.analysis.function.Divide;
import org.apache.commons.math3.analysis.function.Multiply;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.analysis.function.Subtract;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootsException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverWithSimpleFunction() {
        UnivariateRealFunction f = new Add(new Subtract(new Constant(1.0), new Constant(2.0)),
                new Multiply(new Constant(3.0), new Pow(new Constant(x), new Constant(2.0))));
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, root, 1E-6);
    }

    @Test
    public void testBisectionSolverWithFunctionThatHasNoRealRoots() {
        UnivariateRealFunction f = new Subtract(new Constant(x), new Constant(1.0));
        assertThrows(NoRootsException.class, () -> solver.solve(f, -5.0, 5.0));
    }

    @Test
    public void testBisectionSolverWithFunctionThatHasMultipleRoots() {
        UnivariateRealFunction f = new Add(new Constant(x), new Multiply(new Constant(x), new Constant(x)));
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -5.0, 5.0));
    }

    @Test
    public void testBisectionSolverWithFunctionThatIsAlwaysPositive() {
        UnivariateRealFunction f = new Constant(1.0);
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(f, -10.0, 10.0));
    }

    @Test
    public void testBisectionSolverWithFunctionThatIsAlwaysNegative() {
        UnivariateRealFunction f = new Constant(-1.0);
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(f, -10.0, 10.0));
    }

    @Test
    public void testBisectionSolverWithFunctionThatIsAlwaysZero() {
        UnivariateRealFunction f = new Constant(0.0);
        double root = solver.solve(f, -10.0, 10.0);
        assertEquals(0.0, root, 1E-6);
    }

    @Test
    public void testBisectionSolverWithFunctionThatHasLargeRoot() {
        UnivariateRealFunction f = new Divide(new Constant(1.0), new Pow(new Constant(x), new Constant(2.0)));
        double root = solver.solve(f, 1.0, 1000.0);
        assertEquals(1.0, root, 1E-6);
    }

    @Test
    public void testBisectionSolverWithFunctionThatHasSmallRoot() {
        UnivariateRealFunction f = new Divide(new Constant(1.0), new Pow(new Constant(x), new Constant(8.0)));
        double root = solver.solve(f, 0.0, 1.0);
        assertEquals(0.5, root, 1E-6);
    }

    @Test
    public void testBisectionSolverWithFunctionThatHasDifferentSignsAtStartingPoints() {
        UnivariateRealFunction f = new Add(new Subtract(new Constant(1.0), new Constant(x)),
                new Multiply(new Constant(-1.0), new Pow(new Constant(x), new Constant(2.0))));
        double root = solver.solve(f, -3.0, 3.0);
        assertEquals(0.0, root, 1E-6);
    }
}