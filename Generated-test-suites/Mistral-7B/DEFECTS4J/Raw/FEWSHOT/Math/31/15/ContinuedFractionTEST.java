package org.apache.commons.cli;

import static org.apache.commons.math3.util.Precision.equals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class ContinuedFractionTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new ContinuedFraction() {
            // empty body
        };
    }

    @Test(expected = UnsupportedOperationException. class)
    public void testAbstractMethods() {
        new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected double getB(int n, double x) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateSimpleContinuedFractionWithInfiniteValues() {
        new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? 1 : Double.POSITIVE_INFINITY;
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 1 : Double.POSITIVE_INFINITY;
            }
        }.evaluate(0.0, 1e-9, Integer.MAX_VALUE);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateSimpleContinuedFractionWithNaNValues() {
        new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? 1 : Double.NaN;
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 1 : 1;
            }
        }.evaluate(0.0, 1e-9, Integer.MAX_VALUE);
    }

    @Test(expected = ConvergenceException.class)
    public void testEvaluateSimpleContinuedFractionWithNonConvergingValues() {
        new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? 1 : (n % 2 == 0 ? -1 : 1);
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 0 : 1;
            }
        }.evaluate(0.0, 1e-9, Integer.MAX_VALUE);
    }

    @Test
    public void testEvaluateSimpleContinuedFraction() {
        ContinuedFraction simpleContinuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? 1 : 1;
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 1 : 0;
            }
        };

        assertEquals(1.0, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 2, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 3, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 4, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 5, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 6, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 7, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 8, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 9, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 10, simpleContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(0.1, simpleContinuedFraction.evaluate(0.1), 1e-9);
        assertEquals(0.01, simpleContinuedFraction.evaluate(0.01), 1e-9);
        assertEquals(0.001, simpleContinuedFraction.evaluate(0.001), 1e-9);
    }

    @Test
    public void testEvaluateComplexContinuedFraction() {
        ContinuedFraction complexContinuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? 1 : (n % 2 == 0 ? 1 : -1);
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 1 : 2;
            }
        };

        assertEquals(1.0, complexContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 2, complexContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 3, complexContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 4, complexContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 5, complexContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(1.0 / 6, complexContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(0.5, complexContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(0.25, complexContinuedFraction.evaluate(0.0), 1e-9);
        assertEquals(0.12142857142857142857, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.06060606060606060606, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.03030303030303030303, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.015151515151515151515, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0075757575757575757575, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0037878787878787878787, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0018939393939393939393, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0009469696969696969696, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0004734848484848484848, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0002367423674236742367, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0001183711837118371183, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000591859185918591859, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000295929592959295929, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000147964796479647964, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000073982398239823982, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000036991198854154236, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000018495599427077118, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000009247799713538559, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000004623899856769279, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000002311949928384639, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000001155974964192319, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000000577987482096159, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000000288993741048079, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000000144496870524039, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000000072248435262019, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000000036124217631009, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000000018062108815504, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000000009031054407752, complexContinuedFraction.evaluate(0.0), 1e-15);
        assertEquals(0.0000000004515527203876, complexContinuedFraction.evaluate(0.0), 1e-15);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateComplexContinuedFractionWithMaxIterations() {
        ContinuedFraction complexContinuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n == 0 ? 1 : (n % 2 == 0 ? 1 : -1);
            }

            @Override
            protected double getB(int n, double x) {
                return n == 0 ? 1 : 2;
            }
        };

        complexContinuedFraction.evaluate(0.0, 0.0, 1);
    }

    @Test
    public void testGetA() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n % 2 == 0 ? 1 : 2;
            }

            @Override
            protected double getB(int n, double x) {
                return n % 2 == 0 ? 1 : 1;
            }
        };

        assertEquals(1.0, continuedFraction.getA(0, 0.0), 1e-9);
        assertEquals(1.0, continuedFraction.getA(2, 0.0), 1e-9);
        assertEquals(2.0, continuedFraction.getA(1, 0.0), 1e-9);
    }

    @Test
    public void testGetB() {
        ContinuedFraction continuedFraction = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return n % 2 == 0 ? 1 : 2;
            }

            @Override
            protected double getB(int n, double x) {
                return n % 2 == 0 ? 1 : 1;
            }
        };

        assertEquals(1.0, continuedFraction.getB(0, 0.0), 1e-9);
        assertEquals(1.0, continuedFraction.getB(2, 0.0), 1e-9);
        assertEquals(1.0, continuedFraction.getB(1, 0.0), 1e-9);
    }

}