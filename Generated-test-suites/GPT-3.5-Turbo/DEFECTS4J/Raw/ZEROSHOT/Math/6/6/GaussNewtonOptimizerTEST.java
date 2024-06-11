import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
    }

    @Test
    public void testDoOptimize() {
        // Test case 1
        double[] targetValues1 = {1, 2, 3, 4, 5};
        RealMatrix weightMatrix1 = new Array2DRowRealMatrix(new double[][]{{1, 0, 0, 0, 0},
                                                                           {0, 2, 0, 0, 0},
                                                                           {0, 0, 3, 0, 0},
                                                                           {0, 0, 0, 4, 0},
                                                                           {0, 0, 0, 0, 5}});
        double[] startPoint1 = {0, 0, 0, 0, 0};
        double[] expectedPoint1 = {1, 2, 3, 4, 5};
        PointVectorValuePair expected1 = new PointVectorValuePair(expectedPoint1, computeObjectiveValue(expectedPoint1));

        optimizer.setTarget(targetValues1);
        optimizer.setWeight(weightMatrix1);
        optimizer.setStartPoint(startPoint1);
        PointVectorValuePair result1 = optimizer.doOptimize();

        assertArrayEquals(expected1.getPoint(), result1.getPoint(), 0.0001);
        assertArrayEquals(expected1.getValue(), result1.getValue(), 0.0001);

        // Test case 2
        double[] targetValues2 = {10, 20, 30};
        RealMatrix weightMatrix2 = new Array2DRowRealMatrix(new double[][]{{2, 0, 0},
                                                                           {0, 2, 0},
                                                                           {0, 0, 2}});
        double[] startPoint2 = {1, 2, 3};
        double[] expectedPoint2 = {1.6667, 1.3333, 1};
        PointVectorValuePair expected2 = new PointVectorValuePair(expectedPoint2, computeObjectiveValue(expectedPoint2));

        optimizer.setTarget(targetValues2);
        optimizer.setWeight(weightMatrix2);
        optimizer.setStartPoint(startPoint2);
        PointVectorValuePair result2 = optimizer.doOptimize();

        assertArrayEquals(expected2.getPoint(), result2.getPoint(), 0.0001);
        assertArrayEquals(expected2.getValue(), result2.getValue(), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimizeConverged() {
        double[] targetValues = {1, 2, 3};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0},
                                                                          {0, 2, 0},
                                                                          {0, 0, 1}});
        double[] startPoint = {0, 0, 0};
        PointVectorValuePair expected = new PointVectorValuePair(startPoint, computeObjectiveValue(startPoint));

        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker(0.0001, 0.0001));
        PointVectorValuePair result = optimizer.doOptimize();

        assertArrayEquals(expected.getPoint(), result.getPoint(), 0.0001);
        assertArrayEquals(expected.getValue(), result.getValue(), 0.0001);
    }

}