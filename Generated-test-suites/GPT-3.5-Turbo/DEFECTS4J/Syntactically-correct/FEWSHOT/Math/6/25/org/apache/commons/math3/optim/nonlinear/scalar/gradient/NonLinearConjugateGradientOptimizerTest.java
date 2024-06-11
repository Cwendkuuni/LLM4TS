package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer.ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker();
        UnivariateSolver lineSearchSolver = new BrentSolver();
        NonLinearConjugateGradientOptimizer.Preconditioner preconditioner = new NonLinearConjugateGradientOptimizer.IdentityPreconditioner();
        NonLinearConjugateGradientOptimizer.ConjugateGradientFormula updateFormula = NonLinearConjugateGradientOptimizer.ConjugateGradientFormula.FLETCHER_REEVES;

        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(updateFormula, convergenceChecker, lineSearchSolver, preconditioner);
        optimizer.setMaxEvaluations(1000);
        optimizer.setInitialStep(1.0);

        double[] startPoint = {0, 0};
        GoalType goalType = GoalType.MINIMIZE;

        MultivariateFunction objectiveFunction = new QuadraticFunction(new double[]{3.0, 4.0});

        PointValuePair result = optimizer.optimize(new MaxEval(100), new ObjectiveFunction(objectiveFunction), goalType,
                                                   new InitialGuess(startPoint),
                                                   new SimpleBounds(new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                                                                    new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}),
                                                   new CMAESOptimizer.PopulationSize(5));

        assertNotNull(result);
        double[] optimum = result.getPoint();
        assertEquals(0, optimum[0], 1e-5);
        assertEquals(0, optimum[1], 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }
    
    @Test
    public void testFindUpperBound() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(null);
        UnivariateFunction f = new QuadraticFunction(1, 0, 0);
        double a = -10;
        double h = 1;
        
        double b = optimizer.findUpperBound(f, a, h);
        
        assertEquals(10, b, 1e-5);
    }
    
    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundNegativeIteration() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(null);
        UnivariateFunction f = new QuadraticFunction(1, 0, 0);
        double a = -10;
        // This causes the loop in findUpperBound to run indefinitely
        double h = Double.POSITIVE_INFINITY;
        
        optimizer.findUpperBound(f, a, h);
    }
    
    @Test(expected = MathIllegalStateException.class)
    public void testFindUpperBoundUnableToBracketOptimumInLineSearch() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(null);
        UnivariateFunction f = new QuadraticFunction(1, 0, 0);
        // The function is missing its minimum due to only requiring f(a) and f(b)
        double a = Double.NEGATIVE_INFINITY;
        double h = 1;
        
        optimizer.findUpperBound(f, a, h);
    }
    
    private static class QuadraticFunction implements MultivariateFunction {
        private final double[] coefficients;
        
        public QuadraticFunction(double... coefficients) {
            this.coefficients = coefficients;
        }
        
        public double value(double[] point) {
            double sum = 0;
            for (int i = 0; i < point.length; i++) {
                sum += coefficients[i] * point[i] * point[i];
            }
            return sum;
        }
    }
}