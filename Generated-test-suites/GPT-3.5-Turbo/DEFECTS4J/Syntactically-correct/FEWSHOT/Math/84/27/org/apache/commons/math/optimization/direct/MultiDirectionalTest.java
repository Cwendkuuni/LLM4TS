import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;
import java.util.Comparator;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional(2.0, 0.5);
    }

    @Test
    public void testIterateSimplex() {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        RealPointValuePair[] original = new RealPointValuePair[4];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 4.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 4.0}, 5.0);
        original[3] = new RealPointValuePair(new double[]{4.0, 5.0}, 6.0);
        optimizer.simplex = original;

        try {
            optimizer.iterateSimplex(comparator);
        } catch (Exception e) {
            assertNull(e);
        }

        RealPointValuePair[] expectedSimplex = new RealPointValuePair[4];
        expectedSimplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0);
        expectedSimplex[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 4.0);
        expectedSimplex[2] = new RealPointValuePair(new double[]{1.5, 2.5}, Double.NaN);
        expectedSimplex[3] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0);

        for (int i = 0; i < 4; i++) {
            assertEquals(expectedSimplex[i].getValue(), optimizer.simplex[i].getValue(), 1e-12);
            for (int j = 0; j < 2; j++) {
                assertEquals(expectedSimplex[i].getPoint()[j], optimizer.simplex[i].getPoint()[j], 1e-12);
            }
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 3.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 4.0);
        original[2] = new RealPointValuePair(new double[]{3.0, 4.0}, 5.0);

        try {
            RealPointValuePair best = optimizer.evaluateNewSimplex(original, 0.5, comparator);
            assertEquals(2.0, best.getValue(), 1e-12);
            for (int i = 0; i < 2; i++) {
                assertEquals(1.5 + 0.5 * (1.5 - original[i + 1].getPoint()[i]), best.getPoint()[i], 1e-12);
            }
        } catch (Exception e) {
            assertNull(e);
        }
    }

}