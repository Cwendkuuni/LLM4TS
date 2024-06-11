package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.SingleValueEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    private static final double EPSILON = 1e-10;
    private static final double ACCURACY = 1e-6;
    private static final double TOLERANCE = 1e-10;
    private static final double INITIAL_TIME = 0.0;
    private static final double FINAL_TIME = 10.0;
    private static final double STEP_SIZE = 0.1;
    private static final double[] INITIAL_VALUE = {0.0, 1.0};

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new EulerIntegrator(EulerIntegrator.DEFAULT_NAME,
                new double[]{}, new double[]{}, new double[]{0.0},
                new EulerStepInterpolator(), STEP_SIZE);
        equations = (FirstOrderDifferentialEquations) () -> new double[]{2.0 * x, Math.sin(x)};
    }

    @Test
    public void testIntegrateSimple() {
        assertArrayEquals("Initial values should be correct.", INITIAL_VALUE,
                integrator.integrate(equations, INITIAL_TIME, INITIAL_VALUE, FINAL_TIME, new double[2]));
    }

    @Test
    public void testIntegrateWithSingleEvent() {
        SingleValueEvent event = new SingleValueEvent(2.0, 3.0, 1.0);
        integrator.addEventHandler(event);

        double[] result = integrator.integrate(equations, INITIAL_TIME, INITIAL_VALUE, FINAL_TIME, new double[2]);
        assertEquals("The integration should stop at the event.", 3.0, result[0], TOLERANCE);
        assertEquals("The second value should continue integration.", 1.0 + INITAL_VALUE[1] * STEP_SIZE, result[1], TOLERANCE);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithNoBracketingEvent() {
        SingleValueEvent event = new SingleValueEvent(1.5, 2.0, 1.0);
        integrator.addEventHandler(event);

        integrator.integrate(equations, INITIAL_TIME, INITIAL_VALUE, FINAL_TIME, new double[2]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithMaxCountExceededEvent() {
        SingleValueEvent event = new SingleValueEvent(2.0, 2.0, 1.0, 1000);
        integrator.addEventHandler(event);

        integrator.integrate(equations, INITIAL_TIME, INITIAL_VALUE, FINAL_TIME, new double[2]);
    }

    @Test
    public void testIntegrateWithStepHandler() {
        StepHandler stepHandler = (step, y) -> {
            assertEquals("Check initial values.", INITIAL_VALUE, y, EPSILON);
        };
        integrator.addStepHandler(stepHandler);

        integrator.integrate(equations, INITIAL_TIME, INITIAL_VALUE, FINAL_TIME, new double[2]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIntegrateWithInvalidEquations() {
        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] value(double t, double[] y) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] value(double t, double[] y, double step) {
                throw new UnsupportedOperationException();
            }
        }, INITIAL_TIME, INITIAL_VALUE, FINAL_TIME, new double[2]);
    }

}