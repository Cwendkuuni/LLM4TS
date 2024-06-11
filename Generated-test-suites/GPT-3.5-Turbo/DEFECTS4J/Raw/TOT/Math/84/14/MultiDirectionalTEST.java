import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.MultiDirectional;

import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(2.0, optimizer.getKhi(), 0.001);
        assertEquals(0.5, optimizer.getGamma(), 0.001);
    }

    @Test
    public void testParametrizedConstructor() {
        optimizer = new MultiDirectional(3.0, 0.3);
        assertEquals(3.0, optimizer.getKhi(), 0.001);
        assertEquals(0.3, optimizer.getGamma(), 0.001);
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException {
        // Some dummy simplex to test the method
        RealPointValuePair[] simplex = new RealPointValuePair[] {
                new RealPointValuePair(new double[] {1.0, 2.0, 3.0}, 0.0),
                new RealPointValuePair(new double[] {2.0, 3.0, 4.0}, 1.0),
                new RealPointValuePair(new double[] {3.0, 4.0, 5.0}, 2.0),
                new RealPointValuePair(new double[] {4.0, 5.0, 6.0}, 3.0)
        };

        // comparator to sort simplex vertices
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        // Call the method and check the result
        optimizer.iterateSimplex(comparator);
        RealPointValuePair[] newSimplex = optimizer.getSimplex();
        assertNotNull(newSimplex);
        assertNotSame(simplex, newSimplex);
        assertEquals(4, newSimplex.length);
        assertEquals(0.0, newSimplex[0].getValue(), 0.001);
        assertEquals(1.0, newSimplex[1].getValue(), 0.001);
        assertEquals(2.0, newSimplex[2].getValue(), 0.001);
        assertEquals(3.0, newSimplex[3].getValue(), 0.001);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        // Some dummy original simplex to test the method
        RealPointValuePair[] originalSimplex = new RealPointValuePair[] {
                new RealPointValuePair(new double[] {1.0, 2.0, 3.0}, 0.0),
                new RealPointValuePair(new double[] {2.0, 3.0, 4.0}, 1.0),
                new RealPointValuePair(new double[] {3.0, 4.0, 5.0}, 2.0),
                new RealPointValuePair(new double[] {4.0, 5.0, 6.0}, 3.0)
        };

        // Linear coefficient to transform simplex
        double coeff = 0.5;

        // comparator to sort simplex vertices
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        // Call the method and check the result
        RealPointValuePair newSimplex = optimizer.evaluateNewSimplex(originalSimplex, coeff, comparator);
        assertNotNull(newSimplex);
        assertNotSame(originalSimplex, optimizer.getSimplex());
        assertEquals(4, optimizer.getSimplex().length);
        assertEquals(0.0, optimizer.getSimplex()[0].getValue(), 0.001);
        assertEquals(1.0, optimizer.getSimplex()[1].getValue(), 0.001);
        assertEquals(2.0, optimizer.getSimplex()[2].getValue(), 0.001);
        assertEquals(3.0, optimizer.getSimplex()[3].getValue(), 0.001);
        assertNotSame(originalSimplex[0], optimizer.getSimplex()[0]);
        assertNotSame(originalSimplex[1], optimizer.getSimplex()[1]);
        assertNotSame(originalSimplex[2], optimizer.getSimplex()[2]);
        assertNotSame(originalSimplex[3], optimizer.getSimplex()[3]);
        assertEquals(0.5, optimizer.getSimplex()[0].getPointRef()[0], 0.001);
        assertEquals(1.0, optimizer.getSimplex()[0].getPointRef()[1], 0.001);
        assertEquals(1.5, optimizer.getSimplex()[0].getPointRef()[2], 0.001);
    }

}