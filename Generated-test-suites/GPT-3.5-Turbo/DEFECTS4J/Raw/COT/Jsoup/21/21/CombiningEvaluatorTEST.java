import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    
    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;
    
    @Before
    public void setUp() {
        andEvaluator = new CombiningEvaluator.And();
        orEvaluator = new CombiningEvaluator.Or();
    }
    
    @Test
    public void testAndMatches() {
        // Test when all evaluators in andEvaluator return true
        andEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        andEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndNotMatches() {
        // Test when one evaluator in andEvaluator returns false
        andEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });
        andEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testOrMatches() {
        // Test when one evaluator in orEvaluator returns true
        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        });

        assertTrue(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrNotMatches() {
        // Test when all evaluators in orEvaluator return false
        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });
        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        });

        assertFalse(orEvaluator.matches(null, null));
    }
}