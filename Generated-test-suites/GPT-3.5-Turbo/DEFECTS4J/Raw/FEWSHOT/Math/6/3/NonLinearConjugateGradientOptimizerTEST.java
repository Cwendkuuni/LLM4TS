
package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.junit.Before;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
    }

    @Test
    public void testDoOptimize() {
        optimizer.setConvergenceChecker(new SimpleValueChecker(1e-5, 1e-5));
        optimizer.setMaxEvaluations(1000);

        UnivariateFunction univariateFunction = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };

        double[] initialPoint = {0};

        PointValuePair result = optimizer.optimize(100, univariateFunction, GoalType.MINIMIZE, initialPoint);

        assertTrue(optimizer.getStartPoint() == initialPoint);
        assertEquals(-Math.PI / 2, result.getPoint()[0], 1e-5);
        assertEquals(-1, result.getValue(), 1e-5);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBound() {
        UnivariateFunction univariateFunction = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        optimizer.findUpperBound(univariateFunction, 1, 1);
    }

    @Test
    public void testIdentityPreconditioner() {
        NonLinearConjugateGradientOptimizer.IdentityPreconditioner preconditioner =
                new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();

        double[] variables = {1, 2, 3, 4};
        double[] r = {1, 2, 3, 4};

        double[] preconditionedR = preconditioner.precondition(variables, r);

        assertTrue(preconditionedR != r);
        for (int i = 0; i < r.length; i++) {
            assertEquals(r[i], preconditionedR[i], 1e-5);
        }
    }

    @Test
    public void testLineSearchFunction() {
        NonLinearConjugateGradientOptimizer optimizer =
                new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());

        double[] point = {1, 2, 3};
        double[] searchDirection = {1, 2, 3};

        NonLinearConjugateGradientOptimizer.LineSearchFunction lineSearchFunction =
                optimizer.new LineSearchFunction(searchDirection);

        double value = lineSearchFunction.value(2);

        assertEquals(18, value, 1e-5);
    }
}
