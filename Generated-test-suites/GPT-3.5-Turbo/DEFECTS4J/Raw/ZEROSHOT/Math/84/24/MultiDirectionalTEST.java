import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testIterateSimplex() {
        MultiDirectional multiDir = new MultiDirectional();

        // Set up test data
        double[] point = {1, 2, 3};
        double[] value = {4, 5, 6};
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(point, value[0]);
        original[1] = new RealPointValuePair(point, value[1]);
        multiDir.setSimplex(original);

        // Call the method to be tested
        try {
            multiDir.iterateSimplex(null);
        } catch (Exception e) {
            fail("Exception should not be thown");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        MultiDirectional multiDir = new MultiDirectional();

        // Set up test data
        double[] xSmallest = {1, 2, 3};
        double coeff = 0.5;
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(xSmallest, 4);
        original[1] = new RealPointValuePair(xSmallest, 5);

        // Call the method to be tested
        try {
            RealPointValuePair result = multiDir.evaluateNewSimplex(original, coeff, null);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Exception should not be thown");
        }
    }

}