import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConvergenceChecker;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.function.FunctionUtils;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;
import org.apache.commons.math3.optim.point.OpenPointValuePair;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private double[] xSmallest;
    private double[] xBest;
    private double[] xReflected;
    private double[] xExpanded;
    private double[] xContracted;
    private RealConvergenceChecker convergenceChecker;
    private Comparator<PointValuePair> comparator;
    private PolynomialFunction function;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        xSmallest = new double[]{0.0, 0.0};
        xBest = new double[]{1.0, 1.0};
        xReflected = new double[]{0.5, 0.5};
        xExpanded = new double[]{2.5, 2.5};
        xContracted = new double[]{0.25, 0.25};
        convergenceChecker = new RealConvergenceChecker(1e-6, 100);
        comparator = new java.util.Collections.reverseOrder(new SimpleGoalType());
        function = new PolynomialFunction(new double[]{-1, 1, 1, -1}, new double[]{0.0, 0.0, 0.0, 1.0});
    }

    @Test
    public void testIterateSimplex() throws Exception {
        RealPointValuePair[] original = new RealPointValuePair[]{
                new RealPointValuePair(new OpenPointValuePair(xSmallest), 0.0, false),
                new RealPointValuePair(new OpenPointValuePair(xBest), 1.0, false)};

        // Reflection
        PointValuePair reflected = optimizer.iterateSimplex(comparator);
        assertTrue(comparator.compare(reflected, new RealPointValuePair(new OpenPointValuePair(xReflected), Double.NaN, false)) < 0);

        // Check if reflected simplex is accepted
        RealPointValuePair[] reflectedSimplex = simplexToArray(optimizer.simplex);
        assertTrue(MathArrays.areSame(reflectedSimplex[0].getPointRef(), xReflected));

        // Expansion
        PointValuePair expanded = optimizer.iterateSimplex(comparator);
        assertTrue(comparator.compare(expanded, new RealPointValuePair(new OpenPointValuePair(xExpanded), Double.NaN, false)) <= 0);

        // Check if expanded simplex is not accepted
        RealPointValuePair[] expandedSimplex = simplexToArray(optimizer.simplex);
        assertNotSame(reflectedSimplex[0].getPointRef(), expandedSimplex[0].getPointRef());

        // Contraction
        PointValuePair contracted = optimizer.iterateSimplex(comparator);
        assertTrue(contracted.getValue() < 0);

        // Check convergence
        optimizer.setMaxIterations(1);
        optimizer.optimize(function, null, convergenceChecker);
        assertEquals(0, optimizer.getIterations());
    }

    private RealPointValuePair[] simplexToArray(RealPointValuePair[] simplex) {
        RealPointValuePair[] result = new RealPointValuePair[simplex.length];
        for (int i = 0; i < simplex.length; i++) {
            result[i] = simplex[i];
        }
        return result;
    }

}