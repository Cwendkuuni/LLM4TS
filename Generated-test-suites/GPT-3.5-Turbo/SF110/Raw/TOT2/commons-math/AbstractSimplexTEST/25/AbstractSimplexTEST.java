import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractSimplexTest {

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new ConcreteSimplex(3, 1.0);
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new ConcreteSimplex(3, 1.0);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterate() {
        // Test case for iterate method
    }

    // Add more test cases for other public methods

    class ConcreteSimplex extends AbstractSimplex {
        public ConcreteSimplex(int n, double sideLength) {
            super(n, sideLength);
        }

        public void iterate(final MultivariateFunction p0, final Comparator<PointValuePair> p1) {
            // Concrete implementation of iterate method for testing
        }
    }
}