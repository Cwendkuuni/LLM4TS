import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.apache.commons.math3.optim.direct.MultiDirectionalOptions;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructor() {
        // Test default constructor
        MultiDirectional md = new MultiDirectional();
        assertNotNull(md.khi);
        assertNotNull(md.gamma);

        // Test constructor with specified coefficients
        MultiDirectional mdCustom = new MultiDirectional(3.0, 0.6);
        assertEquals(3.0, mdCustom.khi, 0.01);
        assertEquals(0.6, mdCustom.gamma, 0.01);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        // Given a MultiDirectional optimizer with default coefficients
        MultiDirectional md = new MultiDirectional();

        // Given a simple function for testing
        MultiDirectionalOptions options = new MultiDirectionalOptions();
        options.setMaxIterations(100);
        options.setConvergenceChecker(new RealConvergenceChecker(1e-6, 1e-6));
        Function f = new Function() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        // When iterating simplex
        RealPointValuePair[] simplex = md.optimize(f, options);

        // Then the method should return the minimum point
        assertEquals(-1.0, simplex[0].getPoint()[0], 0.01);

        // Test with a different function
        Function g = new Function() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }
        };

        simplex = md.optimize(g, options);
        // Then the method should return the minimum point
        assertEquals(-1.0, simplex[0].getPoint()[0], 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithNegativeCoefficients() {
        // Given a MultiDirectional optimizer with negative coefficients
        MultiDirectional md = new MultiDirectional(-2.0, -0.5);

        // When trying to iterate simplex
        try {
            md.iterateSimplex(null);
        } catch (Exception e) {
            fail("Should throw an IllegalArgumentException");
        }
        // Then an IllegalArgumentException should be thrown
    }

    // Add more test cases here for each method and potential edge cases

}