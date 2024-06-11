import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractSimplexTest {
    
    @Test 
    public void testAbstractSimplexConstructor() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        assertEquals(3, simplex.getDimension());
    }
    
    @Test
    public void testAbstractSimplexBuild() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        assertEquals(3, simplex.getSize());
    }
    
    @Test
    public void testAbstractSimplexEvaluate() {
        AbstractSimplex simplex = new AbstractSimplex(3);
        double[] startPoint = {1.0, 2.0, 3.0};
        simplex.build(startPoint);
        simplex.evaluate((point) -> point[0] + point[1] + point[2], (p1, p2) -> Double.compare(p1.getValue(), p2.getValue()));
        assertEquals(6.0, simplex.getPoint(0).getValue(), 0.0001);
    }
    
    @Test
    public void testAbstractSimplexReplaceWorstPoint() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        simplex.evaluate((point) -> point[0] + point[1], (p1, p2) -> Double.compare(p1.getValue(), p2.getValue()));
        
        double[] newPoint = {2.0, 3.0};
        PointValuePair pointValuePair = new PointValuePair(newPoint, 5.0);
        
        simplex.replaceWorstPoint(pointValuePair, (p1, p2) -> Double.compare(p1.getValue(), p2.getValue()));
        
        assertEquals(5.0, simplex.getPoint(1).getValue(), 0.0001);
    }
    
    @Test 
    public void testAbstractSimplexSetPoint() {
        AbstractSimplex simplex = new AbstractSimplex(2);
        double[] startPoint = {1.0, 2.0};
        simplex.build(startPoint);
        
        double[] newPoint = {2.0, 3.0};
        PointValuePair newPointValuePair = new PointValuePair(newPoint, 5.0);

        simplex.setPoint(0, newPointValuePair);
        
        assertEquals(5.0, simplex.getPoint(0).getValue(), 0.0001);
    }
    
    @Test
    public void testAbstractSimplexCreateHypercubeSteps() {
        double[] steps = AbstractSimplex.createHypercubeSteps(4, 2.0);
        assertEquals(4, steps.length);
        for (double step : steps) {
            assertEquals(2.0, step, 0.0001);
        }
    }

}