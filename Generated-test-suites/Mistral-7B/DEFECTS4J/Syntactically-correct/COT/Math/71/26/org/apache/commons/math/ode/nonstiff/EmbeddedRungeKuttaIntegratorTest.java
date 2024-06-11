package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nointegrators.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nointegrators.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for EmbeddedRungeKuttaIntegrator.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[] {0.5, 0.25, 0.1, 0.5},
                new double[][] {{-0.2536010037134399, 0.25}, {0.4536989948271702, 0.4999999999999999},
                        {-0.483658377321533, 0.497487177949224}, {0.3667926245843035, 0.4999999999999999}},
                new double[] {0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        assertNotNull(integrator);
    }

    /**
     * Test getOrder method.
     */
    @Test
    public void testGetOrder() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[] {0.5, 0.25, 0.1, 0.5},
                new double[][] {{-0.2536010037134399, 0.25}, {0.4536989948271702, 0.4999999999999999},
                        {-0.483658377321533, 0.497487177949224}, {0.3667926245843035, 0.4999999999999999}},
                new double[] {0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);
        assertEquals(4, integrator.getOrder());
    }

    /**
     * Test getSafety method.
     */
    @Test
    public void testGetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[] {0.5, 0.25, 0.1, 0.5},
                new double[][] {{-0.2536010037134399, 0.25}, {0.4536989948271702, 0.4999999999999999},
                        {-0.483658377321533, 0.497487177949224}, {0.3667926245843035, 0.4999999999999999}},
                new double[] {0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);
        assertEquals(0.9, integrator.getSafety(), 1e-6);
    }

    /**
     * Test setSafety method.
     */
    @Test
    public void testSetSafety() {
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[] {0.5, 0.25, 0.1, 0.5},
                new double[][] {{-0.2536010037134399, 0.25}, {0.4536989948271702, 0.4999999999999999},
                        {-0.483658377321533, 0.497487177949224}, {0.3667926245843035, 0.4999999999999999}},
                new double[] {0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        integrator.setSafety(0.5);
        assertEquals(0.5, integrator.getSafety(), 1e-6);
    }

    /**
     * Test integrate method with simple differential equations.
     */
    @Test
    public void testIntegrateSimple() {
        double[] y0 = new double[] {1.0, 1.0};
        double[] y1 = new double[] {2.0, 2.0};
        double[] param = new double[] {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[] {0.5, 0.25, 0.1, 0.5},
                new double[][] {{-0.2536010037134399, 0.25}, {0.4536989948271702, 0.4999999999999999},
                        {-0.483658377321533, 0.497487177949224}, {0.3667926245843035, 0.4999999999999999}},
                new double[] {0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        double result = integrator.integrate(equations, param[0], y0, param[1], y1);
        assertEquals(param[1], result, 1e-6);
    }

    /**
     * Test integrate method with singular differential equations.
     */
    @Test
    public void testIntegrateSingular() {
        double[] y0 = new double[] {1.0, 0.0};
        double[] y1 = new double[] {2.0, 1.0};
        double[] param = new double[] {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1] / (y[0] + 1.0);
                yPrime[1] = -y[1] + y[0];
            }
        };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[] {0.5, 0.25, 0.1, 0.5},
                new double[][] {{-0.2536010037134399, 0.25}, {0.4536989948271702, 0.4999999999999999},
                        {-0.483658377321533, 0.497487177949224}, {0.3667926245843035, 0.4999999999999999}},
                new double[] {0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

        try {
            integrator.integrate(equations, param[0], y0, param[1], y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }
    }

    /**
     * Test integrate method with events.
     */
    @Test
    public void testIntegrateEvents() {
        double[] y0 = new double[] {1.0, 1.0};
        double[] y1 = new double[] {2.0, 2.0};
        double[] param = new double[] {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }

            @Override
            public boolean hasEvent(double t, double[] y) {
                return t > 0.5;
            }

            @Override
            public double eventTime(double t, double[] y) {
                return t;
            }

            @Override
            public void handleEvent(double t, double[] y, double[] yPrime) {
                y[0] = 1.0;
            }
        };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[] {0.5, 0.25, 0.1, 0.5},
                new double[][] {{-0.2536010037134399, 0.25}, {0.4536989948271702, 0.4999999999999999},
                        {-0.483658377321533, 0.497487177949224}, {0.3667926245843035, 0.4999999999999999}},
                new double[] {0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);
        integrator.addEndTimeChecker(1.0, 1.0, new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int step) {
                assertEquals(1.0, integrator.getTime(), 1e-6);
            }

            @Override
            public void handleStepException(StepHandler event, int step, Throwable exception) {
                fail("Unexpected exception: " + exception.getMessage());
            }
        });

        double result = integrator.integrate(equations, param[0], y0, param[1], y1);
        assertEquals(param[1], result, 1e-6);
    }

    /**
     * Test integrate method with step size control.
     */
    @Test
    public void testIntegrateStepSizeControl() {
        double[] y0 = new double[] {1.0, 1.0};
        double[] y1 = new double[] {2.0, 2.0};
        double[] param = new double[] {0.0, 1.0};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = -y[0];
            }
        };
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", false, new double[] {0.5, 0.25, 0.1, 0.5},
                new double[][] {{-0.2536010037134399, 0.25}, {0.4536989948271702, 0.4999999999999999},
                        {-0.483658377321533, 0.497487177949224}, {0.3667926245843035, 0.4999999999999999}},
                new double[] {0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.0001, 0.01, null, null);

        double result = integrator.integrate(equations, param[0], y0, param[1], y1);
        assertEquals(param[1], result, 1e-6);
        assertTrue(integrator.getStepSize() > 0.0001);
        assertFalse(integrator.getStepSize() > 0.001);

        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(1.1);
        integrator.setSafety(0.5);
        integrator.resetEvaluations();
        integrator.integrate(equations, param[0], y0, param[1], y1);
        assertTrue(integrator.getStepSize() > 0.001);
        assertFalse(integrator.getStepSize() > 0.01);

        double[] y2 = new double[] {1.0, 1.0};
        double[] y3 = new double[] {2.0, 2.0};
        integrator.setStepSize(0.01);
        integrator.resetEvaluations();
        integrator.integrate(equations, param[0], y2, param[1], y3);
        assertEquals(param[1], integrator.getTime(), 1e-6);
        assertEquals(y3[0], y1[0], 1e-6);
        assertEquals(y3[1], y1[1], 1e-6);
    }
}