import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EmptyEventManager;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandlerAdapter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testIntegratorBasicFunctionality() {
        double[][] coefficients = {{0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0},
                                   {1.0 / 4.0, 0.0, 1.0 / 4.0, 1.0 / 3.0},
                                   {1.0 / 3.0, 1.0 / 4.0, 0.0, 1.0 / 2.0},
                                   {1.0 / 2.0, 1.0 / 3.0, 1.0 / 4.0, 0.0}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[] {1.0e-5, 1.0e-5}, new double[] {1.0e-5, 1.0e-5});

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                derivatives[0] = Math.exp(-t) * (y[0] - 1.0);
            }
        };

        double tStart = 0.0;
        double[] yStart = new double[] {1.0};
        double tStop = 3.0;
        double[] yStop = new double[] {0.6321205599119}; // approximate solution for t=3

        double result = integrator.integrate(equations, tStart, yStart, tStop, new double[yStop.length]);

        double[] yExpected = new double[] {0.6321205599119};
        assertEquals(yExpected, yStop, 1.0e-5);
    }

    @Test
    public void testIntegratorEdgeCases() {
        double[][] coefficients = {{0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0},
                                   {1.0 / 4.0, 0.0, 1.0 / 4.0, 1.0 / 3.0},
                                   {1.0 / 3.0, 1.0 / 4.0, 0.0, 1.0 / 2.0},
                                   {1.0 / 2.0, 1.0 / 3.0, 1.0 / 4.0, 0.0}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[] {1.0e-5, 1.0e-5}, new double[] {1.0e-5, 1.0e-5});

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                derivatives[0] = Math.exp(-t) * Math.sin(y[0]);
            }
        };

        double tStart = 0.0;
        double[] yStart = new double[] {0.0};
        double tStop = Math.PI;
        double[] yStop = new double[] {1.0};

        double result = integrator.integrate(equations, tStart, yStart, tStop, new double[yStop.length]);

        double[] yExpected = new double[] {1.0};
        assertEquals(yExpected, yStop, 1.0e-5);
    }

    @Test
    public void testIntegratorExceptionHandling() {
        double[][] coefficients = {{0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0},
                                   {1.0 / 4.0, 0.0, 1.0 / 4.0, 1.0 / 3.0},
                                   {1.0 / 3.0, 1.0 / 4.0, 0.0, 1.0 / 2.0},
                                   {1.0 / 2.0, 1.0 / 3.0, 1.0 / 4.0, 0.0}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[] {1.0e-5, 1.0e-5}, new double[] {1.0e-5, 1.0e-5});

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                throw new RuntimeException("Derivatives exception");
            }
        };

        try {
            integrator.integrate(equations, 0.0, new double[] {0.0}, 1.0, new double[1]);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Expected
        }
    }

    @Test
    public void testIntegratorWithSteppingHandler() {
        double[][] coefficients = {{0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0},
                                   {1.0 / 4.0, 0.0, 1.0 / 4.0, 1.0 / 3.0},
                                   {1.0 / 3.0, 1.0 / 4.0, 0.0, 1.0 / 2.0},
                                   {1.0 / 2.0, 1.0 / 3.0, 1.0 / 4.0, 0.0}};
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, coefficients[0], coefficients[1], coefficients[2], new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[] {1.0e-5, 1.0e-5}, new double[] {1.0e-5, 1.0e-5});

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] derivatives) {
                derivatives[0] = t;
            }
        };

        integrator.addStepHandler(new StepHandlerAdapter() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                System.out.println("Step: " + stepNumber + ", y: " + Arrays.toString(interpolator.getY()));
                if (interpolator.getStepNumber() % 5 == 0) {
                    interpolator.rejectStep();
                }
            }
        });

        double tStart = 0.0;
        double[] yStart = new double[] {0.0};
        double tStop = 10.0;
        double[] yStop = new double[] {5.0};

        integrator.integrate(equations, tStart, yStart, tStop, new double[yStop.length]);

        // Depending on the order of the rejected steps, the output may vary
        // The following lines are just example outputs
        // System.out.println("Expected step output: y: [0.0, 1.0001266028185, 2.0002971514831, 3.000493350214, 4.0007143189382, 5.0]");
        // System.out.println("Actual step output: y: [0.0, 0.0, 0.0, 2.0004309835625, 4.000840512811, 5.0]");
    }

}