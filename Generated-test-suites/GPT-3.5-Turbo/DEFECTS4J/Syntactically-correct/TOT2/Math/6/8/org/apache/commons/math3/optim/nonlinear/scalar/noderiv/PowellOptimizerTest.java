import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-12);
        assertNotNull(optimizer);
    }

    @Test
    public void testCustomConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-12, 1e-5, 1e-9, null);
        assertNotNull(optimizer);
    }

    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-12);
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.1, 0.2, 0.3};
        double optimum = 0.5;
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        assertArrayEquals(new double[]{1.05, 2.1, 3.15}, result[0], 1e-12);
        assertArrayEquals(new double[]{0.05, 0.1, 0.15}, result[1], 1e-12);
    }

    @Test
    public void testLineSearch() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-12);
        PowellOptimizer.LineSearch lineSearch = optimizer.new LineSearch(1e-6, 1e-12);
        double[] p = {1.0, 2.0, 3.0};
        double[] d = {0.1, 0.2, 0.3};
        PowellOptimizer.LineSearch.BracketFinder bracket = lineSearch.bracket;
        bracket.search(new UnivariateFunction() {
            public double value(double alpha) {
                return alpha * alpha;
            }
        }, GoalType.MINIMIZE, 0, 1);
        assertEquals(0.0, bracket.getLo(), 1e-12);
        assertEquals(1.0, bracket.getHi(), 1e-12);
        assertEquals(0.6180339887498949, bracket.getMid(), 1e-12);
    }

    // Add more test cases as needed for other methods and edge cases

}