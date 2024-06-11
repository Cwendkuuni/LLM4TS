import org.junit.Test;

import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

    @Test
    public void testIntegrate() throws Exception {
        //create test class object
        RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();

        //set up test inputs
        FirstOrderDifferentialEquations equations = new TestDifferentialEquations();
        double t0 = 0.0;
        double[] y0 = {1.0, 2.0};
        double t = 1.0;
        double[] y = new double[2];

        //run the method to be tested
        double result = integrator.integrate(equations, t0, y0, t, y);

        //assert the result is as expected
        assertEquals(0.0, result, 1e-12);
        assertEquals(1.0, y[0], 1e-12);
        assertEquals(2.0, y[1], 1e-12);
    }

    //create a test implementation of the RungeKuttaIntegrator
    private class TestRungeKuttaIntegrator extends RungeKuttaIntegrator {
        public TestRungeKuttaIntegrator() {
            super("Test Integrator", new double[]{0.0, 1.0}, new double[][]{{1.0}}, new double[]{1.0},
                    new RungeKuttaStepInterpolator(), 1.0);
        }
    }

    //create a test implementation of the FirstOrderDifferentialEquations
    private class TestDifferentialEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 2;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = y[0];
            yDot[1] = y[1];
        }
    }

}