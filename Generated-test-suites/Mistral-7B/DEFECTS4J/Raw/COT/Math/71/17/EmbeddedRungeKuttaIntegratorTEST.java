import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.ModelParameterChangeEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations model;
    private double[] initialValues;

    @Before
    public void setUp() {
        // Set up a simple model for testing
        model = new SimpleDifferentialEquations();
        initialValues = new double[]{0.1, 0.2};
        integrator = new RungeKuttaFehlberg78Method(1.0e-10, 1.0e-8, 7, new RungeKuttaStepInterpolator(new AbstractRungeKuttaMethod() {
            @Override
            public int getOrder() {
                return 7;
            }
        }));
        integrator.setMinStep(1.0e-8);
        integrator.setMaxStep(1.0e2);
    }

    @Test
    public void testIntegrateBasicFunctionality() throws Exception {
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y0 = initialValues;
        double[] y1 = integrator.integrate(model, t0, y0, t1, new double[initialValues.length]);
        assertArrayEquals("The computed y is not the expected one.", initialValues, y1);
    }

    @Test(expected = DerivativeException.class)
    public void testIntegrateWithDerivativeException() throws Exception {
        // Create a model with a method that throws a DerivativeException
        model = new ModelWithDerivativeException();
        double[] initialValues = {0.1, 0.2};
        integrator.integrate(model, 0.0, initialValues, 1.0, new double[initialValues.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegrateWithIntegratorException() throws Exception {
        // Create a model with a method that throws an IntegratorException
        model = new ModelWithIntegratorException();
        double[] initialValues = {0.1, 0.2};
        integrator.integrate(model, 0.0, initialValues, 1.0, new double[initialValues.length]);
    }

    @Test
    public void testIntegrateWithEvents() throws Exception {
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y0 = initialValues;

        // Set up a ModelParameterChangeEvent
        double eventTime = 0.5;
        double eventValue = 1.0;
        EventManager eventManager = new EventManager();
        eventManager.addEvent(new ModelParameterChangeEvent(eventTime, 0, eventValue));
        model = new ModelWithEvent(eventManager);

        // Set up a step handler to check the event
        double[] expectedY1 = new double[]{1.2, 2.4}; // y = (1.0 + t) * (0.1, 0.2)
        StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(StepContext context, int stepNumber) {
                double[] y = context.getY();
                assertEquals("Unexpected value of y.", expectedY1[0], y[0]);
                assertEquals("Unexpected value of y'.", expectedY1[1], y[1]);
            }
        };
        integrator.addStepHandler(eventHandler);

        double[] y1 = integrator.integrate(model, t0, y0, t1, new double[initialValues.length]);
        assertArrayEquals("The computed y is not the expected one.", initialValues, y1);
        assertEquals("The event handler was not called.", stepNumber, 1);
    }

    @Test
    public void testIntegrateWithStepSizeControl() throws Exception {
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y0 = initialValues;

        integrator = new RungeKuttaFehlberg78Method(1.0e-10, 1.0e-8, 7, new RungeKuttaStepInterpolator(new AbstractRungeKuttaMethod() {
            @Override
            public int getOrder() {
                return 7;
            }

            @Override
            public double estimateError(double[] yDotK, double[] y0, double[] y1, double h) {
                double error = 1.0;
                if (h > 0.5) {
                    error /= 2.0;
                }
                return error;
            }
        }));
        integrator.setMinStep(1.0e-6);
        integrator.setMaxStep(1.0e-2);
        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(10.0);
        integrator.setSafety(0.9);

        double[] y1 = integrator.integrate(model, t0, y0, t1, new double[initialValues.length]);
        assertArrayEquals("The computed y is not the expected one.", initialValues, y1);
    }

}

class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = 2.0 * y[0];
    }

}

class ModelWithDerivativeException implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new DerivativeException("Test DerivativeException.");
    }

}

class ModelWithIntegratorException implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        throw new IntegratorException("Test IntegratorException.");
    }

}

class ModelWithEvent implements FirstOrderDifferentialEquations {

    private final EventManager eventManager;

    public ModelWithEvent(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        if (eventManager.hasNextEvent()) {
            Event event = eventManager.nextEvent();
            if (event instanceof ModelParameterChangeEvent) {
                ModelParameterChangeEvent e = (ModelParameterChangeEvent) event;
                yPrime[0] = e.getValue();
                yPrime[1] = 2.0 * y[0];
            }
            eventManager.nextEventConsumed();
        } else {
            yPrime[0] = 1.0 + t;
            yPrime[1] = 2.0 * y[0];
        }
    }

}