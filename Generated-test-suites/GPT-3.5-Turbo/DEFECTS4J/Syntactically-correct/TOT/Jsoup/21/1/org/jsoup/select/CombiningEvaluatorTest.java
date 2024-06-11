import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches_MultipleEvaluators_Match() {
        Element root = new Element("div");
        Element node = new Element("div");
        Element childNode = new Element("span");
        node.appendChild(childNode);
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new Evaluator.HasText("Hello"),
                new Evaluator.Attribute("class", "red")
        );
        
        Assert.assertTrue(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndMatches_MultipleEvaluators_DoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("div");
        Element childNode = new Element("span");
        node.appendChild(childNode);
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new Evaluator.HasText("Hello"),
                new Evaluator.Attribute("class", "blue")
        );
        
        Assert.assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testAndMatches_EmptyEvaluators_DoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("div");
        
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(Collections.emptyList());
        
        Assert.assertFalse(andEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches_MultipleEvaluators_Match() {
        Element root = new Element("div");
        Element node = new Element("div");
        Element childNode = new Element("span");
        node.appendChild(childNode);
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("div"),
                new Evaluator.HasText("Hello"),
                new Evaluator.Attribute("class", "red")
        );
        
        Assert.assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches_MultipleEvaluators_DoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("div");
        Element childNode = new Element("span");
        node.appendChild(childNode);
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("span"),
                new Evaluator.HasText("Goodbye"),
                new Evaluator.Attribute("class", "red")
        );
        
        Assert.assertFalse(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches_EmptyEvaluators_DoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("div");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Collections.emptyList());
        
        Assert.assertFalse(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches_SingleEvaluator_Match() {
        Element root = new Element("div");
        Element node = new Element("div");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("div")
        );
        
        Assert.assertTrue(orEvaluator.matches(root, node));
    }
    
    @Test
    public void testOrMatches_SingleEvaluator_DoesNotMatch() {
        Element root = new Element("div");
        Element node = new Element("div");
        
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("span")
        );
        
        Assert.assertFalse(orEvaluator.matches(root, node));
    }

}