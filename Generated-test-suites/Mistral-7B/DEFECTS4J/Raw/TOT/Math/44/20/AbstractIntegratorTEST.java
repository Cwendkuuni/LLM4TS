import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testIntegration() {
        // create a simple differential equation
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[1];
                yDot[1] = -0.1 * y[1];
            }
        };

        // create an integrator instance
        AbstractIntegrator integrator = new EulerIntegrator(0.001);

        // set the differential equations and initial values
        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.setInitialValues(new double[] {0.1, 0.2});

        // integrate up to t = 1.0
        try {
            double t = integrator.integrate(equations, 0.0, new double[] {0.1, 0.2}, 1.0);
            assertEquals(1.0, t, 1e-6);
            assertArrayEquals(new double[] {0.2, -0.1}, integrator.getPrimaryState(), 1e-6);
        } catch (Exception e) {
            fail("Integration should not have failed: " + e.getMessage());
        }

        // integrate backwards up to t = 0.0
        try {
            double t = integrator.integrate(equations, 1.0, new double[] {0.2, -0.1}, -1.0);
            assertEquals(0.0, t, 1e-6);
            assertArrayEquals(new double[] {0.1, 0.2}, integrator.getPrimaryState(), 1e-6);
        } catch (Exception e) {
            fail("Integration should not have failed: " + e.getMessage());
        }
    }

    @Test
    public void testEventHandler() {
        // create a simple differential equation
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }
        };

        // create an event handler that stops integration when y crosses 1.0
        final EventHandler eventHandler = new EventHandler() {
            private double previousValue = Double.NEGATIVE_INFINITY;

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void initialize(double t, double[] y) {
                previousValue = y[0];
            }

            @Override
            public EventState evaluateEvent(double t, double[] y) {
                if (y[0] > 1.0) {
                    return new EventState(this, 0.0, 1.0, 10);
                }
                return null;
            }

            @Override
            public void handleEvent(double t, double[] y) {
                // do something when the event is triggered
                System.out.println("Event detected: y = " + y[0]);
            }

            @Override
            public void reset() {
                previousValue = Double.NEGATIVE_INFINITY;
            }

        };

        // create an integrator instance and add the event handler
        AbstractIntegrator integrator = new RungeKuttaFehlbergIntegrator(0.001);
        integrator.addEventHandler(eventHandler, 0.01, 1.0, 10);

        // set the differential equations and initial values
        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.setInitialValues(new double[] {0.5});

        // integrate up to t = 2.0
        try {
            double t = integrator.integrate(equations, 0.0, new double[] {0.5}, 2.0);
            assertTrue("Integration should have stopped
                due to an event", integrator.isLastStep());
            assertArrayEquals(new double[] {1.0}, integrator.getPrimaryState(), 1e-6);
            assertEquals(2.0, t, 1e-6);
        } catch (Exception e) {
            fail("Integration should not have failed: " + e.getMessage());
        }

        // check that the event handler was triggered
        assertTrue("Event handler should have been triggered", integrator.resetOccurred);
    }

    @Test
    public void testStateReset() {
        // create a simple differential equation
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0];
            }
        };

        // create an integrator instance and add an event handler that resets the state
        AbstractIntegrator integrator = new RungeKuttaFehlbergIntegrator(0.001);
        integrator.addEventHandler(new ResetEventHandler(), 0.01, 1.0, 10);

        // set the differential equations and initial values
        integrator.setEquations(new ExpandableStatefulODE(equations));
        integrator.setInitialValues(new double[] {0.5});

        // integrate up to t = 1.0
        try {
            double t = integrator.integrate(equations, 0.0, new double[] {0.5}, 1.0);
            assertArrayEquals(new double[] {0.5}, integrator.getPrimaryState(), 1e-6);
            assertEquals(1.0, t, 1e-6);
        } catch (Exception e) {
            fail("Integration should not have failed: " + e.getMessage());
        }

        // integrate up to t = 1.1
        try {
            double t = integrator.integrate(equations, 1.0, new double[] {0.5}, 1.1);
            assertArrayEquals(new double[] {0.5}, integrator.getPrimaryState(), 1e-6);
            assertEquals(1.1, t, 1e-6);
        } catch (Exception e) {
            fail("Integration should not have failed: " + e.getMessage());
        }

        // integrate up to t = 1.2
        try {
            double t = integrator.integrate(equations, 1.1, new double[] {0.5}, 1.2);
            assertArrayEquals(new double[] {0.0}, integrator.getPrimaryState(), 1e-6);
            assertEquals(1.2, t, 1e-6);
        } catch (Exception e) {
            fail("Integration should not have failed: " + e.getMessage());
        }

        // integrate up to t = 1.3
        try {
            double t = integrator.integrate(equations, 1.2, new double[] {0.0}, 1.3);
            assertArrayEquals(new double[] {0.0}, integrator.getPrimaryState(), 1e-6);
            assertEquals(1.3, t, 1e-6);
        } catch (Exception e) {
            fail("Integration should not have failed: " + e.getMessage());
        }

    }

    private static class ResetEventHandler implements EventHandler {
        @Override
        public void reset() {
            // no-op
        }

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public EventState evaluateEvent(double t, double[] y) {
            // no event to trigger
            return null;
        }

        @Override
        public void handleEvent(double t, double[] y) {
            // no action to take
        }
    }

}