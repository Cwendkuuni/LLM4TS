import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {
  
  @Test
  public void testIntegrate_WithEquations_ReturnsStopTime() {
    // create mock objects
    FirstOrderDifferentialEquations equations = mock(FirstOrderDifferentialEquations.class);
    AbstractStepInterpolator interpolator = mock(AbstractStepInterpolator.class);
    StepHandler stepHandler = mock(StepHandler.class);
    CombinedEventsManager manager = mock(CombinedEventsManager.class);
    
    // create test object
    EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("test", true,
                                                                               new double[] {0.1},
                                                                               new double[][] {{0.2}},
                                                                               new double[] {0.3},
                                                                               interpolator,
                                                                               0.01, 0.1, 0.001, 0.01) {
      @Override
      public int getOrder() {
        return 4;
      }

      @Override
      protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        return 0;
      }
    };
    
    // set mock objects
    integrator.clearStepHandlers();
    integrator.clearEventHandlers();
    integrator.setEquations(equations);
    integrator.addStepHandler(stepHandler);
    integrator.addEventHandler(manager);
    
    double t0 = 0.0;
    double[] y0 = new double[] {1.0, 2.0};
    double t = 1.0;
    double[] y = new double[2];
    
    try {
      double result = integrators.integrate(equations, t0, y0, t, y);
      assertEquals(result, 0.01, 0.0001);
    } catch (DerivativeException e) {
      fail(e.getMessage());
    } catch (IntegratorException e) {
      fail(e.getMessage());
    }
  }
}