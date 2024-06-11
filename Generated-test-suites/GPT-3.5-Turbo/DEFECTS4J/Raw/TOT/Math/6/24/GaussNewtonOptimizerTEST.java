import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Check that the useLU field is set to true
        assertTrue(optimizer.useLU);
        // Check that the convergence checker is a SimpleVectorValueChecker
        assertTrue(optimizer.getConvergenceChecker() instanceof SimpleVectorValueChecker);
    }

    @Test
    public void testConstructorWithUseLU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);

        // Check that the useLU field is set to true
        assertTrue(optimizer.useLU);
        // Check that the convergence checker is a SimpleVectorValueChecker
        assertTrue(optimizer.getConvergenceChecker() instanceof SimpleVectorValueChecker);
    }

    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);

        // Check that the useLU field is set to true
        assertTrue(optimizer.useLU);
        // Check that the convergence checker is set correctly
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testConstructorWithUseLUAndConvergenceChecker() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);

        // Check that the useLU field is set correctly
        assertTrue(optimizer.useLU);
        // Check that the convergence checker is set correctly
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    // Test other methods...
}