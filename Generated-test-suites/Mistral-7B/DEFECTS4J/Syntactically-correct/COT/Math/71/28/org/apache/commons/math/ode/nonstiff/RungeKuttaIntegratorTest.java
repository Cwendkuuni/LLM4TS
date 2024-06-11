import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultValueEvent;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventsManager;
import org.apache.commons.math3.ode.events.SimpleEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setup() {
        double[] c = {0.23692688505667133, 0.47394531511816467, 0.8339921698591441};
        double[][] a = {{1.0 / 3.0, 0.0, 1. / 3.}, {1.0 / 4.0, 0.25, 0.25}, {0.5, 0.0, 0.0}};
        double[] b = {1. / 6.0, 1. / 3.0, 1. / 2.};

        integrator = new RungeKuttaIntegrator("RK3", c, a, b, new RungeKuttaStepInterpolator(), 0.01) {
            @Override
            public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                throw new UnsupportedOperationException();
            }
        };

        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * Math.exp(-1);
            }
        };
    }

    @Test
    public void testIntegration() throws Exception {
        double[] initialValues = {1.0};
        double startTime = 0.0;
        double finalTime = 1.0;

        integrator.addEvent(new SimpleEvent(finalTime, new DefaultValueEvent(initialValues[0] * Math.exp(-1)), false));

        double[] result = new double[initialValues.length];
        double step = 0.01;
        integrator.integrate(equations, startTime, initialValues, finalTime, result);

        double expectedResult = initialValues[0] * Math.exp(-1);
        Assert.assertArrayEquals(expectedResult, result, 0.001);
    }

    @Test(expected = IntegratorException.class)
    public void testEventDetection() throws Exception {
        double[] initialValues = {1.0};
        double startTime = 0.0;
        double finalTime = 1.0;

        integrator.addEvent(new SimpleEvent(finalTime - 0.01, new DefaultValueEvent(initialValues[0] * Math.exp(-1)), false));

        integrator.integrate(equations, startTime, initialValues, finalTime, new double[initialValues.length]);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        double[] initialValues = {1.0 / 0.0};
        double startTime = 0.0;
        double finalTime = 1.0;

        integrator.integrate(equations, startTime, initialValues, finalTime, new double[initialValues.length]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArguments() {
        double[] initialValues = null;
        double startTime = 0.0;
        double finalTime = 1.0;

        try {
            integrator.integrate(equations, startTime, initialValues, finalTime, new double[initialValues.length]);
        } catch (Exception e) {
            Assert.assertNotNull(e.getCause());
            Assert.assertEquals("initialValues", e.getCause().getMessage());
        }

        try {
            integrator.integrate(equations, startTime, initialValues, finalTime, initialValues.length * new double[0]);
        } catch (Exception e) {
            Assert.assertNotNull(e.getCause());
            Assert.assertEquals("y", e.getCause().getMessage());
        }

        try {
            integrator.integrate(equations, startTime, initialValues, finalTime, null);
        } catch (Exception e) {
            Assert.assertNotNull(e.getCause());
            Assert.assertEquals("result", e.getCause().getMessage());
        }
    }
}