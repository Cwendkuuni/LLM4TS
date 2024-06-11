package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MatricesException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.VectorUtils;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueComparator;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.SingularityEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test RungeKuttaIntegrator class.
 */
public class RungeKuttaIntegratorTest extends RungeKuttaIntegratorTest {

    private static final double EPSILON = 1.0e-12;
    private static final double STEP = 0.1;

    /** Integrator to test */
    protected RungeKuttaIntegrator integrator;

    /** Differential equations to test the integrator */
    private FirstOrderDifferentialEquations equations;

    /** Step interpolator */
    protected RungeKuttaStepInterpolator stepInterpolator;

    /**
     * Sets up the test.
     * @throws MatricesException thrown if the Butcher array is not valid
     * @throws MathArithmeticException thrown if the weights are not valid
     */
    @Before
    public void setUp() throws MatricesException, MathArithmeticException {
        final double[] c = {0.0, 0.25, 0.5, 0.875};
        final double[][] a = {{0.0, 0.25}, {0.0, 0}, {0.0, 0.625}, {0.0, 0.875}};
        final double[] b = {0.375, 0.46875, 0.6875, 0.84375};
        integrator = new RungeKuttaIntegrator("My RK4 integrator", c, a, b, new RungeKuttaStepInterpolator(4), STEP) {
            @Override
            protected void computeDerivatives(double t, double[] y, double[] yPrime) {
                // do nothing, we are only testing the integrator itself
            }
        };
        stepInterpolator = new RungeKuttaStepInterpolator(4);
        equations = new DummyDifferentialEquations();
    }

    /**
     * Test integration of a simple differential equation.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testSimpleIntegration() throws Exception {
        testIntegration(0.0, 2.0, new double[] {1.0, 1.0}, new double[] {2.0, 2.0});
    }

    /**
     * Test integration with events.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testIntegrationWithEvents() throws Exception {
        final double[] y0 = new double[] {1.0, 1.0};
        final double tStart = 0.0;
        final double tEnd = 2.0;

        // set up an event manager
        final EventManager manager = new EventManager();
        manager.addEvent(new SingularityEvent(1.5, 2.0));

        // set up a comparator for the events
        final DefaultValueComparator comparator = new DefaultValueComparator(1.0e-12);

        // set up the integrator with the event manager
        integrator.setEventsHandlersManager(manager);
        integrator.setEventsComparator(comparator);

        // integrate
        final double t = integrator.integrate(equations, tStart, y0, tEnd, new double[y0.length]);

        // check the results
        final Event[] events = manager.getEvents();
        Assert.assertNotNull("No event detected", events);
        Assert.assertEquals("Wrong number of events", 1, events.length);
        Assert.assertArrayEquals("Wrong event position", new double[] {1.5}, events[0].getTime(), EPSILON);
        Assert.assertArrayEquals("Wrong event value", new double[] {2.0}, events[0].getValue(), EPSILON);

        // check the state at the end of the integration
        final double[] y = new double[y0.length];
        System.arraycopy(integrator.getY(), 0, y, 0, y0.length);
        Assert.assertArrayEquals("Wrong final state", new double[] {2.0, 2.0}, y, EPSILON);
    }

    /**
     * Test integration with a step handler.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testIntegrationWithStepHandler() throws Exception {
        final StepHandler handler = new DummyStepHandler();
        integrator.setStepHandlers(new StepHandler[] {handler});

        // integrate
        integrator.integrate(equations, 0.0, new double[] {1.0, 1.0}, 2.0, new double[2]);

        // check the step handler was called
        Assert.assertArrayEquals("Wrong number of calls to the step handler", new int[] {3}, handler.getCallCount(), EPSILON);
    }

    /**
     * Test integration with a dense output.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testIntegrationWithDenseOutput() throws Exception {
        final double[][] y = new double[3][];
        for (int i = 0; i < 3; ++i) {
            y[i] = new double[2];
        }

        // integrate
        integrator.integrate(equations, 0.0, new double[] {1.0, 1.0}, 2.0, y);

        // check the dense output
        Assert.assertArrayEquals("Wrong number of output points", 3, y[0].length, EPSILON);
        for (int i = 0; i < y.length; ++i) {
            Assert.assertArrayEquals("Wrong output point", new double[] {i * STEP, y0[0] + i * STEP, y0[1] + i * STEP}, y[i], EPSILON);
        }

        // check the state at the end of the integration
        final double[] yEnd = new double[] {2.0, 2.0};
        Assert.assertArrayEquals("Wrong final state", yEnd, integrator.getY(), EPSILON);
    }

    /**
     * Test integration with a sparse output.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testIntegrationWithSparseOutput() throws Exception {
        integrator.setRequiresDenseOutput(false);

        // integrate
        integrator.integrate(equations, 0.0, new double[] {1.0, 1.0}, 2.0, new Object[2]);

        // check the step handler was called
        Assert.assertTrue("The step handler should have been called", stepInterpolator.size() > 0);

        // check the state at the end of the integration
        final double[] yEnd = new double[] {2.0, 2.0};
        Assert.assertArrayEquals("Wrong final state", yEnd, integrator.getY(), EPSILON);
    }

}