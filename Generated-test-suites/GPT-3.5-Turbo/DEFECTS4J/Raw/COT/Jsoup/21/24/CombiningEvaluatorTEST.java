
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndConstructor() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new DummyEvaluator());
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        assertEquals(1, and.evaluators.size());
    }

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        DummyEvaluator trueEvaluator = new DummyEvaluator(true);
        DummyEvaluator falseEvaluator = new DummyEvaluator(false);
        
        Collection<Evaluator> evaluatorsAndTrue = new ArrayList<>();
        evaluatorsAndTrue.add(trueEvaluator);
        evaluatorsAndTrue.add(trueEvaluator);
        CombiningEvaluator.And andTrue = new CombiningEvaluator.And(evaluatorsAndTrue);
        assertTrue(andTrue.matches(root, node));
        
        Collection<Evaluator> evaluatorsAndFalse = new ArrayList<>();
        evaluatorsAndFalse.add(trueEvaluator);
        evaluatorsAndFalse.add(falseEvaluator);
        CombiningEvaluator.And andFalse = new CombiningEvaluator.And(evaluatorsAndFalse);
        assertFalse(andFalse.matches(root, node));
    }

    @Test
    public void testAndToString() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new DummyEvaluator());
        evaluators.add(new DummyEvaluator());
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        assertEquals(":dummy :dummy", and.toString());
    }

    @Test
    public void testOrConstructor() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new DummyEvaluator());
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        assertEquals(1, or.evaluators.size());
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        DummyEvaluator trueEvaluator = new DummyEvaluator(true);
        DummyEvaluator falseEvaluator = new DummyEvaluator(false);

        Collection<Evaluator> evaluatorsOrTrue = new ArrayList<>();
        evaluatorsOrTrue.add(trueEvaluator);
        evaluatorsOrTrue.add(falseEvaluator);
        CombiningEvaluator.Or orTrue = new CombiningEvaluator.Or(evaluatorsOrTrue);
        assertTrue(orTrue.matches(root, node));

        Collection<Evaluator> evaluatorsOrFalse = new ArrayList<>();
        evaluatorsOrFalse.add(falseEvaluator);
        evaluatorsOrFalse.add(falseEvaluator);
        CombiningEvaluator.Or orFalse = new CombiningEvaluator.Or(evaluatorsOrFalse);
        assertFalse(orFalse.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        EvaluatorsEvaluator e = new EvaluatorsEvaluator(evaluators);
        DummyEvaluator dummy = new DummyEvaluator();

        e.add(dummy);
        assertEquals(1, evaluators.size());
    }
    
    class EvaluatorsEvaluator extends CombiningEvaluator.Or {
        EvaluatorsEvaluator(Collection<Evaluator> evaluators) {
            super(evaluators);
        }
    }

    @Test
    public void testOrToString() {
        Collection<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new DummyEvaluator());
        evaluators.add(new DummyEvaluator());
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        assertEquals(":or[dummy, dummy]", or.toString());
    }

    class DummyEvaluator extends Evaluator {
        private final boolean returnValue;

        DummyEvaluator() {
            returnValue = true;
        }

        DummyEvaluator(boolean returnValue) {
            this.returnValue = returnValue;
        }

        @Override
        public boolean matches(Element root, Element node) {
            return returnValue;
        }

        @Override
        public String toString() {
            return ":dummy";
        }
    }
}
