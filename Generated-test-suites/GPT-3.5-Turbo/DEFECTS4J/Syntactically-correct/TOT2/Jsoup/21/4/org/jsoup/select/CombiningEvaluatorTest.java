import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.And and = new CombiningEvaluator.And(new Evaluator.Tag("tag"), new Evaluator.Id("id"));
        assertTrue(and.matches(root, node));

        CombiningEvaluator.And andFalse = new CombiningEvaluator.And(new Evaluator.Class("class"));
        assertFalse(andFalse.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(new Evaluator.Tag("tag"));
        assertTrue(or.matches(root, node));

        CombiningEvaluator.Or orFalse = new CombiningEvaluator.Or(new Evaluator.Class("class"), new Evaluator.Attribute("attr"));
        assertFalse(orFalse.matches(root, node));
    }

    @Test
    public void testOrAdd() {
        Element root = new Element("root");
        Element node = new Element("node");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(new Evaluator.Tag("tag"));
        or.add(new Evaluator.Id("id"));

        assertTrue(or.matches(root, node));

        or.add(new Evaluator.Attribute("attr"));
        assertTrue(or.matches(root, node));
    }

}