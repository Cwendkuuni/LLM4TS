import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegration() throws Exception {
        double[] y0 = {0.0, 0.0};
        double[][] params = {
            {0.03, 0.4, 0.0, 0.074, 0.0, 0.3, 0.2, 0.6, 0.1, 0.1, 0.1, 0.1},
            {0.03, 0.4, 0.0, 0.09, 0.0, 0.3, 0.2, 0.6, 0.1, 0.1, 0.1, 0.1},
            {0.03, 0.4, 0.0, 0.126, 0.0, 0.3, 0.2, 0.6, 0.1, 0.1, 0.1, 0.1}
        };

        for (double[] param : params) {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.0, 0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.25}, {0.25, 0}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(),
                0.001, 0.01,
                null, null);
            integrator.setSafety(0.99);
            integrator.setMinReduction(0.1);
            integrator.setMaxGrowth(1.01);

            FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
            double tEnd = 1.0;
            double[] yEnd = {1.0, 1.0};

            double t = 0.0;
            double[] y = y0.clone();

            StepHandler eventHandler = new StepCounter(10);
            integrator.addStepHandler(eventHandler);

            try {
                double result = integrator.integrate(equations, t, y, tEnd, yEnd);
                assertEquals(tEnd, result, 1e-12);
                assertArrayEquals(yEnd, y, 1e-12);
            } finally {
                integrator.resetInternalState();
            }

            for (Event event : eventHandler.getEvents()) {
                assertEquals(event.getTime(), t + stepSize * event.getMultiplicity(), 1e-12);
                assertEquals(event.getValue(), y[0], 1e-12);
            }
        }
    }

    @Test
    public void testErrorHandling() throws Exception {
        double[] y0 = {0.0, 0.0};
        double[][] params = {
            {0.03, 0.4, 0.0, 0.074, 0.0, 0.3, 0.2, 0.6, 0.1, 0.1, 0.1, 0.1},
            {0.03, 0.4, 0.0, 0.09, 0.0, 0.3, 0.2, 0.6, 0.1, 0.1, 0.1, 0.1}
        };

        for (double[] param : params) {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.0, 0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.25}, {0.25, 0}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(),
                0.001, 0.01,
                null, null);
            integrator.setSafety(0.99);
            integrator.setMinReduction(0.1);
            integrator.setMaxGrowth(1.01);

            FirstOrderDifferentialEquations equations = new DummyDifferentialEquationsWithException();

            try {
                integrator.integrate(equations, 0.0, new double[]{0.0, 0.0}, 1.0, new double[]{1.0, 1.0});
                fail("Expected IntegratorException");
            } catch (IntegratorException e) {
                // expected
            }
        }
    }

    @Test
    public void testStepSizeControl() throws Exception {
        double[] y0 = {0.0, 0.0};
        double[][] params = {
            {0.03, 0.4, 0.0, 0.074, 0.0, 0.3, 0.2, 0.6, 0.1, 0.1, 0.1, 0.1},
            {0.03, 0.4, 0.0, 0.09, 0.0, 0.3, 0.2, 0.6, 0.1, 0.1, 0.1, 0.1}
        };

        for (double[] param : params) {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
                new double[]{0.0, 0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0.25}, {0.25, 0}},
                new double[]{0.25, 0.25, 0.25, 0.25},
                new RungeKuttaStepInterpolator(),
                0.001, 0.01,
                null, null);
            integrator.setSafety(0.99);
            integrator.setMinReduction(0.1);
            integrator.setMaxGrowth(1.01);

            FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
            double tEnd = 1.0;
            double[] yEnd = {1.0, 1.0};

            double t = 0.0;
            double[] y = y0.clone();

            StepHandler eventHandler = new StepCounter(10);
            integrator.addStepHandler(eventHandler);

            // test small step
            integrator.setMinStep(0.0001);
            try {
                double result = integrator.integrate(equations, t, y, tEnd, yEnd);
                fail("Expected IntegratorException");
            } catch (IntegratorException e) {
                // expected
            }
            integrator.resetInternalState();

            integrator.setMinStep(0.001);
            try {
                double result = integrator.integrate(equations, t, y, tEnd, yEnd);
                assertEquals(tEnd, result, 1e-12);
                assertArrayEquals(yEnd, y, 1e-12);
            } finally {
                integrator.resetInternalState();
            }

            // test large step
            integrator.setMaxStep(1.0);
            try {
                double result = integrator.integrate(equations, t, y, tEnd, yEnd);
                fail("Expected IntegratorException");
            } catch (IntegratorException e) {
                // expected
            }
            integrator.resetInternalState();

            integrator.setMaxStep(0.1);
            try {
                double result = integrator.integrate(equations, t, y, tEnd, yEnd);
                assertEquals(tEnd, result, 1e-12);
                assertArrayEquals(yEnd, y, 1e-12);
            } finally {
                integrator.resetInternalState();
            }

            // test step size control
            integrator.setMinStep(0.01);
            integrator.setMaxStep(0.1);

            double stepSize = 0.1;
            try {
                integrator.integrate(equations, t, y, tEnd, yEnd);
                fail("Expected IntegratorException");
            } catch (IntegratorException e) {
                // expected
            }

            integrator.resetInternalState();

            integrator.setSafety(0.5);
            integrator.setMinReduction(0.5);
            integrator.setMaxGrowth(2.0);

            try {
                integrator.integrate(equations, t, y, tEnd, yEnd);
                fail("Expected IntegratorException");
            } catch (IntegratorException e) {
                // expected
            }

            integrator.resetInternalState();

            integrator.setMinStep(0.01);
            integrator.setMaxStep(0.1);

            double tPrev = 0.0;
            double error = 0.0;
            for (int i = 0; i < 10; ++i) {
                double tNext = tPrev + stepSize;
                try {
                    integrator.integrate(equations, tPrev, y, tNext, y);
                    error = integrator.getLastError();

                    assertTrue(error < 1.0);
                    assertTrue(error > 0.0);

                    tPrev = tNext;

                } catch (IntegratorException e) {
                    fail("Unexpected IntegratorException");
                }

                assertEquals(stepSize, integrator.getLastStep(), 1e-12);

                integrator.resetInternalState();
            }
        }
    }
}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = y[0];
    }
}

class DummyDifferentialEquationsWithException implements FirstOrderDifferentialEquations {
    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        throw new RuntimeException("Test exception");
    }
}

class StepCounter implements StepHandler {
    private int counter;
    private int maxCount;

    public StepCounter(int maxCount) {
        this.maxCount = maxCount;
    }

    @Override
    public void handleStep(StepHandler event, int stepNumber) {
        if (stepNumber > counter) {
            counter = stepNumber;
        }
    }

    public int getEventsCount() {
        return counter;
    }

    public Event getEvent(int index) {
        return new Event() {
            @Override
            public double getTime() {
                return stepSize * index;
            }

            @Override
            public Object getValue() {
                return new double[]{y[0], y[1]};
            }

            @Override
            public boolean isRegularArrivalType() {
                return true;
            }

            @Override
            public boolean isFirstArrivalType() {
                return index == 0;
            }
        };
    }

    public Event[] getEvents() {
        Event[] events = new Event[counter + 1];
        for (int i = 0; i <= counter; ++i) {
            events[i] = getEvent(i);
        }
        return events;
    }

    public void reset() {
        counter = 0;
    }
}