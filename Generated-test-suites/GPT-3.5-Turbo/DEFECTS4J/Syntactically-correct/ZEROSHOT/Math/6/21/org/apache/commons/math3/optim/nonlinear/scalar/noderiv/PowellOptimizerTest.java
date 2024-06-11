import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01);
        double[] startPoint = {0, 0};
        double[] expectedPoint = {1, 2};
        double expectedValue = 0.0;
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        double[] resultPoint = optimizer.doOptimize().getPoint();
        double resultValue = optimizer.doOptimize().getValue();
        Assert.assertArrayEquals(expectedPoint, resultPoint, FastMath.ulp(1d));
        Assert.assertEquals(expectedValue, resultValue, FastMath.ulp(1d));
    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01);
        double[] p = {0, 0};
        double[] d = {1, 2};
        double optimum = 0.5;
        double[][] expected = {{0.5, 1.0}, {0.5, 1.0}};
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        Assert.assertArrayEquals(expected[0], result[0], FastMath.ulp(1d));
        Assert.assertArrayEquals(expected[1], result[1], FastMath.ulp(1d));
    }

    @Test
    public void testSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.01);
        LineSearch lineSearch = optimizer.new LineSearch(0.01, 0.01);
        double[] p = {0, 0};
        double[] d = {1, 2};
        UnivariateFunction f = Mockito.mock(UnivariateFunction.class);
        Mockito.when(f.value(0.0)).thenReturn(1.0);
        Mockito.when(f.value(0.5)).thenReturn(0.0);
        Mockito.when(f.value(1.0)).thenReturn(1.0);
        UnivariatePointValuePair expected = new UnivariatePointValuePair(0.5, 0.0);
        UnivariatePointValuePair result = lineSearch.search(p, d);
        Assert.assertEquals(expected, result);
    }
}