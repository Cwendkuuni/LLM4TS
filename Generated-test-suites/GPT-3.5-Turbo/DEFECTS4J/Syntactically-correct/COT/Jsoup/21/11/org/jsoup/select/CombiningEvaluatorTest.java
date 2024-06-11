
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.Selector;
import org.jsoup.select.Selector.SelectorParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches_AllEvaluatorsMatch_ReturnsTrue() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.IndexEquals(2);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        Element root = new Element("html");
        root.appendElement("body").appendElement("div");
        root.appendElement("body").appendElement("div");
        root.appendElement("body").appendElement("div");

        assertTrue(and.matches(root, root.select("div").get(2)));
    }

    @Test
    public void testAndMatches_EvaluatorDoesNotMatch_ReturnsFalse() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.IndexEquals(2);
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        Element root = new Element("html");
        root.appendElement("body").appendElement("div");

        assertFalse(and.matches(root, root));
    }

    @Test
    public void testAndToString_ReturnsValidStringRepresentation() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("p");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.And and = new CombiningEvaluator.And(evaluators);

        assertEquals(":div :p", and.toString());
    }

    @Test
    public void testOrMatches_AtLeastOneEvaluatorMatches_ReturnsTrue() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("p");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        Element root = new Element("html");
        root.appendElement("body").appendElement("div");

        assertTrue(or.matches(root, root.select("div").first()));
    }

    @Test
    public void testOrMatches_NoEvaluatorMatches_ReturnsFalse() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("p");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        Element root = new Element("html");
        root.appendElement("body").appendElement("span");

        assertFalse(or.matches(root, root));
    }

    @Test
    public void testOrToString_ReturnsValidStringRepresentation() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("p");
        List<Evaluator> evaluators = new ArrayList<>();
        evaluators.add(evaluator1);
        evaluators.add(evaluator2);

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(evaluators);

        assertEquals(":or[:div, :p]", or.toString());
    }

    @Test
    public void testOrAdd_AddsEvaluator() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("p");

        CombiningEvaluator.Or or = new CombiningEvaluator.Or(Arrays.asList(evaluator1));
        or.add(evaluator2);

        Element root = new Element("html");
        root.appendElement("body").appendElement("p");

        assertTrue(or.matches(root, root.select("p").first()));
    }
}
