package org.apache.commons.math.ode.nonstiff;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.noouverte.Rk4Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] coefficients = {
                {0.0, 0.3, 0.6, 1.0},
                {0.0, 0.27, 0.45, 0.62, 0.77},
                {0.0, 0.2, 0.35, 0.46, 0.56, 0.65},
                {0.0, 0.16, 0.33, 0.4, 0.47, 0.53, 0.6}
        };

        double[] absoluteTolerance = {1.0e-6, 1.0e-6, 1.0e-6, 1.0e-6};
        double[] relativeTolerance = {1.0e-6, 1.0e-6, 1.0e-6, 1.0e-6};

        for (int i = 0; i < coefficients.length; i++) {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                    "Embedded RK" + (i + 1),
                    false,
                    coefficients[i],
                    null,
                    null,
                    new Rk4Integrator(),
                    1.0e-6,
                    1.0e6,
                    absoluteTolerance[i],
                    relativeTolerance[i]
            );

            double[] y = new double[]{0.0, 1.0};
            double tStart = 0.0;
            double tEnd = 10.0;

            StepInterpolator stepInterpolator = integrator.createStepInterpolator();
            StepHandler stepHandler = new StepHandler() {
                @Override
                public void handleStep(StepInterpolator interpolator, int step) {
                    System.out.println("Step " + step + " : y = " + interpolator.getY());
                }
            };

            FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

            try {
                integrator.integrate(equations, tStart, y, tEnd, y);
            } catch (DerivativeException | IntegratorException e) {
                fail("Integration failed: " + e.getMessage());
            }

            StepHandler handler = integrator.getStepHandlers().get(0);
            handler.handleStep(stepInterpolator, integrator.getNumberOfSteps());
        }
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithEvents() {
        double[][] coefficients = {
                {0.0, 0.3, 0.6, 1.0},
                {0.0, 0.27, 0.45, 0.62, 0.77},
                {0.0, 0.2, 0.35, 0.46, 0.56, 0.65},
                {0.0, 0.16, 0.33, 0.4, 0.47, 0.53, 0.6}
        };

        double[] absoluteTolerance = {1.0e-6, 1.0e-6, 1.0e-6, 1.0e-6};
        double[] relativeTolerance = {1.0e-6, 1.0e-6, 1.0e-6, 1.0e-6};

        for (int i = 0; i < coefficients.length; i++) {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                    "Embedded RK" + (i + 1),
                    false,
                    coefficients[i],
                    null,
                    null,
                    new Rk4Integrator(),
                    1.0e-6,
                    1.0e6,
                    absoluteTolerance[i],
                    relativeTolerance[i]
            );

            double[] y = new double[]{0.0, 1.0};
            double tStart = 0.0;
            double tEnd = 10.0;

            CombinedEventsManager eventsManager = new CombinedEventsManager();
            eventsManager.addEvent(tStart + 2.0, new MyEvent());

            StepInterpolator stepInterpolator = integrator.createStepInterpolator();
            StepHandler stepHandler = new StepHandler() {
                @Override
                public void handleStep(StepInterpolator interpolator, int step) {
                    System.out.println("Step " + step + " : y = " + interpolator.getY());
                }
            };

            FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

            try {
                integrator.integrate(equations, tStart, y, tEnd, y, stepHandler, eventsManager);
            } catch (DerivativeException | IntegratorException e) {
                fail("Integration failed: " + e.getMessage());
            }

            StepHandler handler = integrator.getStepHandlers().get(0);
            handler.handleStep(stepInterpolator, integrator.getNumberOfSteps());
        }
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorFsal() {
        double[][] coefficients = {
                {0.0, 0.3, 1.0},
                {0.0, 0.2, 0.4, 1.0},
                {0.0, 0.1, 0.3, 0.4, 1.0},
                {0.0, 0.05, 0.2, 0.2, 0.3, 1.0}
        };

        double[] absoluteTolerance = {1.0e-6, 1.0e-6, 1.0e-6, 1.0e-6};
        double[] relativeTolerance = {1.0e-6, 1.0e-6, 1.0e-6, 1.0e-6};

        for (int i = 0; i < coefficients.length; i++) {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                    "Embedded RK" + (i + 1),
                    true,
                    coefficients[i],
                    null,
                    null,
                    new Rk4Integrator(),
                    1.0e-6,
                    1.0e6,
                    absoluteTolerance[i],
                    relativeTolerance[i]
            );

            double[] y = new double[]{0.0, 1.0};
            double tStart = 0.0;
            double tEnd = 10.0;

            StepInterpolator stepInterpolator = integrator.createStepInterpolator();
            StepHandler stepHandler = new StepHandler() {
                @Override
                public void handleStep(StepInterpolator interpolator, int step) {
                    System.out.println("Step " + step + " : y = " + interpolator.getY());
                }
            };

            FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();

            try {
                integrator.integrate(equations, tStart, y, tEnd, y, stepHandler);
            } catch (DerivativeException | IntegratorException e) {
                fail("Integration failed: " + e.getMessage());
            }

            StepHandler handler = integrator.getStepHandlers().get(0);
            handler.handleStep(stepInterpolator, integrator.getNumberOfSteps());
        }
    }

    static class MyEvent extends org.apache.commons.math3.ode.events.Event {
        @Override
        public boolean isSatisfied(double t, double[] y) {
            return y[0] > 5.0;
        }

        @Override
        public void evaluateEvent(double t, double[] y, double[] dy, double[] yPrime, double[] yDot) {
            dy[0] = 1.0;
        }
    }

    static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getNumberOfEquations() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[0] * 0.1;
        }
    }
}