import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional multiDirectional = new MultiDirectional();
        multiDirectional.iterateSimplex(null); // Replace null with appropriate comparator
        // assert statements
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        MultiDirectional multiDirectional = new MultiDirectional();
        RealPointValuePair[] original = { new RealPointValuePair(new double[]{1.0, 2.0}, 0.0),
                                           new RealPointValuePair(new double[]{2.0, 3.0}, 0.0) };
        RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 0.0, null); // Replace null with appropriate comparator
        // assert statements
    }

    // Add more test methods for other public methods in the MultiDirectional class

}