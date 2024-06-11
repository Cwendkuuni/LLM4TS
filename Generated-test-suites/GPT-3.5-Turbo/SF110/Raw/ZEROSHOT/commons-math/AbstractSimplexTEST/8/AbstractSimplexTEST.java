package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.junit.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.apache.commons.math3.analysis.*;
import java.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test(expected = NullArgumentException.class)
    public void testConstructor_NullSteps() {
        AbstractSimplex simplex = new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_EmptySteps() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{});
    }

    @Test(expected = ZeroException.class)
    public void testConstructor_ZeroStep() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{0.0});
    }

    @Test
    public void testGetDimension() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        assertEquals(2, simplex.getDimension());
    }

    @Test
    public void testGetSize() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        assertEquals(0, simplex.getSize());
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] variables) {
                return 0.0;
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return 0;
            }
        };
        simplex.iterate(function, comparator);
    }

    @Test
    public void testBuild() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        double[] startPoint = new double[]{1.0, 1.0};
        simplex.build(startPoint);
    }

    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] variables) {
                return 0.0;
            }
        };
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return 0;
            }
        };
        simplex.evaluate(function, comparator);
    }

    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        PointValuePair pointValuePair = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        Comparator<PointValuePair> comparator = new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return 0;
            }
        };
        simplex.replaceWorstPoint(pointValuePair, comparator);
    }

    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        PointValuePair[] copy = simplex.getPoints();
        for (int i = 0; i < copy.length; i++) {
            assertEquals(simplex.getPoint(i), copy[i]);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_IndexTooLow() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        simplex.getPoint(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetPoint_IndexTooHigh() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        simplex.getPoint(simplex.getSize());
    }

    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        simplex.setPoint(0, point);
        assertEquals(point, simplex.getPoint(0));
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_IndexTooLow() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        simplex.setPoint(-1, point);
    }

    @Test(expected = OutOfRangeException.class)
    public void testSetPoint_IndexTooHigh() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        PointValuePair point = new PointValuePair(new double[]{1.0, 1.0}, 0.0);
        simplex.setPoint(simplex.getSize(), point);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSetPoints_DimensionMismatch() {
        AbstractSimplex simplex = new AbstractSimplex(new double[]{1.0, 1.0});
        PointValuePair[] points = new PointValuePair[]{new PointValuePair(new double[]{1.0, 1.0}, 0.0)};
        simplex.setPoints(points);
    }
}