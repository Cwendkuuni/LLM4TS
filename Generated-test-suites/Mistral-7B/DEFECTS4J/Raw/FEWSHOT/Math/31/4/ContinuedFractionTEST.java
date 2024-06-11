package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testDefaultConstructor() {
        new ContinuedFraction();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAUnsupported() {
        new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                throw new UnsupportedOperationException();
            }
        }.evaluate(0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBUnsupported() {
        new ContinuedFraction() {
            @Override
            protected double getB(int n, double x) {
                throw new UnsupportedOperationException();
            }
        }.evaluate(0.0);
    }

    @Test
    public void testEvaluateDefaultEpsilon() {
        ContinuedFraction cf = new MyContinuedFraction();
        assertEquals(3.0, cf.evaluate(0.0), 1e-9);
    }

    @Test
    public void testEvaluateDefaultEpsilonAndMaxIterations() {
        ContinuedFraction cf = new MyContinuedFraction();
        assertEquals(3.0, cf.evaluate(0.0, 1e-9, 100), 1e-9);
    }

    @Test
    public void testEvaluateCustomEpsilon() {
        ContinuedFraction cf = new MyContinuedFraction();
        assertEquals(3.0, cf.evaluate(0.0, 1e-12), 1e-12);
    }

    @Test
    public void testEvaluateCustomEpsilonAndMaxIterations() {
        ContinuedFraction cf = new MyContinuedFraction();
        assertEquals(3.0, cf.evaluate(0.0, 1e-12, 100), 1e-12);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction cf = new MyContinuedFractionConvergenceException();
        cf.evaluate(0.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededException() {
        ContinuedFraction cf = new MyContinuedFractionMaxCountExceededException();
        cf.evaluate(0.0, 1e-9, 1);
    }

    @Test
    public void testStripLeadingHyphensCustomFirstValue() {
        assertEquals("1", ContinuedFraction.stripLeadingHyphens("--1"));
        assertEquals("1", ContinuedFraction.stripLeadingHyphens("--1--"));
    }

    @Test
    public void testStripLeadingHyphensCustomNthValue() {
        assertEquals("2", ContinuedFraction.stripLeadingHyphens("1--2"));
        assertEquals("2", ContinuedFraction.stripLeadingHyphens("1--2--"));
    }

    @Test
    public void testStripLeadingHyphensNoLeadingHyphens() {
        assertEquals("1", ContinuedFraction.stripLeadingHyphens("1"));
    }

    @Test
    public void testStripLeadingHyphensNull() {
        assertNull(ContinuedFraction.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingQuotesCustomFirstValue() {
        assertEquals("1", ContinuedFraction.stripLeadingAndTrailingQuotes("\"--1\""));
        assertEquals("1", ContinuedFraction.stripLeadingAndTrailingQuotes("\"--1--\""));
    }

    @Test
    public void testStripLeadingQuotesCustomNthValue() {
        assertEquals("2", ContinuedFraction.stripLeadingAndTrailingQuotes("\"1\"--2\""));
        assertEquals("2", ContinuedFraction.stripLeadingAndTrailingQuotes("\"1\"--2--\""));
    }

    @Test
    public void testStripLeadingQuotesNoLeadingQuotes() {
        assertEquals("1", ContinuedFraction.stripLeadingAndTrailingQuotes("1"));
    }

    @Test
    public void testStripLeadingQuotesNull() {
        assertNull(ContinuedFraction.stripLeadingAndTrailingQuotes(null));
    }

    static class MyContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n == 0) {
                return 1.0;
            }
            return (x + Math.sqrt(x * x + 3.0)) / 2.0;
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0;
        }
    }

    static class MyContinuedFractionConvergenceException extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n == 0) {
                return 1.0 / 2.0;
            }
            return Math.sin(x) / (x + 1);
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0;
        }
    }

    static class MyContinuedFractionMaxCountExceededException extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            if (n == 0) {
                return 1.0;
            }
            if (n % 2 == 0) {
                return -1.0;
            }
            return 1.0 / (x + n);
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0;
        }
    }
}