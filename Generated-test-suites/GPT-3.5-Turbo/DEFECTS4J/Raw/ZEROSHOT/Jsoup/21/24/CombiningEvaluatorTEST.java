import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(Arrays.asList(
                new Evaluator.Tag("div"),
                new Evaluator.Attribute("class", "box")
        ));

        Element root = new Element("body");
        Element node = new Element("div");
        node.addClass("box");

        boolean result = evaluator.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testAndNotMatches() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(Arrays.asList(
                new Evaluator.Tag("div"),
                new Evaluator.Attribute("class", "box")
        ));

        Element root = new Element("body");
        Element node = new Element("div");
        node.addClass("container");

        boolean result = evaluator.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testAndToString() {
        CombiningEvaluator.And evaluator = new CombiningEvaluator.And(Arrays.asList(
                new Evaluator.Tag("div"),
                new Evaluator.Attribute("class", "box")
        ));

        String expected = "Tag div Attribute[class=box]";

        String result = evaluator.toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOrMatches() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(
                new Evaluator.Tag("div"),
                new Evaluator.Tag("span")
        ));

        Element root = new Element("body");
        Element node = new Element("div");

        boolean result = evaluator.matches(root, node);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrNotMatches() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(
                new Evaluator.Tag("div"),
                new Evaluator.Tag("span")
        ));

        Element root = new Element("body");
        Element node = new Element("p");

        boolean result = evaluator.matches(root, node);

        Assert.assertFalse(result);
    }

    @Test
    public void testOrAdd() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(
                new Evaluator.Tag("div"),
                new Evaluator.Tag("span")
        ));

        Evaluator e = new Evaluator.Tag("p");
        evaluator.add(e);

        boolean result = evaluator.evaluators.contains(e);

        Assert.assertTrue(result);
    }

    @Test
    public void testOrToString() {
        CombiningEvaluator.Or evaluator = new CombiningEvaluator.Or(Arrays.asList(
                new Evaluator.Tag("div"),
                new Evaluator.Tag("span")
        ));

        String expected = ":or[Tag div, Tag span]";

        String result = evaluator.toString();

        Assert.assertEquals(expected, result);
    }
}