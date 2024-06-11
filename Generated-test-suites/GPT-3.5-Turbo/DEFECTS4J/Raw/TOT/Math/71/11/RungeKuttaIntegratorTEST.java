import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math.ode.DerivativeException;
import org.apache.commons.math.ode.IntegratorException;
import org.apache.commons.math.ode.nonstiff.RungeKuttaIntegrator;

public class RungeKuttaIntegratorTest {

  @Test
  public void testIntegrate() {
    // Define the dummy first order differential equations
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
        public int getDimension() {
            return 2;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = y[0];
            yDot[1] = y[1];
        }
    };
    
    // Create the RungeKuttaIntegrator instance
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[]{0.0, 0.5, 0.5, 1.0}, 
                                                              new double[][]{{}, {0.5}, {0.0, 0.5}, {0.0, 0.0, 1.0}}, 
                                                              new double[]{1.0, 2.0, 2.0, 1.0}, 
                                                              new RungeKuttaStepInterpolator(), 0.01);
    
    // Define the initial conditions and integration parameters
    double t0 = 0.0;
    double[] y0 = {1.0, 0.0};
    double t = 1.0;
    double[] y = new double[2];
    
    try {
        // Integrate the differential equations
        integrator.integrate(equations, t0, y0, t, y);
        
        // Check the solution
        assertEquals(Math.exp(t), y[0], 0.01);
        assertEquals(0.0, y[1], 0.01);
    } catch (DerivativeException e) {
        fail("DerivativeException thrown: " + e.getMessage());
    } catch (IntegratorException e) {
        fail("IntegratorException thrown: " + e.getMessage());
    }
  }

  @Test
  public void testIntegrateEdgeCases() {
    // Define the dummy first order differential equations
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
        public int getDimension() {
            return 1;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            yDot[0] = y[0];
        }
    };
    
    // Create the RungeKuttaIntegrator instance
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[]{0.0, 0.5, 0.5, 1.0}, 
                                                              new double[][]{{}, {0.5}, {0.0, 0.5}, {0.0, 0.0, 1.0}}, 
                                                              new double[]{1.0, 2.0, 2.0, 1.0}, 
                                                              new RungeKuttaStepInterpolator(), 0.01);
    
    // Define the initial conditions and integration parameters
    double t0 = 0.0;
    double[] y0 = {1.0};
    double t = 1.0;
    double[] y = new double[1];
    
    try {
        // Integrate the differential equations
        integrator.integrate(equations, t0, y0, t, y);
        
        // Check the solution
        assertEquals(Math.exp(t), y[0], 0.01);
    } catch (DerivativeException e) {
        fail("DerivativeException thrown: " + e.getMessage());
    } catch (IntegratorException e) {
        fail("IntegratorException thrown: " + e.getMessage());
    }
  }

  @Test(expected=DerivativeException.class)
  public void testIntegrateErrorScenario() throws DerivativeException, IntegratorException {
    // Define the dummy first order differential equations
    FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
        public int getDimension() {
            return 1;
        }
        public void computeDerivatives(double t, double[] y, double[] yDot) throws DerivativeException {
            throw new DerivativeException("Some error occurred while computing derivatives.");
        }
    };
    
    // Create the RungeKuttaIntegrator instance
    RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[]{0.0, 0.5, 0.5, 1.0}, 
                                                              new double[][]{{}, {0.5}, {0.0, 0.5}, {0.0, 0.0, 1.0}}, 
                                                              new double[]{1.0, 2.0, 2.0, 1.0}, 
                                                              new RungeKuttaStepInterpolator(), 0.01);
    
    // Define the initial conditions and integration parameters
    double t0 = 0.0;
    double[] y0 = {1.0};
    double t = 1.0;
    double[] y = new double[1];
    
    // Integrate the differential equations (should throw an exception)
    integrator.integrate(equations, t0, y0, t, y);
  }
}