import static org.junit.Assert.*;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws Exception {
        // Test integrating a simple differential equation
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.4, 0.3}, {0.1, 0.2, 0.3}, {0.2, 0.1, 0.1}}, new double[]{0.5, 0.4, 0.3}, new MyRungeKuttaStepInterpolator(), 0.01, 0.05, null, null);
        double[] y0 = {1.0};
        double tStart = 0.0;
        double tEnd = 1.0;
        double[] yExpected = {1.718281828, 2.718281828};

        double result[] = integrator.integrate(new MyDifferentialEquations(), tStart, y0, tEnd, new double[y0.length]);

        assertArrayEquals("Error in integration result", yExpected, result);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        // Test integrating a differential equation with events
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.4, 0.3}, {0.1, 0.2, 0.3}, {0.2, 0.1, 0.1}}, new double[]{0.5, 0.4, 0.3}, new MyRungeKuttaStepInterpolator(), 0.01, 0.05, null, null);
        double[] y0 = {1.0};
        double tStart = 0.0;
        double tEnd = 2.0;

        StepHandler stepHandler = new MyStepHandler();

        integrator.setStepHandlers(Collections.singletonList(stepHandler));

        double[] yExpected = {1.0, 2.718281828, 6.283185307, 10.0};
        double[] expectedEvents = {0.549306145, 1.631383238};

        integrator.integrate(new MyDifferentialEquationsWithEvents(), tStart, y0, tEnd, new double[y0.length], stepHandler);

        assertArrayEquals("Error in integration result", yExpected, integrator.getSolutions());
        assertEquals("Error in event detection", 2, integrator.getNumberSteps());
        List<Event> events = integrator.getEvents();
        for (int i = 0; i < events.size(); i++) {
            assertEquals("Error in event time " + i, expectedEvents[i], ((When) events.get(i)).getTime(), 0.001);
        }
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        // Test integrating a differential equation that throws a DerivativeException
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.4, 0.3}, {0.1, 0.2, 0.3}, {0.2, 0.1, 0.1}}, new double[]{0.5, 0.4, 0.3}, new MyRungeKuttaStepInterpolator(), 0.01, 0.05, null, null);
        double[] y0 = {1.0};
        double tStart = 0.0;
        double tEnd = 1.0;

        integrator.setDifferentialEquations(new MyDifferentiableDifferentialEquationsWithException());

        integrator.integrate(new FirstOrderDifferentialEquations() {
            @Override
            public int getOrder() {
                return integrator.getOrder();
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("Derivative error");
            }
        }, tStart, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        // Test integrating a differential equation that throws an IntegratorException
        EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.5, 0.8}, new double[][]{{0.0, 0.4, 0.3}, {0.1, 0.2, 0.3}, {0.2, 0.1, 0.1}}, new double[]{0.5, 0.4, 0.3}, new MyRungeKuttaStepInterpolator(), 0.01, 0.05, null, null);
        double[] y0 = {1.0};
        double tStart = 0.0;
        double tEnd = 1.0;

        integrator.setDifferentialEquations(new MyExceptionalDifferentialEquations());

        try {
            integrator.integrate(new FirstOrderDifferentialEquations() {
                @Override
                public int getOrder() {
                    return integrator.getOrder();
                }
            }, tStart, y0, tEnd, new double[y0.length]);
            fail();
        } catch (IntegratorException e) {
            // Expected
        }
    }

}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = Math.exp(y[0]);
    }
}

class MyDifferentialEquationsWithEvents implements FirstOrderDifferentialEquations {
    private static final double EVENT_THRESHOLD = 2.0;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = Math.exp(y[0]);
    }

    @Override
    public List<Event> getEvents(double t, double[] y, double h) {
        List<Event> events = new ArrayList<>();

        if (y[0] >= EVENT_THRESHOLD) {
            events.add(new When(t + h, this::handleEvent));
        }

        return events;
    }

    private void handleEvent() {
        // Handle event logic here
    }
}

class MyDifferentialEquationsWithException implements FirstOrderDifferentialEquations {
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new DerivativeException("Derivative error");
    }
}

class MyExceptionalDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new IntegratorException("Integrator error");
    }
}

class MyRungeKuttaStepInterpolator extends RungeKuttaStepInterpolator {
    @Override
    protected void initialize(RungeKuttaIntegrator integrator, double[] yTmp, double[][] yDotK, boolean forward) {
        super.initialize(integrator, yTmp, yDotK, forward);
    }
}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
    public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double[] scalAbsoluteTolerance, double[] scalRelativeTolerance) {
        super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double error = 0.0;

        for (int i = 0; i < y0.length; i++) {
            double diff = y1[i] - y0[i];
            error += diff * diff;
        }

        return Math.sqrt(error);
    }
}

class MyStepHandler implements StepHandler {
    private int count = 0;

    @Override
    public void handleStep(StepContext context, int stepNumber) {
        count++;
    }
}