import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.optimization.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @BeforeEach
    public void setup() {
        optimizer = new BrentOptimizer(0.01, 0.001);
    }
    
    @Test
    public void testDoOptimize_WithMinimizeGoal_MinimumValue() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        
        double min = -10.0;
        double max = 10.0;
        
        optimizer.setMaxEvaluations(100);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setFunction(function);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.optimize();
        
        assertThat(result.getPoint(), closeTo(0.0, 0.001));
        assertThat(result.getValue(), closeTo(0.0, 0.001));
    }
    
    @Test
    public void testDoOptimize_WithMaximizeGoal_MaximumValue() {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return -x * x;
            }
        };
        
        double min = -10.0;
        double max = 10.0;
        
        optimizer.setMaxEvaluations(100);
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setFunction(function);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        UnivariatePointValuePair result = optimizer.optimize();
        
        assertThat(result.getPoint(), closeTo(0.0, 0.001));
        assertThat(result.getValue(), closeTo(0.0, 0.001));
    }
    
    // Add more test cases for other methods in the BrentOptimizer class
    
}