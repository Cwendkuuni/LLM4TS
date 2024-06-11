import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.ode.ExpandableStatefulODE;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {
  
  private ExpandableStatefulODE equations;
  private double t;
  private double[] y0;
  private double[] y1;
  private double[] yTmp;
  private double[][] yDotK;
  private double stepStart;
  private boolean forward;
  private double stepSize;
  private double hNew;
    
  @Before
  public void setUp() {
    this.equations = new ExpandableStatefulODE();
    this.t = 0.0;
    this.y0 = new double[]{1.0, 2.0, 3.0};
    this.y1 = new double[]{2.0, 3.0, 4.0};
    this.yTmp = new double[]{1.0, 2.0, 3.0};
    this.yDotK = new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}};
    this.stepStart = 0.0;
    this.forward = true;
    this.stepSize = 0.01;
    this.hNew = 0.01;
  }
  
  @Test
  public void testIntegrater() throws MathIllegalStateException, MathIllegalArgumentException {
    EmbeddedRungeKuttaIntegrator integrater = new EmbeddedRungeKuttaIntegrator("test", true, new double[]{0.5}, new double[][]{{1.0}}, new double[]{1.0}, null, 0.01, 1.0, 1e-6, 1e-2) {
      @Override
      public int getOrder() {
        return 0;
      }
  
      @Override
      protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        return 1.0;
      }
    };
    integrater.integrate(equations, t);
    assertTrue(integrater.getSafety() == 0.9);
    assertTrue(integrater.getMinReduction() == 0.2);
    assertTrue(integrater.getMaxGrowth() == 10.0);
  }
  
  private class EmbedRungeKuttaIntegrater extends EmbeddedRungeKuttaIntegrator {
    
    public EmbedRungeKuttaIntegrater(boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minSetp, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
      super("", fsal, c, a, b, prototype, minSetp, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }
    
    @Override
    public int getOrder() {
      return 0;
    }
  
    @Override
    protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
      return 1.0;
    }
  }
}