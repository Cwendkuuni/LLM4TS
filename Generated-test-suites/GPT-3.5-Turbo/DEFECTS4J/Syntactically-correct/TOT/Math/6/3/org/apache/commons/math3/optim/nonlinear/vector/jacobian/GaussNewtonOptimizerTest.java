import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

  private GaussNewtonOptimizer optimizer;
  private double[] target;
  private RealMatrix weight;
  private double[] startPoint;

  @Before
  public void setUp() {
    optimizer = new GaussNewtonOptimizer();
    target = new double[]{1, 2, 3};
    weight = MatrixUtils.createRealIdentityMatrix(3);
    startPoint = new double[]{0, 0, 0};
  }

  @After
  public void tearDown() {
    optimizer = null;
    target = null;
    weight = null;
    startPoint = null;
  }

  // ###### Test cases for constructor ######

  @Test
  public void testGaussNewtonOptimizer_DefaultSettings() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
    assertNotNull(optimizer);
  }

  @Test
  public void testGaussNewtonOptimizer_UseLU() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
    assertNotNull(optimizer);
  }

  @Test
  public void testGaussNewtonOptimizer_UseQR() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
    assertNotNull(optimizer);
  }

  @Test(expected = NullArgumentException.class)
  public void testGaussNewtonOptimizer_NullConvergenceChecker() {
    ConvergenceChecker<PointVectorValuePair> checker = null;
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
  }

  // ###### Test cases for doOptimize() ######

  @Test(expected = NullArgumentException.class)
  public void testDoOptimize_NullConvergenceChecker() {
    optimizer.doOptimize();
  }

  @Test
  public void testDoOptimize_ConvergenceReached() {
    optimizer.setTarget(target);
    optimizer.setWeight(weight);
    optimizer.setStartPoint(startPoint);
    PointVectorValuePair result = optimizer.doOptimize();

    assertNotNull(result);
    assertNotNull(result.getPoint());
    assertNotNull(result.getValue());

    // Assert convergence has been reached
    assertTrue(optimizer.getConvergenceChecker().converged(result, result));
  }

  @Test (expected = MathInternalError.class)
  public void testDoOptimize_MathInternalError() {
    ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
    optimizer.setTarget(null);
    optimizer.setWeight(null);
    optimizer.setStartPoint(null);
    optimizer.setConvergenceChecker(checker);
    optimizer.doOptimize();
  }

  @Test (expected = ConvergenceException.class)
  public void testDoOptimize_SingularMatrixException() {
    ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
    double[] target = new double[]{2, 2, 2};
    optimizer.setTarget(target);
    optimizer.setWeight(weight);
    optimizer.setStartPoint(startPoint);
    optimizer.setConvergenceChecker(checker);
    optimizer.doOptimize();
  }

  // ###### Test cases for getters and setters ######

  @Test
  public void testGetSetUseLU() {
    assertTrue(optimizer.getUseLU());
    optimizer.setUseLU(false);
    assertFalse(optimizer.getUseLU());
  }

  @Test
  public void testGetSetChecker() {
    ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
    optimizer.setConvergenceChecker(checker);
    assertSame(checker, optimizer.getConvergenceChecker());
  }

  @Test
  public void testGetSetCost() {
    double cost = 10.0;
    optimizer.setCost(cost);
    assertEquals(cost, optimizer.getCost(), 1e-15);
  }

  @Test
  public void testGetSetPoint() {
    double[] point = new double[]{1, 1, 1};
    optimizer.setPoint(point);
    assertArrayEquals(point, optimizer.getPoint(), 1e-15);
  }

  // ###### Test cases for helper methods ######

  @Test
  public void testComputeObjectiveValue() {
    double[] point = new double[]{1, 2, 3};
    optimizer.setStartPoint(point);
    double[] objectiveValue = optimizer.computeObjectiveValue(point);
    assertNotNull(objectiveValue);
    assertArrayEquals(point, objectiveValue, 1e-15);
  }

  @Test
  public void testComputeResiduals() {
    double[] point = new double[]{1, 2, 3};
    optimizer.setStartPoint(point);
    double[] residuals = optimizer.computeResiduals(point);
    assertNotNull(residuals);
    assertArrayEquals(residuals, new double[]{0, 0, 0}, 1e-15);
  }

  @Test
  public void testComputeWeightedJacobian() {
    double[] point = new double[]{1, 2, 3};
    optimizer.setStartPoint(point);
    RealMatrix jacobian = optimizer.computeWeightedJacobian(point);
    assertNotNull(jacobian);
    assertEquals(jacobian.getRowDimension(), 3);
    assertEquals(jacobian.getColumnDimension(), 3);
  }

  @Test
  public void testComputeCost() {
    double[] residuals = new double[]{1, 2, 3};
    optimizer.setTarget(target);
    double cost = optimizer.computeCost(residuals);
    assertEquals(cost, 14, 1e-15);
  }

  // ###### Other test cases ######

  @Test(expected = NullArgumentException.class)
  public void testSetWeight_NullMatrix() {
    optimizer.setWeight(null);
  }

  @Test
  public void testSetWeight_ValidMatrix() {
    RealMatrix weight = MatrixUtils.createRealIdentityMatrix(3);
    optimizer.setWeight(weight);
    assertSame(weight, optimizer.getWeight());
  }

  @Test(expected = NullArgumentException.class)
  public void testSetTarget_NullArray() {
    optimizer.setTarget(null);
  }

  @Test
  public void testSetTarget_ValidArray() {
    double[] target = new double[]{1, 2, 3};
    optimizer.setTarget(target);
    assertArrayEquals(target, optimizer.getTarget(), 1e-15);
  }

  @Test(expected = NullArgumentException.class)
  public void testSetStartPoint_NullArray() {
    optimizer.setStartPoint(null);
  }

  @Test
  public void testSetStartPoint_ValidArray() {
    double[] startPoint = new double[]{1, 2, 3};
    optimizer.setStartPoint(startPoint);
    assertArrayEquals(startPoint, optimizer.getStartPoint(), 1e-15);
  }

}