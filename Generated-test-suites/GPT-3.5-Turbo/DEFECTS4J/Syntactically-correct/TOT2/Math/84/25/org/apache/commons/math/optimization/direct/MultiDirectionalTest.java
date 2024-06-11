import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.ObjectiveFunction;
import org.apache.commons.math.optimization.OptimizationException;

public class MultiDirectionalTest {

    @Test
    public void testDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertNotNull(md);
        assertEquals(2.0, md.getKhi(), 0.0001);
        assertEquals(0.5, md.getGamma(), 0.0001);
    }

    @Test
    public void testSpecifiedConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertNotNull(md);
        assertEquals(3.0, md.getKhi(), 0.0001);
        assertEquals(0.7, md.getGamma(), 0.0001);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[3]; // create a sample simplex
        // add some points to the simplex
        md.iterateSimplex(simplex, null);
        // test for expected behaviour after iterating simplex
    }

    @Test(expected = Exception.class)
    public void testEvaluateNewSimplex_Exception() throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[3]; // create a sample original simplex
        md.evaluateNewSimplex(original, 1.5, null); // force an exception
    }

    @Test
    public void testEvaluateNewSimplex_Convergence() throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[3]; // create a sample original simplex
        RealPointValuePair best = md.evaluateNewSimplex(original, 0.5, null); // converge the simplex
        // test for expected behaviour when simplex converges
    }

}