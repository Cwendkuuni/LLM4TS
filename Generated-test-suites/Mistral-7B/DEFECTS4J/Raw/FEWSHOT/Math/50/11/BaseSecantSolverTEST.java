package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.commons.math.analysis.FunctionUtils;
import org.apache.commons.commons.math.analysis.univariate.CubicPolynomial;
import org.apache.commons.commons.math.analysis.univariate.LinearFunction;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver.Method;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BisectionSolver(0.001, 0.01);
        UnivariateRealFunction function = new CubicPolynomial(-1.0, 2.0, 1.0, -1.0);
        double root = 1.0;
        assertEquals(root, solver.solve(function, -3.0, 3.0), 0.001);
    }

    @Test
    public void testIllinois() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
        UnivariateRealFunction function = new LinearFunction(1.0);
        double root = 0.0;
        assertEquals(root, solver.solve(function, -1.0, 1.0), 0.001);
    }

    @Test
    public void testPegasus() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, Method.PEGASUS);
        UnivariateRealFunction function = new CubicPolynomial(-1.0, 2.0, 1.0, -1.0);
        double root = 1.0;
        assertEquals(root, solver.solve(function, -3.0, 3.0), 0.001);
    }

    @Test(expected = MathInternalError.class)
    public void testUnsupportedMethod() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, Method.SECANT);
        UnivariateRealFunction function = new CubicPolynomial(-1.0, 2.0, 1.0, -1.0);
        double root = 1.0;
        solver.solve(function, -3.0, 3.0);
    }
}

class BisectionSolver extends BaseSecantSolver {

    public BisectionSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, 0.0, Method.REGULA_FALSI);
    }

    @Override
    protected double doSolve() {
        double x0 = getMin();
        double x1 = getMax();
        double f0 = computeObjectiveValue(x0);
        double f1 = computeObjectiveValue(x1);

        while (f0 * f1 > 0) {
            if (x1 - x0 < getAbsoluteAccuracy()) {
                return x0;
            }
            double x = (x0 + x1) / 2;
            double fx = computeObjectiveValue(x);
            if (fx > 0) {
                x0 = x;
                f0 = fx;
            } else {
                x1 = x;
            }
        }
        return x;
    }
}