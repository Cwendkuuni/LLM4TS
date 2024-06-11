import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import java.util.Comparator;


public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testConstructorWithDefaultCoefficients() {
        Assert.assertEquals(2.0, optimizer.khi, 0.0);
        Assert.assertEquals(0.5, optimizer.gamma, 0.0);
    }

    @Test
    public void testConstructorWithSpecifiedCoefficients() {
        MultiDirectional customOptimizer = new MultiDirectional(1.0, 0.2);
        Assert.assertEquals(1.0, customOptimizer.khi, 0.0);
        Assert.assertEquals(0.2, customOptimizer.gamma, 0.0);
    }

    @Test
    public void testIterateSimplex() {
        // Create a fake simplex
        RealPointValuePair[] simplex = {
            new RealPointValuePair(new double[]{1.0}, 1.0),
            new RealPointValuePair(new double[]{2.0}, 2.0),
            new RealPointValuePair(new double[]{3.0}, 3.0)
        };
        optimizer.simplex = simplex;

        // Mock the comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        try {
            optimizer.iterateSimplex(comparator);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }

        // Assert the result
        Assert.assertNotNull(optimizer.simplex);
        Assert.assertEquals(3, optimizer.simplex.length);
        Assert.assertEquals(1.0, optimizer.simplex[0].getValue(), 0.0);
        Assert.assertEquals(1.5, optimizer.simplex[1].getValue(), 0.0);
        Assert.assertEquals(3.0, optimizer.simplex[2].getValue(), 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplex_ExceptionThrown() {
        // Create a fake simplex
        RealPointValuePair[] simplex = {
            new RealPointValuePair(new double[]{1.0}, 1.0),
            new RealPointValuePair(new double[]{2.0}, 2.0),
            new RealPointValuePair(new double[]{3.0}, 3.0)
        };
        optimizer.simplex = simplex;

        // Mock the comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        // Throw a FunctionEvaluationException to simulate an exception during evaluation
        throw new FunctionEvaluationException();

        // Should throw FunctionEvaluationException
        optimizer.iterateSimplex(comparator);
    }
}