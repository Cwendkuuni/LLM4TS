import org.junit.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;

public class AbstractSimplexTest {
   
   // Test case for the constructor with one argument
   @Test(expected = ZeroException.class)
   public void testConstructor1() {
      AbstractSimplex s = new AbstractSimplex(0);
   }
   
   // Test case for the constructor with two arguments
   @Test(expected = ZeroException.class)
   public void testConstructor2() {
      AbstractSimplex s = new AbstractSimplex(2, 0.0);
   }
   
   // Test case for the constructor with steps argument
   @Test(expected = ZeroException.class)
   public void testConstructor3() {
      double[] steps = {0.0, 1.0, 2.0};
      AbstractSimplex s = new AbstractSimplex(steps);
   }
   
   // Test case for the constructor with referenceSimplex argument
   @Test(expected = NotStrictlyPositiveException.class)
   public void testConstructor4() {
      double[][] referenceSimplex = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
      AbstractSimplex s = new AbstractSimplex(referenceSimplex);
   }
   
   // Test case for getDimension method
   @Test
   public void testGetDimension() {
      AbstractSimplex s = new AbstractSimplex(3);
      int dimension = s.getDimension();
      assertEquals(3, dimension);
   }
   
   // Test case for getSize method
   @Test
   public void testGetSize() {
      AbstractSimplex s = new AbstractSimplex(3);
      s.build(new double[]{1.0, 2.0, 3.0});
      int size = s.getSize();
      assertEquals(4, size);
   }
   
   // Test case for build method
   @Test(expected = DimensionMismatchException.class)
   public void testBuild() {
      AbstractSimplex s = new AbstractSimplex(3);
      s.build(new double[]{1.0, 2.0});
   }
   
   // Test case for evaluate method
   @Test
   public void testEvaluate() {
      AbstractSimplex s = new AbstractSimplex(3);
      s.build(new double[]{1.0, 2.0, 3.0});
      MultivariateFunction function = new MultivariateFunction() {
         public double value(double[] point) {
            return point[0] + point[1] + point[2];
         }
      };
      s.evaluate(function, null);
      PointValuePair[] simplex = s.getPoints();
      assertEquals(3.0, simplex[0].getValue(), 0.0001);
      assertEquals(6.0, simplex[simplex.length - 1].getValue(), 0.0001);
   }
   
   // Test case for replaceWorstPoint method
   @Test
   public void testReplaceWorstPoint() {
      AbstractSimplex s = new AbstractSimplex(3);
      s.build(new double[]{1.0, 2.0, 3.0});
      PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 9.0);
      s.replaceWorstPoint(point, null);
      PointValuePair[] simplex = s.getPoints();
      assertEquals(9.0, simplex[simplex.length - 1].getValue(), 0.0001);
   }
   
   // Test case for getPoints method
   @Test
   public void testGetPoints() {
      AbstractSimplex s = new AbstractSimplex(3);
      s.build(new double[]{1.0, 2.0, 3.0});
      PointValuePair[] points = s.getPoints();
      assertNotNull(points);
   }
   
   // Test case for getPoint method
   @Test(expected = OutOfRangeException.class)
   public void testGetPoint() {
      AbstractSimplex s = new AbstractSimplex(3);
      s.build(new double[]{1.0, 2.0, 3.0});
      PointValuePair point = s.getPoint(4);
   }
   
   // Test case for setPoint method
   @Test(expected = OutOfRangeException.class)
   public void testSetPoint() {
      AbstractSimplex s = new AbstractSimplex(3);
      s.build(new double[]{1.0, 2.0, 3.0});
      PointValuePair point = new PointValuePair(new double[]{4.0, 5.0, 6.0}, 9.0);
      s.setPoint(4, point);
   }
   
   // Test case for setPoints method
   @Test(expected = DimensionMismatchException.class)
   public void testSetPoints() {
      AbstractSimplex s = new AbstractSimplex(3);
      s.build(new double[]{1.0, 2.0, 3.0});
      PointValuePair[] points = new PointValuePair[2];
      s.setPoints(points);
   }
   
   // Test case for createHypercubeSteps method
   @Test
   public void testCreateHypercubeSteps() {
      double[] steps = AbstractSimplex.createHypercubeSteps(3, 2.0);
      assertEquals(2.0, steps[0], 0.0001);
      assertEquals(2.0, steps[1], 0.0001);
      assertEquals(2.0, steps[2], 0.0001);
   }
   
}