package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
    private EmbeddedRungeKuttaIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new EmbeddedRungeKuttaIntegrator() {
            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 0.0;
            }
        };
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateNullEquations() throws MathIllegalStateException, MathIllegalArgumentException {
        ExpandableStatefulODE equations = null;
        integrator.integrate(equations, 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateNullIntegrationTime() throws MathIllegalStateException, MathIllegalArgumentException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(null);
        integrator.integrate(equations, Double.NaN);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateInvalidIntegrationRange() throws MathIllegalStateException, MathIllegalArgumentException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE(null);
        integrator.integrate(equations, Double.POSITIVE_INFINITY);
    }

    // Add more test cases here...

    @Test
    public void testEstimateError() {
        // TODO: implement test
    }

}