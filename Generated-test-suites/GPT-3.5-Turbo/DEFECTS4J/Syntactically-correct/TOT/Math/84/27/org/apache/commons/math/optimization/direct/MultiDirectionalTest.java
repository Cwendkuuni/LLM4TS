import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testSimplexInitialization() {
        // Arrange

        // Act
        // Initialize simplex
        multiDirectional.initializeSimplex(2, 4.5, new double[2]);
        RealPointValuePair[] simplex = multiDirectional.getSimplex();

        // Assert
        assertEquals(3, simplex.length);
        // Check the points of the simplex
        assertEquals(2, simplex[0].getPointRef().length);
        assertEquals(2, simplex[1].getPointRef().length);
        assertEquals(2, simplex[2].getPointRef().length);
        // Check that the values of the points in the simplex are correctly initialized
        assertEquals(4.5, simplex[0].getPointRef()[0], 0.0);
        assertEquals(4.5, simplex[1].getPointRef()[0], 0.0);
        assertEquals(4.5, simplex[2].getPointRef()[0], 0.0);
        assertEquals(4.5, simplex[0].getPointRef()[1], 0.0);
        assertEquals(4.5, simplex[1].getPointRef()[1], 0.0);
        assertEquals(4.5, simplex[2].getPointRef()[1], 0.0);
    }

    @Test
    public void testIterateSimplexWithReflection() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Arrange

        // Act
        // Initialize simplex
        multiDirectional.initializeSimplex(2, 4.5, new double[2]);
        RealPointValuePair[] original = multiDirectional.getSimplex();
        Comparator<RealPointValuePair> comparator = new Comparator<>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        multiDirectional.iterateSimplex(comparator);
        RealPointValuePair[] simplex = multiDirectional.getSimplex();

        // Assert
        assertEquals(3, simplex.length);
        // Check the points of the simplex
        assertEquals(2, simplex[0].getPointRef().length);
        assertEquals(2, simplex[1].getPointRef().length);
        assertEquals(2, simplex[2].getPointRef().length);
        // Check that the values of the points in the simplex are correctly updated
        assertEquals(4.5, simplex[0].getPointRef()[0], 0.0);
        assertEquals(4.5, simplex[1].getPointRef()[0], 0.0);
        assertEquals(4.5, simplex[2].getPointRef()[0], 0.0);
        assertEquals(4.5, simplex[0].getPointRef()[1], 0.0);
        assertEquals(4.5, simplex[1].getPointRef()[1], 0.0);
        assertEquals(4.5, simplex[2].getPointRef()[1], 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithIllegalComparator() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Arrange

        // Act
        // Initialize simplex
        multiDirectional.initializeSimplex(2, 4.5, new double[2]);
        RealPointValuePair[] original = multiDirectional.getSimplex();
        Comparator<RealPointValuePair> comparator = null;
        multiDirectional.iterateSimplex(comparator);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        // Arrange

        // Act
        // Initialize original simplex
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{2.0, 4.5}, 1.5, false);
        original[1] = new RealPointValuePair(new double[]{3.0, 4.5}, 2.5, false);
        original[2] = new RealPointValuePair(new double[]{4.0, 4.5}, 3.5, false);
        multiDirectional.initializeSimplex(original);
        Comparator<RealPointValuePair> comparator = new Comparator<>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        RealPointValuePair best = multiDirectional.evaluateNewSimplex(original, 0.5, comparator);

        // Assert
        assertEquals(2, best.getPointRef().length);
        assertEquals(2.5, best.getPointRef()[0], 0.0);
        assertEquals(4.5, best.getPointRef()[1], 0.0);
    }

}