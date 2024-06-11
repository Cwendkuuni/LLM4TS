package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {
    
    private FUNC f;
    private GoalType goal;
    private double min;
    private double max;
    private double startValue;
    private int starts;
    private RandomGenerator generator;
    
    private BaseUnivariateRealOptimizer<FUNC> optimizer;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    
    @Before
    public void setup() {
        f = null;
        min = 0.0;
        max = 1.0;
        startValue = 0.5;
        starts = 5;
        generator = null;
        
        optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                
            }

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                
            }

            @Override
            public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }

            @Override
            public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max, double startValue)
                    throws FunctionEvaluationException {
                return null;
            }

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public int getMaxEvaluations() {
                return 0;
            }
            
        };
        
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, starts, generator);
    }

    @Test
    public void testSetMaxEvaluations() {
        multiStartOptimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max);
        assertNull(result);
    }
    
    @Test
    public void testOptimizeWithStartValue() throws FunctionEvaluationException {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max, startValue);
        assertNull(result);
    }
    
    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithoutOptimization() {
        multiStartOptimizer.getOptima();
    }
            
    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithNoConvergence() throws FunctionEvaluationException {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(f, goal, min, max, startValue);
        assertNull(result);
    }
}