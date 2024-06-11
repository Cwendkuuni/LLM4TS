import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.events.EmptyEventManager;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator_constructor() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbedgedRungeKuttaIntegrator("test", true,
                new double[]{0.3, 0.6, 0.8, 1.0}, new double[][]{{0.237, 0.688}, {0.236, 0.678}},
                new double[]{0.2, 0.3, 0.4, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01,
                null, null) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0;
            }
        };
        assertNotNull(integrator);
    }

    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbedgedRungeKuttaIntegrator("test", true,
                new double[]{0.3, 0.6, 0.8, 1.0}, new double[][]{{0.237, 0.688}, {0.236, 0.678}},
                new double[]{0.2, 0.3, 0.4, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01,
                null, null) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0;
            }
        };
        assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true,
                new double[]{0.3, 0.6, 0.8, 1.0}, new double[][]{{0.237, 0.688}, {0.236, 0.678}},
                new double[]{0.2, 0.3, 0.4, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01,
                null, null) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0;
            }
        };
        assertFalse(integrator.getSafety() < 0.9);
    }

    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true,
                new double[]{0.3, 0.6, 0.8, 1.0}, new double[][]{{0.237, 0.688}, {0.236, 0.678}},
                new double[]{0.2, 0.3, 0.4, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01,
                null, null) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0;
            }
        };
        integrator.setSafety(0.8);
        assertTrue(integrator.getSafety() == 0.8);
    }

    @Test
    public void testIntegrate_noEvents() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true,
                new double[]{0.3, 0.6, 0.8, 1.0}, new double[][]{{0.237, 0.688}, {0.236, 0.678}},
                new double[]{0.2, 0.3, 0.4, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01,
                null, null) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0;
            }
        };

        double[] y0 = {1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        double result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        assertEquals(t1, result, 1e-10);
    }

    @Test
    public void testIntegrate_events() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] dy) {
                dy[0] = Math.exp(y[0]);
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true,
                new double[]{0.3, 0.6, 0.8, 1.0}, new double[][]{{0.237, 0.688}, {0.236, 0.678}},
                new double[]{0.2, 0.3, 0.4, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01,
                null, null) {
            @Override
            protected int getOrder() {
                return 4;
            }

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0;
            }
        };

        EmptyEventManager eventManager = new EmptyEventManager();

        double[] y0 = {1.0};
        double t0 = 0.0;
        double t1 = 1.0;

        double result = integrator.integrate(equations, t0, y0, t1, y0, eventManager);
        assertEquals(t1, result, 1e-10);
        assertTrue(eventManager.isEmpty());
    }

}