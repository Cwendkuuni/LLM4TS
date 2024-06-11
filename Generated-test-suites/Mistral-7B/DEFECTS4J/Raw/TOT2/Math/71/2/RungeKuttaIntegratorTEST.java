import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() throws Exception {
        double[][] func = {{0.0, 1.0}, {-1.0, 2.0}};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yPrime) {
                yPrime[0] = func[0][0] * y[0] + func[0][1] * y[1];
                yPrime[1] = func[1][0] * y[0] + func[1][1] * y[1];
            }
        };

        RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.25, 0.25}, {0.5, 0.25}},
                new double[]{0.5, 0.5, 0.5, 1.0}, new RungeKuttaStepInterpolator()) {
            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
            throws DerivativeException, IntegratorException {
                // empty implementation
            }
        };

        rk4.setStepSize(0.1);
        double[] y0 = {1.0, 1.0};
        double[] result = rk4.integrate(equations, 0.0, y0, 2.0, null);
        Assert.assertArrayEquals(new double[]{2.0, 2.0}, result, 0.001);
    }

    @Test
    public void testStepHandler() throws Exception {
        double[][] func = {{0.0, 1.0}, {-1.0, 2.0}};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yPrime) {
                yPrime[0] = func[0][0] * y[0] + func[0][1] * y[1];
                yPrime[1] = func[1][0] * y[0] + func[1][1] * y[1];
            }
        };

        RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.25, 0.25}, {0.5, 0.25}},
                new double[]{0.5, 0.5, 0.5, 1.0}, new RungeKuttaStepInterpolator()) {
            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y)
                    throws DerivativeException, IntegratorException {
                // empty implementation
            }
        };

        rk4.setStepSize(0.1);
        double[] y0 = {1.0, 1.0};
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandler event, int type) {
                if (type == StepHandler.EVENT_FOUND) {
                    Event event = (Event) event;
                    Assert.assertEquals(new When(1.5), event.getEvent());
                }
            }
        };
        rk4.addStepHandler(stepHandler);

        rk4.integrate(equations, 0.0, y0, 2.0, null);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        double[][] func = {{0.0, 1.0}, {-1.0, 2.0}};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yPrime) {
                throw new UnsupportedOperationException();
            }
        };

        RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.25, 0.25}, {0.5, 0.25}},
                new double[]{0.5, 0.5, 0.5, 1.0}, new RungeKuttaStepInterpolator()) {
            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0,
                                  double t, double[] y) throws DerivativeException, IntegratorException {
                // empty implementation
            }
        };

        rk4.setStepSize(0.1);
        double[] y0 = {1.0, 1.0};
        rk4.integrate(equations, 0.0, y0, 2.0, null);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        double[][] func = {{0.0, 1.0}, {-1.0, 2.0}};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yPrime) {
                throw new IntegratorException("Test Exception");
            }
        };

        RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.25, 0.25}, {0.5, 0.25}},
                new double[]{0.5, 0.5, 0.5, 1.0}, new RungeKuttaStepInterpolator()) {
            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0,
                                  double t, double[] y) throws DerivativeException, IntegratorException {
                // empty implementation
            }
        };

        rk4.setStepSize(0.1);
        double[] y0 = {1.0, 1.0};
        rk4.integrate(equations, 0.0, y0, 2.0, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() throws Exception {
        double[][] func = {{0.0, 1.0}, {-1.0, 2.0}};
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double time, double[] y, double[] yPrime) {
                // empty implementation
            }
        };

        RungeKuttaIntegrator rk4 = new RungeKuttaIntegrator("RK4", new double[]{0.25, 0.25, 0.25, 0.25},
                new double[][]{{0.25, 0}, {0, 0.25}, {0.25, 0.25}, {0.5, 0.25}},
                new double[]{0.5, 0.5, 0.5, 1.0}, new RungeKuttaStepInterpolator()) {
            @Override
            public void integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0,
                                  double t, double[] y) throws DerivativeException, IntegratorException {
                throw new IllegalStateException("Test Exception");
            }
        };

        rk4.setStepSize(0.1);
        double[] y0 = {1.0, 1.0};
        rk4.integrate(equations, 0.0, y0, 2.0, null);
    }
}