package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Test class for {@link AbstractIntegrator}
 */
public class AbstractIntegratorTest {

    /** A stub implementation of {@link EventHandler} for testing purposes */
    static class TestEventHandler implements EventHandler {

        private double eventTime;
        private double[] eventState;

        @Override
        public void init(double t, double[] y, double tEnd) {
            eventTime = tEnd;
            eventState = y;
        }

        @Override
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return true;
        }

        @Override
        public void stepAccepted(double t, double[] y) {
            // do nothing
        }

        @Override
        public void reset(double t, double[] y) {
            // do nothing
        }

        @Override
        public boolean stop() {
            return false;
        }

        public double getEventTime() {
            return eventTime;
        }

        public double[] getEventState() {
            return eventState;
        }

    }

    /**
     * Test method for {@link AbstractIntegrator#initIntegration(double, double[], double)}
     * @throws Exception
     */
    @Test
    public void testInitIntegration() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator();
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertFalse(integrator.statesInitialized);

        // Test initialization with EventHandler
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }
        };
        integrator.addEventHandler(new TestEventHandler(), 0.5, 1.0e-6, 10);
        integrator.initIntegration(0.0, new double[0], 1.0);
        assertTrue(integrator.statesInitialized);
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * @throws Exception
     */
    @Test
    public void testIntegrate() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }
        };

        // Test with no equations
        integrator.integrate(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 0;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // do nothing
            }
        }), 1.0);

        // Test with equations
        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        }));
        integrator.integrate(integrator.expandable, 0.0);
        double[] result = new double[1];
        integrator.getPrimaryState(result);
        assertEquals(0.0, result[0], 1e-6);

        integrator.initIntegration(0.0, new double[1], 1.0);
        result = new double[1];
        integrator.getPrimaryState(result);
        assertEquals(0.0, result[0], 1e-6);

        integrator.integrate(integrator.expandable, 1.0);
        result = new double[1];
        integrator.getPrimaryState(result);
        assertEquals(1.0, result[0], 1e-6);

        // Test integration with event handler
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }
        };
        integrator.addEventHandler(new TestEventHandler(), 0.5, 1.0e-6, 10);
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.integrate(integrator.expandable, 1.0);
        TestEventHandler eventHandler = (TestEventHandler) integrator.getEventHandlers().iterator().next();
        double eventTime = eventHandler.getEventTime();
        double[] eventState = eventHandler.getEventState();
        assertEquals(0.5, eventTime, 1e-6);
        assertEquals(0.5 * 0.1, eventState[0], 1e-6);
    }

    /**
     * Test method for {@link AbstractIntegrator#computeDerivatives(double, double[], double[])}
     * @throws Exception
     */
    @Test
    public void testComputeDerivatives() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }
        };

        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        }));

        double[] y = new double[1];
        integrator.computeDerivatives(1.0, y, new double[1]);
        assertEquals(1.0 * 0.1, y[0], 1e-6);

        // Test with maxEvaluations set
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }

            @Override
            public int getMaxEvaluations() {
                return 1;
            }
        };
        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        }));
        try {
            integrator.computeDerivatives(1.0, y, new double[1]);
            fail("Should have thrown MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}
     * @throws Exception
     */
    @Test
    public void testAcceptStep() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }

            @Override
            public int getMaxEvaluations() {
                return 1;
            }
        };

        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        }));

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return 0.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }
        };

        double[] y = new double[1];
        double yDot = 0.0;
        double tEnd = 1.0;

        integrator.acceptStep(interpolator, y, new double[1], tEnd);
        assertEquals(0.0, y[0], 1e-6);

        // Test with events
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }

            @Override
            public int getMaxEvaluations() {
                return 1;
            }
        };

        integrator.addEventHandler(new TestEventHandler(), 0.5, 1.0e-6, 10);
        integrator.initIntegration(0.0, new double[1], 1.0);
        integrator.acceptStep(interpolator, y, new double[1], tEnd);
        assertEquals(0.5, y[0], 1e-6);

        TestEventHandler eventHandler = (TestEventHandler) integrator.getEventHandlers().iterator().next();
        assertEquals(0.5, eventHandler.getEventTime(), 1e-6);
        assertEquals(0.5 * 0.1, eventHandler.getEventState()[0], 1e-6);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized()}
     * @throws Exception
     */
    @Test
    public void testGetStateInitialized() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }
        };
        assertFalse(integrator.statesInitialized);
        integrator.setStateInitialized(true);
        assertTrue(integrator.statesInitialized);
    }

    /**
     * Test method for {@link AbstractIntegrator#sanityChecks(ExpandableStatefulODE, double)}
     * @throws Exception
     */
    @Test
    public void testSanityChecks() throws Exception {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                fail("Should not be called");
            }
        };

        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        }));

        try {
            integrator.sanityChecks(integrator.expandable, 0.0);
            fail("Should have thrown NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            integrator.sanityChecks(integrator.expandable, 1.0e-324);
            fail("Should have thrown NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            integrator.sanityChecks(integrator.expandable, 1.0);
            // Expected
        } catch (Exception e) {
            fail("Should not have thrown an exception");
        }
    }
}