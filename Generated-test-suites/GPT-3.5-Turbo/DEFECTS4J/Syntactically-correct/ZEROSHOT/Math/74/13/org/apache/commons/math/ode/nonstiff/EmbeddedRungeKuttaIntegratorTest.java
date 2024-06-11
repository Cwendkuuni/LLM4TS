import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
  
  private EmbeddedRungeKuttaIntegrator integrator;
  
  public EmbeddedRungeKuttaIntegratorTest() {
      
    double[] c = {0.0, 1.0/4.0, 3.0/8.0, 12.0/13.0, 1.0, 1.0/2.0};
    double[][] a = {{},
                    {1.0/4.0},
                    {3.0/32.0, 9.0/32.0},
                    {1932.0/2197.0, -7200.0/2197.0, 7296.0/2197.0},
                    {439.0/216.0, -8.0, 3680/513.0, -845.0/4104.0},
                    {-8.0/27.0, 2.0, -3544.0/2565.0, 1859.0/4104.0, -11.0/40.0}};
    double[] b = {16.0/135.0, 0, 6656.0/12825.0, 28561.0/56430.0, -9.0/50.0, 2.0/55.0};
    
    integrator = new EmbeddedRungeKuttaIntegrator("Runge-Kutta", false, c, a, b, null, 1e-6, 1000, 1e-6, 0.0) {
      public int getOrder() {
        return 5;
      }
      public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        return 0;
      }
    };
  }
  
  @Test
  public void testIntegrate() {
    // TODO: Implement test case
  }
}