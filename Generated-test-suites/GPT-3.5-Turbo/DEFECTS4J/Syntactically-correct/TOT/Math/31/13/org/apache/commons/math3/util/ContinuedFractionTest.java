import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testGetA() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl1();
        assertEquals(1.0, cf.getA(0, 0.5), 0.0001);
        
        // Test edge case
        ContinuedFraction cf = new ContinuedFractionImpl2();
        assertEquals(10.0, cf.getA(0, 0.0), 0.0001);
        
        // Test error scenario
        ContinuedFraction cf = new ContinuedFractionImpl3();
        assertEquals(Double.NaN, cf.getA(0, Double.NaN), 0.0001);
    }

    @Test
    public void testGetB() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl1();
        assertEquals(2.5, cf.getB(0, 0.5), 0.0001);
        
        // Test edge case
        ContinuedFraction cf = new ContinuedFractionImpl2();
        assertEquals(0.0, cf.getB(0, -1.0), 0.0001);
        
        // Test error scenario
        ContinuedFraction cf = new ContinuedFractionImpl3();
        assertEquals(Double.POSITIVE_INFINITY, cf.getB(0, 10.0), 0.0001);
    }

    @Test
    public void testEvaluate() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl1();
        assertEquals(1/(1+0.5), cf.evaluate(0.5), 0.0001);
        
        // Test edge case
        ContinuedFraction cf = new ContinuedFractionImpl2();
        assertEquals(Double.POSITIVE_INFINITY, cf.evaluate(-1.0), 0.0001);
        
        // Test error scenario
        ContinuedFraction cf = new ContinuedFractionImpl3();
        assertEquals(Double.NaN, cf.evaluate(Double.NaN), 0.0001);
    }

    @Test
    public void testEvaluateWithEpsilon() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl1();
        assertEquals(1/(1+0.5), cf.evaluate(0.5, 0.0001), 0.0001);
        
        // Test edge case
        ContinuedFraction cf = new ContinuedFractionImpl2();
        assertEquals(Double.POSITIVE_INFINITY, cf.evaluate(-1.0, 0.0001), 0.0001);
        
        // Test error scenario
        ContinuedFraction cf = new ContinuedFractionImpl3();
        assertEquals(Double.NaN, cf.evaluate(Double.NaN, 0.0001), 0.0001);
    }

    @Test
    public void testEvaluateWithMaxIterations() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl1();
        assertEquals(1/(1+0.5), cf.evaluate(0.5, 10), 0.0001);
        
        // Test edge case
        ContinuedFraction cf = new ContinuedFractionImpl2();
        assertEquals(Double.POSITIVE_INFINITY, cf.evaluate(-1.0, 10), 0.0001);
        
        // Test error scenario
        ContinuedFraction cf = new ContinuedFractionImpl3();
        assertEquals(Double.NaN, cf.evaluate(Double.NaN, 10), 0.0001);
    }

    @Test
    public void testEvaluateWithEpsilonAndMaxIterations() {
        // Test typical use case
        ContinuedFraction cf = new ContinuedFractionImpl1();
        assertEquals(1/(1+0.5), cf.evaluate(0.5, 0.0001, 10), 0.0001);
        
        // Test edge case
        ContinuedFraction cf = new ContinuedFractionImpl2();
        assertEquals(Double.POSITIVE_INFINITY, cf.evaluate(-1.0, 0.0001, 10), 0.0001);
        
        // Test error scenario
        ContinuedFraction cf = new ContinuedFractionImpl3();
        assertEquals(Double.NaN, cf.evaluate(Double.NaN, 0.0001, 10), 0.0001);
    }

    // Additional test cases for specific implementations of ContinuedFraction
    @Test
    public void testSpecificImplementation1() {
        ContinuedFraction cf = new ContinuedFractionImpl1();
        
        // Test specific behavior of getA and getB methods for this implementation
        assertEquals(2.0, cf.getA(1, 0.5), 0.0001);
        assertEquals(3.0, cf.getB(1, 0.5), 0.0001);
    }
    
    @Test
    public void testSpecificImplementation2() {
        ContinuedFraction cf = new ContinuedFractionImpl2();
        
        // Test specific behavior of getA and getB methods for this implementation
        assertEquals(5.0, cf.getA(1, 0.5), 0.0001);
        assertEquals(0.5, cf.getB(1, 0.5), 0.0001);
    }
    
    @Test
    public void testSpecificImplementation3() {
        ContinuedFraction cf = new ContinuedFractionImpl3();
        
        // Test specific behavior of getA and getB methods for this implementation
        assertEquals(Double.NaN, cf.getA(1, 0.5), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, cf.getB(1, 0.5), 0.0001);
    }
}

class ContinuedFractionImpl1 extends ContinuedFraction {
    protected double getA(int n, double x) {
        if (n == 0) {
            return 1.0;
        } else {
            return n+1;
        }
    }
    
    protected double getB(int n, double x) {
        if (n == 0) {
            return 2.0;
        } else {
            return n+2;
        }
    }
}

class ContinuedFractionImpl2 extends ContinuedFraction {
    protected double getA(int n, double x) {
        if (n == 0) {
            return 10.0;
        } else {
            return n+5;
        }
    }
    
    protected double getB(int n, double x) {
        if (n == 0) {
            return 0.0;
        } else {
            return n/2;
        }
    }
}

class ContinuedFractionImpl3 extends ContinuedFraction {
    protected double getA(int n, double x) {
        return Double.NaN;
    }
    
    protected double getB(int n, double x) {
        return Double.POSITIVE_INFINITY;
    }
}