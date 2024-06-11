import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {
  
  @Test
  public void testIntegrate() {
    // Create test cases
    double t0 = 0.0;
    double[] y0 = {1.0, 2.0, 3.0};
    double t = 1.0;
    double[] y = new double[y0.length];

    // Create expected result
    double expected = t;

    // Create instance of AbstractIntegrator
    AbstractIntegrator integrator = new AbstractIntegrator() {
        public void integrate(ExpandableStatefulODE equations, double t) {}
    };

    // Perform integration
    double result = integrator.integrate(new FirstOrderDifferentialEquations() {
        public int getDimension() { return y0.length; }
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            for (int i = 0; i < y0.length; i++) {
                yDot[i] = y[i];
            }
        }
    }, t0, y0, t, y);

    // Check result
    assertEquals(expected, result, 0.0);
  }
}