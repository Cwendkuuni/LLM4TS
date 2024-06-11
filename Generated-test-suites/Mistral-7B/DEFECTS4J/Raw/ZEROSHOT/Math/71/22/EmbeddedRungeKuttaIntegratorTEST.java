package org.apache.commons.math.ode.nonstiff.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.DummyEquations;
import org.apache.commons.math3.ode.test.TestODEIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests {@link EmbeddedRungeKuttaIntegrator} with various tests.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private DummyEquations equations;
    private double[] y0;
    private double[] y1;
    private double tStart;
    private double tEnd;

    @Before
    public void setUp() {
        // create a test integrator instance
        integrator = new EmbeddedRungeKutta45Integrator(
                "Embedded Runge-Kutta 4-5 (Fehlberg 1968)", true,
                new double[] { 0.25, 0.4, 0.625, 0.875, 1.0 },
                new double[][] { { 0.25, 0 },
                                 { 0.6926926926926926, 0.25 },
                                 { 0.6568542447461903, 0.4 },
                                 { 0.3926990816981104, 0.625 },
                                 { 0.1904534969387754, 0.875 },
                                 { 0.0498585937519963, 1.0 } },
                new double[] { 0.25, 0.4551224746190323, 0.4914286248571982,
                              0.3351136088526716, 0.0 },
                new RungeKuttaStepInterpolator());

        // create test equations instance
        equations = new DummyEquations();

        // initialize the test data
        y0 = new double[] { 1.0, 1.0 };
        y1 = new double[] { 1.1, 1.1 };
        tStart = 0.0;
        tEnd = 1.0;
    }

    /**
     * Test the integration of a simple differential equation.
     */
    @Test
    public void testSimpleIntegration() {
        TestODEIntegrator testIntegrator = new TestODEIntegrator(integrator, equations, tStart, y0, tEnd, y1, 1e-12, 1e-12);
        testIntegrator.testIntegrate();
        assertEquals("The computed result is not correct.", testIntegrator.getActualEnd(), tEnd, 1e-12);
        assertArrayEquals("The computed result is not correct.", testIntegrator.getActualY(), y1, 1e-12);
    }

    /**
     * Test the integration of a differential equation with events.
     */
    @Test
    public void testIntegrationWithEvents() {
        double[] y2 = new double[] { 1.0, 1.1 };
        double tEvent = 0.5;

        StepHandler handler = new StepHandler() {

            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                if (interpolator.getStepTime() >= tEvent) {
                    throw new EventOccurredException();
                }
            }

        };

        equations.addEvent(new When(tEvent).onEnter(new Event() {

            @Override
            public void integrateVariables(FirstOrderDifferentialEquations equations, double t, double[] y) {
                y[1] = 1.2;
            }

        }));

        StepHandler[] stepHandlers = new StepHandler[] { handler };
        integrator.setStepHandlers(stepHandlers);
        integrator.setEventsHandlerManager(equations.getEventsManager());

        integrator.integrate(equations, tStart, y0, tEnd, y2);

        assertEquals("The computed result is not correct.", y2[1], 1.2, 1e-12);
    }

}

class EventOccurredException extends IntegratorException {}