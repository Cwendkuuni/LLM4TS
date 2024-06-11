import org.junit.Test;
import static org.junit.Assert.*;

public class RungeKuttaIntegratorTest {

  @Test
  public void testIntegrate_SingleStep() {
    // Arrange
    RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    double t0 = 0.0;
    double[] y0 = new double[] { 1.0, 2.0, 3.0 };
    double t = 1.0;
    double[] y = new double[3];

    // Act
    double result = integrator.integrate(equations, t0, y0, t, y);

    // Assert
    assertEquals(1.0, result, 1e-6);
    assertEquals(2.0, y[0], 1e-6);
    assertEquals(4.0, y[1], 1e-6);
    assertEquals(6.0, y[2], 1e-6);
  }

  @Test
  public void testIntegrate_MultipleSteps() {
    // Arrange
    RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    double t0 = 0.0;
    double[] y0 = new double[] { 1.0, 2.0, 3.0 };
    double t = 2.0;
    double[] y = new double[3];

    // Act
    double result = integrator.integrate(equations, t0, y0, t, y);

    // Assert
    assertEquals(2.0, result, 1e-6);
    assertEquals(3.0, y[0], 1e-6);
    assertEquals(6.0, y[1], 1e-6);
    assertEquals(9.0, y[2], 1e-6);
  }

  @Test
  public void testIntegrate_ZeroStep() {
    // Arrange
    RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    double t0 = 0.0;
    double[] y0 = new double[] { 1.0, 2.0, 3.0 };
    double t = 0.0;
    double[] y = new double[3];

    // Act
    double result = integrator.integrate(equations, t0, y0, t, y);

    // Assert
    assertEquals(0.0, result, 1e-6);
    assertEquals(1.0, y[0], 1e-6);
    assertEquals(2.0, y[1], 1e-6);
    assertEquals(3.0, y[2], 1e-6);
  }

  @Test
  public void testIntegrate_BackwardIntegration() {
    // Arrange
    RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    double t0 = 0.0;
    double[] y0 = new double[] { 1.0, 2.0, 3.0 };
    double t = -1.0;
    double[] y = new double[3];

    // Act
    double result = integrator.integrate(equations, t0, y0, t, y);

    // Assert
    assertEquals(-1.0, result, 1e-6);
    assertEquals(0.0, y[0], 1e-6);
    assertEquals(0.0, y[1], 1e-6);
    assertEquals(0.0, y[2], 1e-6);
  }

  @Test(expected = DerivativeException.class)
  public void testIntegrate_DerivativeException() {
    // Arrange
    RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    equations.setDerivativeException(true);
    double t0 = 0.0;
    double[] y0 = new double[] { 1.0, 2.0, 3.0 };
    double t = 1.0;
    double[] y = new double[3];

    // Act
    integrator.integrate(equations, t0, y0, t, y);
  }

  @Test(expected = IntegratorException.class)
  public void testIntegrate_IntegratorException() {
    // Arrange
    RungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    FirstOrderDifferentialEquations equations = new TestFirstOrderDifferentialEquations();
    equations.setIntegratorException(true);
    double t0 = 0.0;
    double[] y0 = new double[] { 1.0, 2.0, 3.0 };
    double t = 1.0;
    double[] y = new double[3];

    // Act
    integrator.integrate(equations, t0, y0, t, y);
  }

  private class TestRungeKuttaIntegrator extends RungeKuttaIntegrator {
    public TestRungeKuttaIntegrator() {
      super("", new double[0], new double[0][0], new double[0], null, 0.0);
    }
    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {}
    @Override
    public int getOrder() {
        return 0;
    }
    @Override
    public boolean requiresDenseOutput() {
        return false;
    }
  }

  private class TestFirstOrderDifferentialEquations implements FirstOrderDifferentialEquations {

    private boolean derivativeException;
    private boolean integratorException;

    public void setDerivativeException(boolean derivativeException) {
        this.derivativeException = derivativeException;
    }

    public void setIntegratorException(boolean integratorException) {
        this.integratorException = integratorException;
    }

    public int getDimension() {
      return 3;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
      if (derivativeException) {
          throw new DerivativeException();
      }
      if (integratorException) {
          throw new IntegratorException();
      }
      yDot[0] = 1.0;
      yDot[1] = 2.0;
      yDot[2] = 3.0;
    }

  }

}