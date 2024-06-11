import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class ContinuedFractionTest {

    @Test
    public void testEvaluate_defaultParameters() {
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
        assertEquals(1.0, result, 0.0001);
    }

    @Test
    public void testEvaluate_withEpsilon() {
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
        
        double result = cf.evaluate(1.0, 0.001);
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testEvaluate_withMaxIterations() {
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
        
        double result = cf.evaluate(1.0, 10);
        assertEquals(1.0, result, 0.0001);
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
                return 0.0; // Force non-convergence
            }
        };
        
        cf.evaluate(1.0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testEvaluate_maxIterationsExceeded() {
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
        
        cf.evaluate(1.0, 1);
    }

}