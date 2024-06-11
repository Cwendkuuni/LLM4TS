import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;

    @Before
    public void setup() {
        double[] c = {0, 1/3, 2/3, 1};
        double[][] a = {{}, {1/3}, {0, 2/3}, {0, 0, 1}};
        double[] b = {1/8, 3/8, 3/8, 1/8};
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();
        double step = 0.1;
        integrator = new RungeKuttaIntegrator("test", c, a, b, prototype, step);
    }

    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new DummyEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 1.0;
        double[] y = new double[y0.length];
        try {
            double stopTime = integrator.integrate(equations, t0, y0, t, y);
            assertEquals(t, stopTime, 0.0);
            // TODO: Add more assertions
        } catch (DerivativeException | IntegratorException e) {
            fail(e.getMessage());
        }
    }

    // TODO: Add more tests for other methods in RungeKuttaIntegrator

}

class DummyEquations implements FirstOrderDifferentialEquations {

    public void computeDerivatives(double t, double[] y, double[] yDot) {
        // TODO: Implement this method
    }

    public int getDimension() {
        // TODO: Implement this method
        return 0;
    }

}