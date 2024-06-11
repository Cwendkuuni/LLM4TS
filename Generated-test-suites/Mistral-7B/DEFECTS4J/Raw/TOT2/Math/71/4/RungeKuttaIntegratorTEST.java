import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    @Test
    public void testRungeKuttaIntegrator() {
        // Test case for RungeKuttaIntegrator with simple differential equations
        double[] y0 = {0.0, 0.0};
        double t0 = 0.0;
        double[] y1 = {1.0, 1.0};
        double t1 = 1.0;
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }
        };

        RungeKuttaIntegrator integrator = new EulerIntegrator(0.1, new EulerStepInterpolator(), equations);
        double result = integrator.integrate(equations, t0, y0, t1, y1);
        assertEquals(t1, result, 1e-6);
        assertArrayEquals(y1, y0, 1e-6);

        // Test case for RungeKuttaIntegrator with exception handling
        try {
            integrator.integrate(equations, t0, y0, Double.NaN, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Expected
        }

        try {
            integrator.integrate(equations, t0, y0, t1, null);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected
        }

        // Test case for RungeKuttaIntegrator with events
        double[] y2 = {0.0, 0.0};
        double t2 = 0.0;
        double[] y3 = {1.0, 1.0};
        double t3 = 1.5;
        FirstOrderDifferentialEquations equationsWithEvent = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = y[1];
                dy[1] = -y[0];
            }

            @Override
            public Event[] getEvents(double t, double[] y, double[] yDot, double[] yDotDot) {
                if (t > 1.0 && t < 1.5) {
                    return new Event[] {new When(t, y[0], 0.0)};
                }
                return new Event[0];
            }
        };

        RungeKuttaIntegrator integratorWithEvent = new RungeKuttaIntegrator(
                0.1, new RungeKuttaStepInterpolator(), equationsWithEvent, new EulerStepInterpolator(), StepHandler.DO_NOTHING);

        integratorWithEvent.addEventDetectionHandler(event -> {
            System.out.println("Event detected at t = " + event.getTime());
        });

        double resultWithEvent = integratorWithEvent.integrate(equationsWithEvent, t2, y2, t3, y3);
        assertEquals(t3, resultWithEvent, 1e-6);
        assertArrayEquals(y3, y2, 1e-6);
        assertTrue(System.out.toString().contains("Event detected at t = 1.25"));
    }
}