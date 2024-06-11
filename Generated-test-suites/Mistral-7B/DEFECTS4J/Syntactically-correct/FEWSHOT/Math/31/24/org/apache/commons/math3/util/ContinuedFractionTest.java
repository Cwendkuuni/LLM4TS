package org.apache.commons.math3.util;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test(expected = ConvergenceException.class)
    public void testEvaluateSquareRootOfTwoConvergenceException() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        cf.evaluate(2.0, 1e-9, Integer.MAX_VALUE);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateSineContinuousFractionMaxCount() {
        ContinuedFraction cf = new SineContinuedFraction();
        cf.evaluate(0.0, 1e-9, 1);
    }

    @Test
    public void testEvaluateSquareRootOfTwo() {
        ContinuedFraction cf = new SquareRootContinuedFraction();
        assertEquals(Math.sqrt(2), cf.evaluate(2.0), 1e-6);
    }

    @Test
    public void testEvaluateSine() {
        ContinuedFraction cf = new SineContinuedFraction();
        assertEquals(Math.sin(Math.PI / 2), cf.evaluate(Math.PI / 2), 1e-6);
    }

    @Test
    public void testEvaluateCotangent() {
        ContinuedFraction cf = new CotangentContinuedFraction();
        assertEquals(1 / Math.tan(Math.PI / 4), cf.evaluate(Math.PI / 4), 1e-6);
    }

    @Test
    public void testEvaluateGauss() {
        ContinuedFraction cf = new GaussContinuedFraction();
        assertEquals(Math.exp(Math.PI / 2), cf.evaluate(0.0), 1e-6);
    }
}

class SquareRootContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return x / Math.sqrt(x);
        } else {
            return 1.0;
        }
    }

    @Override
    protected double getB(int n, double x) {
        if (n % 2 == 0) {
            return -1.0 / (2 * x);
        } else {
            return 1.0;
        }
    }
}

class SineContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return x;
        } else {
            return 1.0 / (2 * x + 1);
        }
    }

    @Override
    protected double getB(int n, double x) {
        if (n % 2 == 0) {
            return 1.0;
        } else {
            return -1.0 / (2 * x + 2);
        }
    }
}

class CotangentContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        if (n % 2 == 0) {
            return 1.0 / (x * Math.sqrt(1 + x * x));
        } else {
            return 1.0 / (x * (1 + x * x));
        }
    }

    @Override
    protected double getB(int n, double x) {
        if (n % 2 == 0) {
            return -1.0 / (2 * x);
        } else {
            return -1.0 / (2 * x + 1);
        }
    }
}

class GaussContinuedFraction extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
        return 1.0;
    }

    @Override
    protected double getB(int n, double x) {
        return 1.0 / ((1 + 2 * n) * x);
    }
}