
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = Arrays.asList(new Evaluator.Tag("root"), new Evaluator.Tag("node"));
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        
        assertTrue(and.matches(root, node));
    }
    
    @Test
    public void testAndNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = Arrays.asList(new Evaluator.Tag("root"), new Evaluator.Tag("other"));
        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);
        
        assertFalse(and.matches(root, node));
    }
    
    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = Arrays.asList(new Evaluator.Tag("root"), new Evaluator.Tag("node"));
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        
        assertTrue(or.matches(root, node));
    }
    
    @Test
    public void testOrNotMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        List<Evaluator> evaluators = Arrays.asList(new Evaluator.Tag("other"), new Evaluator.Tag("element"));
        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);
        
        assertFalse(or.matches(root, node));
    }
}
