import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testAbstractIntegrator() {
        // create an instance of the abstract integrator
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation of the integration method is not important for this test
            }
        };

        // set up some test equations
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        // set up the test data
        double t0 = 0.0;
        double[] y0 = new double[1];
        double tTarget = 1.0;

        // initialize the integrator
        integrator.initIntegration(t0, y0, tTarget);

        // set up the equations
        integrator.setEquations(new ExpandableStatefulODE(equations) {
            @Override
            public double getTime() {
                return t0;
            }

            @Override
            public double[] getPrimaryState() {
                return y0.clone();
            }

            @Override
            public void setTime(double time) {
                t0 = time;
            }

            @Override
            public void setPrimaryState(double[] state) {
                System.arraycopy(state, 0, y0, 0, y0.length);
            }
        });

        // integrate the equations
        try {
            integrator.integrate(tTarget);
            fail("Expected an exception");
        } catch (DimensionMismatchException e) {
            // expected exception
        } catch (NumberIsTooSmallException e) {
            // expected exception
        } catch (NoBracketingException e) {
            // expected exception
        } catch (MaxCountExceededException e) {
            // expected exception
        }

        // check some properties of the integrator
        assertFalse(integrator.isLastStep());
        assertFalse(integrator.resetOccurred());
        assertNull(integrator.getEventsStates());
        assertFalse(integrator.getEventHandlers().isEmpty());
        assertEquals(0.0, integrator.getCurrentStepStart(), 0.0);
        assertNull(integrator.getCurrentSignedStepsize());
        assertEquals(-1, integrator.getMaxEvaluations());
        assertEquals(0, integrator.getEvaluations());

        // integrate the equations again with a proper step size
        integrator.setMaxEvaluations(10);
        try {
            integrator.integrate(tTarget);
            // check integration result
            double y = integrator.getEquations().getPrimaryState()[0];
            assertEquals(Math.exp(0.1), y, 1e-6);
            assertEquals(tTarget, integrator.getTime(), 1e-6);
            // check some properties of the integrator
            assertTrue(integrator.isLastStep());
            assertTrue(integrator.resetOccurred);
            assertNotNull(integrator.getEventsStates());
            assertEquals(1, integrator.getEventsStates().size());
            assertFalse(integrator.getEventHandlers().isEmpty());
            assertEquals(tTarget, integrator.getCurrentStepStart(), 1e-6);
            assertEquals(integrator.getCurrentSignedStepsize(), 0.0);
            assertEquals(10, integrator.getMaxEvaluations());
            assertEquals(10, integrator.getEvaluations());
        } catch (DimensionMismatchException e) {
            // expected exception
        } catch (NumberIsTooSmallException e) {
            // expected exception
        } catch (NoBracketingException e) {
            // expected exception
        } catch (MaxCountExceededException e) {
            // expected exception
        }

        // integrate the equations with an event
        ExpandableStatefulODE equationsWithEvent = new ExpandableStatefulODE(equations) {
            @Override
            public double getTime() {
                return t0;
            }

            @Override
            public double[] getPrimaryState() {
                return y0.clone();
            }

            @Override
            public void setTime(double time) {
                t0 = time;
            }

            @Override
            public void setPrimaryState(double[] state) {
                System.arraycopy(state, 0, y0, 0, y0.length);
            }

            @Override
            public void addEventHandler(EventHandler eventHandler,
                                         double maxCheckInterval,
                                         double convergence,
                                         int maxIterationCount,
                                         UnivariateSolver solver) {
                super.addEventHandler(eventHandler, maxCheckInterval, convergence,
                                       maxIterationCount, solver);
                eventHandler.init(t0, y0, tTarget);
            }
        };
        integrator.setEquations(equationsWithEvent);

        // integrate the equations with an event
        try {
            integrator.integrate(tTarget);
            fail("Expected an exception");
        } catch (DimensionMismatchException e) {
            // expected exception
        } catch (NumberIsTooSmallException e) {
            // expected exception
        } catch (NoBracketingException e) {
            // expected exception
        } catch (MaxCountExceededException e) {
            // expected exception
        }

        // integrate the equations with an event
        integrator.setMaxEvaluations(10);
        try {
            integrator.integrate(tTarget);
            // check integration result
            double y = integrator.getEquations().getPrimaryState()[0];
            assertEquals(Math.exp(0.1) + 1.0, y, 1e-6);
            assertEquals(tTarget, integrator.getTime(), 1e-6);
            // check some properties of the integrator
            assertTrue(integrator.isLastStep());
            assertTrue(integrator.resetOccurred);
            assertNotNull(integrator.getEventsStates());
            assertEquals(1, integrator.getEventsStates().size());
            assertFalse(integrator.getEventHandlers().isEmpty());
            assertEquals(tTarget, integrator.getCurrentStepStart(), 1e-6);
            assertEquals(integrator.getCurrentSignedStepsize(), 0.0);
            assertEquals(10, integrator.getMaxEvaluations());
            assertEquals(10, integrator.getEvaluations());
            // check event state
            EventState eventState = integrator.getEventsStates().iterator().next();
            assertEquals(EventHandler.Type.ROOT_FINDING, eventState.getType());
            assertNotNull(eventState.getEventHandler());
            assertEquals(0.5, eventState.getEventHandler().getEventTime(), 1e-6);
            assertArrayEquals(new double[]{0.5}, eventState.getEventHandler().getEventX(), 1e-6);
            assertArrayEquals(new double[]{-0.5}, eventState.getEventHandler().getEventY(), 1e-6);
        } catch (DimensionMismatchException e) {
            // expected exception
        } catch (NumberIsTooSmallException e) {
            // expected exception
        } catch (NoBracketingException e) {
            // expected exception
        } catch (MaxCountExceededException e) {
            // expected exception
        }

        // integrate the equations with an event and a step handler
        ExpandableStatefulODE equationsWithEventAndHandler = new ExpandableStatefulODE(equations) {
            @Override
            public double getTime() {
                return t0;
            }

            @Override
            public double[] getPrimaryState() {
                return y0.clone();
            }

            @Override
            public void setTime(double time) {
                t0 = time;
            }

            @Override
            public void setPrimaryState(double[] state) {
                System.arraycopy(state, 0, y0, 0, y0.length);
            }

            @Override
            public void addEventHandler(EventHandler eventHandler,
                                         double maxCheckInterval,
                                         double convergence,
                                         int maxIterationCount,
                                         UnivariateSolver solver) {
                super.addEventHandler(eventHandler, maxCheckInterval, convergence,
                                       maxIterationCount, solver);
                eventHandler.init(t0, y0, tTarget);
            }

            @Override
            public void addStepHandler(StepHandler stepHandler) {
                super.addStepHandler(stepHandler);
                stepHandler.init(t0, y0, tTarget);
            }
        };
        integrator.setEquations(equationsWithEventAndHandler);

        // integrate the equations with an event and a step handler
        integrator.setMaxEvaluations(10);
        try {
            integrator.integrate(tTarget);
            // check integration result
            double y = integrator.getEquations().getPrimaryState()[0];
            assertEquals(Math.exp(0.1) + 2.0, y, 1e-6);
            assertEquals(tTarget, integrator.getTime(), 1e-6);
            // check some properties of the integrator
            assertTrue(integrator.isLastStep());
            assertTrue(integrator.resetOccurred);
            assertNotNull(integrator.getEventsStates());
            assertEquals(1, integrator.getEventsStates().size());
            assertFalse(integrator.getEventHandlers().isEmpty());
            assertEquals(tTarget, integrator.getCurrentStepStart(), 1e-6);
            assertEquals(integrator.getCurrentSignedStepsize(), 0.0);
            assertEquals(10, integrator.getMaxEvaluations());
            assertEquals(10, integrator.getEvaluations());
            // check event state
            EventState eventState = integrator.getEventsStates().iterator().next();
            assertEquals(EventHandler.Type.ROOT_FINDING, eventState.getType());
            assertNotNull(eventState.getEventHandler());
            assertEquals(0.5, eventState.getEventHandler().getEventTime(), 1e-6);
            assertArrayEquals(new double[]{0.5}, eventState.getEventHandler().getEventX(), 1e-6);
            assertArrayEquals(new double[]{-0.5}, eventState.getEventHandler().getEventY(), 1e-6);
            // check step handler state
            StepHandler stepHandlerState = integrator.getStepHandlers().iterator().next();
            assertTrue(stepHandlerState.isLastStep());
            assertEquals(1, stepHandlerState.getEncounteredSteps());
        } catch (DimensionMismatchException e) {
            // expected exception
        } catch (NumberIsTooSmallException e) {
            // expected exception
        } catch (NoBracketingException e) {
            // expected exception
        } catch (MaxCountExceededException e) {
            // expected exception
        }
    }

}