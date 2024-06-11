import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

   @Test
   public void testConstructor1() {
      AbstractSimplex simplex = new AbstractSimplex(3);
      assertNotNull(simplex);
      assertEquals(3, simplex.getDimension());
   }
   
   @Test
   public void testConstructor2() {
      AbstractSimplex simplex = new AbstractSimplex(5, 2.0);
      assertNotNull(simplex);
      assertEquals(5, simplex.getDimension());
   }
   
   @Test
   public void testConstructor3() {
      double[] steps = {1.0, 2.0, 3.0};
      AbstractSimplex simplex = new AbstractSimplex(steps);
      assertNotNull(simplex);
      assertEquals(3, simplex.getDimension());
   }
   
   @Test
   public void testConstructor4() {
      double[][] referenceSimplex = {
         {1.0, 2.0},
         {3.0, 4.0},
         {5.0, 6.0}
      };
      AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
      assertNotNull(simplex);
      assertEquals(2, simplex.getDimension());
   }
   
   @Test(expected = NullArgumentException.class)
   public void testConstructor_NullSteps() {
      double[] steps = null;
      AbstractSimplex simplex = new AbstractSimplex(steps);
   }
   
   @Test(expected = ZeroException.class)
   public void testConstructor_ZeroSteps() {
      double[] steps = {};
      AbstractSimplex simplex = new AbstractSimplex(steps);
   }
   
   @Test(expected = NotStrictlyPositiveException.class)
   public void testConstructor_EmptyReferenceSimplex() {
      double[][] referenceSimplex = {};
      AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
   }
   
   @Test(expected = DimensionMismatchException.class)
   public void testConstructor_ReferenceSimplexDimensionMismatch() {
      double[][] referenceSimplex = {
         {1.0},
         {2.0, 3.0}
      };
      AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
   }
   
   @Test(expected = DimensionMismatchException.class)
   public void testBuild_DimensionMismatch() {
      double[] startPoint = {1.0, 2.0, 3.0};
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.build(startPoint);
   }
   
   @Test
   public void testBuild() {
      double[] startPoint = {1.0, 2.0};
      AbstractSimplex simplex = new AbstractSimplex(2);
      simplex.build(startPoint);
      assertNotNull(simplex.getPoint(0));
      assertEquals(startPoint, simplex.getPoint(0).getPoint());
   }
   
   @Test(expected = OutOfRangeException.class)
   public void testGetPoint_OutOfRange() {
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.getPoint(3);
   }
   
   @Test
   public void testGetPoints() {
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.build(new double[]{1.0, 2.0, 3.0});
      PointValuePair[] points = simplex.getPoints();
      assertNotNull(points);
      assertEquals(4, points.length);
      assertEquals(1.0, points[0].getPoint()[0], 0.0);
      assertEquals(2.0, points[0].getPoint()[1], 0.0);
      assertEquals(3.0, points[0].getPoint()[2], 0.0);
   }
   
   @Test(expected = OutOfRangeException.class)
   public void testSetPoint_OutOfRange() {
      PointValuePair point = new PointValuePair(new double[]{4.0, 5.0}, 6.0);
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.setPoint(3, point);
   }
   
   @Test
   public void testSetPoint() {
      PointValuePair point = new PointValuePair(new double[]{4.0, 5.0}, 6.0);
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.setPoint(0, point);
      assertEquals(point, simplex.getPoint(0));
   }
   
   @Test(expected = DimensionMismatchException.class)
   public void testSetPoints_DimensionMismatch() {
      PointValuePair[] points = {
         new PointValuePair(new double[]{1.0, 2.0, 3.0}, 4.0),
         new PointValuePair(new double[]{5.0, 6.0, 7.0}, 8.0)
      };
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.setPoints(points);
   }
   
   @Test
   public void testIterate() {
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.iterate(new MultivariateFunction() {
         @Override
         public double value(double[] point) {
            return 0.0;
         }
      }, new Comparator<PointValuePair>() {
         @Override
         public int compare(PointValuePair o1, PointValuePair o2) {
            return 0;
         }
      });
      // Add asserts as needed
   }
   
   @Test
   public void testEvaluate() {
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.evaluate(new MultivariateFunction() {
         @Override
         public double value(double[] point) {
            return 0.0;
         }
      }, new Comparator<PointValuePair>() {
         @Override
         public int compare(PointValuePair o1, PointValuePair o2) {
            return 0;
         }
      });
      // Add asserts as needed
   }
   
   @Test
   public void testReplaceWorstPoint() {
      PointValuePair point = new PointValuePair(new double[]{4.0, 5.0}, 6.0);
      AbstractSimplex simplex = new AbstractSimplex(3);
      simplex.replaceWorstPoint(point, new Comparator<PointValuePair>() {
         @Override
         public int compare(PointValuePair o1, PointValuePair o2) {
            return 0;
         }
      });
      // Add asserts as needed
   }

}