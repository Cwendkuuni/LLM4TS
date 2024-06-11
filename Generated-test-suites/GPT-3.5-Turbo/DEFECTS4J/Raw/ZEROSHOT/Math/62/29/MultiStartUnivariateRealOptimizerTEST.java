import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testSetConvergenceChecker() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException {
                return null;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        multiStartOptimizer.setConvergenceChecker(null);
    }

    @Test
    public void testGetConvergenceChecker() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException {
                return null;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        assertNull(multiStartOptimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException {
                return null;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        assertEquals(0, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException {
                return null;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException {
                return null;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        multiStartOptimizer.setMaxEvaluations(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testOptimize() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException {
                return null;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0, 1);
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOptimize2() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new BaseUnivariateRealOptimizer<UnivariateRealFunction>() {
            public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
            public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
                return null;
            }
            public int getMaxEvaluations() {
                return 0;
            }
            public int getEvaluations() {
                return 0;
            }
            public void setMaxEvaluations(int maxEvaluations) {}
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) throws FunctionEvaluationException {
                return null;
            }
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) throws FunctionEvaluationException {
                return null;
            }
        };
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0, 1, 0.5);
        } catch (FunctionEvaluationException e) {
            e.printStackTrace();
        }
    }
}