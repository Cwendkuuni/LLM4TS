package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Null;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setup() {
        ConjugateGradientFormula updateFormula = ConjugateGradientFormula.FLETCHER_REEVES;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        Preconditioner preconditioner = new IdentityPreconditioner();
        optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, checker, lineSearchSolver, preconditioner);
    }

    @Test
    public void testSetInitialStep() {
        double initialStep = 0.5;
        optimizer.setInitialStep(initialStep);
        assertEquals(initialStep, optimizer.getInitialStep(), 0.0);
    }

    @Test
    public void testDoOptimize() {
        double[] startPoint = {1.0, 2.0, 3.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(new SimpleValueChecker());
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }

    @Test
    public void testPrecondition() {
        Preconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        double[] variables = {1.0, 2.0, 3.0};
        double[] r = {2.0, 3.0, 4.0};
        double[] preconditioned = preconditioner.precondition(variables, r);
        assertNotNull(preconditioned);
        assertArrayEquals(preconditioned, r, 0.0);
    }

    @Test
    public void testLineSearchFunction() {
        double[] searchDirection = {1.0, 2.0, 3.0};
        NonLinearConjugateGradientOptimizer.LineSearchFunction lineSearchFunction =
            optimizer.new LineSearchFunction(searchDirection);

        double x = 1.0;
        double expectedValue = searchDirection[0] + 2*searchDirection[1] + 3*searchDirection[2];
        assertEquals(expectedValue, lineSearchFunction.value(x), 0.0);
    }
}