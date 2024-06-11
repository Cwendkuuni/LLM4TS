
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches_AllEvaluatorsMatch_ReturnsTrue() {
        List<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                }
        );

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        boolean result = and.matches(null, null);

        assertTrue(result);
    }

    @Test
    public void testAndMatches_OneEvaluatorDoesNotMatch_ReturnsFalse() {
        List<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        );

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        boolean result = and.matches(null, null);

        assertFalse(result);
    }

    @Test
    public void testOrMatches_OneEvaluatorMatches_ReturnsTrue() {
        List<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return true;
                    }
                }
        );

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        boolean result = or.matches(null, null);

        assertTrue(result);
    }

    @Test
    public void testOrMatches_NoneOfTheEvaluatorsMatch_ReturnsFalse() {
        List<Evaluator> evaluators = Arrays.asList(
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                },
                new Evaluator() {
                    @Override
                    public boolean matches(Element root, Element node) {
                        return false;
                    }
                }
        );

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        boolean result = or.matches(null, null);

        assertFalse(result);
    }

    @Test
    public void testOrAdd_AddNewEvaluator_EvaluatorIsAddedToList() {
        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(evaluator));
        or.add(evaluator);

        assertEquals(2, or.evaluators.size());
    }

}
