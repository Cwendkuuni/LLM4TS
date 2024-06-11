import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Initialize simplex
        RealPointValuePair[] simplex = new RealPointValuePair[3];
        simplex[0] = new RealPointValuePair(new double[] { 1.0, 2.0, 3.0 }, 1.0);
        simplex[1] = new RealPointValuePair(new double[] { 2.0, 3.0, 4.0 }, 2.0);
        simplex[2] = new RealPointValuePair(new double[] { 3.0, 4.0, 5.0 }, 3.0);
        optimizer.setSimplex(simplex);

        // Comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);

        // Assert results
        RealPointValuePair[] newSimplex = optimizer.getSimplex();
        assertEquals(3, newSimplex.length);
        assertEquals(1.0, newSimplex[0].getValue(), 0.0);
        assertEquals(3.0, newSimplex[1].getValue(), 0.0);
        assertEquals(2.0, newSimplex[2].getValue(), 0.0);
    }

    // Additional test cases for edge cases and exception handling scenarios:

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Set up invalid function
        optimizer.setObjectiveFunction(new InvalidFunction());

        // Set up simplex
        RealPointValuePair[] simplex = new RealPointValuePair[1];
        simplex[0] = new RealPointValuePair(new double[] { 1.0 }, 1.0);
        optimizer.setSimplex(simplex);

        // Comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexOptimizationException() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Set up invalid maximum number of evaluations
        optimizer.setMaximalIterationCount(0);

        // Set up simplex
        RealPointValuePair[] simplex = new RealPointValuePair[1];
        simplex[0] = new RealPointValuePair(new double[] { 1.0 }, 1.0);
        optimizer.setSimplex(simplex);

        // Comparator
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        optimizer.iterateSimplex(comparator);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        // Set up original simplex
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[] { 1.0, 2.0, 3.0 }, 1.0);
        original[1] = new RealPointValuePair(new double[] { 2.0, 3.0, 4.0 }, 2.0);
        original[2] = new RealPointValuePair(new double[] { 3.0, 4.0, 5.0 }, 3.0);

        // Evaluate new simplex with coefficient 0.5
        RealPointValuePair newSimplex = optimizer.evaluateNewSimplex(original, 0.5, null);

        // Assert results
        double[] expectedPoint = { 0.5, 1.0, 1.5 };
        assertEquals(0.0, newSimplex.getValue(), 0.0);
        assertTrue(Arrays.equals(expectedPoint, newSimplex.getPoint()));
    }

    // Additional test cases for evaluateNewSimplex():

    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluateNewSimplexWithInvalidFunction() throws FunctionEvaluationException, OptimizationException {
        // Set up invalid function
        optimizer.setObjectiveFunction(new InvalidFunction());

        // Set up original simplex
        RealPointValuePair[] original = new RealPointValuePair[1];
        original[0] = new RealPointValuePair(new double[] { 1.0 }, 1.0);

        // Evaluate new simplex with coefficient 0.5
        optimizer.evaluateNewSimplex(original, 0.5, null);
    }

    @Test
    public void testEvaluateNewSimplexWithEmptyOriginal() throws FunctionEvaluationException, OptimizationException {
        // Set up original simplex
        RealPointValuePair[] original = new RealPointValuePair[0];

        // Evaluate new simplex with coefficient 0.5
        RealPointValuePair newSimplex = optimizer.evaluateNewSimplex(original, 0.5, null);

        // Assert results
        assertTrue(Arrays.equals(new double[0], newSimplex.getPoint()));
        assertEquals(Double.NaN, newSimplex.getValue(), 0.0);
        assertFalse(newSimplex.isOptimal());
    }

    
}