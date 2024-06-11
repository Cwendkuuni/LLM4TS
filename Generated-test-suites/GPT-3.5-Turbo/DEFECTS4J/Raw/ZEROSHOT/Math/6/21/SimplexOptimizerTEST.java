import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.exception.NullArgumentException;
import java.util.Comparator;

public class SimplexOptimizerTest {

   @Test
   public void testConstructor() {
       SimplexOptimizer optimizer = new SimplexOptimizer();
       assertNotNull(optimizer);
   }

   @Test
   public void testConstructorWithChecker() {
       ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
       SimplexOptimizer optimizer = new SimplexOptimizer(checker);
       assertNotNull(optimizer);
   }

   @Test
   public void testConstructorWithThresholds() {
       double rel = 0.001;
       double abs = 0.001;
       SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);
       assertNotNull(optimizer);
   }

   @Test
   public void testSetSimplex() {
       SimplexOptimizer optimizer = new SimplexOptimizer();
       AbstractSimplex simplex = new AbstractSimplex();
       optimizer.setSimplex(simplex);
       assertEquals(simplex, optimizer.simplex);
   }

   @Test(expected = NullArgumentException.class)
   public void testDoOptimizeNoSimplex() {
       SimplexOptimizer optimizer = new SimplexOptimizer();
       optimizer.doOptimize();
   }

   @Test
   public void testDoOptimize() {
       SimplexOptimizer optimizer = new SimplexOptimizer();
       AbstractSimplex simplex = new AbstractSimplex();
       optimizer.setSimplex(simplex);
       PointValuePair startPoint = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
       optimizer.setStartPoint(startPoint);
       MultivariateFunction evalFunc = new MultivariateFunction() {
           public double value(double[] point) {
               return 0.0;
           }
       };
       GoalType goalType = GoalType.MINIMIZE;
       OptimizationData[] optData = new OptimizationData[0];
       PointValuePair result = optimizer.doOptimize();
       assertNotNull(result);
   }

}