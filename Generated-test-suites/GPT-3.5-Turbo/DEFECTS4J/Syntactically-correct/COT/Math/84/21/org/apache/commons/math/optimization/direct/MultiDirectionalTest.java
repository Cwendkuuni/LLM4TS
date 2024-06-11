import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(multiDirectional);
    }

    @Test
    public void testCustomConstructor() {
        double khi = 3.0;
        double gamma = 0.8;
        multiDirectional = new MultiDirectional(khi, gamma);
        assertNotNull(multiDirectional);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        // Create a test implementation of Comparator<RealPointValuePair>
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        // Set up the initial simplex
        int n = 3; // Number of dimensions
        RealPointValuePair[] simplex = new RealPointValuePair[n + 1];
        double[] point0 = {1.0, 2.0, 3.0};
        simplex[0] = new RealPointValuePair(point0, 0.0);

        // Set up the expected final simplex
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[n + 1];
        double[] expectedPoint0 = {1.0, 2.0, 3.0};
        double[] expectedPoint1 = {1.5, 2.5, 3.5};
        expectedSimplex[0] = new RealPointValuePair(expectedPoint0, 0.0);
        expectedSimplex[1] = new RealPointValuePair(expectedPoint1, 0.0);

        // Perform the iteration
        multiDirectional.setSimplex(simplex); // Set the initial simplex
        multiDirectional.iterateSimplex(comparator);

        // Check the final simplex
        RealPointValuePair[] actualSimplex = multiDirectional.getSimplex();
        for (int i = 0; i <= n; i++) {
            assertEquals(expectedSimplex[i], actualSimplex[i]);
        }
    }
}