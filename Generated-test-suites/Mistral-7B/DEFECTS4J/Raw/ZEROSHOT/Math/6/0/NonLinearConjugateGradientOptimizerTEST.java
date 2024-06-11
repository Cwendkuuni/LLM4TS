package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.AbstractScalarDifferentiableOptimizer;
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.optimization.LineSearchFunction;
import org.apache.commons.math3.optimization.SimpleScalarFunction;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testNonLinearConjugateGradientOptimizer() {
        // Test constructor with default update formula, checker, line search solver and preconditioner.
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);

        // Test constructor with custom checker, line search solver and preconditioner.
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                // Define custom convergence condition.
                return current.getValue() < 0.001;
            }
        };
        NonLinearConjugateGradientOptimizer customOptimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, customChecker, new BrentSolver());

        // Test constructor with custom update formula, checker, line search solver and preconditioner.
        Preconditioner customPreconditioner = new Preconditioner() {
            @Override
            public double[] precondition(double[] variables, double[] r) {
                // Define custom preconditioner.
                double[] result = new double[variables.length];
                for (int i = 0; i < variables.length; i++) {
                    result[i] = variables[i] + r[i];
                }
                return result;
            }
        };
        NonLinearConjugateGradientOptimizer customPreconditionedOptimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), customPreconditioner);

        // Test setInitialStep method.
        optimizer.setInitialStep(2.0);

        // Test optimization method
        UnivariateFunction testFunction = new SimpleScalarFunction("testFunction", x -> Math.pow(Math.sin(x), 2) + Math.pow(x - 2.0, 2));
        PointValuePair result = optimizer.optimize(testFunction, GoalType.MINIMIZE);

        // Assert optimization result
        double expectedResult = 1.1283791670955126;
        assertEquals(expectedResult, result.getValue(), 0.0001);
        assertArrayEquals(new double[]{2.0}, result.getPoint(), 0.0001);

        // Test optimization method with custom checker
        PointValuePair customResult = customOptimizer.optimize(testFunction, GoalType.MINIMIZE);
        assertEquals(expectedResult, customResult.getValue(), 0.0001);
        assertArrayEquals(new double[]{2.0}, customResult.getPoint(), 0.0001);

        // Test optimization method with custom preconditioner
        PointValuePair customPreconditionedResult = customPreconditionedOptimizer.optimize(testFunction, GoalType.MINIMIZE);
        assertEquals(expectedResult, customPreconditionedResult.getValue(), 0.0001);
        assertArrayEquals(new double[]{2.0}, customPreconditionedResult.getPoint(), 0.0001);
    }
}