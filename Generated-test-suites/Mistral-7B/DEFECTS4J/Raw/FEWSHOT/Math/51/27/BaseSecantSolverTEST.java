package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MaxEvalException;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.Rational;
import org.apache.commons.commons.math.util.RationalFunction;
import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedRealSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.RegulaFalsiSolver;
import org.apache.commons.cli.IllinoisSolver;
import org.apache.commons.cli.PegasusSolver;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedRealSolver solver = new RegulaFalsiSolver(1.0e-6, new RegulaFalsiSolver.Method());
        double root = solver.solve(0, function, -5.0, 5.0, 2.0);
        assertEquals(2.0, root, 1.0e-6);
        try {
            solver.solve(0, function, 5.0, 5.0, 2.0);
            fail();
        } catch (MaxEvalException e) {
            // expected
        }
    }

    @Test
    public void testIllinois() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedRealSolver solver = new IllinoisSolver(1.0e-6, new IllinoisSolver.Method());
        double root = solver.solve(0, function, -5.0, 5.0, 2.0);
        assertEquals(2.0, root, 1.0e-6);
        try {
            solver.solve(0, function, 5.0, 5.0, 2.0);
            fail();
        } catch (MaxEvalException e) {
            // expected
        }
    }

    @Test
    public void testPegasus() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedRealSolver solver = new PegasusSolver(1.0e-6, new PegasusSolver.Method());
        double root = solver.solve(0, function, -5.0, 5.0, 2.0);
        assertEquals(2.0, root, 1.0e-6);
        try {
            solver.solve(0, function, 5.0, 5.0, 2.0);
            fail();
        } catch (MaxEvalException e) {
            // expected
        }

        // test with rational function
        UnivariateRealFunction rationalFunction = new RationalFunction( new Rational(3, 1), new Rational(4, 1) );
        BracketedRealSolver solverRational = new PegasusSolver(1.0e-6, new PegasusSolver.Method());
        double rootRational = solverRational.solve(0, rationalFunction, -10.0, 10.0, new Rational(3, 4));
        assertEquals(Rational.ONE, new Rational(3, 4), rootRational);

        UnivariateRealFunction rationalFunctionNegative = new RationalFunction( new Rational(3, 1), new Rational(4, 1) );
        BracketedRealSolver solverRationalNegative = new PegasusSolver(1.0e-6, new PegasusSolver.Method());
        double rootRationalNegative = solverRationalNegative.solve(0, rationalFunctionNegative, -10.0, 10.0, new Rational(-3, 4));
        assertEquals(-Rational.ONE, new Rational(-3, 4), rootRationalNegative);
    }

    @Test
    public void testRegulaFalsiAllowedSolution() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6, new RegulaFalsiSolver.Method(), AllowedSolution.LEFT_SIDE);
        double root = solver.solve(0, function, -5.0, 5.0, 2.0);
        assertEquals(2.0, root, 1.0e-6);
        try {
            solver.solve(0, function, 5.0, 5.0, 2.0);
            fail();
        } catch (MaxEvalException e) {
            // expected
        }
    }

    @Test
    public void testIllinoisAllowedSolution() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new IllinoisSolver(1.0e-6, new IllinoisSolver.Method(), AllowedSolution.RIGHT_SIDE);
        double root = solver.solve(0, function, -5.0, 5.0, 2.0);
        assertEquals(2.0, root, 1.0e-6);
        try {
            solver.solve(0, function, 5.0, 5.0, 2.0);
            fail();
        } catch (MaxEvalException e) {
            // expected
        }
    }

    @Test
    public void testPegasusAllowedSolution() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new PegasusSolver(1.0e-6, new PegasusSolver.Method(), AllowedSolution.ABOVE_SIDE);
        double root = solver.solve(0, function, -5.0, 5.0, 2.0);
        assertEquals(2.0, root, 1.0e-6);
        try {
            solver.solve(0, function, 5.0, 5.0, 2.0);
            fail();
        } catch (MaxEvalException e) {
            // expected
        }

        UnivariateRealFunction functionNegative = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedUnivariateRealSolver<UnivariateRealFunction> solverNegative = new PegasusSolver(1.0e-6, new PegasusSolver.Method(), AllowedSolution.BELOW_SIDE);
        double rootNegative = solverNegative.solve(0, functionNegative, -5.0, 5.0, 2.0);
        assertEquals(-2.0, rootNegative, 1.0e-6);
        try {
            solverNegative.solve(0, functionNegative, 5.0, 5.0, 2.0);
            fail();
        } catch (MaxEvalException e) {
            // expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverNullFunction() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6, new RegulaFalsiSolver.Method());
        solver.solve(0, null, -5.0, 5.0, 2.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverNullStartValue() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6, new RegulaFalsiSolver.Method());
        solver.solve(0, function, -5.0, 5.0, null);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverInvalidStartValue() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{-1.0, 1.0, -1.0}, 3 );
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6, new RegulaFalsiSolver.Method());
        solver.solve(0, function, -5.0, 5.0, 5.0);
    }

    @Test(expected = MathInternalError.class)
    public void testSolverError() {
        UnivariateRealFunction function = new PolynomialFunction( new double[]{Double.NaN, 1.0, -1.0}, 3 );
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1.0e-6, new RegulaFalsiSolver.Method());
        solver.solve(0, function, -5.0, 5.0, 2.0);
    }
}