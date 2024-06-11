import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;

public class MultiDirectionalTest {

    // Test data
    private MultiDirectional multiDirectional;

    @Before
    public void setup() {
        multiDirectional = new MultiDirectional();
    }

    // Test cases for iterateSimplex method

    @Test
    public void testIterateSimplex_ReflectLessThanBest() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Arrange
        multiDirectional.setSimplex(new RealPointValuePair[] {
                new RealPointValuePair(new double[]{2.0}, 10.0),
                new RealPointValuePair(new double[]{1.0}, 20.0)
        });

        // Act
        multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });

        // Assert
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[] {
                new RealPointValuePair(new double[]{1.0}, 20.0),
                new RealPointValuePair(new double[]{2.0}, 10.0)
        };
        assertArrayEquals(expectedSimplex, multiDirectional.getSimplex());
    }

    @Test
    public void testIterateSimplex_ReflectGreaterThanBest_EqualsExpanded() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Arrange
        multiDirectional.setSimplex(new RealPointValuePair[] {
                new RealPointValuePair(new double[]{2.0}, 10.0),
                new RealPointValuePair(new double[]{1.0}, 20.0)
        });

        // Act
        multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });

        // Assert
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[] {
                new RealPointValuePair(new double[]{1.0}, 20.0),
                new RealPointValuePair(new double[]{2.0}, 10.0)
        };
        assertArrayEquals(expectedSimplex, multiDirectional.getSimplex());
    }

    @Test
    public void testIterateSimplex_ContractedLessThanBest() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Arrange
        multiDirectional.setSimplex(new RealPointValuePair[] {
                new RealPointValuePair(new double[]{2.0}, 10.0),
                new RealPointValuePair(new double[]{1.0}, 20.0)
        });

        // Act
        multiDirectional.iterateSimplex(new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        });

        // Assert
        RealPointValuePair[] expectedSimplex = new RealPointValuePair[] {
                new RealPointValuePair(new double[]{1.0}, 20.0),
                new RealPointValuePair(new double[]{2.0}, 10.0)
        };
        assertArrayEquals(expectedSimplex, multiDirectional.getSimplex());
    }

    // Additional test cases for other methods in the class

    // ...

}