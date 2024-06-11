package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test cases for {@link ContinuedFraction} class.
 */
public class ContinuedFractionTest {

    private ContinuedFraction continuedFraction;
    private double epsilon;
    private int maxIterations;

    @Before
    public void setUp() {
        continuedFraction = mock(ContinuedFraction.class);
        epsilon = 1e-10;
        maxIterations = 5;
    }

    @Test
    public void testGetAAndB() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) + 1;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1);
            }
        });

        assertEquals(1.5, continuedFraction.evaluate(1.0), epsilon);
    }

    @Test
    public void testEvaluateWithDefaultParameters() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) / 2;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1) + 1;
            }
        });

        assertEquals(1.0, continuedFraction.evaluate(0.0), ContinuedFractionTest.DEFAULT_EPSILON);
        assertEquals(0.25, continuedFraction.evaluate(1.0), ContinuedFractionTest.DEFAULT_EPSILON);
    }

    @Test
    public void testEvaluateWithCustomEpsilon() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) / 3;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1) + 1;
            }
        });

        assertEquals(1.0 / 3.0, continuedFraction.evaluate(0.0), epsilon);
        assertEquals(0.25 * 2 / 3.0, continuedFraction.evaluate(1.0), epsilon);
    }

    @Test
    public void testEvaluateWithCustomMaxIterations() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) / 2;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1) + 1;
            }
        });

        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(0.0, 0.0));
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, 0.0));
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(0.0, maxIterations + 1));

        double[] results = new double[maxIterations];
        for (int i = 0; i < maxIterations; i++) {
            results[i] = continuedFraction.evaluate(0.0, epsilon, i);
        }

        assertArrayEquals(MathArrays.create(results), new double[]{1.0 / 2.0, 1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0}, 0.0);
    }

    @Test
    public void testEvaluateWithConvergenceException() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) / 2;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1) + 1;
            }
        });

        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(0.0, 0.0, 0));
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, 0.0, 0));

        try {
            continuedFraction.evaluate(0.0, 1e-12, 0);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateWithInfinityDivergenceException() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) / 2;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1) + 1;
            }
        });

        when(continuedFraction.getB(anyInt(), anyDouble())).thenThrow(new RuntimeException("Test exception"));

        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(0.0, 1e-12, Integer.MAX_VALUE));
        assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, 1e-12, Integer.MAX_VALUE));

        try {
            continuedFraction.evaluate(0.0, 1e-12, 10);
            fail();
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE.getMessage()));
        }

        try {
            continuedFraction.evaluate(1.0, 1e-12, 10);
            fail();
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE.getMessage()));
        }
    }

    @Test
    public void testEvaluateWithNaN() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) / 2;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1) + 1;
            }
        });

        when(continuedFraction.getA(anyInt(), anyDouble())).thenThrow(new RuntimeException("Test exception"));

        try {
            continuedFraction.evaluate(0.0, 1e-12, Integer.MAX_VALUE);
            fail();
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE.getMessage()));
        }

        try {
            continuedFraction.evaluate(1.0, 1e-12, Integer.MAX_VALUE);
            fail();
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE.getMessage()));
        }
    }

    @Test
    public void testEvaluateWithMaxCountExceededException() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) / 2;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1) + 1;
            }
        });

        try {
            continuedFraction.evaluate(0.0, Double.NEGATIVE_INFINITY, Integer.MAX_VALUE);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }

        try {
            continuedFraction.evaluate(1.0, Double.NEGATIVE_INFINITY, Integer.MAX_VALUE);
            fail();
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateWithNullInput() {
        when(continuedFraction.getA(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 0) / 2;
            }
        });
        when(continuedFraction.getB(anyInt(), anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, 1) + 1;
            }
        });

        assertNull(continuedFraction.evaluate(null));
        assertNull(continuedFraction.evaluate(0.0, null));
        assertNull(continuedFraction.evaluate(0.0, Integer.MIN_VALUE));
    }
}