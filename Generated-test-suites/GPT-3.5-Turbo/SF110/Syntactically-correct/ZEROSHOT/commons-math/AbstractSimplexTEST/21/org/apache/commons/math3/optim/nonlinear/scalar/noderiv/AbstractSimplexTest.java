import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    private AbstractSimplex simplex;

    @Before
    public void setUp() {
        simplex = new AbstractSimplexImpl(3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(4, simplex.getSize());
    }

    @Test
    public void testIterate() {
        // TODO: Implement test case
    }

    @Test
    public void testBuild() {
        // TODO: Implement test case
    }

    @Test
    public void testEvaluate() {
        // TODO: Implement test case
    }

    @Test
    public void testReplaceWorstPoint() {
        // TODO: Implement test case
    }

    @Test
    public void testGetPoints() {
        // TODO: Implement test case
    }

    @Test
    public void testGetPoint() {
        // TODO: Implement test case
    }

    @Test
    public void testSetPoint() {
        // TODO: Implement test case
    }

    @Test
    public void testSetPoints() {
        // TODO: Implement test case
    }

    private class AbstractSimplexImpl extends AbstractSimplex {

        public AbstractSimplexImpl(int n) {
            super(n);
        }

        @Override
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // TODO: Implement method
        }
    }
}