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
import org.apache.commons.math3.optimization.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.ConjugateGradientFormula;
import org.apache.commons.math3.optimization.Preconditioner;
import org.apache.commons.math3.util.FastMath;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NonLinearConjugateGradientOptimizerTest {

    private NonLinearConjugateGradientOptimizer optimizer;
    private UnivariateFunction function;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES,
                new SimpleValueChecker(), new BrentSolver(), null);
        function = f -> Math.pow(f, 2) + 1;
        startPoint = new double[]{1.0, 1.0};
    }

    @Test
    public void testOptimize_Minimization() {
        double expectedResult = 0.0;
        PointValuePair result = optimizer.optimize(function, startPoint, GoalType.MINIMIZE);
        assertEquals(expectedResult, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testOptimize_Maximization() {
        double expectedResult = 2.0;
        optimizer.setGoalType(GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(function, startPoint);
        assertEquals(expectedResult, result.getValue(), 0.001);
        assertArrayEquals(startPoint, result.getPoint(), 0.001);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testOptimize_IllegalState() {
        optimizer.optimize(null, null, GoalType.MINIMIZE);
    }

    @Test
    public void testSetInitialStep() {
        optimizer.setInitialStep(2.0);
        assertEquals(2.0, optimizer.getInitialStep(), 0.001);
    }

    @Test
    public void testSetInitialStep_Negative() {
        optimizer.setInitialStep(-1.0);
        assertEquals(1.0, optimizer.getInitialStep(), 0.001);
    }

}

class TestConjugateGradientFormula implements UnivariateFunction {
    @Override
    public double value(double x) {
        return x * x;
    }
}

class TestPreconditioner implements Preconditioner {
    @Override
    public double[] precondition(double[] variables, double[] r) {
        double[] result = new double[variables.length];
        for (int i = 0; i < variables.length; i++) {
            result[i] = r[i] / 2.0;
        }
        return result;
    }
}

class TestConvergenceChecker implements ConvergenceChecker<PointValuePair> {
    @Override
    public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
        return current.getValue() < 0.001;
    }
}