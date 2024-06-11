import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        equations = new MyDifferentialEquations();
        integrator = new MyEmbeddedRungeKuttaIntegrator(
                         "MyIntegrator",
                         true,
                         0.3,
                         new double[] {0.33333333333333333, 0.2, 0.66666666666666666},
                         new double[] {0, 0.4, 0.5, 0.5},
                         new RungeKuttaStepInterpolator(),
                         0.001, 10.0,
                         null, null);
    }

    @Test
    public void testIntegrateBasic() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        try {
            double result = integrator.integrate(equations, t0, y0, t1, y1);
            Assert.assertEquals(t1, result, 1e-6);
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration should have succeeded.");
        }
    }

    @Test
    public void testIntegrateWithEvents() {
        StepHandler eventHandler = new MyStepHandler();
        integrator.setStepHandlers(eventHandler);

        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        try {
            double result = integrator.integrate(equations, t0, y0, t1, y1);
            Assert.assertEquals(t1, result, 1e-6);
            Assert.assertTrue(eventHandler.isStepFired());
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration should have succeeded.");
        }
    }

    @Test
    public void testIntegrateWithSmallStep() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        integrator.setMinStep(0.01);

        try {
            double result = integrator.integrate(equations, t0, y0, t1, y1);
            Assert.assertEquals(t1, result, 1e-6);
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration should have succeeded.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeMinStep() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        integrator.setMinStep(-0.01);

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            Assert.fail("Integration should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeMaxStep() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        integrator.setMaxStep(-0.01);

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            Assert.fail("Integration should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIntegrateWithSmallAbsoluteTolerance() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        integrator.setScalAbsoluteTolerance(1e-10);

        try {
            double result = integrator.integrate(equations, t0, y0, t1, y1);
            Assert.assertEquals(t1, result, 1e-9);
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration should have succeeded.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeAbsoluteTolerance() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        integrator.setScalAbsoluteTolerance(-1e-10);

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            Assert.fail("Integration should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIntegrateWithSmallRelativeTolerance() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        integrator.setScalRelativeTolerance(1e-10);

        try {
            double result = integrator.integrate(equations, t0, y0, t1, y1);
            Assert.assertEquals(t1, result, 1e-9 * Math.max(Math.abs(y0[0]), Math.abs(y0[1])));
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration should have succeeded.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateWithNegativeRelativeTolerance() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        integrator.setScalRelativeTolerance(-1e-10);

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            Assert.fail("Integration should have thrown an exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testResetInternalState() {
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] y1 = new double[y0.length];

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration should have succeeded.");
        }

        integrator.resetInternalState();

        try {
            integrator.integrate(equations, t0, y0, t1, y1);
            Assert.assertEquals(t1, y1[0], 1e-6);
            Assert.assertEquals(t1, y1[1], 1e-6);
        } catch (DerivativeException | IntegratorException e) {
            Assert.fail("Integration should have succeeded.");
        }
    }
}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getNumberOfEquations() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        yPrime[0] = y[1];
        yPrime[1] = 2.0 * y[1];
    }

}

class MyStepHandler implements StepHandler {

    private boolean fired = false;

    @Override
    public void handleStep(StepHandler handler, int stepNumber) {
        fired = true;
    }

    public boolean isStepFired() {
        return fired;
    }

}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

    // Empty constructor for testing

}