import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NullArgumentException;
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
    public void testConstructor() {
        assertNotNull(optimizer);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeNoConvergenceChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());
        PointVectorValuePair result = optimizer.doOptimize();
        assertNotNull(result);
    }
}