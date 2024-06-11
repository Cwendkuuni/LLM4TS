import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventManager;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues;
    private EventManager eventManager;

    @Before
    public void setup() {
        integrator = new RungeKuttaFehlberg78MethodIntegrator(5.0, 0.0, new RungeKuttaStepInterpolator(5.0, 0.0));
        equations = new DummyDifferentialEquations();
        initialValues = new double[]{1.0, 1.0};
        eventManager = new EventManager();
    }

    @Test
    public void testIntegration() throws Exception {
        double start = 0.0;
        double end = 2.0;
        double[] result = integrator.integrate(equations, start, initialValues, end, new double[initialValues.length]);
        Assert.assertArrayEquals(new double[]{2.718281828459045, 2.718281828459045}, result, 1e-10);
        Assert.assertEquals(2.0, integrator.getStepCount(), 1);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        equations = new ExceptionThrowingDifferentialEquations();
        integrator.integrate(equations, 0.0, initialValues, 0.1, new double[initialValues.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        eventManager.addStepHandler(new StepCountReachingStepHandler(1));
        integrator = new RungeKuttaFehlberg78MethodIntegrator(5.0, 0.0, new RungeKuttaStepInterpolator(5.0, 0.0), eventManager);
        integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);
    }

    @Test
    public void testStepHandlers() throws Exception {
        eventManager.addStepHandler(new StepCounter());
        integrator = new RungeKuttaFehlberg78MethodIntegrator(5.0, 0.0, new RungeKuttaStepInterpolator(5.0, 0.0), eventManager);
        integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);
        Assert.assertEquals(3, eventManager.getNumStepsTaken());
    }

    @Test
    public void testMinimalStep() throws Exception {
        integrator = new RungeKuttaFehlberg78MethodIntegrator(5.0, 1.0e-12, new RungeKuttaStepInterpolator(5.0, 0.0));
        integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);
        Assert.assertEquals(2, integrator.getStepCount(), 1);
    }

    @Test
    public void testMaximalStep() throws Exception {
        integrator = new RungeKuttaFehlberg78MethodIntegrator(5.0, 100.0, new RungeKuttaStepInterpolator(5.0, 0.0));
        integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);
        Assert.assertEquals(1, integrator.getStepCount(), 1);
    }

    @Test
    public void testSafetyFactor() throws Exception {
        integrator = new RungeKuttaFehlberg78MethodIntegrator(5.0, 0.9, new RungeKuttaStepInterpolator(5.0, 0.0));
        integrator.integrate(equations, 0.0, initialValues, 2.0, new double[initialValues.length]);
        Assert.assertEquals(3, integrator.getStepCount(), 1);
    }

    private static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = y[0];
        }
    }

    private static class ExceptionThrowingDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            throw new DerivativeException("Test exception");
        }
    }

    private static class StepCounter implements StepHandler {

        private int stepCount = 0;

        @Override
        public void handleStep(StepHandlerContext context, int stepNumber) {
            stepCount++;
        }

        public int getNumStepsTaken() {
            return stepCount;
        }
    }

    private static class StepCountReachingStepHandler implements StepHandler {

        private final int stepNumber;

        public StepCountReachingStepHandler(int stepNumber) {
            this.stepNumber = stepNumber;
        }

        @Override
        public void handleStep(StepHandlerContext context, int stepNumber) {
            if (stepNumber == this.stepNumber) {
                throw new IntegratorException("Test exception");
            }
        }
    }
}