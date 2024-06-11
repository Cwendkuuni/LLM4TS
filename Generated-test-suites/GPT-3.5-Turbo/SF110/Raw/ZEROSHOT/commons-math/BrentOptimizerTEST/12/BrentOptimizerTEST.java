import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        // Test case 1
        double rel1 = 0.01;
        double abs1 = 0.1;
        BrentOptimizer optimizer1 = new BrentOptimizer(rel1, abs1);
        // Set initial values
        optimizer1.setMin(0.0);
        optimizer1.setMax(10.0);
        optimizer1.setStartValue(5.0);
        // Define objective function
        UnivariateFunction function1 = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x-3, 2);
            }
        };
        optimizer1.setObjectiveFunction(function1);
        // Optimize
        UnivariatePointValuePair result1 = optimizer1.doOptimize();
        // Check result
        assertEquals(3.0, result1.getPoint(), 0.0001);
    
        // Test case 2
        double rel2 = 0.001;
        double abs2 = 0.01;
        BrentOptimizer optimizer2 = new BrentOptimizer(rel2, abs2);
        // Set initial values
        optimizer2.setMin(-10.0);
        optimizer2.setMax(10.0);
        optimizer2.setStartValue(0.0);
        // Define objective function
        UnivariateFunction function2 = new UnivariateFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        optimizer2.setObjectiveFunction(function2);
        // Optimize
        UnivariatePointValuePair result2 = optimizer2.doOptimize();
        // Check result
        assertEquals(Math.PI, result2.getPoint(), 0.0001);
    
        // Add more test cases...

    }
}