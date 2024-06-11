import static org.junit.Assert.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.junit.Test;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalDefaultConstructor() {
        MultiDirectional md = new MultiDirectional();
        assertNotNull(md);
        assertEquals(2.0, md.khi, 0.01);
        assertEquals(0.5, md.gamma, 0.01);
    }

    @Test
    public void testMultiDirectionalCustomConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertNotNull(md);
        assertEquals(3.0, md.khi, 0.01);
        assertEquals(0.7, md.gamma, 0.01);
    }

    @Test
    public void testIterateSimplexThrowsFunctionEvaluationException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[1];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return 0;
            }
        };
        try {
            md.iterateSimplex(comparator);
            fail("FunctionEvaluationException expected");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testIterateSimplexThrowsOptimizationException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[1];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return 0;
            }
        };
        try {
            for (int i = 0; i < 1000; i++) {
                md.iterateSimplex(comparator);
            }
            fail("OptimizationException expected");
        } catch (OptimizationException e) {
            // expected
        }
    }

    @Test
    public void testIterateSimplexThrowsIllegalArgumentException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] simplex = new RealPointValuePair[0];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return 0;
            }
        };
        try {
            md.iterateSimplex(comparator);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateNewSimplexThrowsFunctionEvaluationException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[1];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return 0;
            }
        };
        try {
            md.evaluateNewSimplex(original, 1.0, comparator);
            fail("FunctionEvaluationException expected");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateNewSimplexThrowsOptimizationException() throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[1];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return 0;
            }
        };
        try {
            md.evaluateNewSimplex(original, 10.0, comparator);
            fail("OptimizationException expected");
        } catch (OptimizationException e) {
            // expected
        }
    }

    @Test
    public void testEvaluateNewSimplexReturnsBestPointWhenReflectedSimplexFitter()
        throws Exception {
        MultiDirectional md = new MultiDirectional();
        RealPointValuePair[] original = new RealPointValuePair[2];
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        };
        original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0, false);
        original[1] = new RealPointValuePair(new double[]{2.0, 2.0}, 4.0, false);
        RealPointValuePair best = original[0];

        RealPointValuePair reflected = md.evaluateNewSimplex(original, 1.0, comparator);
        PointValuePair reflectedValue = reflected.getValue();
        assertTrue(reflectedValue.getValue() < best.getValue());

        RealPointValuePair expanded = md.evaluateNewSimplex(original, md.khi, comparator);
        PointValuePair expandedValue = expanded.getValue();
        assertTrue(reflectedValue.getValue() <= expandedValue.getValue());

        simplex = original; // reset simplex array for next test

        RealPointValuePair contracted = md.evaluateNewSimplex(original, md.gamma, comparator);
        PointValuePair contractedValue = contracted.getValue();
        assertTrue(contractedValue.getValue() < best.getValue());
    }

}