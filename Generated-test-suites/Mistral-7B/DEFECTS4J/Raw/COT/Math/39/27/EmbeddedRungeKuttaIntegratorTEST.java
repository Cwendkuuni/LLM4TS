package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.AbstractContinuousIntegratorEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-6;
    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE ode;

    @Before
    public void setUp() {
        // Set up the test ODE
        ode = TestUtils.createDummyODE();

        // Set up the integrator
        integrator = new EmbeddedRungeKutta245DOPriessley(true, new double[]{-0.236926885, 0.275390634, 0.236926885, 0.275390634},
                new double[]{-0.623103457, 0.346573788, 0.623103457, 0.346573788},
                new double[]{0.236926885, 0.275390634},
                new RungeKuttaStepInterpolator() {
                    @Override
                    public int getOrder() {
                        return 5;
                    }
                }, 0.001, 0.01, null, null);
    }

    @Test
    public void testIntegrate() {
        double[] initialState = {0.0, 1.0};
        double  initialTime  = 0.0;
        double  finalTime    = 10.0;

        integrator.integrate(ode, initialTime, initialState);

        assertEquals(Precision.round(4.36232863161e-07, EPSILON), ode.getValue(0, finalTime), EPSILON);
        assertEquals(Precision.round(5.81332295359e-07, EPSILON), ode.getValue(1, finalTime), EPSILON);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidStepSize() {
        double[] initialState = {0.0, 1.0};
        double  initialTime  = 0.0;
        double  finalTime    = 10.0;

        integrator.setMaxStep(0.0);
        integrator.integrate(ode, initialTime, initialState);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIntegrateWithInvalidMinStep() {
        double[] initialState = {0.0, 1.0};
        double  initialTime  = 0.0;
        double  finalTime    = 10.0;

        integrator.setMinStep(-1.0);
        integrator.integrate(ode, initialTime, initialState);
    }

    @Test
    public void testIntegrateWithEvents() {
        double[] initialState = {0.0, 1.0, 0.0};
        double  initialTime  = 0.0;
        double  finalTime    = 5.0;

        AbstractContinuousIntegratorEvent event = new AbstractContinuousIntegratorEvent() {
            @Override
            public void evaluateEvent(double time, double[] state, double[] derivatives, double[] eventParameters) {
                assertEquals(5.0, time, EPSILON);
                assertArrayEquals(new double[]{1.0, 1.0}, state, EPSILON);
            }
        };

        ode.addEvent(event);

        integrator.integrate(ode, initialTime, initialState);
    }

}