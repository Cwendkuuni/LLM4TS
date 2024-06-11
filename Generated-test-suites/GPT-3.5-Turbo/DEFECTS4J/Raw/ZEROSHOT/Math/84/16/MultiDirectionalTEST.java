import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setup() {
        optimizer = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() {
        try {
            optimizer.iterateSimplex(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // catch the expected exception
        } catch (Exception e) {
            fail("Expected IllegalArgumentException");
        }
    }

    @Test
    public void testEvaluateNewSimplex() {
        try {
            optimizer.evaluateNewSimplex(null, 0.0, null);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // catch the expected exception
        } catch (Exception e) {
            fail("Expected FunctionEvaluationException");
        }
    }

    @Test
    public void testDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        assertNotNull(optimizer);
        assertEquals(2.0, optimizer.khi, 0.0);
        assertEquals(0.5, optimizer.gamma, 0.0);
    }

    @Test
    public void testParameterizedConstructor() {
        MultiDirectional optimizer = new MultiDirectional(1.0, 0.0);
        assertNotNull(optimizer);
        assertEquals(1.0, optimizer.khi, 0.0);
        assertEquals(0.0, optimizer.gamma, 0.0);
    }

    @Test
    public void testIterationCounter() {
        assertEquals(0, optimizer.getIterations());
        try {
            optimizer.iterateSimplex(null);
            assertEquals(1, optimizer.getIterations());
            optimizer.iterateSimplex(null);
            assertEquals(2, optimizer.getIterations());
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    public void testSimplexNull() {
        assertNull(optimizer.simplex);
        try {
            optimizer.iterateSimplex(null);
            assertNotNull(optimizer.simplex);
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    public void testSimplexSize() {
        assertEquals(0, optimizer.simplex.length);
        try {
            optimizer.iterateSimplex(null);
            assertEquals(2, optimizer.simplex.length);
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    public void testSimplexOrder() {
        try {
            optimizer.iterateSimplex(null);
            assertTrue(optimizer.comparator.compare(optimizer.simplex[0], optimizer.simplex[1]) <= 0);
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    public void testEvaluateSimplex() {
        try {
            optimizer.evaluateSimplex(null);
            assertNull(optimizer.simplex[0].getPointRef());
        } catch (FunctionEvaluationException e) {
            // catch the expected exception
        } catch (Exception e) {
            fail("Expected FunctionEvaluationException");
        }
    }

    @Test
    public void testEvaluateSimplexSize() {
        try {
            optimizer.evaluateSimplex(null);
            assertNotNull(optimizer.simplex[0].getPointRef());
            assertEquals(0, optimizer.simplex[0].getPointRef().length);
        } catch (FunctionEvaluationException e) {
            // catch the expected exception
        } catch (Exception e) {
            fail("Expected FunctionEvaluationException");
        }
    }

    @Test
    public void testEvaluateSimplexValues() {
        try {
            optimizer.evaluateSimplex(null);
            assertNotNull(optimizer.simplex[0].getPointRef());
            double[] pointRef = optimizer.simplex[0].getPointRef();
            for (int i = 0; i < pointRef.length; i++) {
                assertEquals(Double.NaN, pointRef[i], 0.0);
            }
        } catch (FunctionEvaluationException e) {
            // catch the expected exception
        } catch (Exception e) {
            fail("Expected FunctionEvaluationException");
        }
    }

    @Test
    public void testEvaluateSimplexException() {
        try {
            optimizer.simplex = new RealPointValuePair[1];
            optimizer.simplex[0] = new RealPointValuePair(new double[0], 0.0, false);
            optimizer.evaluateSimplex(null);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // catch the expected exception
        } catch (Exception e) {
            fail("Expected FunctionEvaluationException");
        }
    }

}