import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.nonstiff.*;

public class EmbeddedRungeKuttaIntegratorTest {

  @Test
  public void testGetOrder() {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    int expectedOrder = 4;
    int actualOrder = integrator.getOrder();
    assertEquals(expectedOrder, actualOrder);
  }

  @Test
  public void testGetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    double expectedSafety = 0.9;
    double actualSafety = integrator.getSafety();
    assertEquals(expectedSafety, actualSafety, 0.001);
  }

  @Test
  public void testSetSafety() {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    double safety = 0.8;
    integrator.setSafety(safety);
    double actualSafety = integrator.getSafety();
    assertEquals(safety, actualSafety, 0.001);
  }

  @Test
  public void testGetMinReduction() {

    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    double expectedMinReduction = 0.2;
    double actualMinReduction = integrator.getMinReduction();
    assertEquals(expectedMinReduction, actualMinReduction, 0.001);
  }

  @Test
  public void testSetMinReduction() {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    double minReduction = 0.1;
    integrator.setMinReduction(minReduction);
    double expectedMinReduction = 0.1;
    double actualMinReduction = integrator.getMinReduction();
    assertEquals(expectedMinReduction, actualMinReduction, 0.001);
  }

  @Test
  public void testGetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    double expectedMaxGrowth = 10.0;
    double actualMaxGrowth = integrator.getMaxGrowth();
    assertEquals(expectedMaxGrowth, actualMaxGrowth, 0.001);
  }

  @Test
  public void testSetMaxGrowth() {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    double maxGrowth = 12.0;
    integrator.setMaxGrowth(maxGrowth);
    double expectedMaxGrowth = 12.0;
    double actualMaxGrowth = integrator.getMaxGrowth();
    assertEquals(expectedMaxGrowth, actualMaxGrowth, 0.001);
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testIntegrateWithNumberIsTooSmallException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    ExpandableStatefulODE equations = new TestODE();
    double t = 0.0;
    integrator.integrate(equations, t);
  }

  @Test(expected = DimensionMismatchException.class)
  public void testIntegrateWithDimensionMismatchException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    ExpandableStatefulODE equations = new TestODE();
    double t = 10.0;
    integrator.integrate(equations, t);
  }

  @Test(expected = MaxCountExceededException.class)
  public void testIntegrateWithMaxCountExceededException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    ExpandableStatefulODE equations = new TestODE();
    double t = 10.0;
    integrator.integrate(equations, t);
  }

  @Test(expected = NoBracketingException.class)
  public void testIntegrateWithNoBracketingException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
    EmbeddedRungeKuttaIntegrator integrator = new TestRungeKuttaIntegrator();
    ExpandableStatefulODE equations = new TestODE();
    double t = 10.0;
    integrator.integrate(equations, t);
  }

  private static class TestRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
    public TestRungeKuttaIntegrator() {
      super("TestIntegrator", true, new double[]{1.0}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.0, 1.0, 0.0, 0.0);
    }

    @Override
    public int getOrder() {
      return 4;
    }

    @Override
    protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
      return 0.0;
    }

    @Override
    protected void computeDerivatives(double arg0, double[] arg1, double[] arg2) throws MaxCountExceededException, DimensionMismatchException {
      // Empty body for testing purposes
    }
  }

  private static class TestODE extends ExpandableStatefulODE {
    public TestODE() {
      super();
    }

    @Override
    public double getTime() {
      return 0.0;
    }

    @Override
    public double[] getCompleteState() {
      return new double[0];
    }

    @Override
    public void setTime(double arg0) throws NumberIsTooSmallException {
      // Empty body for testing purposes
    }

    @Override
    public void setCompleteState(double[] arg0) throws DimensionMismatchException {
      // Empty body for testing purposes
    }
  }

}