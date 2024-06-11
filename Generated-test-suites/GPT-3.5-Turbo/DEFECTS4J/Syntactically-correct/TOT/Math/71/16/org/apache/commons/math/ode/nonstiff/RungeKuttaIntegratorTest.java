import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;

public class RungeKuttaIntegratorTest {

  private static final double EPSILON = 1e-10;
  private RungeKuttaIntegrator integrator;
  private FirstOrderDifferentialEquations equations;
  
  @Before
  public void setUp() {
    integrator = new RungeKuttaIntegrator("TestIntegrator",
      new double[] {0.0, 0.5, 0.5, 1.0},
      new double[][] {{}, {0.5}, {0.0, 0.5}, {1.0, 0.0, 0.0}},
      new double[] {0.16666666666666666, 0.3333333333333333, 0.3333333333333333, 0.16666666666666666},
      null, 0.1);
    equations = new FirstOrderDifferentialEquations() {
      public int getDimension() {
        return 2;
      }
      public void computeDerivatives(double t, double[] y, double[] yDot)
        throws DerivativeException {
        yDot[0] = 3.0 * y[0] + 2.0 * y[1];
        yDot[1] = -2.0 * y[0] + 2.0 * y[1];
      }
    };
  }
  
  @Test
  public void testIntegrateTypical() throws Exception {
    double[] y0 = new double[] {1.0, 0.0};
    double[] y = new double[2];
    double t = 0.5;
    integrator.integrate(equations, 0.0, y0, t, y);
    assertEquals(1.9436434129038976, y[0], EPSILON);
    assertEquals(0.6321205588285577, y[1], EPSILON);
  }
  
  @Test
  public void testIntegrateEdge() throws Exception {
    double[] y0 = new double[] {-1.0, 1.0};
    double[] y = new double[2];
    double t = 2.0;
    integrator.integrate(equations, 0.0, y0, t, y);
    assertEquals(-3.058316141311216, y[0], EPSILON);
    assertEquals(1.9427352960976306, y[1], EPSILON);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testIntegrateError() throws Exception {
    double[] y0 = new double[] {1.0};
    double[] y = new double[1];
    double t = 0.5;
    integrator.integrate(equations, 0.0, y0, t, y);
  }
}