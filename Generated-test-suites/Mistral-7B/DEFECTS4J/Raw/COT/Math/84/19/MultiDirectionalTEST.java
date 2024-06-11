import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;

    @Before
    public void setup() {
        optimizer = new MultiDirectional();
    }

    // Test constructor with default coefficients
    @Test
    public void testMultiDirectionalDefaultConstructor() {
        assertEquals(2.0, optimizer.getKhi(), 0.01);
        assertEquals(0.5, optimizer.getGamma(), 0.01);
    }

    // Test constructor with specified coefficients
    @Test
    public void testMultiDirectionalSpecifiedConstructor() {
        MultiDirectional optimizerWithCustomCoefficients = new MultiDirectional(3.0, 0.6);
        assertEquals(3.0, optimizerWithCustomCoefficients.getKhi(), 0.01);
        assertEquals(0.6, optimizerWithCustomCoefficients.getGamma(), 0.01);
    }

    // Test iterateSimplex method
    @Test(expected = UnboundedDomainException.class)
    public void testIterateSimplexUnboundedDomainException() {
        optimizer = new MultiDirectional();
        optimizer.setMaxFunctionEvaluations(0);
        optimizer.optimize(new SimpleBounds(-10, 10), new TestFunction(), null);
    }

    @Test
    public void testIterateSimplexReflectionStep() {
        optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        double[] expectedX = {1.5, 1.5};
        double[] expectedFx = {2.5, 2.5};
        RealPointValuePair initialPoint = new RealPointValuePair(x, 0.0);
        RealPointValuePair reflectedPoint = optimizer.iterate(initialPoint);
        assertArrayEquals(expectedX, reflectedPoint.getPointRef(), 0.01);
        assertEquals(expectedFx[0], reflectedPoint.getFunctionValue(), 0.01);
        assertEquals(expectedFx[1], reflectedPoint.getFunctionValue(), 0.01);
    }

    @Test
    public void testIterateSimplexReflectionStepFailed() {
        optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        double[] expectedX = {1.5, 1.5};
        double[] expectedFx = {3.0, 3.0};
        RealPointValuePair initialPoint = new RealPointValuePair(x, 0.0);
        optimizer.setFunction(new TestFunctionWithLargeValues());
        RealPointValuePair reflectedPoint = optimizer.iterate(initialPoint);
        assertNull(reflectedPoint);
    }

    @Test
    public void testIterateSimplexExpansionStep() {
        optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        double[] expectedX = {2.5, 1.5};
        double[] expectedFx = {4.5, 2.5};
        RealPointValuePair initialPoint = new RealPointValuePair(x, 0.0);
        RealPointValuePair expandedPoint = optimizer.iterate(initialPoint);
        assertArrayEquals(expectedX, expandedPoint.getPointRef(), 0.01);
        assertEquals(expectedFx[0], expandedPoint.getFunctionValue(), 0.01);
        assertEquals(expectedFx[1], expandedPoint.getFunctionValue(), 0.01);
    }

    @Test
    public void testIterateSimplexExpansionStepFailed() {
        optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        double[] expectedX = {3.0, 1.5};
        double[] expectedFx = {6.0, 3.0};
        RealPointValuePair initialPoint = new RealPointValuePair(x, 0.0);
        optimizer.setFunction(new TestFunctionWithLargeValues());
        RealPointValuePair expandedPoint = optimizer.iterate(initialPoint);
        assertNull(expandedPoint);
    }

    @Test
    public void testIterateSimplexContractStep() {
        optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        double[] expectedX = {1.25, 1.75};
        double[] expectedFx = {1.5, 1.875};
        RealPointValuePair initialPoint = new RealPointValuePair(x, 0.0);
        RealPointValuePair contractedPoint = optimizer.iterate(initialPoint);
        assertArrayEquals(expectedX, contractedPoint.getPointRef(), 0.01);
        assertEquals(expectedFx[0], contractedPoint.getFunctionValue(), 0.01);
        assertEquals(expectedFx[1], contractedPoint.getFunctionValue(), 0.01);
    }

    @Test
    public void testIterateSimplexContractStepFailed() {
        optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        double[] expectedX = {1.0, 1.4};
        double[] expectedFx = {1.2, 1.08};
        RealPointValuePair initialPoint = new RealPointValuePair(x, 0.0);
        optimizer.setFunction(new TestFunctionWithSmallValues());
        RealPointValuePair contractedPoint = optimizer.iterate(initialPoint);
        assertArrayEquals(expectedX, contractedPoint.getPointRef(), 0.01);
        assertEquals(expectedFx[0], contractedPoint.getFunctionValue(), 0.01);
        assertEquals(expectedFx[1], contractedPoint.getFunctionValue(), 0.01);
    }

    @Test
    public void testIterateSimplexConvergence() {
        optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        double[] expectedX = {1.5, 1.5};
        double[] expectedFx = {2.5, 2.5};
        RealPointValuePair initialPoint = new RealPointValuePair(x, 0.0);
        RealPointValuePair convergencePoint = optimizer.iterate(initialPoint);
        assertArrayEquals(expectedX, convergencePoint.getPointRef(), 0.01);
        assertEquals(expectedFx[0], convergencePoint.getFunctionValue(), 0.01);
        assertEquals(expectedFx[1], convergencePoint.getFunctionValue(), 0.01);
    }

    @Test
    public void testIterateSimplexConvergenceFailed() {
        optimizer = new MultiDirectional();
        double[] x = {1.0, 2.0};
        double[] expectedX = {1.5, 1.5};
        double[] expectedFx = {3.0, 3.0};
        RealPointValuePair initialPoint = new RealPointValuePair(x, 0.0);
        optimizer.setFunction(new TestFunctionWithLargeValues());
        RealPointValuePair convergencePoint = optimizer.iterate(initialPoint);
        assertNull(convergencePoint);
    }
}

class TestFunction implements org.apache.commons.math3.optim.UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 2);
    }

    @Override
    public double derivative(double x) {
        return 2 * x;
    }

}

class TestFunctionWithLargeValues implements org.apache.commons.math3.optim.UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, 4);
    }

    @Override
    public double derivative(double x) {
        return 4 * Math.pow(x, 3);
    }

}

class TestFunctionWithSmallValues implements org.apache.commons.math3.optim.UnivariateFunction {

    @Override
    public double value(double x) {
        return Math.pow(x, -2);
    }

    @Override
    public double derivative(double x) {
        return -2 * Math.pow(x, -3);
    }

}