
package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.junit.Test;

public class SimplexOptimizerTest {
    @Test
    public void testDoOptimize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        double[] startingPoint = {1.0, 2.0, 3.0};
        double[] expectedOptimalPoint = {1.0, 1.0, 1.0};

        // Create an optimization data object with the starting point.
        OptimizationData[] optData = new OptimizationData[]{
            new InitialGuess(startingPoint)
        };
        
        // Define the objective function.
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                double x = point[0];
                double y = point[1];
                double z = point[2];
                
                return x*x + y*y + z*z;
            }
        };

        // Perform the optimization.
        PointValuePair result = optimizer.optimize(1000, objectiveFunction, GoalType.MINIMIZE, optData);

        assertNotNull(result);
        double[] optimalPoint = result.getPoint();
        assertNotNull(optimalPoint);
        assertTrue(optimalPoint.length == expectedOptimalPoint.length);
        for (int i = 0; i < optimalPoint.length; i++) {
            assertEquals(expectedOptimalPoint[i], optimalPoint[i], 0.000001);
        }
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeWithNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        
        double[] startingPoint = {1.0, 2.0, 3.0};
        
        OptimizationData[] optData = new OptimizationData[]{
            new InitialGuess(startingPoint)
        };
        
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return 0; // Objective function is not used for this test.
            }
        };
        
        optimizer.optimize(1000, objectiveFunction, GoalType.MINIMIZE, optData);
    }
}
