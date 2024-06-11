package org.apache.commons.math.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.util.UnmodifiableODEStatefulSolver;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.events.WhenReached;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.special.Erf;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    /** Test integrator. */
    private AbstractIntegrator integrator;

    /** Differential equations. */
    private FirstOrderDifferentialEquations equations;

    /** Step interpolator. */
    private AbstractStepInterpolator interpolator;

    /** Step handlers. */
    private List<StepHandler> stepHandlers;

    /** Event states. */
    private List<EventState> eventStates;

    /** Constructor. */
    public AbstractIntegratorTest() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // we don't need to implement this method as we'll use a pre-built solver
            }
        };
        equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 3 * y[0] - 2 * t;
            }
        });
        interpolator = new UnmodifiableODEStatefulSolver(new BrentSolver(0.001));
        stepHandlers = new ArrayList<>();
        eventStates = new ArrayList<>();
    }

    /** Set up the tests. */
    @Before
    public void setUp() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                System.out.println("Step handler 1: t=" + interpolator.getGlobalCurrentTime());
            }
        });
        integrator.addEventHandler(new WhenReached(0.5), new Event() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot,
                                       double[] events, int index)
                    throws EventException {
                if (index == 0) {
                    throw new EventException("Test event 1");
                }
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                System.out.println("Test event 1: t=" + t);
            }

            @Override
            public double getEventTime() {
                return 0.5;
            }
        });
        integrator.addEventHandler(new WhenReached(1.0), new Event() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot,
                                       double[] events, int index)
                    throws EventException {
                if (index == 0) {
                    throw new EventException("Test event 2");
                }
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                System.out.println("Test event 2: t=" + t);
            }

            @Override
            public double getEventTime() {
                return 1.0;
            }
        });
    }

    /** Test basic functionality. */
    @Test
    public void testBasicFunctionality() {
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(100);
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                System.out.println("Step handler: t=" + interpolator.getGlobalCurrentTime());
            }
        });
        integrator.integrate(equations, 0.0, new double[1], 2.0);
        assertEquals("Unexpected last step indicator", false, integrator.isLastStep());
        assertEquals("Unexpected number of evaluations", 78, integrator.getEvaluations());
    }

    /** Test handling of events. */
    @Test
    public void testHandlingOfEvents() {
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(100);
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                System.out.println("Step handler: t=" + interpolator.getGlobalCurrentTime());
            }
        });
        integrator.addEventHandler(new WhenReached(0.5), new Event() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot,
                                       double[] events, int index)
                    throws EventException {
                if (index == 0) {
                    throw new EventException("Test event 1");
                }
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                System.out.println("Test event 1: t=" + t);
            }

            @Override
            public double getEventTime() {
                return 0.5;
            }
        });
        integrator.addEventHandler(new WhenReached(1.0), new Event() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot,
                                       double[] events, int index)
                    throws EventException {
                if (index == 0) {
                    throw new EventException("Test event 2");
                }
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                System.out.println("Test event 2: t=" + t);
            }

            @Override
            public double getEventTime() {
                return 1.0;
            }
        });
        integrator.integrate(equations, 0.0, new double[1], 2.0);
        assertEquals("Unexpected last step indicator", true, integrator.isLastStep());
        assertEquals("Unexpected number of evaluations", 78, integrator.getEvaluations());
        assertEquals("Unexpected number of events triggered", 2, integrator.getEventHandlers().size());
        stepHandlers.forEach(handler -> assertTrue("Step handler not called", handler.isCalled()));
    }

    /** Test edge cases. */
    @Test
    public void testEdgeCases() {
        integrator.setEquations(new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = Math.sqrt(FastMath.abs(y[0]));
            }
        });
        integrator.setMaxEvaluations(100);
        integrator.integrate(equations, 0.0, new double[1], FastMath.NEXT_DOUBLE * 1000.0);
        assertTrue("Unexpected number of evaluations (too few)", integrator.getEvaluations() > 100);
        integrator.reset();
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[1], FastMath.NEXT_DOUBLE);
        assertTrue("Unexpected number of evaluations (too many)", integrator.getEvaluations() <= 1);
    }

    /** Test exception scenarios. */
    @Test(expected = MathIllegalArgumentException.class)
    public void testExceptionScenarios() {
        integrator.setEquations(equations);
        integrator.integrate(equations, 1.0, new double[1], 1.0);
    }

    /** Test exception scenarios. */
    @Test(expected = NumberIsTooSmallException.class)
    public void testExceptionScenarios2() {
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1);
        integrator.integrate(equations, 0.0, new double[1], 1.0);
    }

    /** Test exception scenarios. */
    @Test(expected = EventException.class)
    public void testExceptionScenarios3() {
        integrator.setEquations(equations);
        integrator.addEventHandler(new WhenReached(0.0), new Event() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot,
                                       double[] events, int index)
                    throws EventException {
                throw new EventException("Test event");
            }

            @Override
            public void stepAccepted(double t, double[] y) {
            }

            @Override
            public double getEventTime() {
                return 0.0;
            }
        });
        integrator.integrate(equations, 0.0, new double[1], 1.0);
    }

    /** Test exception scenarios. */
    @Test(expected = MathIllegalStateException.class)
    public void testExceptionScenarios4() {
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(100);
        integrator.addEventHandler(new EventState() {
            @Override
            public double getEventTime() {
                return 0.0;
            }

            @Override
            public void stepAccepted(double t, double[] y) {
                throw new MathIllegalStateException("Test exception");
            }
        });
        integrator.integrate(equations, 0.0, new double[1], 1.0);
    }

}