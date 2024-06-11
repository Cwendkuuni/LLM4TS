package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                // TODO: Implement computeObjectiveValue method
                return 0;
            }
            @Override
            protected void verifyBracketing(double lower, double upper) throws IllegalArgumentException {
                // TODO: Implement verifyBracketing method
            }
        };
    }

    @Test
    public void testSolve_withMaxEvalAndFunction() {
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                // TODO: Implement function
                return 0;
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);

        // TODO: Implement test
    }

    @Test
    public void testSolve_withMaxEvalFunctionAndAllowedSolution() {
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                // TODO: Implement function
                return 0;
            }
        };
        double min = 0;
        double max = 10;
        double startValue = 5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(maxEval, f, min, max, startValue);

        // TODO: Implement test
    }

    @Test
    public void testSolve_withMaxEvalFunctionMinAndMax() {
        int maxEval = 100;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                // TODO: Implement function
                return 0;
            }
        };
        double min = 0;
        double max = 10;

        double result = solver.solve(maxEval, f, min, max);

        // TODO: Implement test
    }

    @Test
    public void testDoSolve() {
        double x = solver.doSolve();

        // TODO: Implement test
    }
}