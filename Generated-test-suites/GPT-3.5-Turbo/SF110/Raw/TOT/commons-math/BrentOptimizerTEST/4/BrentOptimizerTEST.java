package org.apache.commons.math3.optim.univariate;

import java.util.function.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BrentOptimizerTest {
    
    private static final double EPSILON = 1e-7;
    
    private double relThreshold;
    private double absThreshold;
    private double min;
    private double max;
    private GoalType goalType;
    private double expectedSolution;
    
    public BrentOptimizerTest(double relThreshold, double absThreshold, double min, double max, GoalType goalType, double expectedSolution) {
        this.relThreshold = relThreshold;
        this.absThreshold = absThreshold;
        this.min = min;
        this.max = max;
        this.goalType = goalType;
        this.expectedSolution = expectedSolution;
    }
    
    @Parameterized.Parameters
    public static Collection<Object[]> testParameters() {
        return Arrays.asList(new Object[][] {
            { 1e-4, 1e-6, 0.0, 10.0, GoalType.MINIMIZE, 0.0 }, // typical use case
            { 1e-4, 1e-6, -10.0, 0.0, GoalType.MAXIMIZE, -10.0 }, // typical use case
            { 1e-4, 1e-6, -100.0, 100.0, GoalType.MINIMIZE, 0.0 }, // edge case
            { 1e-4, 1e-6, 0.0, 0.0, GoalType.MAXIMIZE, 0.0 }, // edge case
            { 1e-4, 1e-6, 0.0, 10.0, GoalType.MINIMIZE, Double.POSITIVE_INFINITY }, // error scenario
            { 1e-4, 1e-6, -10.0, 0.0, GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY } // error scenario
        });
    }
    
    @Test
    public void testBrentOptimizer() {
        UnivariateOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold);
        
        UnivariatePointValuePair result = optimizer.optimize(new UnivariateObjectiveFunction(x -> x * x), goalType,
                                                             new SearchInterval(min, max));
        
        assertEquals(expectedSolution, result.getPoint(), EPSILON);
    }
}