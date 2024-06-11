import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import java.util.Comparator;

public class MultiDirectionalTest {
    @Test
    public void testIterateSimplex() {
        MultiDirectional optimizer = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = null;
        try {
            optimizer.iterateSimplex(comparator);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[3];
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = null;
        try {
            optimizer.evaluateNewSimplex(original, coeff, comparator);
        } catch (FunctionEvaluationException | OptimizationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}