import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        optimizer = new GaussNewtonOptimizer(true, checker);
    }

    @Test
    public void testDoOptimize() {
        // TODO: Write comprehensive test cases to test the doOptimize() method
        fail("Not yet implemented");
    }

    @Test
    public void testGaussNewtonOptimizer() {
        try {
            GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizerConvergenceCheckerOfPointVectorValuePair() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerBoolean() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        assertEquals(true, optimizer.isUseLU());
    }

    @Test
    public void testGaussNewtonOptimizerBooleanConvergenceCheckerOfPointVectorValuePair() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        assertEquals(true, optimizer.isUseLU());
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimizeThrowsNullArgumentException() {
        try {
            optimizer.doOptimize();
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDoOptimizeThrowsConvergenceException() {
        try {
            optimizer.setConvergenceChecker(null);
            optimizer.doOptimize();
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}