package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.*;
import org.apache.commons.math3.optim.point.*;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest extends AbstractMathTestCase {

    private PowellOptimizer optimizer;
    private UnivariateFunction function;
    private PointValuePair expectedResult;

    @Before
    public void setUp() {
        // Create a simple univariate function for testing.
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        // Create an optimizer with default settings.
        optimizer = new PowellOptimizer(1e-6, 1e-8);
    }

    @Test
    public void testEmptyConstructor() {
        PowellOptimizer emptyOptimizer = new PowellOptimizer();
        try {
            emptyOptimizer.optimize(function, new Point2D(0));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
        assertEquals("Expected an empty initializer list",
                     OptimizationProblem.EMPTY_INITIALIZER_LIST,
                     emptyOptimizer.getInitializer());
    }

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer defaultOptimizer = new PowellOptimizer(1e-6, 1e-8);
        assertEquals("Expected a SimpleXStartPoint initializer",
                     SimpleXStartPoint.class,
                     defaultOptimizer.getInitializer().getClass());
    }

    @Test
    public void testConstructorWithCustomInitializer() {
        Point2D startPoint = new Point2D(1, 2);
        PowellOptimizer customInitializerOptimizer = new PowellOptimizer(1e-6, 1e-8, startPoint);
        assertEquals("Expected a custom initializer", startPoint,
                     customInitializerOptimizer.getInitializer());
    }

    @Test
    public void testMinimize() {
        // Test with default settings.
        PointValuePair result = optimizer.optimize(function, new Point2D(0));
        expectedResult = new PointValuePair(new Point2D(0, Math.sqrt(0.0)), 0.0);
        assertEquals("Unexpected optimization result", expectedResult, result);

        // Test with custom settings.
        PowellOptimizer customOptimizer = new PowellOptimizer(1e-4, 1e-10);
        result = customOptimizer.optimize(function, new Point2D(0));
        expectedResult = new PointValuePair(new Point2D(0, Math.sqrt(0.0)), 0.0);
        assertEquals("Unexpected optimization result", expectedResult, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        new PowellOptimizer(1e-6, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        new PowellOptimizer(0.0, 1e-8);
    }
}

class Point2D implements Point {
    private double[] coordinates;

    public Point2D(double x, double y) {
        this.coordinates = new double[] {x, y};
    }

    @Override
    public double[] getCoordinates() {
        return coordinates;
    }

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", coordinates[0], coordinates[1]);
    }
}