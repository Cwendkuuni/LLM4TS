
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

    // Test computeObjectiveGradient method
    @Test
    public void testComputeObjectiveGradient() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        
        // Test case 1
        double[] point1 = {1.0, 2.0, 3.0};
        double[] expectedGradient1 = {2.0, 4.0, 6.0};
        double[] actualGradient1 = optimizer.computeObjectiveGradient(point1);
        assertArrayEquals(expectedGradient1, actualGradient1, 0.0001);

        // Test case 2
        double[] point2 = {-1.0, -2.0, -3.0};
        double[] expectedGradient2 = {-2.0, -4.0, -6.0};
        double[] actualGradient2 = optimizer.computeObjectiveGradient(point2);
        assertArrayEquals(expectedGradient2, actualGradient2, 0.0001);
    }

    // Test computeObjectiveValue method
    @Test
    public void testComputeObjectiveValue() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        
        // Test case 1
        double[] point1 = {1.0, 2.0, 3.0};
        double expectedValue1 = 14.0;
        double actualValue1 = optimizer.computeObjectiveValue(point1);
        assertEquals(expectedValue1, actualValue1, 0.0001);

        // Test case 2
        double[] point2 = {-1.0, -2.0, -3.0};
        double expectedValue2 = -14.0;
        double actualValue2 = optimizer.computeObjectiveValue(point2);
        assertEquals(expectedValue2, actualValue2, 0.0001);
    }

    // Test setInitialStep method
    @Test
    public void testSetInitialStep() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        
        // Test case 1
        double initialStep1 = 2.0;
        optimizer.setInitialStep(initialStep1);
        assertEquals(initialStep1, optimizer.getInitialStep(), 0.0001);

        // Test case 2
        double initialStep2 = -1.0;
        optimizer.setInitialStep(initialStep2);
        assertEquals(1.0, optimizer.getInitialStep(), 0.0001);
    }

    // Test doOptimize method
    @Test
    public void testDoOptimize() {
        NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer();
        
        // Test case 1
        double[] startPoint1 = {1.0, 2.0, 3.0};
        double[] expectedPoint1 = {0.0, 0.0, 0.0};
        double expectedObjective1 = 0.0;
        PointValuePair expectedResult1 = new PointValuePair(expectedPoint1, expectedObjective1);
        PointValuePair actualResult1 = optimizer.doOptimize(startPoint1);
        assertEquals(expectedResult1, actualResult1);

        // Test case 2
        double[] startPoint2 = {-1.0, -2.0, -3.0};
        double[] expectedPoint2 = {0.0, 0.0, 0.0};
        double expectedObjective2 = 0.0;
        PointValuePair expectedResult2 = new PointValuePair(expectedPoint2, expectedObjective2);
        PointValuePair actualResult2 = optimizer.doOptimize(startPoint2);
        assertEquals(expectedResult2, actualResult2);
    }
}
