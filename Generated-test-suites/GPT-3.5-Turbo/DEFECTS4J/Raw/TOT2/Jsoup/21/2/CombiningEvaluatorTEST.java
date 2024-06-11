import org.jsoup.nodes.Element;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node1");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And();
        andEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true; // For testing purposes, always return true
            }
        });
        
        assertEquals(true, andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node1");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return false; // For testing purposes, always return false
            }
        });
        
        orEvaluator.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true; // For testing purposes, this one should return true
            }
        });
        
        assertEquals(true, orEvaluator.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        Element root = new Element("root");
        Element node = new Element("node1");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or();
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return true; // Always true
            }
        });
        
        orEvaluator.add(new CombiningEvaluator.And(evaluators));
        
        assertEquals(true, orEvaluator.matches(root, node));
    }

}