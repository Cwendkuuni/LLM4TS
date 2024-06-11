import static org.junit.Assert.*;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

  RungeKuttaIntegrator integrator;

  @Before
  public void setUp() {
    double[] c = {0.0, 0.5, 0.5, 1.0};
    double[][] a = {{}, {0.5}, {0.0, 0.5}, {0.0, 0.0, 1.0}};
    double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0};
    RungeKuttaStepInterpolator prototype;
    double step = 0.01;
    integrator = new RungeKuttaIntegrator(c, a, b, prototype, step);
  }

  @Test
  public void testIntegrate_typicalCase() {
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations();
    double t0 = 0.0;
    double[] y0 = {1.0, 0.0};
    double t = 1.0;
    double[] y = new double[y0.length];

    double stopTime = integrator.integrate(equations, t0, y0, t, y);

    assertEquals(t, stopTime, 0.001);
    assertEquals(2.718, y[0], 0.001);
    assertEquals(0.154, y[1], 0.001);
  }

  @Test
  public void testIntegrate_edgeCase() {
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations();
    double t0 = 0.0;
    double[] y0 = {1.0, 0.0};
    double t = 10.0;
    double[] y = new double[y0.length];

    double stopTime = integrator.integrate(equations, t0, y0, t, y);

    assertEquals(t, stopTime, 0.001);
    assertEquals(22026.465, y[0], 0.001);
    assertEquals(0.735, y[1], 0.001);
  }

  @Test(expected = DerivativeException.class)
  public void testIntegrate_errorScenario() {
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations();
    double t0 = 0.0;
    double[] y0 = {1.0};
    double t = 1.0;
    double[] y = new double[y0.length];

    integrator.integrate(equations, t0, y0, t, y);
  }

}