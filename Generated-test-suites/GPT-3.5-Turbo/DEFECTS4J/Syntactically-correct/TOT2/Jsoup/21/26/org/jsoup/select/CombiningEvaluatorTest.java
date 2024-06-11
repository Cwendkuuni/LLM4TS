import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element();
        Element node = new Element();

        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        List<Evaluator> evaluators = new ArrayList<>(Arrays.asList(evaluator1, evaluator2));
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element();
        Element node = new Element();

        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };
        
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        List<Evaluator> evaluators = new ArrayList<>(Arrays.asList(evaluator1, evaluator2));
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        Element root = new Element();
        Element node = new Element();

        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());
        orEvaluator.add(evaluator);

        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrSingleEvaluatorMatches() {
        Element root = new Element();
        Element node = new Element();

        Evaluator evaluator = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false;
            }
        };

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator));

        assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrNoEvaluatorMatches() {
        Element root = new Element();
        Element node = new Element();

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());

        assertFalse(orEvaluator.matches(root, node));
    }

}