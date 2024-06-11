package org.apache.commons.math.ode.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultEventHandler;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.apache.commons.math3.special.BesselFunction;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the AbstractIntegrator class.
 */
public class AbstractIntegratorTest extends AbstractODETest {

    /** Integrator under test. */
    private AbstractIntegrator integrator;

    /** Test equations. */
    private FirstOrderDifferentialEquations equations;

    /** Test initial conditions. */
    private double[] y0;

    /** Test final time. */
    private double t;

    /** Test step size. */
    private double stepSize;

    /** Test event handler. */
    private DefaultEventHandler eventHandler;

    /** Test step handler. */
    private StepHandler stepHandler;

    /** Test solver for roots. */
    private BrentSolver solver;

    /** Test event. */
    private Event event;

    /** Test events states. */
    private List<EventState> eventsStates;

    /** Test events. */
    private List<Event> events;

    /** Test constructor. */
    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void addEventHandler(final EventHandler handler,
                                           final double maxCheckInterval,
                                           final double convergence,
                                           final int maxIterationCount,
                                           final UnivariateRealSolver solver) {
                super.addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, solver);
                this.solver = (BrentSolver) solver;
            }
        };

        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * (1 - y[0]);
            }
        };

        y0 = new double[] { 0.1 };
        t = 1.0;
        stepSize = 0.1;

        eventHandler = new DefaultEventHandler() {
            @Override
            public EventRate getEventRate(double t, double[] y) {
                return EventRate.RATE_CONTINUE;
            }
        };

        stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                assertEquals(0.1, interpolator.getGlobalCurrentTime(), 1e-14);
            }
        };

        event = new Event() {
            @Override
            public double getEventTime() {
                return 0.5;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean stop() {
                return true;
            }
        };

        eventsStates = new ArrayList<>();
        events = new ArrayList<>();

        integrator.addEventHandler(eventHandler, 0.1, 1e-10, 10, solver);
        integrator.addStepHandler(stepHandler);
    }

    /** Test constructor. */
    @Test
    public void testConstructor() {
        assertEquals("AbstractIntegrator", integrator.getName());
        assertEquals(Collections.emptyList(), integrator.getStepHandlers());
        assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0);
        assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0);
        assertEquals(false, integrator.isLastStep());
        assertEquals(false, integrator.resetOccurred());
        assertEquals(Collections.emptyList(), integrator.getEventHandlers());
        assertEquals(false, integrator.statesInitialized);
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());
    }

    /** Test addStepHandler method. */
    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(stepHandler);
        assertEquals(1, integrator.getStepHandlers().size());
    }

    /** Test addEventHandler method. */
    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(eventHandler, 0.1, 1e-10, 10, solver);
        assertEquals(1, integrator.getEventHandlers().size());
    }

    /** Test clearStepHandlers method. */
    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(stepHandler);
        integrator.clearStepHandlers();
        assertEquals(0, integrator.getStepHandlers().size());
    }

    /** Test clearEventHandlers method. */
    @Test
    public void testClearEventHandlers() {
        integrator.addEventHandler(eventHandler, 0.1, 1e-10, 10, solver);
        integrator.clearEventHandlers();
        assertEquals(0, integrator.getEventHandlers().size());
    }

    /** Test getCurrentStepStart method. */
    @Test
    public void testGetCurrentStepStart() {
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(0.0, integrator.getCurrentStepStart(), 0);
    }

    /** Test getCurrentSignedStepsize method. */
    @Test
    public void testGetCurrentSignedStepsize() {
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(stepSize, integrator.getCurrentSignedStepsize(), 0);
    }

    /** Test isLastStep method. */
    @Test
    public void testIsLastStep() {
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(false, integrator.isLastStep());
    }

    /** Test resetOccurred method. */
    @Test
    public void testResetOccurred() {
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(false, integrator.resetOccurred());
    }

    /** Test integrate method. */
    @Test
    public void testIntegrate() {
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(t, integrator.getCurrentStepStart(), 1e-14);
        assertEquals(y0[0], integrator.getPrimaryState()[0], 1e-14);
    }

    /** Test computeDerivatives method. */
    @Test
    public void testComputeDerivatives() {
        integrator.setEquations(equations);
        integrator.integrate(equations, 0.0, y0, t);
        integrator.computeDerivatives(t, integrator.getPrimaryState(), new double[1]);
        assertEquals(y0[0] * (1 - y0[0]), integrator.getPrimaryState()[0], 1e-14);
    }

    /** Test setMaxEvaluations method. */
    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /** Test getMaxEvaluations method. */
    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(10);
        assertEquals(10, integrator.getMaxEvaluations());
    }

    /** Test getEvaluations method. */
    @Test
    public void testGetEvaluations() {
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(0, integrator.getEvaluations());
        integrator.computeDerivatives(t, integrator.getPrimaryState(), new double[1]);
        assertEquals(1, integrator.getEvaluations());
    }

    /** Test setStateInitialized method. */
    @Test
    public void testsetStateInitialized() {
        integrator.setStateInitialized(true);
        assertEquals(true, integrator.statesInitialized);
    }

    /** Test getStateInitialized method. */
    @Test
    public void testGetStateInitialized() {
        integrator.setStateInitialized(true);
        assertEquals(true, integrator.statesInitialized);
    }

    /** Test integrate method with events. */
    @Test
    public void testIntegrateWithEvents() {
        integrator.setEquations(equations);
        integrator.addEventHandler(eventHandler, 0.1, 1e-10, 10, solver);
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(y0[0], integrator.getPrimaryState()[0], 1e-14);
        assertEquals(stepSize, integrator.getCurrentSignedStepsize(), 0);
        assertEquals(0.0, integrator.getCurrentStepStart(), 0);
        assertEquals(1, integrator.getEvaluations());
        assertThrows(MathIllegalStateException.class, () -> integrator.integrate(equations, 0.0, y0, t));
    }

    /** Test integrate method with events and solver. */
    @Test
    public void testIntegrateWithEventsAndSolver() {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = BesselFunction.sinh(y[0]);
            }
        });
        integrator.addEventHandler(eventHandler, 0.1, 1e-10, 10, new PolynomialFunction(1, -2));
        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(y0[0], integrator.getPrimaryState()[0], 1e-14);
        assertEquals(stepSize, integrator.getCurrentSignedStepsize(), 0);
        assertEquals(0.0, integrator.getCurrentStepStart(), 0);
        assertEquals(1, integrator.getEvaluations());
        assertThrows(MathIllegalStateException.class, () -> integrator.integrate(equations, 0.0, y0, t));
    }

    /** Test integrate method with events, solver, and step interpolator. */
    @Test
    public void testIntegrateWithEventsSolverAndInterpolator() {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = BesselFunction.sinh(y[0]);
            }
        });
        integrator.addEventHandler(eventHandler, 0.1, 1e-10, 10, new PolynomialFunction(1, -2));
        integrator.addStepHandler(stepHandler);
        integrator.setStepInterpolator(new StepInterpolator() {
            @Override
            public double interpolate(double t, double[] y) {
                return y[0] + 0.5;
            }
        });
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(y0[0] + 0.5, integrator.getPrimaryState()[0], 1e-14);
        assertEquals(stepSize, integrator.getCurrentSignedStepsize(), 0);
        assertEquals(0.0, integrator.getCurrentStepStart(), 0);
        assertEquals(1, integrator.getEvaluations());
        assertThrows(MathIllegalStateException.class, () -> integrator.integrate(equations, 0.0, y0, t));
    }

    /** Test integrate method with events, solver, step interpolator, and event. */
    @Test
    public void testIntegrateWithEventsSolverInterpolatorAndEvent() {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = BesselFunction.sinh(y[0]);
            }
        });
        integrator.addEventHandler(eventHandler, 0.1, 1e-10, 10, new PolynomialFunction(1, -2));
        integrator.addEventHandler(e -> {
            assertEquals(0.5, e.getEventTime(), 1e-14);
            assertEquals(1.0, e.getState()[0], 1e-14);
        }, new Event() {
            @Override
            public double getEventTime() {
                return 0.5;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                eventsStates.add(new EventState(eventHandler, 0.1, 1e-10, 10, solver));
            }

            @Override
            public boolean stop() {
                return true;
            }
        });
        integrator.addStepHandler(stepHandler);
        integrator.setStepInterpolator(new StepInterpolator() {
            @Override
            public double interpolate(double t, double[] y) {
                return y[0] + 0.5;
            }
        });
        integrator.integrate(equations, 0.0, y0, t);
        assertEquals(y0[0] + 0.5, integrator.getPrimaryState()[0], 1e-14);
        assertEquals(stepSize, integrator.getCurrentSignedStepsize(), 0);
        assertEquals(0.0, integrator.getCurrentStepStart(), 0);
        assertEquals(1, integrator.getEvaluations());
        assertEquals(1, integrator.eventsStates.size());
        assertEquals(event, integrator.eventsStates.get(0).getEventHandler());
        assertEquals(0.1, integrator.eventsStates.get(0).getMaxCheckInterval(), 1e-14);
        assertEquals(1e-10, integrator.eventsStates.get(0).getConvergence(), 1e-14);
        assertEquals(10, integrator.eventsStates.get(0).getMaxIterationCount());
        assertEquals(solver, integrator.eventsStates.get(0).getSolver());
    }

}