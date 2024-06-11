import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

public class AbstractIntegratorTest {
  
  private AbstractIntegrator integrator;
  private FirstOrderDifferentialEquations equations;
  private double t0;
  private double[] y0;
  private double t;
  private double[] y;
  
  @BeforeClass
  public static void setUpBeforeClass() {
    // Execute once before the test class starts.
  }
  
  @Before
  public void setUp() {
    // Executes before each test case.
    integrator = new AbstractIntegratorMock();
    equations = new FirstOrderDifferentialEquationsMock();
    t0 = 0.0;
    y0 = new double[]{0.0, 1.0, 2.0};
    t = 1.0;
    y = new double[]{0.1, 1.1, 2.1};
  }
  
  @Test
  public void testIntegrate() throws Exception {
    double result = integrator.integrate(equations, t0, y0, t, y);
    assertEquals(1.0, result, 1e-10);
    assertArrayEquals(new double[]{0.1, 1.1, 2.1}, y, 1e-10);
  }
  
  @After
  public void tearDown() {
    // Executes after each test case.
  }
  
  @AfterClass
  public static void tearDownAfterClass() {
    // Execute once after the test class finishes.
  }
  
  private static class AbstractIntegratorMock extends AbstractIntegrator {
    
    public AbstractIntegratorMock() {
      super("AbstractIntegratorMock");
    }
    
    public void integrate(ExpandableStatefulODE equations, double t) {
      // Do nothing in this mock implementation.
    }
    
  }
  
  private static class FirstOrderDifferentialEquationsMock implements FirstOrderDifferentialEquations {
    
    public int getDimension() {
      return 3;
    }
    
    public void computeDerivatives(double t, double[] y, double[] yDot) {
      // Do nothing in this mock implementation.
    }
    
  }
  
}