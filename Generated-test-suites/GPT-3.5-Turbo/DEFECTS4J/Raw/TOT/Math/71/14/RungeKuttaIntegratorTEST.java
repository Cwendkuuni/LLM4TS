import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

   private RungeKuttaIntegrator integrator;
   private FirstOrderDifferentialEquations equations;
   private double t0;
   private double[] y0;
   private double t;
   private double[] y;

   @Before
   public void setUp() {
      equations = new FirstOrderDifferentialEquationsImplementation();
      t0 = 0.0;
      y0 = new double[]{1.0, 2.0, 3.0};
      t = 10.0;
      y = new double[3];
   }

   @Test
   public void testIntegrate() {
      integrator = new RungeKuttaIntegratorImplementation();
      double stopTime = integrator.integrate(equations, t0, y0, t, y);
      // Check that the integration stops at the specified time
      assertEquals(t, stopTime, 0.0);
      // Check the final values of the variables
      assertEquals(10.0, y[0], 0.0);
      assertEquals(20.0, y[1], 0.0);
      assertEquals(30.0, y[2], 0.0);
   }

   @Test(expected = DerivativeException.class)
   public void testIntegrate_DerivativeException() {
      equations = new FirstOrderDifferentialEquationsWithDerivativeException();
      integrator = new RungeKuttaIntegratorImplementation();
      integrator.integrate(equations, t0, y0, t, y);
   }

   @Test(expected = IntegratorException.class)
   public void testIntegrate_IntegratorException() {
      equations = new FirstOrderDifferentialEquationsWithIntegratorException();
      integrator = new RungeKuttaIntegratorImplementation();
      integrator.integrate(equations, t0, y0, t, y);
   }

   private static class FirstOrderDifferentialEquationsImplementation implements FirstOrderDifferentialEquations {

      public int getDimension() {
         return 3;
      }

      public void computeDerivatives(double t, double[] y, double[] yDot) {
         yDot[0] = 2.0;
         yDot[1] = 4.0;
         yDot[2] = 6.0;
      }
   }

   private static class FirstOrderDifferentialEquationsWithDerivativeException implements FirstOrderDifferentialEquations {

      public int getDimension() {
         return 3;
      }

      public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
         throw new DerivativeException("Error computing derivatives");
      }
   }

   private static class FirstOrderDifferentialEquationsWithIntegratorException implements FirstOrderDifferentialEquations {

      public int getDimension() {
         return 3;
      }

      public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
         yDot[0] = 2.0;
         yDot[1] = 4.0;
         yDot[2] = 6.0;
         throw new DerivativeException("Error integrating");
      }
   }
}