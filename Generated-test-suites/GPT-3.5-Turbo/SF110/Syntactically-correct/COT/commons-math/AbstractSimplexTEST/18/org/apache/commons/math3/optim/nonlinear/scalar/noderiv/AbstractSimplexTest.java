import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testConstructor1() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testConstructor2() {
        AbstractSimplex simplex = new ConcreteSimplex(3, 2.0);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testConstructor3() {
        double[] steps = {1.0, 2.0, 3.0};
        AbstractSimplex simplex = new ConcreteSimplex(steps);
        assertEquals(3, simplex.getDimension());
        assertEquals(4, simplex.getSize());
    }
    
    @Test
    public void testConstructor4() {
        double[][] referenceSimplex = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        AbstractSimplex simplex = new ConcreteSimplex(referenceSimplex);
        assertEquals(2, simplex.getDimension());
        assertEquals(3, simplex.getSize());
    }

    @Test
    public void testIterate() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        simplex.iterate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return 0;
            }
        });
        // TODO: Add assertions
    }
    
    @Test
    public void testBuild() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        // TODO: Add assertions
    }
    
    @Test
    public void testEvaluate() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.evaluate(new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        }, new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return 0;
            }
        });
        // TODO: Add assertions
    }
    
    @Test
    public void testReplaceWorstPoint() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.replaceWorstPoint(new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0), new Comparator<PointValuePair>() {
            public int compare(PointValuePair p1, PointValuePair p2) {
                return 0;
            }
        });
        // TODO: Add assertions
    }
    
    @Test
    public void testGetPoints() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair[] points = simplex.getPoints();
        assertEquals(4, points.length);
        // TODO: Add assertions
    }
    
    @Test
    public void testGetPoint() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        PointValuePair point = simplex.getPoint(1);
        // TODO: Add assertions
    }
    
    @Test
    public void testSetPoint() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.setPoint(1, new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0));
        // TODO: Add assertions
    }
    
    @Test
    public void testSetPoints() {
        AbstractSimplex simplex = new ConcreteSimplex(3);
        simplex.build(new double[]{1.0, 2.0, 3.0});
        simplex.setPoints(new PointValuePair[]{new PointValuePair(new double[]{4.0, 5.0, 6.0}, 0.0)});
        // TODO: Add assertions
    }

    private class ConcreteSimplex extends AbstractSimplex {
        public ConcreteSimplex(int dimension) {
            super(dimension);
        }
        public ConcreteSimplex(int dimension, double sideLength) {
            super(dimension, sideLength);
        }
        public ConcreteSimplex(double[] steps) {
            super(steps);
        }
        public ConcreteSimplex(double[][] referenceSimplex) {
            super(referenceSimplex);
        }
        public void iterate(MultivariateFunction p0, Comparator<PointValuePair> p1) {
            // TODO: Implement
        }
    }
}