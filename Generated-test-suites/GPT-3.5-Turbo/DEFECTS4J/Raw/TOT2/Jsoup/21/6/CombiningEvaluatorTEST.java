import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class CombiningEvaluatorTest {
    
    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("div"),
                new Evaluator.Class("class1")
        );

        boolean result = evaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new Evaluator.Tag("div"),
                        new Evaluator.Id("id1")
                )
        );

        boolean result = evaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void testOrAdd() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("div")
        );
        
        evaluator.add(new Evaluator.Class("class1"));

        boolean result = evaluator.matches(root, node);

        assertTrue(result);
    }

    @Test
    public void testOrNoMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("div"),
                new Evaluator.Id("id1")
        );

        boolean result = evaluator.matches(root, node);

        assertFalse(result);
    }
}