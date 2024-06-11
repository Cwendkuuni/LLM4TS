import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;

public class MultiDirectionalTest {
    
    @Test
    public void testMultiDirectional() {
        MultiDirectional multiDirectional = new MultiDirectional();
        assertEquals(2.0, multiDirectional.khi, 0.0);
        assertEquals(0.5, multiDirectional.gamma, 0.0);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[3];
        double[] xSmallest = {1.0, 2.0, 3.0};
        double[] xOriginal1 = {2.0, 3.0, 4.0};
        double[] xTransformed1 = {0.0, 1.0, 2.0};
        double[] xOriginal2 = {3.0, 4.0, 5.0};
        double[] xTransformed2 = {-1.0, 0.0, 1.0};
        original[0] = new RealPointValuePair(xSmallest, 0.0, false);
        original[1] = new RealPointValuePair(xOriginal1, 0.0, false);
        original[2] = new RealPointValuePair(xOriginal2, 0.0, false);
        MultiDirectional multiDirectional = new MultiDirectional();
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 0.5, comparator);
        assertArrayEquals(xTransformed1, result.getPointRef(), 0.0);
    }

}