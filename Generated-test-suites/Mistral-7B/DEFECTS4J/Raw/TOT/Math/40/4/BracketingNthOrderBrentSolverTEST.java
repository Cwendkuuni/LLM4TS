package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolverWithDefaultParameters() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = f -> Math.pow(Math.sin(x), 2);
        double root = Math.PI / 2;
        double[] inputs = {-2.0, -1.0, 1.0, 2.0};
        double[] expectedOutputs = {1.0, 0.0, 1.0, 1.0};
        for (int i = 0; i < inputs.length; i++) {
            assertEquals(root, solver.solve(100, function, inputs[i], 4.0, AllowedSolution.ANY_SIDE), 1e-6);
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolverWithLowerMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1, 1);
        UnivariateFunction function = x -> Math.exp(x) - 2;
        double root = Math.log(2);
        solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithNegativeRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(-0.1, 0.0, 1, 3);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithNegativeAbsoluteAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.0, -1e-6, 1, 3);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithNegativeFunctionValueAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0.1, 1e-6, -1e-6, 3);
    }

    @Test
    public void testSolverWithSpecifiedAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        UnivariateFunction function = x -> Math.pow(x, 2) - 2;
        double root = -1.0;
        double input = 0.0;
        assertEquals(root, solver.solve(100, function, input, 4.0, AllowedSolution.LEFT_SIDE), 1e-6);
    }

    @Test
    public void testSolverWithUserDefinedInterval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        UnivariateFunction function = x -> Math.exp(x) - 2;
        double root = Math.log(2);
        double inputMin = -2.0;
        double inputMax = 2.0;
        assertEquals(root, solver.solve(100, function, inputMin, inputMax, AllowedSolution.ANY_SIDE), 1e-6);
    }

    @Test
    public void testSolverWithUserDefinedStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 3);
        UnivariateFunction function = x -> Math.sin(x) * Math.cos(x) - 1;
        double root = Math.PI / 4;
        double input = 0.0;
        assertEquals(root, solver.solve(100, function, input, 2.0 * Math.PI, AllowedSolution.ANY_SIDE), 1e-6);
    }

}