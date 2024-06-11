package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.function.UnivariateFunctionAdapter;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BracketedBrentSolver;
import org.apache.commons.math3.solver.PowellSolver;
import org.apache.commons.math3.solver.SimpleBrentSolver;

/**
 * Test class for {@link UnivariateRealSolverUtils}.
 */
public class UnivariateRealSolverUtilsTest {

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double)}.
     */
    @org.junit.Test
    public void testSolve() throws Exception {
        UnivariateFunction f = new UnivariateFunctionAdapter(x -> Math.pow(Math.E, x) - 2);
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 2.0);
        assertEquals("Solve should return a root of the function.", 1.718281828, result, 1e-12);
        
        UnivariateFunction fNegative = new UnivariateFunctionAdapter(x -> -Math.pow(Math.E, x) + 2);
        result = UnivariateRealSolverUtils.solve(fNegative, 0.0, 1.0);
        assertEquals("Solve should return a root of the function.", -1.718281828, result, 1e-12);
        
        UnivariateFunction fDegenerate = new UnivariateFunctionAdapter(x -> x * x);
        assertThrows(NoRootsException.class, () -> UnivariateRealSolverUtils.solve(fDegenerate, 0.0, 0.0));
        
        UnivariateFunction fNaN = new UnivariateFunctionAdapter(x -> Double.NaN);
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(fNaN, 0.0, 0.0));
        
        UnivariateFunction fNull = null;
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(fNull, 0.0, 0.0));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#solve(UnivariateRealFunction, double, double, double)}.
     */
    @org.junit.Test
    public void testSolveWithAbsoluteAccuracy() throws Exception {
        UnivariateFunction f = new UnivariateFunctionAdapter(x -> Math.pow(Math.E, x) - 2);
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 2.0, 1e-12);
        assertEquals("Solve should return a root of the function with absolute accuracy.", 1.718281828, result, 1e-13);
        
        UnivariateFunction fNegative = new UnivariateFunctionAdapter(x -> -Math.pow(Math.E, x) + 2);
        result = UnivariateRealSolverUtils.solve(fNegative, 0.0, 1.0, 1e-12);
        assertEquals("Solve should return a root of the function with absolute accuracy.", -1.718281828, result, 1e-13);
        
        UnivariateFunction fDegenerate = new UnivariateFunctionAdapter(x -> x * x);
        assertThrows(NoRootsException.class, () -> UnivariateRealSolverUtils.solve(fDegenerate, 0.0, 0.0, 0.0));
        
        UnivariateFunction fNaN = new UnivariateFunctionAdapter(x -> Double.NaN);
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.solve(fNaN, 0.0, 0.0, 0.0));
        
        UnivariateFunction fNull = null;
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.solve(fNull, 0.0, 0.0, 0.0));
        
        UnivariateFunction fZero = new UnivariateFunctionAdapter(x -> 0.0);
        double zero = UnivariateRealSolverUtils.solve(fZero, 0.0, 0.0, 0.0);
        assertEquals("Solve should return zero with absolute accuracy.", 0.0, zero, 1e-13);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double)}.
     */
    @org.junit.Test
    public void testBracket() throws Exception {
        UnivariateFunction f = new UnivariateFunctionAdapter(x -> Math.pow(Math.E, x) - 2);
        double[] result = UnivariateRealSolverUtils.bracket(f, 1.0, 1.0, 2.0);
        double[] expected = new double[]{1.2, 1.8};
        assertArrayEquals("Bracket should return an interval bracketing a root.", expected, result);
        
        UnivariateFunction fNegative = new UnivariateFunctionAdapter(x -> -Math.pow(Math.E, x) + 2);
        result = UnivariateRealSolverUtils.bracket(fNegative, 0.0, 0.0, 1.0);
        expected = new double[]{-1.2, -0.8};
        assertArrayEquals("Bracket should return an interval bracketing a root.", expected, result);
        
        UnivariateFunction fDegenerate = new UnivariateFunctionAdapter(x -> x * x);
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(fDegenerate, 0.0, 0.0, 0.0));
        
        UnivariateFunction fNaN = new UnivariateFunctionAdapter(x -> Double.NaN);
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(fNaN, 0.0, 0.0, 0.0));
        
        UnivariateFunction fNull = null;
        assertThrows(MathIllegalArgumentException.class, () -> UnivariateRealSolverUtils.bracket(fNull, 0.0, 0.0, 0.0));
        
        UnivariateFunction fZero = new UnivariateFunctionAdapter(x -> 0.0);
        result = UnivariateRealSolverUtils.bracket(fZero, 0.0, 0.0, 0.0);
        assertEquals("Bracket should return an interval bracketing zero.", new double[]{0.0, 0.0}, result);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#bracket(UnivariateRealFunction, double, double, double, int)}.
     */
    @org.junit.Test
    public void testBracketWithMaxIterations() throws Exception {
        UnivariateFunction f = new UnivariateFunctionAdapter(x -> Math.pow(Math.E, x) - 2);
        double[] result = UnivariateRealSolverUtils.bracket(f, 1.0, 1.0, 2.0, 100);
        double[] expected = new double[]{1.2, 1.8};
        assertArrayEquals("Bracket with a maximum number of iterations should return an interval bracketing a root.", expected, result);
        
        UnivariateFunction fNegative = new UnivariateFunctionAdapter(x -> -Math.pow(Math.E, x) + 2);
        result = UnivariateRealSolverUtils.bracket(fNegative, 0.0, 0.0, 1.0, 100);
        double maxIterations = 101;
        expected = new double[]{-1.2, -0.8};
        assertArrayEquals("Bracket with a maximum number of iterations should return an interval bracketing a root.", expected, result);
        
        UnivariateFunction fDegenerate = new UnivariateFunctionAdapter(x -> x * x);
        assertThrows(ConvergenceException.class, () -> UnivariateRealSolverUtils.bracket(fDegenerate, 0.0, 0.0, 0.0, 1));
        
        UnivariateFunction fNaN = new UnivariateFunctionAdapter(x -> Double.NaN);
        assertThrows(FunctionEvaluationException.class, () -> UnivariateRealSolverUtils.bracket(fNaN, 0.0, 0.0, 0.0, 1));
        
        UnivariateFunction fZero = new UnivariateFunctionAdapter(x -> 0.0);
        result = UnivariateRealSolverUtils.bracket(fZero, 0.0, 0.0, 0.0, 1);
        assertEquals("Bracket with a maximum number of iterations should return an interval bracketing zero.", new double[]{0.0, 0.0}, result);
        
        UnivariateFunction fMaxIterations = new UnivariateFunctionAdapter(x -> {
            throw new MaxIterationsExceededException(maxIterations);
        });
        assertThrows(MaxIterationsExceededException.class, () -> UnivariateRealSolverUtils.bracket(fMaxIterations, 0.0, 1.0, 1.0, 1));
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#midpoint(double, double)}.
     */
    @org.junit.Test
    public void testMidpoint() throws Exception {
        double a = 1.0;
        double b = 2.0;
        double expected = 1.5;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals("Midpoint should return the average of a and b.", expected, result, 1e-12);
    }

    /**
     * Test method for {@link UnivariateRealSolverUtils#setup(UnivariateRealFunction)}.
     */
    @org.junit.Test
    public void testSetup() throws Exception {
        UnivariateFunction f = new UnivariateFunctionAdapter(x -> Math.pow(Math.E, x) - 2);
        UnivariateRealSolverUtils.setup(f);
    }

}

class UnivariateFunctionAdapter implements UnivariateRealFunction {

    private final UnivariateFunction function;

    public UnivariateFunctionAdapter(UnivariateFunction function) {
        this.function = function;
    }

    @Override
    public double value(double x) throws FunctionEvaluationException {
        return function.value(x);
    }

}

class UnivariateRealSolverFactoryAdapter implements UnivariateRealSolverFactory {

    private final UnivariateRealSolverFactory factory;

    public UnivariateRealSolverFactoryAdapter(UnivariateRealSolverFactory factory) {
        this.factory = factory;
    }

    @Override
    public UnivariateRealSolver newBrentSolver() {
        return factory.newBrentSolver();
    }

    @Override
    public UnivariateRealSolver newBracketedBrentSolver() {
        return factory.newBracketedBrentSolver();
    }

    @Override
    public UnivariateRealSolver newPowellSolver() {
        return factory.newPowellSolver();
    }

    @Override
    public UnivariateRealSolver newSimpleBrentSolver() {
        return factory.newSimpleBrentSolver();
    }

}