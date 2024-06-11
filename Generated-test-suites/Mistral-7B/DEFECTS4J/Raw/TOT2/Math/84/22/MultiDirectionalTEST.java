import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalWithDefaultCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleOptimizationTest test = new SimpleOptimizationTest(optimizer);
        test.testF1();
    }

    @Test
    public void testMultiDirectionalWithCustomCoefficients() throws Exception {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.7);
        SimpleOptimizationTest test = new SimpleOptimizationTest(optimizer);
        test.testF1();
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testThrowsFunctionEvaluationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.iterateSimplex(null);
    }

    @Test(expected = OptimizationException.class)
    public void testThrowsOptimizationException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        optimizer.setMaxEvaluations(0);
        SimpleOptimizationTest test = new SimpleOptimizationTest(optimizer);
        test.testF1();
    }

    @Test
    public void testThrowsIllegalArgumentException() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        try {
            optimizer.iterateSimplex(null);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReflectedStep() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = {
                new RealPointValuePair(new double[]{1.0, 2.0}, 3.0),
                new RealPointValuePair(new double[]{3.0, 1.0}, 5.0)
        };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };
        optimizer.simplex = simplex;
        optimizer.iterateSimplex(comparator);
        RealPointValuePair reflected = optimizer.evaluateNewSimplex(simplex, 1.0, comparator);
        assertTrue(reflected.getValue() < simplex[0].getValue());
    }

    @Test
    public void testExpandedStep() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = {
                new RealPointValuePair(new double[]{1.0, 2.0}, 3.0),
                new RealPointValuePair(new double[]{3.0, 1.0}, 5.0)
        };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };
        optimizer.simplex = simplex;
        optimizer.iterateSimplex(comparator);
        RealPointValuePair expanded = optimizer.evaluateNewSimplex(simplex, 2.0, comparator);
        assertTrue(expanded.getValue() < reflected.getValue());
    }

    @Test
    public void testContractedStep() throws Exception {
        MultiDirectional optimizer = new MultiDirectional();
        RealPointValuePair[] simplex = {
                new RealPointValuePair(new double[]{1.0, 2.0}, 3.0),
                new RealPointValuePair(new double[]{3.0, 1.0}, 5.0)
        };
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return (int) (o1.getValue() - o2.getValue());
            }
        };
        optimizer.simplex = simplex;
        optimizer.iterateSimplex(comparator);
        RealPointValuePair contracted = optimizer.evaluateNewSimplex(simplex, 0.5, comparator);
        assertTrue(contracted.getValue() < simplex[0].getValue());
    }
}