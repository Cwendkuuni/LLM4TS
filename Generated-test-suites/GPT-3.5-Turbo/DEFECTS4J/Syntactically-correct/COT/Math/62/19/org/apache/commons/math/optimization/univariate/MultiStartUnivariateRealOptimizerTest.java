import static org.junit.Assert.*;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOptimizer() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(null, 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeStarts() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, -1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullGenerator() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetConvergenceCheckerWithNullChecker() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        optimizer.setConvergenceChecker(null);
    }

    @Test
    public void testGetConvergenceChecker() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testSetMaxEvaluationsWithNegativeMaxEvaluations() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        optimizer.setMaxEvaluations(-1);
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetOptimaWithoutCallingOptimize() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        try {
            optimizer.getOptima();
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            assertEquals("No optimum computed yet", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullObjectiveFunction() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullGoalType() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, null, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithMinGreaterThanMax() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, GoalType.MINIMIZE, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithStartValueOutsideRange() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {}, 1, null);
        optimizer.optimize(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0;
            }
        }, GoalType.MINIMIZE, 0, 1, 2);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        MultiStartUnivariateRealOptimizer<Object> optimizer = new MultiStartUnivariateRealOptimizer<>(new BaseUnivariateRealOptimizer<Object>() {
            @Override
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
                return null;
            }

            @Override
            public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) {
                return null;
            }
        }, 1, null);
        optimizer.setConvergenceChecker(new ConvergenceChecker<UnivariateRealPointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
                return true;
            }
        });
        // Add test case with custom convergence checker
    }
}