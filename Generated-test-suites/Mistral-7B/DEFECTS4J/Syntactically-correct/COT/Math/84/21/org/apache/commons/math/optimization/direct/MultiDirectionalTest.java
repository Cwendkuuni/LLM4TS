package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleValueFunction;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;
    private RealPointValuePair[] simplex;
    private Comparator<RealPointValuePair> comparator;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
        simplex = new RealPointValuePair[3];
        comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
    }

    @Test
    public void testIterateSimplexDefaultConstructor() throws Exception {
        // Test default constructor
        RealPointValuePair[] original = new RealPointValuePair[] {
            new RealPointValuePair(new double[] {1.0, 1.0}, 2.0),
            new RealPointValuePair(new double[] {1.5, 1.5}, 3.0),
            new RealPointValuePair(new double[] {2.0, 2.0}, 4.0)
        };
        multiDirectional.iterateSimplex(comparator);
        // Check that the simplex has been updated
        assertEquals(3, simplex.length);
        assertEquals(original[0], simplex[0]);
        // Reflection test
        PointValuePair reflected = new SimpleValueFunction(f -> Math.pow(f[0], 2) + Math.pow(f[1], 2))
            .value(new double[] {1.5, 1.5});
        RealPointValuePair expectedReflected = new RealPointValuePair(new double[] {1.7320508075688772, 1.7320508075688772}, reflected.getValue(), false);
        assertEquals(expectedReflected, simplex[1]);
        // Expansion test
        PointValuePair expanded = new SimpleValueFunction(f -> Math.pow(f[0], 2) + Math.pow(f[1], 2))
            .value(new double[] {2.865925896943948, 2.865925896943948});
        RealPointValuePair expectedExpanded = new RealPointValuePair(new double[] {3.605551272463554, 3.605551272463554}, expanded.getValue(), false);
        assertTrue(Arrays.equals(simplex, new RealPointValuePair[] {original[0], expectedExpanded, expectedReflected}));
    }

    @Test
    public void testIterateSimplexCustomConstructor() throws Exception {
        // Test custom constructor
        RealPointValuePair[] original = new RealPointValuePair[] {
            new RealPointValuePair(new double[] {1.0, 1.0}, 2.0),
            new RealPointValuePair(new double[] {1.5, 1.5}, 3.0),
            new RealPointValuePair(new double[] {2.0, 2.0}, 4.0)
        };
        double khi = 3.0;
        double gamma = 1.0;
        MultiDirectional multiDirectional = new MultiDirectional(khi, gamma);
        multiDirectional.iterateSimplex(comparator);
        // Check that the simplex has been updated
        assertEquals(3, simplex.length);
        assertEquals(original[0], simplex[0]);
        // Reflection test
        PointValuePair reflected = new SimpleValueFunction(f -> Math.pow(f[0], 2) + Math.pow(f[1], 2))
            .value(new double[] {1.5, 1.5});
        RealPointValuePair expectedReflected = new RealPointValuePair(new double[] {1.982958345497508, 1.982958345497508}, reflected.getValue(), false);
        assertEquals(expectedReflected, simplex[1]);
        // Expansion test
        PointValuePair expanded = new SimpleValueFunction(f -> Math.pow(f[0], 2) + Math.pow(f[1], 2))
            .value(new double[] {3.131377551021261, 3.131377551021261});
        RealPointValuePair expectedExpanded = new RealPointValuePair(new double[] {4.494897929142868, 4.494897929142868}, expanded.getValue(), false);
        assertTrue(Arrays.equals(simplex, new RealPointValuePair[] {original[0], expectedExpanded, expectedReflected}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexNullComparator() throws Exception {
        // Test null comparator
        multiDirectional.iterateSimplex(null);
    }

}