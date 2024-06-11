import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Evaluator;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

    private Element rootElement;

    @Before
    public void setUp() {
        String html = "<div id=\"parent\">" +
                "<div class=\"child\"></div>" +
                "<div class=\"child\"></div>" +
                "<div class=\"child\"></div>" +
                "</div>";
        Document document = Jsoup.parse(html);
        rootElement = document.getElementById("parent");
    }

    @Test
    public void testAndMatches_AllEvaluatorsMatch() {
        Evaluator evaluator1 = new Evaluator.Class("child");
        Evaluator evaluator2 = new Evaluator.IsRootElement();
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        boolean result = andEvaluator.matches(rootElement, rootElement);

        assertTrue(result);
    }

    @Test
    public void testAndMatches_OneEvaluatorDoesNotMatch() {
        Evaluator evaluator1 = new Evaluator.Class("child");
        Evaluator evaluator2 = new Evaluator.Tag("span");
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        boolean result = andEvaluator.matches(rootElement, rootElement);

        assertFalse(result);
    }

    @Test
    public void testAndToString() {
        Evaluator evaluator1 = new Evaluator.Class("child");
        Evaluator evaluator2 = new Evaluator.IsRootElement();
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(evaluators);

        String result = andEvaluator.toString();

        assertEquals(".child :root", result);
    }

    @Test
    public void testOrMatches_OneEvaluatorMatches() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("span");
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        boolean result = orEvaluator.matches(rootElement, rootElement);

        assertTrue(result);
    }

    @Test
    public void testOrMatches_NoEvaluatorMatches() {
        Evaluator evaluator1 = new Evaluator.Tag("span");
        Evaluator evaluator2 = new Evaluator.Class("nonexistent");
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        boolean result = orEvaluator.matches(rootElement, rootElement);

        assertFalse(result);
    }

    @Test
    public void testOrToString() {
        Evaluator evaluator1 = new Evaluator.Tag("div");
        Evaluator evaluator2 = new Evaluator.Tag("span");
        Collection<Evaluator> evaluators = Arrays.asList(evaluator1, evaluator2);

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(evaluators);

        String result = orEvaluator.toString();

        assertEquals(":or[div, span]", result);
    }

}