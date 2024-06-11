package org.apache.commons.math3.ode.integrator.test;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stepper.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.stepper.RungeKuttaFehlberg78Stepper;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * Test suite for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /**
     * Sample system of first order differential equations.
     */
    private static final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
        public int getDimension() {
            return 2;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            yDot[0] = y[1];
            yDot[1] = -y[0] + Math.sin(t);
        }
    };

    /**
     * Sample initial conditions.
     */
    private static final double[] y0 = {1.0, 0.0};

    /**
     * Sample target time.
     */
    private static final double t = 3.0;

    /**
     * Sample step size.
     */
    private static final double initialStepSize = 0.1;

    /**
     * Sample maximum number of evaluations.
     */
    private static final int maxEvaluations = 100;

    /**
     * Sample event handler.
     */
    private static final EventHandler eventHandler = new EventHandler() {
        public void evaluateEvent(double t, double[] y, double[] yDot, double[] eventParameters) {
            assertEquals(t, 2.0, 1e-15); // arbitrary test value
        }
        public void handleEvent(double t, double[] y, double[] yDot, double[] eventParameters) {
            // no-op
        }
        public double getEventTime() {
            return 2.0;
        }
    };

    /**
     * Sample step interpolator.
     */
    private AbstractStepInterpolator interpolator;

    /**
     * Sample integrator.
     */
    private AbstractIntegrator integrator;

    /**
     * Initialize the test environment.
     */
    @Before
    public void setUp() {
        integrator = new ClassicalRungeKuttaIntegrator(5, t, maxEvaluations);
        integrator.setEquations(equations);
        integrator.setStepSize(initialStepSize);
        integrator.addEventHandler(eventHandler, 1.0e-15, 1.0e-15, 50, new RungeKuttaFehlberg78Stepper());
        interpolator = integrator.getInterpolator();
    }

    /**
     * Test integration with default constructor.
     */
    @Test
    public void testAbstractIntegrator() {
        integrator = new AbstractIntegrator();
        assertNull(integrator.getName());
        integrator.addEventHandler(eventHandler, 1.0e-15, 1.0e-15, 50, new RungeKuttaFehlberg78Stepper());
        assertNotNull(integrator.getInterpolator());
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, y0, t);
        integrator.integrate(interpolator);
        assertEquals(t, integrator.getCurrentStepStart(), 1e-15);
    }

    /**
     * Test integration with name constructor.
     */
    @Test
    public void testAbstractIntegratorWithName() {
        integrator = new AbstractIntegrator("TestIntegrator") {
            // explicit override to test method name
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                super.integrate(equations, t);
                assertEquals(getName(), "TestIntegrator", "Integrator name is not set");
            }
        };
        integrator.addEventHandler(eventHandler, 1.0e-15, 1.0e-15, 50, new RungeKuttaFehlberg78Stepper());
        assertNotNull(integrator.getInterpolator());
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, y0, t);
        integrator.integrate(interpolator);
        assertEquals("TestIntegrator", integrator.getName(), "Integrator name is not set");
        assertEquals(t, integrator.getCurrentStepStart(), 1e-15);
    }

    /**
     * Test integration with maximum number of evaluations.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxEvaluations() {
        integrator = new ClassicalRungeKuttaIntegrator(5, t, maxEvaluations);
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, y0, t);
        integrator.integrate(interpolator);
        fail("Expected MaxCountExceededException but integration completed");
    }

    /**
     * Test integration with minimum step size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testMinStepSize() {
        integrator = new ClassicalRungeKuttaIntegrator(5, t, 0);
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, y0, t);
        integrator.integrate(interpolator);
        fail("Expected NumberIsTooSmallException but integration completed");
    }

    /**
     * Test integration with events.
     */
    @Test
    public void testEventHandler() {
        integrator = new ClassicalRungeKuttaIntegrator(5, t, maxEvaluations);
        integrator.addEventHandler(eventHandler, 1.0e-15, 1.0e-15, 50, new RungeKuttaFehlberg78Stepper());
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, y0, t);
        integrator.integrate(interpolator);
        assertEquals(2.0, integrator.getEventHandler().getEventTime(), 1e-15);
    }

    /**
     * Test integration with events and step handlers.
     */
    @Test
    public void testStepHandlers() {
        integrator = new ClassicalRungeKuttaIntegrator(5, t, maxEvaluations);
        integrator.addEventHandler(eventHandler, 1.0e-15, 1.0e-15, 50, new RungeKuttaFehlberg78Stepper());
        integrator.addStepHandler(new StepHandler() {
            public void handleStep(AbstractIntegrator integrator, boolean lastStep) {
                if (lastStep) {
                    assertEquals(2.0, integrator.getCurrentStepStart(), 1e-15);
                }
            }
        });
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, y0, t);
        integrator.integrate(interpolator);
        assertEquals(2.0, integrator.getCurrentStepStart(), 1e-15);
    }

    /**
     * Test integration with resetOccurred flag.
     */
    @Test
    public void testResetOccurredFlag() {
        integrator = new ClassicalRungeKuttaIntegrator(5, t, maxEvaluations);
        integrator.addEventHandler(eventHandler, 1.0e-15, 1.0e-15, 50, new RungeKuttaFehlberg78Stepper());
        integrator.addStepHandler(new StepHandler() {
            public void handleStep(AbstractIntegrator integrator, boolean lastStep) {
                if (lastStep) {
                    assertEquals(2.0, integrator.getCurrentStepStart(), 1e-15);
                    integrator.getEventHandler().resetBegin();
                }
            }
        });
        integrator.setEquations(equations);
        integrator.initIntegration(0.0, y0, t);
        integrator.integrate(interpolator);
        assertFalse(integrator.isResetOccurred());
        integrator.integrate(interpolator);
        assertTrue(integrator.isResetOccurred());
    }

}