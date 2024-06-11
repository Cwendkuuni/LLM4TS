import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMatches() {
        Element root = new Document(""); // Creating a dummy root element for testing
        Element node = new Element(""); // Creating a dummy node element for testing

        Evaluator evaluator1 = rootElement -> true;
        Evaluator evaluator2 = rootElement -> false;

        CombiningEvaluator evaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        assertFalse(evaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        Evaluator evaluator1 = rootElement -> true;
        Evaluator evaluator2 = rootElement -> false;

        CombiningEvaluator evaluator = new CombiningEvaluator.And(Arrays.asList(evaluator1, evaluator2));

        String expected = "true false";
        String actual = evaluator.toString();
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testOrMatches() {
        Element root = new Document(""); // Creating a dummy root element for testing
        Element node = new Element(""); // Creating a dummy node element for testing

        Evaluator evaluator1 = rootElement -> false;
        Evaluator evaluator2 = rootElement -> true;

        CombiningEvaluator evaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        assertTrue(evaluator.matches(root, node));
    }

    @Test
    public void testOrToString() {
        Evaluator evaluator1 = rootElement -> true;
        Evaluator evaluator2 = rootElement -> false;

        CombiningEvaluator evaluator = new CombiningEvaluator.Or(Arrays.asList(evaluator1, evaluator2));

        String expected = ":or[true, false]";
        String actual = evaluator.toString();
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testOrSingleEvaluator() {
        Element root = new Document(""); // Creating a dummy root element for testing
        Element node = new Element(""); // Creating a dummy node element for testing

        Evaluator evaluator = rootElement -> false;

        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(Collections.singletonList(evaluator));
        assertFalse(orEvaluator.matches(root, node));

        orEvaluator.add(rootElement -> true);
        assertTrue(orEvaluator.matches(root, node));
    }

}