import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testBasicIntegration() {
        // Create a simple differential equation system
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };

        // Create a Runge-Kutta integrator instance
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator(
                "RK4Integrator",
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.0, 0.0, 0.5, 0.5 },
                                 { 0.5, 0.5, 0.0, 0.0 } },
                new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(5),
                0.1
        ) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) {
                // Do nothing
            }
        };

        // Set up the step handler to check the integration result
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, int stepNumber) {
                double expected = 1.0 * stepNumber;
                double actual = integrator.getY()[0];
                Assert.assertEquals(expected, actual, 1e-10);
            }
        };

        // Integrate the differential equation system from t=0 to t=4
        double startTime = 0.0;
        double endTime = 4.0;
        double initialCondition = 0.0;
        double stepSize = 0.1;
        try {
            double result = integrator.integrate(equations, startTime, new double[] { initialCondition }, endTime, new double[1]);
            Assert.assertEquals(endTime, result, 1e-10);
        } catch (DeriveException | IntegratorException e) {
            Assert.fail("Integration failed: " + e.getMessage());
        }

        // The step handler should have been called 41 times (40 steps + 1 final state check)
        Assert.assertEquals(41, integrator.getStepNumber());
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator(
                "RK4Integrator",
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.0, 0.0, 0.5, 0.5 },
                                 { 0.5, 0.5, 0.0, 0.0 } },
                new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(5),
                0.1
        ) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) {
                // Do nothing
            }
        };

        integrator.setStepSize(-0.1);
        integrator.integrate(equations, 0.0, new double[] { 0.0 }, 1.0, new double[1]);
    }

    @Test
    public void testEventDetection() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };

        double[][] events = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } };
        When[] eventHandlers = new When[events.length];
        for (int i = 0; i < events.length; i++) {
            eventHandlers[i] = new When(events[i][0]) {
                @Override
                public void handleEvent(double t, double[] y) {
                    Assert.assertEquals(events[i][1], integrator.getStepNumber());
                    super.handleEvent(t, y);
                }
            };
        }

        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator(
                "RK4Integrator",
                new double[] { 0.25, 0.25, 0.25, 0.25 },
                new double[][] { { 0.0, 0.0, 0.5, 0.5 },
                                 { 0.5, 0.5, 0.0, 0.0 } },
                new double[] { 0.5, 0.5 },
                new RungeKuttaStepInterpolator(5),
                0.1
        ) {
            @Override
            protected void sanityChecks(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) {
                // Do nothing
            }

            @Override
            protected void addEventHandlers(CombinedEventsManager manager) {
                for (When eventHandler : eventHandlers) {
                    manager.addEvent(eventHandler);
                }
            }
        };

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, int stepNumber) {
                Assert.assertEquals(1, integrator.getStepNumber());
            }
        };

        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, 0.0, new double[] { 0.0 }, 4.0, new double[1]);
    }

}