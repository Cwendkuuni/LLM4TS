import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.SimpleRegularStepEvent;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test suite for the AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}.
     */
    @Test
    public void testIntegration() {
        final double t0 = 0.0;
        final double[] y0 = {1.0, 1.0};
        final double t = 2.0;

        // define equations to integrate
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            /** {@inheritDoc} */
            public int getDimension() {
                return y0.length;
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y.length; i++) {
                    yDot[i] = y[i] * (1 - y[i]);
                }
            }
        };

        // create integrator instance
        final AbstractIntegrator integrator = new AbstractIntegrator("testIntegration") {
            /** {@inheritDoc} */
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not tested here
            }
        };

        // set equations and initialize integrator
        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.initIntegration(t0, y0, t);

        // integrate equations up to the target time
        final double[] y = new double[y0.length];
        integrator.integrate(equations, t);

        // check results
        Assert.assertArrayEquals(new double[]{1.27, 0.27}, y, 0.0);
        Assert.assertEquals(t, integrator.getCurrentStepStart(), 0.0);

    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test
    public void testAcceptStep() {
        final double t0 = 0.0;
        final double[] y0 = {1.0, 1.0};
        final double t = 1.0;

        // define equations and event to test
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            /** {@inheritDoc} */
            public int getDimension() {
                return y0.length;
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y.length; i++) {
                    yDot[i] = y[i] * (1 - y[i]);
                }
            }
        };
        final EventHandler eventHandler = new EventHandler() {
            /** {@inheritDoc} */
            public void handleEvent(double t, double[] y, double[] yDot) {
                // implementation not tested here
            }

            /** {@inheritDoc} */
            public double getEventTime() {
                return 0.5;
            }

            /** {@inheritDoc} */
            public boolean isRegular() {
                return true;
            }

            /** {@inheritDoc} */
            public SimpleRegularStepEvent getRegularStepEvent(double t, double[] y) {
                return new SimpleRegularStepEvent(t, y, 0.1);
            }
        };

        // create integrator instance
        final AbstractIntegrator integrator = new AbstractIntegrator("testAcceptStep") {
            /** {@inheritDoc} */
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not tested here
            }

            /** {@inheritDoc} */
            @Override
            protected void acceptStep(AbstractStepInterpolator interpolator,
                                      double[] y, double[] yDot, double tEnd)
                    throws MaxCountExceededException, DimensionMismatchException,
                           NoBracketingException {
                // implementation not tested here
            }

            /** {@inheritDoc} */
            @Override
            public void addEventHandler(EventHandler handler,
                                        double maxCheckInterval,
                                        double convergence,
                                        int maxIterationCount,
                                        UnivariateSolver solver) {
                super.addEventHandler(handler, maxCheckInterval, convergence,
                                      maxIterationCount, solver);
                this.eventHandler = handler;
            }

            private EventHandler eventHandler;
        };

        // set equations and initialize integrator with event
        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.addEventHandler(eventHandler, 0.1, 1.0e-8, 10, null);
        integrator.initIntegration(t0, y0, t);

        // check that event is detected and handled correctly
        integrator.integrate(new ExpandableStatefulODE(equations), 0.5);
        Assert.assertTrue(integrator.isLastStep());

    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}
     * with step handlers.
     */
    @Test
    public void testAcceptStepWithStephandlers() {
        final double t0 = 0.0;
        final double[] y0 = {1.0, 1.0};
        final double t = 1.0;

        // define equations to integrate
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            /** {@inheritDoc} */
            public int getDimension() {
                return y0.length;
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y.length; i++) {
                    yDot[i] = y[i] * (1 - y[i]);
                }
            }
        };

        // create integrator instance with step handlers
        final AbstractIntegrator integrator = new AbstractIntegrator("testAcceptStepWithStephandlers") {
            /** {@inheritDoc} */
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not tested here
            }

            /** {@inheritDoc} */
            @Override
            protected void acceptStep(AbstractStepInterpolator interpolator,
                                      double[] y, double[] yDot, double tEnd)
                    throws MaxCountExceededException, DimensionMismatchException,
                           NoBracketingException {
                // implementation not tested here
            }

            /** {@inheritDoc} */
            @Override
            public void addStepHandler(StepHandler handler) {
                super.addStepHandler(handler);
            }

            /** {@inheritDoc} */
            @Override
            public Collection<StepHandler> getStepHandlers() {
                return super.getStepHandlers();
            }

            /** {@inheritDoc} */
            @Override
            public void clearStepHandlers() {
                super.clearStepHandlers();
            }
        };

        // add step handlers
        integrator.addStepHandler(new StepHandler() {
            /** {@inheritDoc} */
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    Assert.assertArrayEquals(new double[]{1.5, 0.5},
                                            interpolator.getInterpolatedState(), 0.0);
                }
            }
        });
        integrator.addStepHandler(new StepHandler() {
            /** {@inheritDoc} */
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    Assert.assertArrayEquals(new double[]{1.3, 0.3},
                                            interpolator.getInterpolatedState(), 0.0);
                }
            }
        });

        // set equations and initialize integrator
        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.initIntegration(t0, y0, t);

        // integrate equations up to the target time
        integrator.integrate(equations, t);

        // check results
        Assert.assertArrayEquals(new double[]{1.3, 0.3}, integrator.getState().clone(), 0.0);

    }

    /**
     * Test method for {@link AbstractIntegrator#setMaxEvaluations(int)} and
     * {@link AbstractIntegrator#getMaxEvaluations()}.
     */
    @Test
    public void testMaxEvaluations() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testMaxEvaluations") {
            /** {@inheritDoc} */
            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                super.setMaxEvaluations(maxEvaluations);
                this.maxEvaluations = maxEvaluations;
            }

            private int maxEvaluations;
        };

        // check default value
        Assert.assertEquals(-1, integrator.getMaxEvaluations());

        // set and check maximum number of evaluations
        integrator.setMaxEvaluations(10);
        Assert.assertEquals(10, integrator.getMaxEvaluations());

    }

    /**
     * Test method for {@link AbstractIntegrator#resetOccurred()}.
     */
    @Test
    public void testResetOccurred() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testResetOccurred") {
            /** {@inheritDoc} */
            @Override
            protected void acceptStep(AbstractStepInterpolator interpolator,
                                      double[] y, double[] yDot, double tEnd)
                    throws MaxCountExceededException, DimensionMismatchException,
                           NoBracketingException {
                resetOccurred = true;
            }

            private boolean resetOccurred = false;
        };

        // test that reset is not triggered by default
        Assert.assertFalse(integrator.resetOccurred());

        // test that reset is triggered during integration
        integrator.integrate(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            /** {@inheritDoc} */
            public int getDimension() {
                return 0;
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        }), 1.0);
        Assert.assertTrue(integrator.resetOccurred());

    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized()}.
     */
    @Test
    public void testStateInitialized() {
        final AbstractIntegrator integrator = new AbstractIntegrator("testStateInitialized") {
            /** {@inheritDoc} */
            @Override
            protected boolean getStateInitialized() {
                return stateInitialized;
            }

            private boolean stateInitialized = false;

            /** {@inheritDoc} */
            @Override
            protected void setStateInitialized(boolean stateInitialized) {
                this.stateInitialized = stateInitialized;
            }
        };

        // test that state is not initialized by default
        Assert.assertFalse(integrator.isStateInitialized());

        // test that state is initialized during integration
        integrator.integrate(new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
            /** {@inheritDoc} */
            public int getDimension() {
                return 0;
            }

            /** {@inheritDoc} */
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        }), 1.0);
        Assert.assertTrue(integrator.isStateInitialized());

    }

}