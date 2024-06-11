

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;


public class PowellOptimizerTest {

  @Test
  public void testDoOptimize() {
    // Test case 1
    double rel1 = 1.0;
    double abs1 = 1.0;
    PowellOptimizer optimizer1 = new PowellOptimizer(rel1, abs1);
    double[] startPoint1 = {1.0, 2.0, 3.0};
    double[] endPoint1 = {0.0, 0.0, 0.0};
    GoalType goalType1 = GoalType.MINIMIZE;
    ConvergenceChecker<PointValuePair> checker1 = null;
    PointValuePair expectedResult1 = new PointValuePair(endPoint1, 0.0);
    PointValuePair result1 = optimizer1.doOptimize(startPoint1, goalType1, checker1);
    assertArrayEquals(expectedResult1.getPoint(), result1.getPoint());
    assertEquals(expectedResult1.getValue(), result1.getValue(), 0.0001);

    // Test case 2
    double rel2 = 0.5;
    double abs2 = 0.5;
    PowellOptimizer optimizer2 = new PowellOptimizer(rel2, abs2);
    double[] startPoint2 = {-1.0, -2.0, -3.0};
    double[] endPoint2 = {1.0, 2.0, 3.0};
    GoalType goalType2 = GoalType.MAXIMIZE;
    ConvergenceChecker<PointValuePair> checker2 = null;
    PointValuePair expectedResult2 = new PointValuePair(endPoint2, 0.0);
    PointValuePair result2 = optimizer2.doOptimize(startPoint2, goalType2, checker2);
    assertArrayEquals(expectedResult2.getPoint(), result2.getPoint());
    assertEquals(expectedResult2.getValue(), result2.getValue(), 0.0001);

    // Test case 3
    double rel3 = 0.1;
    double abs3 = 0.1;
    PowellOptimizer optimizer3 = new PowellOptimizer(rel3, abs3);
    double[] startPoint3 = {0.0, 0.0, 0.0};
    double[] endPoint3 = {-1.0, -2.0, -3.0};
    GoalType goalType3 = GoalType.MINIMIZE;
    ConvergenceChecker<PointValuePair> checker3 = null;
    PointValuePair expectedResult3 = new PointValuePair(endPoint3, 0.0);
    PointValuePair result3 = optimizer3.doOptimize(startPoint3, goalType3, checker3);
    assertArrayEquals(expectedResult3.getPoint(), result3.getPoint());
    assertEquals(expectedResult3.getValue(), result3.getValue(), 0.0001);
  }
}

