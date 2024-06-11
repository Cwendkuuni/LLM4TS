
package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {
    
    private BaseOptimizer<Integer> optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BaseOptimizer<Integer>() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return null;
            }
        };
    }
    
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetConvergenceChecker() {
        assertEquals(null, optimizer.getConvergenceChecker());
    }
}
