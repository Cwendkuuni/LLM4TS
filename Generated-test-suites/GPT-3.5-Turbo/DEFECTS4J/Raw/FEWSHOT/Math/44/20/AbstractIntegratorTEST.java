package org.apache.commons.math.ode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class AbstractIntegratorTest {
    private static ExpandableStatefulODE equations;
    private static double t0;
    private static double[] y0;
    private static double t;
    private static double[] y;

    @BeforeClass
    public static void setup() {
        equations = new ExpandableStatefulODE();
        t0 = 0.0;
        y0 = new double[] {1.0, 2.0, 3.0};
        t = 1.0;
        y = new double[3];
    }

    @Test
    public void testIntegrate() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl();
        double result = integrator.integrate(equations, t0, y0, t, y);
        assertEquals(1.0, result, 0.0);
        assertEquals(1.0, equations.getTime(), 0.0);
        assertEquals(1.0, y[0], 0.0);
        assertEquals(2.0, y[1], 0.0);
        assertEquals(3.0, y[2], 0.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrate_withIncorrectDimension() {
        AbstractIntegrator integrator = new AbstractIntegratorTestImpl();
        y0 = new double[] {1.0, 2.0};
        integrator.integrate(equations, t0, y0, t, y);
    }

    private static class AbstractIntegratorTestImpl extends AbstractIntegrator {
        public AbstractIntegratorTestImpl() {
            super("AbstractIntegratorTestImpl");
        }
        public void integrate(ExpandableStatefulODE equations, double t) {
            equations.setTime(t);
            System.arraycopy(y0, 0, y, 0, y0.length);
        }
    }
}