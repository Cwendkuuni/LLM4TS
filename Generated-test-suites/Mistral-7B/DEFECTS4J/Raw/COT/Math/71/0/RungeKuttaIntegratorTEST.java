package org.apache.commons.math.ode.nonstiff; // Import necessary classes

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 4 test cases for RungeKuttaIntegrator class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * An instance of RungeKuttaIntegrator for testing.
     */
    private RungeKuttaIntegrator integrator;

    /**
     * Initializes the integrator for testing.
     */
    @Before
    public void setUp() {
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        integrator = new RungeKuttaIntegrator("RK2", new double[] { 0.5, 0.5 },
                new double[][] { { 0.25, 0.25 }, { 0.5, 0.0 } },
                new double[] { 0.5, 0.5 }, prototype, 0.1) {

            @Override
            public double integrate(FirstOrderDifferentialEquations equations,
                                   double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {

                // Override the integration method to make it throw exceptions,
                // but do not forget to call the superclass implementation
                // to perform the actual integration
                try {
                    super.integrate(equations, t0, y0, t, y);
                } catch (Exception e) {
                    throw new IntegratorException(e);
                }
            }
        };
    }

    /**
     * Tests the functionality of the constructor.
     */
    @Test
    public void testConstructor() {
        Assert.assertNotNull(integrator);
    }

    /**
     * Tests the functionality of the integrate method with simple differential equations.
     */
    @Test
    public void testIntegrateSimple() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[0] * 0.1;
            }
        };

        double[] initialValues = new double[] { 1.0 };
        double startTime = 0.0;
        double endTime = 1.0;

        integrator.initialize(equations, initialValues);
        double result = integrator.integrate(startTime, initialValues, endTime);

        // The result should be close to the expected value
        double expectedResult = Math.exp(0.1);
        Assert.assertEquals(expectedResult, result, 0.001);
    }

    /**
     * Tests the functionality of the integrate method with differential equations
     * containing an event.
     */
    @Test
    public void testIntegrateWithEvent() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[0] * 0.1;
            }

            @Override
            public List<Event> getEvents(double t, double[] y) {
                List<Event> events = new ArrayList<>();
                if (t > 1.0) {
                    events.add(new When(1.1, new Event() {
                        @Override
                        public void handleEvent(double t, double[] y) {
                            throw new IntegratorException("Test event occurred.");
                        }
                    }));
                }
                return events;
            }
        };

        double[] initialValues = new double[] { 1.0 };
        double startTime = 0.0;
        double endTime = 2.0;

        integrator.initialize(equations, initialValues);

        try {
            integrator.integrate(startTime, initialValues, endTime);
            Assert.fail("Expected IntegratorException.");
        } catch (IntegratorException e) {
            // Expected exception
        }
    }

    /**
     * Tests the functionality of the integrate method with an exception
     * in the differential equations computation.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateWithException() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                throw new DerivativeException("Test exception.");
            }
        };

        double[] initialValues = new double[] { 1.0 };
        double startTime = 0.0;
        double endTime = 1.0;

        integrator.initialize(equations, initialValues);
        integrator.integrate(startTime, initialValues, endTime);
    }

    /**
     * Tests the functionality of the integrate method with the integrator's step
     * size exceeding the limit.
     */
    @Test(expected = IntegratorException.class)
    public void testIntegrateWithStepSizeLimit() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[0] * 0.1;
            }
        };

        double[] initialValues = new double[] { 1.0 };
        double startTime = 0.0;
        double endTime = 1000.0;

        integrator.initialize(equations, initialValues);
        integrator.integrate(startTime, initialValues, endTime);
    }

    /**
     * Tests the functionality of the integrate method with the integrator's step
     * size causing the number of iterations to exceed the limit.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithIterationLimit() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[0] * 0.1;
            }
        };

        double[] initialValues = new double[] { 1.0 };
        double startTime = 0.0;
        double endTime = 1.0;

        integrator.initialize(equations, initialValues);
        integrator.setMaxSteps(1);
        integrator.integrate(startTime, initialValues, endTime);
    }

    /**
     * Tests the functionality of the integrate method with the integrator's step
     * size causing the number of steps to exceed the limit.
     */
    @Test(expected = TooManyIterationsException.class)
    public void testIntegrateWithStepLimit() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[0] * 0.1;
            }
        };

        double[] initialValues = new double[] { 1.0 };
        double startTime = 0.0;
        double endTime = 1.0;

        integrator.initialize(equations, initialValues);
        integrator.setMaxStepCount(1);
        integrator.integrate(startTime, initialValues, endTime);
    }
}