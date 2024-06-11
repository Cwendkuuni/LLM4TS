import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.select.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Evaluator e1 = new Evaluator.Tag("div");
        Evaluator e2 = new Evaluator.Class("container");
        Evaluator andEvaluator = new CombiningEvaluator.And(e1, e2);
        
        Element root = new Element("html");
        Element node = new Element("div");
        node.addClass("container");

        assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        Evaluator e1 = new Evaluator.Tag("span");
        Evaluator e2 = new Evaluator.Id("uniqueId");
        Evaluator andEvaluator = new CombiningEvaluator.And(e1, e2);
        
        assertEquals("span #uniqueId", andEvaluator.toString());
    }

    @Test
    public void testOrMatches() {
        Evaluator e1 = new Evaluator.Tag("p");
        Evaluator e2 = new Evaluator.AttributeStarting("data");
        Evaluator orEvaluator = new CombiningEvaluator.Or(e1, e2);
        
        Element root = new Element("html");
        Element node1 = new Element("p");
        Element node2 = new Element("span");
        node2.attr("data-attribute", "value");

        assertTrue(orEvaluator.matches(root, node1));
        assertTrue(orEvaluator.matches(root, node2));
    }

    @Test
    public void testOrToString() {
        Evaluator e1 = new Evaluator.Tag("a");
        Evaluator e2 = new Evaluator.Attribute("href");
        Evaluator orEvaluator = new CombiningEvaluator.Or(e1, e2);
        
        assertEquals(":or[tag a] [href]", orEvaluator.toString());
    }

    @Test
    public void testOrAdd() {
        Evaluator e1 = new Evaluator.Class("container");
        Evaluator e2 = new Evaluator.Attribute("data");
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(e1);
        orEvaluator.add(e2);
        
        Element root = new Element("html");
        Element node = new Element("div");
        node.addClass("container");
        node.attr("data", "value");

        assertTrue(orEvaluator.matches(root, node));
    }

}