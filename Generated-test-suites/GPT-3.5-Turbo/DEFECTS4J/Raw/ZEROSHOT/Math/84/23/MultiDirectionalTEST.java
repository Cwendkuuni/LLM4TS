import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.Comparator;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        int initialIterations = optimizer.getIterations();
        Field iterationsCounter = getField("iterations");
        try {
            iterationsCounter.setInt(optimizer, initialIterations + 1);
            assertNotNull(optimizer.iterateSimplex(getComparator()));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[2];
        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = getComparator();
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0);
        original[1] = new RealPointValuePair(new double[]{4.0, 5.0}, 6.0);
        Field simplexField = getField("simplex");
        try {
            simplexField.set(optimizer, original);
            RealPointValuePair result = optimizer.evaluateNewSimplex(original, coeff, comparator);
            assertNotNull(result);
            assertArrayEquals(new double[]{2.5, 3.5}, result.getPoint(), 0.00001);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private Comparator<RealPointValuePair> getComparator() {
        return new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
    }

    private Field getField(String fieldName) throws NoSuchFieldException {
        Field field = optimizer.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

}