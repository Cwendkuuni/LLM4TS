import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;

/**
 * Test cases for EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testValidConstructor() {
        final double[] c = {0.3, 0.5, 0.8};
        final double[][] a = {{0.0, 0.2, 0.3}, {0.0, 0.4, 0.7}, {0.0, 0.5, 0.9}};
        final double[] b = {0.1, 0.2, 0.3};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, 0.01, 0.1, null, null);
        assertEquals(true, integrator.isFsal());
        assertEquals(c.length + 1, integrator.getStages());
        assertEquals(0.3, integrator.getC()[0], 0.001);
        assertEquals(0.5, integrator.getC()[1], 0.001);
        assertEquals(0.8, integrator.getC()[2], 0.001);
        assertEquals(3, integrator.getOrder());
        assertEquals(0.9, integrator.getSafety(), 0.001);
        assertEquals(0.2, integrator.getMinReduction(), 0.001);
        assertEquals(10.0, integrator.getMaxGrowth(), 0.001);
    }

    /**
     * Test constructor with null prototype.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullPrototype() {
        final double[] c = {0.3, 0.5, 0.8};
        final double[][] a = {{0.0, 0.2, 0.3}, {0.0, 0.4, 0.7}, {0.0, 0.5, 0.9}};
        final double[] b = {0.1, 0.2, 0.3};

        new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, null, 0.01, 0.1, null, null);
    }

    /**
     * Test getOrder method.
     */
    @Test
    public void testGetOrder() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, null, null, null, null, 0.01, 0.1, null, null);
        assertEquals(0, integrator.getOrder());

        final double[] c = {0.3, 0.5, 0.8};
        final double[][] a = {{0.0, 0.2, 0.3}, {0.0, 0.4, 0.7}, {0.0, 0.5, 0.9}};
        final double[] b = {0.1, 0.2, 0.3};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);
        integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, 0.01, 0.1, null, null);
        assertEquals(3, integrator.getOrder());
    }

    /**
     * Test getSafety method.
     */
    @Test
    public void testGetSafety() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, null, null, null, null, 0.01, 0.1, null, null);
        assertEquals(0.9, integrator.getSafety(), 0.001);

        final double[] c = {0.3, 0.5, 0.8};
        final double[][] a = {{0.0, 0.2, 0.3}, {0.0, 0.4, 0.7}, {0.0, 0.5, 0.9}};
        final double[] b = {0.1, 0.2, 0.3};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);
        integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, 0.01, 0.1, null, null);
        assertEquals(0.9, integrator.getSafety(), 0.001);

        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    /**
     * Test setSafety method.
     */
    @Test
    public void testSetSafety() {
        final EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, null, null, null, null, 0.01, 0.1, null, null);
        integrator.setSafety(0.8);
        assertEquals(0.8, integrator.getSafety(), 0.001);
    }

    /**
     * Test integrate method with valid arguments.
     */
    @Test
    public void testIntegrateValidArguments() throws Exception {
        final double[] c = {0.3, 0.5, 0.8};
        final double[][] a = {{0.0, 0.2, 0.3}, {0.0, 0.4, 0.7}, {0.0, 0.5, 0.9}};
        final double[] b = {0.1, 0.2, 0.3};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, 0.01, 0.1, null, null);

        final double[] y0 = {1.0, 2.0};
        final double t0 = 0.0;
        final double t1 = 1.0;

        final FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);

        assertEquals(t1, result, 0.001);
    }

    /**
     * Test integrate method with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntegrateInvalidArguments() throws Exception {
        final double[] c = {0.3, 0.5, 0.8};
        final double[][] a = {{0.0, 0.2, 0.3}, {0.0, 0.4, 0.7}, {0.0, 0.5, 0.9}};
        final double[] b = {0.1, 0.2, 0.3};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, 0.01, 0.1, null, null);

        final FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        integrator.integrate(equations, Double.NaN, null, 0.0, new double[0]);
    }

    /**
     * Test integrate method with events.
     */
    @Test
    public void testIntegrateWithEvents() throws Exception {
        final double[] c = {0.3, 0.5, 0.8};
        final double[][] a = {{0.0, 0.2, 0.3}, {0.0, 0.4, 0.7}, {0.0, 0.5, 0.9}};
        final double[] b = {0.1, 0.2, 0.3};
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true, c, a, b, prototype, 0.01, 0.1, null, null);

        final double[] y0 = {1.0, 2.0};
        final double t0 = 0.0;

        final FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

        final StepHandler handler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                assertEquals(3, context.getStepNumber());
                assertEquals(1.5, context.getStepSize(), 0.001);
                assertEquals(1.1, context.getLocalErrorEstimate(), 0.001);
                assertEquals(1.2, context.getLocalErrorActual());
                assertEquals(1.5, context.getYLocal(), 0.001);
                assertEquals(2.1, context.getY(), 0.001);
            }
        };

        final Event event = new When(1.5);
        integrator.addStepHandler(handler);
        integrator.addEventHandler(event);

        double result = integrator.integrate(equations, t0, y0, 2.0, new double[y0.length]);

        assertEquals(2.0, result, 0.001);
    }

}

class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
        yPrime[0] += 0.1 * y[0];
        yPrime[1] += 0.1 * y[1];
    }

}