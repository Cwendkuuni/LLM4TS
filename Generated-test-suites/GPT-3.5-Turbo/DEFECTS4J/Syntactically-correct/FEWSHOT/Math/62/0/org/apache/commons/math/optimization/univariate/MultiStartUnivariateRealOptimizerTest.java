import org.apache.commons.math.optimization.univariate.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.*;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.util.FastMath;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = null;
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            @Override
            public UnivariateRealPointValuePair optimize(FUNC function, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
                return null;
            }

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                
            }

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                
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
        int starts = 0;
        RandomGenerator generator = null;
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        multiStartOptimizer.setConvergenceChecker(checker);
        assertEquals(checker, multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            @Override
            public UnivariateRealPointValuePair optimize(FUNC function, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
                return null;
            }

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                
            }

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public int getMaxEvaluations() {
                return 10;
            }
        };
        int starts = 0;
        RandomGenerator generator = null;
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        assertEquals(10, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            @Override
            public UnivariateRealPointValuePair optimize(FUNC function, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
                return null;
            }

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                
            }

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                
            }

            @Override
            public int getEvaluations() {
                return 5;
            }

            @Override
            public int getMaxEvaluations() {
                return 0;
            }
        };
        int starts = 0;
        RandomGenerator generator = null;
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        assertEquals(5, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            @Override
            public UnivariateRealPointValuePair optimize(FUNC function, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
                return null;
            }

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                
            }

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                
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
        int starts = 0;
        RandomGenerator generator = null;
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        multiStartOptimizer.setMaxEvaluations(10);
        assertEquals(10, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testOptimize() throws ConvergenceException, FunctionEvaluationException {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            @Override
            public UnivariateRealPointValuePair optimize(FUNC function, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
                assertEquals("Function", null, function);
                assertEquals("GoalType", GoalType.MINIMIZE, goal);
                assertEquals("Min", 1.0, min, 0.0);
                assertEquals("Max", 10.0, max, 0.0);
                return null;
            }

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                
            }

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                
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
        int starts = 5;
        RandomGenerator generator = null;
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        multiStartOptimizer.optimize(null, GoalType.MINIMIZE, 1.0, 10.0);
    }

    @Test
    public void testGetOptima() throws ConvergenceException, FunctionEvaluationException {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            @Override
            public UnivariateRealPointValuePair optimize(FUNC function, GoalType goal, double min, double max) throws FunctionEvaluationException, ConvergenceException {
                assertEquals("Function", null, function);
                assertEquals("GoalType", GoalType.MINIMIZE, goal);
                assertEquals("Min", 1.0, min, 0.0);
                assertEquals("Max", 10.0, max, 0.0);
                return null;
            }

            @Override
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
                
            }

            @Override
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }

            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                
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
        int starts = 5;
        RandomGenerator generator = null;
        MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        try {
            multiStartOptimizer.getOptima();
            fail("getOptima should throw MathIllegalStateException when called before optimize");
        } catch (MathIllegalStateException e) {
            assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, e.getSpecifier());
        }
    }
}