import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;
import org.apache.commons.math.optimization.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        Comparator<RealPointValuePair> comparator = null;
        optimizer.iterateSimplex(comparator);
        // Perform checks here
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = null;
        double coeff = 0.0;
        Comparator<RealPointValuePair> comparator = null;
        RealPointValuePair result = optimizer.evaluateNewSimplex(original, coeff, comparator);
        // Perform checks here
    }

}