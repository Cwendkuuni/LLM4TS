package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.DefaultCombinedEventHandler;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.OneStepEventHandler;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.DummyStepInterpolator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() throws EventException {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {
            {1.0 / 3.0, 0.0, 2.0 / 3.0, 1.0},
            {1.0 / 4.0, 0.25, 1.0 / 4.0, 0.0},
            {0.0, 1.0 / 3.0, 2.0 / 3.0, 1.0}
        };
        double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0};
        boolean fsal = true;
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c.length + 1, b);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta-Fehlberg 7-8", fsal, c, a, b, prototype, 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };

        StepHandler handler = new StepHandlerAdapter() {
            private int count = 0;

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                count++;
            }
        };

        integrator.addStepHandler(handler);

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[0] * y[0];
            }
        };

        double[] initialConditions = new double[]{1.0};
        double start = 0.0;
        double end = 1.0;

        try {
            integrator.integrate(equations, start, initialConditions, end, null);
            assertEquals(3, handler.getCount());
        } catch (DerivativeException e) {
            // ignore
        } catch (IntegratorException e) {
            // ignore
        }

        integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta-Midpoint", false, c, a, b, prototype, 0.001, 0.01, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0.0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.abs(y1[i] - y0[i]);
                }
                return error;
            }
        };

        integrator.setSafety(0.5);
        integrator.addStepHandler(new OneStepEventHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator) throws EventException {
                double t = interpolator.getInterpolatedTime();
                double y = interpolator.getValue(0);
                if (y > 2.0) {
                    throw new EventException(new Event() {
                        @Override
                        public double getTime() {
                            return t;
                        }

                        @Override
                        public String getDescription() {
                            return "Reached target value.";
                        }
                    });
                }
            }
        });

        integrator.setEventsHandlerManager(new DefaultCombinedEventHandler());

        integrator.integrate(equations, start, initialConditions, end, null);
    }

}