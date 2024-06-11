package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] c = {0.0, 0.23692688505667133, 0.6787241202694588, 0.8611363115940521};
        double[][] a = {
            {0.0, 0.0, 0.0, 0.0},
            {0.23692688505667133, 0.6267594327328449, 0.1632003695952697, 0.0},
            {0.6787241202694588, 0.3826834373128631, 0.6787241202694588, 0.0},
            {0.8611363115940521, 0.1133497476268543, 0.467913524671156, 0.7468262740177123}
        };
        double[] b = {0.23692688505667133, 0.3826834373128631, 0.7468262740177123, 1.0};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta-Fehlberg78", false, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            public int getOrder() {
                return 7;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h / 1000.0;
            }
        };

        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double y0[] = {1.0, 1.0};
        double y1[] = new double[2];
        double tStart = 0.0;
        double tEnd = 10.0;

        EventHandler eventHandler = new StepHandlerAdapter() {
            @Override
            public void handleStep(StepHandler event) {
                assertFalse(event.isAccepted());
            }
        };
        integrator.setEventsHandlersManager(new CombinedEventsManager(eventHandler));

        try {
            integrator.integrate(equations, tStart, y0, tEnd, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        integrator.setMinStep(0.0001);
        integrator.setMaxStep(0.01);

        double t = 1.0;
        double[] y = y0.clone();

        try {
            double result = integrator.integrate(equations, tStart, y, t, y1);
            assertEquals(t, result, 1e-14);
            assertEquals(y1[0], y[0], 1e-14);
            assertEquals(y1[1], y[1], 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        t = 2.0;
        y = y0.clone();

        try {
            double result = integrator.integrate(equations, tStart, y, t, y1);
            assertEquals(t, result, 1e-14);
            assertEquals(y1[0], y[0], 1e-14);
            assertEquals(y1[1], y[1], 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        t = 5.0;
        y = y0.clone();

        try {
            double result = integrator.integrate(equations, tStart, y, t, y1);
            assertEquals(t, result, 1e-14);
            assertEquals(y1[0], y[0], 1e-14);
            assertEquals(y1[1], y[1], 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        t = 10.0;
        y = y0.clone();

        try {
            double result = integrator.integrate(equations, tStart, y, t, y1);
            assertEquals(t, result, 1e-14);
            assertEquals(y1[0], y[0], 1e-14);
            assertEquals(y1[1], y[1], 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta-Dopri54", true, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h / 1000.0;
            }
        };

        try {
            integrator.integrate(equations, tStart, y0, tEnd, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        integrator.setMinStep(0.0001);
        integrator.setMaxStep(0.01);

        try {
            integrator.integrate(equations, tStart, y0, t, y1);
            assertEquals(t, result, 1e-14);
            assertEquals(y1[0], y[0], 1e-14);
            assertEquals(y1[1], y[1], 1e-14);
        } catch (DerivativeException | IntegratorException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta-Dopri853", false, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            public int getOrder() {
                return 8;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h / 1000.0;
            }

            @Override
            public double[] getInitialStep(FirstOrderDifferentialEquations equations, double[] y, double stepSize, double[] yDot, double[] yDotK, double[] yDotNew, double[] yTmp, double[] yTmpDot) throws DerivativeException {
                throw new UnsupportedOperationException();
            }
        };

        try {
            integrator.integrate(equations, tStart, y0, t, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        integrator.setMinStep(0.0001);
        integrator.setMaxStep(0.01);

        try {
            integrator.integrate(equations, tStart, y0, t, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta-Dopri853", true, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            public int getOrder() {
                return 8;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return 1.0 + h / 1000.0;
            }

            @Override
            public double[] getInitialStep(FirstOrderDifferentialEquations equations, double[] y, double stepSize, double[] yDot, double[] yDotK, double[] yDotNew, double[] yTmp, double[] yTmpDot) throws DerivativeException {
                throw new UnsupportedOperationException();
            }
        };

        try {
            integrator.integrate(equations, tStart, y0, t, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta-Fehlberg23", false, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new MathIllegalArgumentException("h cannot be zero");
            }
        };

        try {
            integrator.integrate(equations, tStart, y0, tEnd, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }

        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta-Fehlberg23", true, c, a, b, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null) {
            @Override
            public int getOrder() {
                return 2;
            }

            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new MathIllegalArgumentException("h cannot be zero");
            }
        };

        try {
            integrator.integrate(equations, tStart, y0, t, y1);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // expected
        }
    }

}