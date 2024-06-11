
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator.Tag("tag1");
        Evaluator evaluator2 = new Evaluator.Class("class1");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        
        assertTrue(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator.Tag("tag1");
        Evaluator evaluator2 = new Evaluator.Tag("tag2");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));
        
        assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator.Tag("tag1");
        Evaluator evaluator2 = new Evaluator.Tag("tag2");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        
        assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator.Tag("tag1");
        Evaluator evaluator2 = new Evaluator.Tag("tag2");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator2));
        
        assertFalse(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrWithAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator.Tag("tag1");
        Evaluator evaluator2 = new Evaluator.Class("class1");
        Evaluator evaluator3 = new Evaluator.Id("id1");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        orEvaluator.add(evaluator3);
        
        assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrWithAndDoesNotMatch() {
        Element root = new Element("root");
        Element node = new Element("node");
        
        Evaluator evaluator1 = new Evaluator.Tag("tag1");
        Evaluator evaluator2 = new Evaluator.Class("class1");
        Evaluator evaluator3 = new Evaluator.Id("id1");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));
        orEvaluator.add(evaluator3);
        
        assertFalse(orEvaluator.matches(root, node));
    }
}
