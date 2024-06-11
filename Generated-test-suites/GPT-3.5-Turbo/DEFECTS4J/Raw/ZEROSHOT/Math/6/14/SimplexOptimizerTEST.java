import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        PointValuePair result = optimizer.optimizeInternal(100, new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] + point[1];
            }
        }, GoalType.MINIMIZE);
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getPoint()[1], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex(2);
        simplex.setPoints(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}});
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.doOptimize();

        assertNotNull(result);
        assertEquals(1.0, result.getPoint()[0], 0.0001);
        assertEquals(1.0, result.getPoint()[1], 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(null);
        AbstractSimplex simplex = new AbstractSimplex(2);
        simplex.setPoints(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}});
        optimizer.setSimplex(simplex);

        PointValuePair result = optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testParseOptimizationDataNull() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.parseOptimizationData((OptimizationData[]) null);
    }

    @Test
    public void testParseOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex(2);
        simplex.setPoints(new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}});
        optimizer.parseOptimizationData(simplex);

        assertSame(simplex, optimizer.simplex);
    }

}