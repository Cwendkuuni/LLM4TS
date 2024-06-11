import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {
    @Test
    public void testAndMatches() {
        Element root = createElement("root");
        Element node1 = createElement("node1");
        Element node2 = createElement("node2");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node1");
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node2");
            }
        };

        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        
        assertTrue(andEvaluator.matches(root, node1));
        assertTrue(andEvaluator.matches(root, node2));
        assertFalse(andEvaluator.matches(root, root));
    }
    
    @Test
    public void testAndToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(createTagEvaluator("node1"));
        evaluators.add(createTagEvaluator("node2"));
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);
        
        assertEquals(":tag(node1) :tag(node2)", andEvaluator.toString());
    }
    
    @Test
    public void testOrMatches() {
        Element root = createElement("root");
        Element node1 = createElement("node1");
        Element node2 = createElement("node2");
        Element node3 = createElement("node3");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node1");
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node2");
            }
        };
        Evaluator evaluator3 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node3");
            }
        };
        
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);
        evaluators.add(evaluator3);
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        
        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
        assertTrue(orEvaluator.matches(root, node3));
        assertFalse(orEvaluator.matches(root, root));
    }
    
    @Test
    public void testOrAdd() {
        Element root = createElement("root");
        Element node1 = createElement("node1");
        Element node2 = createElement("node2");
        
        Evaluator evaluator1 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node1");
            }
        };
        Evaluator evaluator2 = new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals("node2");
            }
        };
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(new ArrayList<>());
        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);
        
        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
        assertFalse(orEvaluator.matches(root, root));
    }
    
    @Test
    public void testOrToString() {
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(createTagEvaluator("node1"));
        evaluators.add(createTagEvaluator("node2"));
        evaluators.add(createTagEvaluator("node3"));
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);
        
        assertEquals(":or[:tag(node1) :tag(node2) :tag(node3)]", orEvaluator.toString());
    }

    private Element createElement(String tagName) {
        return new Element(tagName);
    }
    
    private Evaluator createTagEvaluator(String tagName) {
        return new Evaluator() {
            @Override
            public boolean matches(Element root, Element node) {
                return node.tagName().equals(tagName);
            }
        };
    }
}