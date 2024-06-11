
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Create an instance of PowellOptimizer
        PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-10);
        
        // Set the objective function
        optimizer.setObjectiveFunction(new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0] - 1, 2) + Math.pow(point[1] - 2, 2);
            }
        });
        
        // Set the start point
        optimizer.setStartPoint(new double[] {0, 0});
        
        // Optimize
        PointValuePair result = optimizer.optimize();
        
        // Check the result
        assertEquals(1.0, result.getPoint()[0], 1e-5);
        assertEquals(2.0, result.getPoint()[1], 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);
    }
}

