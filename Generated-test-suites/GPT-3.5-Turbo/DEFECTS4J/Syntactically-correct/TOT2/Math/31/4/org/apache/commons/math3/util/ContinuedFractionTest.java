import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {
    
    @Test
    public void testGetA() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                // Implement getA method for testing
                return 1.0 / (n + 1);
            }

            @Override
            protected double getB(int n, double x) {
                return 0.0;
            }
        };
        
        assertEquals(0.5, cf.getA(1, 1.0), 0.0001);
    }
    
    @Test
    public void testGetB() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 0.0;
            }

            @Override
            protected double getB(int n, double x) {
                // Implement getB method for testing
                return 2.0;
            }
        };
        
        assertEquals(2.0, cf.getB(1, 1.0), 0.0001);
    }
    
    @Test
    public void testEvaluate_defaultEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        
        double result = cf.evaluate(1.0);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test
    public void testEvaluate_customEpsilonAndMaxIterations() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        
        double result = cf.evaluate(1.0, 0.1, 5);
        assertEquals(2.0, result, 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testEvaluate_nonConvergent() {
        ContinuedFraction cf = new ContinuedFraction() {
            @Override
            protected double getA(int n, double x) {
                return 1.0;
            }

            @Override
            protected double getB(int n, double x) {
                return 1.0;
            }
        };
        
        cf.evaluate(1.0, 0.00001, 5);
    }
}