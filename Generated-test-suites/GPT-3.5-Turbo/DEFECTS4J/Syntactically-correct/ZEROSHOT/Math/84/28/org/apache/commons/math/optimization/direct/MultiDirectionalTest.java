import static org.junit.Assert.*;

import java.util.Comparator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    
    private MultiDirectional multiDirectional;
    
    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }
    
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };
        
        multiDirectional.iterateSimplex(comparator);
        
        //TODO: Add assertions for the expected results
        
    }
    
    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[2];
        double coeff = 0.5;
        
        RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, coeff, null);
        
        //TODO: Add assertions for the expected results
        
    }
}