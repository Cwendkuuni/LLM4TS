import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.util.FastMath;


public class MultiStartUnivariateRealOptimizerTest {
    
    @Test
    public void testOptimize() throws Exception {
        BaseUnivariateRealOptimizer<FUNC> optimizerMock = new BaseUnivariateRealOptimizer<FUNC>() {
            
            private Func func;
            
            public void setConvergenceChecker() {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {return null;}
            public int getMaxEvaluations() {return 0;}
            public int getEvaluations() {return 0;}
            public void setMaxEvaluations(int maxEvaluations) {}
            public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max) {return null;}
        };
        
        try {
            MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, null);
            assertNull(multiStartOptimizer.getOptima());
            multiStartOptimizer.optimize(null, null, 0, 0);
            fail("No exception thrown");
        } catch (MathIllegalStateException ex) {
            assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET.getMessage(), ex.getMessage());
        }
        
        multiStartOptimizer.optimize(null, null, 0, 0, 0);
        assertNull(multiStartOptimizer.getOptima());
        
        try {
            BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>(){
                public int getMaxEvaluations() {
                    return 1;
                }

                public int getEvaluations() {
                    return 0;
                }

                public void setMaxEvaluations(int maxEvaluations) {}  

                public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max, double startValue) {
                    return null;
                }

                public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
					// TODO Auto-generated method stub
					
				}
                  
                public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
					// TODO Auto-generated method stub
					return null;
				}
				
            };
            
            MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizerMock = new MultiStartUnivariateRealOptimizer<>(optimizer, -1, null);
            UnivariateRealPointValuePair[] optima = multiStartOptimizerMock.getOptima();
            assertEquals(-1, multiStartOptimizerMock.getStarts());
            assertNull(multiStartOptimizerMock.getGenerator());
            assertNotNull(multiStartOptimizerMock.getOptima());
            assertEquals(0, multiStartOptimizerMock.getEvaluations());
            
            multiStartOptimizerMock.setConvergenceChecker(null);
            assertNull(multiStartOptimizerMock.getConvergenceChecker());
            
            multiStartOptimizerMock.setConvergenceChecker(null);
            assertNull(multiStartOptimizerMock.getConvergenceChecker());

            multiStartOptimizerMock.setMaxEvaluations(1);
            assertEquals(1, multiStartOptimizerMock.getMaxEvaluations());
            
            multiStartOptimizerMock.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
            
            try {
                multiStartOptimizerMock.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
                fail("No exception thrown");
            } catch (MathIllegalStateException ex) {
                assertEquals("Convergence with any start point is not achieved", ex.getMessage());
            }
            
            multiStartOptimizerMock.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
        } catch (Exception ex) {
            fail("Exception occurred");
        }
        
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>(){
            public int getMaxEvaluations() {
                return 1;
            }

            public int getEvaluations() {
                return 0;
            }

            public void setMaxEvaluations(int maxEvaluations) {}  

            public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max, double startValue) {
                return null;
            }

            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
				// TODO Auto-generated method stub
				
			}
              
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
				// TODO Auto-generated method stub
				return null;
			}
			
        };
        
        try {
            RandomGenerator generator = new RandomGenerator();
            MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizerMock = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
            UnivariateRealPointValuePair[] optima = multiStartOptimizerMock.getOptima();
            assertEquals(3, multiStartOptimizerMock.getStarts());
            assertNotNull(multiStartOptimizerMock.getGenerator());
            assertNotNull(multiStartOptimizerMock.getOptima());
            assertEquals(0, multiStartOptimizerMock.getEvaluations());
            
            multiStartOptimizerMock.setConvergenceChecker(null);
            assertNull(multiStartOptimizerMock.getConvergenceChecker());
            
            multiStartOptimizerMock.setConvergenceChecker(null);
            assertNull(multiStartOptimizerMock.getConvergenceChecker());

            multiStartOptimizerMock.setMaxEvaluations(1);
            assertEquals(1, multiStartOptimizerMock.getMaxEvaluations());
            
            multiStartOptimizerMock.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
            
            try {
                multiStartOptimizerMock.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
                fail("No exception thrown");
            } catch (MathIllegalStateException ex) {
                assertEquals("Convergence with any start point is not achieved", ex.getMessage());
            }
            
            multiStartOptimizerMock.optimize(null, GoalType.MINIMIZE, 0, 0, 0);
        } catch (Exception ex) {
            fail("Exception occurred");
        }
        
        
    }
    
}