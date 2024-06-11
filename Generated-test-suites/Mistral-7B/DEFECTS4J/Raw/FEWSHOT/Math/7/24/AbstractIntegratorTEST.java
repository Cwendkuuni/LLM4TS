package org.apache.commons.math3.ode;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link AbstractIntegrator}.
 */
public class AbstractIntegratorTest {

    /** Integrator under test. */
    private AbstractIntegrator integrator;

    /** Step interpolator. */
    private AbstractStepInterpolator interpolator;

    /** Event handler. */
    private static final EventHandler eventHandler = new AbstractEvent() {

        /** {@inheritDoc} */
        public void init(double t, double[] y, double tTarget) {
            // not used
        }

        /** {@inheritDoc} */
        public boolean stepAccepted(double t, double[] y) {
            reset();
            return true;
        }

        /** {@inheritDoc} */
        public void stepReset() {
            // not used
        }

        /** {@inheritDoc} */
        public boolean evaluateStep(AbstractStepInterpolator interpolator) {
            return true;
        }

        /** {@inheritDoc} */
        public boolean reset(double t, double[] y) {
            setValue(1.0);
            return false;
        }

        /** {@inheritDoc} */
        public void handleStep(StepHandler handler, boolean lastStep) {
            // not used
        }

    };

    /** Step handler. */
    private static final StepHandler stepHandler = new StepHandler() {

        /** {@inheritDoc} */
        public void init(double t, double[] y, double tTarget) {
            // not used
        }

        /** {@inheritDoc} */
        public void handleStep(StepHandler handler, boolean lastStep) {
            if (lastStep) {
                reset();
            }
        }

    };

    /**
     * Set up.
     */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // not implemented
            }
        };
        interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return -1.0;
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }

            @Override
            public void setInterpolatedTime(double t) {
                // not used
            }

            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }

            @Override
            public void setSoftPreviousTime(double previousTime) {
                // not used
            }

            @Override
            public void setSoftCurrentTime(double currentTime) {
                // not used
            }
        };
    }

    /**
     * Test {@link AbstractIntegrator#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals("", integrator.getName());
    }

    /**
     * Test {@link AbstractIntegrator#addStepHandler(StepHandler)}.
     */
    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    /**
     * Test {@link AbstractIntegrator#getStepHandlers()}.
     */
    @Test
    public void testGetStepHandlers() {
        integrator.addStepHandler(stepHandler);
        List<StepHandler> handlers = new ArrayList<>(integrator.getStepHandlers());
        assertEquals(1, handlers.size());
        assertEquals(stepHandler, handlers.get(0));
    }

    /**
     * Test {@link AbstractIntegrator#clearStepHandlers()}.
     */
    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /**
     * Test {@link AbstractIntegrator#addEventHandler(EventHandler, double, double, int, UnivariateSolver)}.
     */
    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-10, 10, new BracketingNthOrderBrentSolver(1.0e-10, 5));
        assertEquals(1, integrator.getEventHandlers().size());
    }

    /**
     * Test {@link AbstractIntegrator#getEventHandlers()}.
     */
    @Test
    public void testGetEventHandlers() {
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-10, 10, new BracketingNthOrderBrentSolver(1.0e-10, 5));
        List<EventHandler> handlers = new ArrayList<>(integrator.getEventHandlers());
        assertEquals(1, handlers.size());
        assertEquals(eventHandler, handlers.get(0));
    }

    /**
     * Test {@link AbstractIntegrator#clearEventHandlers()}.
     */
    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(eventHandler, 1.0, 1.0e-10, 10, new BracketingNthOrderBrentSolver(1.0e-10, 5));
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /**
     * Test {@link AbstractIntegrator#getCurrentStepStart()}.
     */
    @Test
    public void testGetCurrentStepStart() {
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
    }

    /**
     * Test {@link AbstractIntegrator#getCurrentSignedStepsize()}.
     */
    @Test
    public void testGetCurrentSignedStepsize() {
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0.0);
    }

    /**
     * Test {@link AbstractIntegrator#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test {@link AbstractIntegrator#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /**
     * Test {@link AbstractIntegrator#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        integrator.setMaxEvaluations(10);
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        assertEquals(2, integrator.getEvaluations());
    }

    /**
     * Test {@link AbstractIntegrator#initIntegration(double, double[], double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testInitIntegration() {
        integrator.initIntegration(1.0, new double[0], 2.0);
    }

    /**
     * Test {@link AbstractIntegrator#setStateInitialized()}.
     */
    @Test
    public void testGetStateInitialized() {
        assertFalse(integrator.isStateInitialized());
    }

    /**
     * Test {@link AbstractIntegrator#setStateInitialized(boolean)}.
     */
    @Test
    public void testSetStateInitialized() {
        integrator.setStateInitialized(true);
        assertTrue(integrator.isStateInitialized());
    }

    /**
     * Test {@link AbstractIntegrator#setEquations(ExpandableStatefulODE)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetEquations() {
        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }
        }));
    }

    /**
     * Test {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIntegrate() throws Exception {
        integrator.integrate(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }
        }), 1.0);
    }

    /**
     * Test {@link AbstractIntegrator#computeDerivatives(double, double[], double[])}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testComputeDerivatives() throws Exception {
        integrator.computeDerivatives(1.0, new double[0], new double[0]);
    }

    /**
     * Test {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAcceptStep() throws Exception {
        integrator.acceptStep(interpolator, new double[0], new double[0], 1.0);
    }

    /**
     * Test {@link AbstractIntegrator#sanityChecks(ExpandableStatefulODE, double)}.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testSanityChecks() throws Exception {
        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 2;
            }
        }));
        integrator.sanityChecks(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }
        }), 1.0);
    }

    /**
     * Test {@link AbstractIntegrator#sanityChecks(ExpandableStatefulODE, double)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSanityChecksTooSmall() throws Exception {
        integrator.setEquations(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }
        }));
        integrator.sanityChecks(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {

            /** {@inheritDoc} */
            public int getDimension() {
                return 1;
            }
        }), 1.0e-1000);
    }

}