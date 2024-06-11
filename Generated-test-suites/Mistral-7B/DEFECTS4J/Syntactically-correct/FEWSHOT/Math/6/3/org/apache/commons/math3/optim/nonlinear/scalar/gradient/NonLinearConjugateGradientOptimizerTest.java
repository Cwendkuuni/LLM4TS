package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.Arrays;
import org.junit.Test;

/**
 * Test cases for {@link NonLinearConjugateGradientOptimizer} class.
 */
public class NonLinearConjugateGradientOptimizerTest {
    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula);
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertNull(optimizer.getSolver());
        assertNull(optimizer.getPreconditioner());
    }

    /**
     * Test the constructor with default solver and preconditioner.
     */
    @Test
    public void testConstructorWithDefaultSolverAndPreconditioner() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker);
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(BrentSolver.class, optimizer.getSolver().getClass());
        assertNull(optimizer.getPreconditioner());
    }

    /**
     * Test the constructor with custom solver and preconditioner.
     */
    @Test
    public void testConstructorWithCustomSolverAndPreconditioner() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        UnivariateSolver solver = mock(UnivariateSolver.class);
        Preconditioner preconditioner = mock(Preconditioner.class);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);
        assertEquals(updateFormula, optimizer.getUpdateFormula());
        assertEquals(solver.getClass(), optimizer.getSolver().getClass());
        assertEquals(preconditioner.getClass(), optimizer.getPreconditioner().getClass());
    }

    /**
     * Test the setInitialStep method.
     */
    @Test
    public void testSetInitialStep() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker);
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep());

        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep());
    }

    /**
     * Test the doOptimize method with a simple test function.
     */
    @Test
    public void testOptimizeSimpleFunction() {
        final double eps = 1e-6;

        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver solver = mock(BrentSolver.class);
        Preconditioner preconditioner = mock(Preconditioner.class);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);

        double[] startPoint = {1.0, 1.0};
        double[] grad = {2.0, 2.0};
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(startPoint)).thenReturn(3.0);
        when(function.value(startPoint[0] + grad[0], startPoint[1] + grad[1])).thenReturn(1.0);
        when(function.gradient(startPoint)).thenReturn(grad);

        optimizer.optimize(function, startPoint, GoalType.MINIMIZE);

        double[] result = optimizer.getPoint();
        assertEquals(Arrays.asList(1.3247119253f, 0.6623559626f), Arrays.asList(result[0], result[1]));
        assertEquals(1.5194732e-4, Precision.doubleValue(optimizer.getObjectiveValue()), eps);
    }

    /**
     * Test the doOptimize method with a non-convergent function.
     */
    @Test
    public void testOptimizeNonConvergentFunction() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        UnivariateSolver solver = mock(BrentSolver.class);
        Preconditioner preconditioner = mock(Preconditioner.class);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);

        double[] startPoint = {0.0, 0.0};
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(startPoint)).thenReturn(0.0);
        when(function.gradient(startPoint)).thenReturn(new double[] {1.0, 1.0});

        assertThrows(MathIllegalStateException.class, () -> optimizer.optimize(function, startPoint, GoalType.MINIMIZE));
    }

    /**
     * Test the doOptimize method with a function that only brackets the optimum.
     */
    @Test
    public void testOptimizeFunctionThatOnlyBracketsTheOptimum() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        UnivariateSolver solver = mock(BrentSolver.class);
        Preconditioner preconditioner = mock(Preconditioner.class);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);

        double[] startPoint = {0.0, 0.0};
        double[] bracketedPoints = {-1.0, 1.0, 0.0};
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(startPoint)).thenReturn(0.0);
        when(function.gradient(startPoint)).thenReturn(new double[] {1.0, 1.0});
        when(function.value(bracketedPoints[0])).thenReturn(Double.POSITIVE_INFINITY);
        when(function.value(bracketedPoints[1])).thenReturn(0.0);

        optimizer.optimize(function, startPoint, GoalType.MINIMIZE);
        PointValuePair result = optimizer.getPoint();
        assertEquals(Arrays.asList(0.0, 0.0), Arrays.asList(result.getPoint()[0], result.getPoint()[1]));
        assertEquals(0.0, Precision.doubleValue(optimizer.getObjectiveValue()), eps);

        // Check that the optimizer didn't throw an exception.
    }

    /**
     * Test the doOptimize method with a function that is not differentiable.
     */
    @Test
    public void testOptimizeFunctionThatIsNotDifferentiable() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        UnivariateSolver solver = mock(BrentSolver.class);
        Preconditioner preconditioner = mock(Preconditioner.class);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);

        double[] startPoint = {0.0, 0.0};
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(startPoint)).thenReturn(0.0);
        when(function.gradient(startPoint)).thenThrow(new UnsupportedOperationException());

        assertThrows(UnsupportedOperationException.class, () -> optimizer.optimize(function, startPoint, GoalType.MINIMIZE));
    }

    /**
     * Test the doOptimize method with a function that has a maximum number of evaluations.
     */
    @Test
    public void testOptimizeFunctionWithMaxEvaluations() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        UnivariateSolver solver = mock(BrentSolver.class);
        Preconditioner preconditioner = mock(Preconditioner.class);
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, solver, preconditioner);

        double[] startPoint = {0.0, 0.0};
        UnivariateFunction function = mock(UnivariateFunction.class);
        when(function.value(startPoint)).thenReturn(0.0);
        when(function.gradient(startPoint)).thenReturn(new double[] {1.0, 1.0});

        optimizer.setMaxEvaluations(1);

        assertThrows(MaxCountExceededException.class, () -> optimizer.optimize(function, startPoint, GoalType.MINIMIZE));
    }
}