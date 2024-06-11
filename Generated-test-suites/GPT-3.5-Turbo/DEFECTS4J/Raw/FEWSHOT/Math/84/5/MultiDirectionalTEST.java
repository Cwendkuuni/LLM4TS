import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    
    private MultiDirectional optimizer;
    
    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }
    
    @Test
    public void testIterationForReflection() throws FunctionEvaluationException, OptimizationException {
        MultiDirectionalReflectionConvergenceChecker checker = new MultiDirectionalReflectionConvergenceChecker();
        optimizer.setConvergenceChecker(checker);
        
        optimizer.iterateSimplex();
        
        assertEquals(checker.getIterationCount(), 1);
    }
    
    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = {new RealPointValuePair(new double[] {1.0, 2.0, 3.0}, 0.0), 
                                         new RealPointValuePair(new double[] {2.0, 3.0, 4.0}, 0.0),
                                         new RealPointValuePair(new double[] {3.0, 4.0, 5.0}, 0.0)};
        double coeff = 0.5;
        MultiDirectionalComparator comparator = new MultiDirectionalComparator();
        
        RealPointValuePair best = optimizer.evaluateNewSimplex(original, coeff, comparator);
        
        assertEquals(best.getPointRef(), new double[] {1.5, 2.5, 3.5});
    }
    
    private class MultiDirectionalReflectionConvergenceChecker extends RealConvergenceChecker {
        private int iterationCount;
        
        public MultiDirectionalReflectionConvergenceChecker() {
            iterationCount = 0;
        }
        
        @Override
        public boolean converged(int iteration, RealPointValuePair previous,
                RealPointValuePair current) {
            iterationCount++;
            return iteration >= 1;
        }
        
        public int getIterationCount() {
            return iterationCount;
        }
    }
    
    private class MultiDirectionalComparator implements Comparator<RealPointValuePair> {

        @Override
        public int compare(RealPointValuePair o1, RealPointValuePair o2) {
            return Double.compare(o1.getValue(), o2.getValue());
        }
        
    }
    
}