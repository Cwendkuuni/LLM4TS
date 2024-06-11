//import statements
import static org.junit.Assert.*;
import org.junit.*;
import java.util.Arrays;

public class MultiStartUnivariateRealOptimizerTest {

    //Test case for setConvergenceChecker method
    @Test
    public void testSetConvergenceChecker() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStart = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, new RandomGenerator());

        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<UnivariateRealPointValuePair>();
        multiStart.setConvergenceChecker(checker);

        assertSame(checker, multiStart.getConvergenceChecker());
    }

    //Test case for getMaxEvaluations and setMaxEvaluations methods
    @Test
    public void testGetAndSetMaxEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStart = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 100;

        multiStart.setMaxEvaluations(maxEvaluations);

        assertEquals(maxEvaluations, multiStart.getMaxEvaluations());
    }

    //Test case for getEvaluations method
    @Test
    public void testGetEvaluations() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStart = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 100;
        multiStart.setMaxEvaluations(maxEvaluations);
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        GoalType goal = GoalType.MINIMIZE;

        UnivariateRealPointValuePair result = multiStart.optimize(f, goal, min, max, startValue);

        assertEquals(multiStart.getEvaluations(), optimizer.getEvaluations());
        assertEquals(result.getEvaluations(), optimizer.getEvaluations());
    }

    //Test case for optimize method
    @Test
    public void testOptimize() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStart = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 100;
        multiStart.setMaxEvaluations(maxEvaluations);
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        GoalType goal = GoalType.MINIMIZE;

        UnivariateRealPointValuePair result = multiStart.optimize(f, goal, min, max, startValue);

        assertEquals(result.getValue(), optimizer.optimize(f, goal, min, max).getValue(), 0.0);

        UnivariateRealPointValuePair[] optima = multiStart.getOptima();

        assertNotNull(optima[0]);
        assertEquals(result.getValue(), optima[0].getValue(), 0.0);
        assertNotNull(optima[1]);
        assertNotNull(optima[2]);
        assertNotNull(optima[3]);
        assertNotNull(optima[4]);

        assertNull(optima[5]);
        assertNull(optima[6]);
        assertNull(optima[7]);
        assertNull(optima[8]);
    }

    //Test case for getOptima method
    @Test
    public void testGetOptima() {
        BaseUnivariateRealOptimizer<FUNC> optimizer = new BaseUnivariateRealOptimizer<FUNC>();
        MultiStartUnivariateRealOptimizer<FUNC> multiStart = new MultiStartUnivariateRealOptimizer<FUNC>(optimizer, 5, new RandomGenerator());
        int maxEvaluations = 100;
        multiStart.setMaxEvaluations(maxEvaluations);
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        GoalType goal = GoalType.MINIMIZE;

        UnivariateRealPointValuePair result = multiStart.optimize(f, goal, min, max, startValue);
        UnivariateRealPointValuePair[] optima = multiStart.getOptima();

        Arrays.sort(optima, new Comparator<UnivariateRealPointValuePair>() {
            public int compare(final UnivariateRealPointValuePair o1,
                               final UnivariateRealPointValuePair o2) {
                if (o1 == null) {
                    return (o2 == null) ? 0 : 1;
                } else if (o2 == null) {
                    return -1;
                }
                final double v1 = o1.getValue();
                final double v2 = o2.getValue();
                return (goal == GoalType.MINIMIZE) ?
                    Double.compare(v1, v2) : Double.compare(v2, v1);
            }
        });

        assertSame(optima[0], multiStart.optimize(f, goal, min, max));
        assertSame(optima[1], multiStart.optimize(f, goal, min, max));
        assertSame(optima[2], multiStart.optimize(f, goal, min, max));
        assertSame(optima[3], multiStart.optimize(f, goal, min, max));
        assertSame(optima[4], multiStart.optimize(f, goal, min, max));

        assertNull(optima[5]);
        assertNull(optima[6]);
        assertNull(optima[7]);
        assertNull(optima[8]);
    }


}