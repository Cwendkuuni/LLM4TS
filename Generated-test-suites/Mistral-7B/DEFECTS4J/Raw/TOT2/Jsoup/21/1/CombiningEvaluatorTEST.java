import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Elements;
import org.jsoup.select.ElementSelector;

public class CombiningEvaluatorTest {

    @Test
    public void testAndConstructorEmpty() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And();
        assertFalse(andEvaluator.matches(null, null));
    }

    @Test
    public void testAndConstructorWithEvaluatorsNull() {
        assertThrows(NullPointerException.class, () -> new CombiningEvaluator.And(null));
    }

    @Test
    public void testAndConstructorWithEmptyList() {
        assertFalse(new CombiningEvaluator.And().matches(new Element("html"), new Element("body")));
    }

    @Test
    public void testAndConstructorWithOneEvaluator() {
        Element root = new Element("html");
        Evaluator evaluator = new ElementSelector("body");
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(evaluator);
        Elements matchedElements = andEvaluator.select(root);
        assertTrue(matchedElements.size() > 0);
    }

    @Test
    public void testAndConstructorWithTwoEvaluators() {
        Element root = new Element("html");
        Evaluator evaluator1 = new ElementSelector("body");
        Evaluator evaluator2 = new ElementSelector("h1");
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        Elements matchedElements = andEvaluator.select(root);
        assertTrue(matchedElements.size() > 0);
    }

    @Test
    public void testAndMatchesFalse() {
        Element root = new Element("html");
        Evaluator evaluator1 = new ElementSelector("body");
        Evaluator evaluator2 = new ElementSelector("p");
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(evaluator1, evaluator2);
        assertFalse(andEvaluator.matches(root, new Element("head")));
    }

    @Test
    public void testOrConstructorEmpty() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or();
        assertFalse(orEvaluator.matches(null, null));
    }

    @Test
    public void testOrConstructorWithNull() {
        assertThrows(NullPointerException.class, () -> new CombiningEvaluator.Or(null));
    }

    @Test
    public void testOrConstructorWithOneEvaluator() {
        Element root = new Element("html");
        Evaluator evaluator = new ElementSelector("body");
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(evaluator);
        Elements matchedElements = orEvaluator.select(root);
        assertTrue(matchedElements.size() > 0);
    }

    @Test
    public void testOrMatchesTrue() {
        Element root = new Element("html");
        Evaluator evaluator1 = new ElementSelector("body");
        Evaluator evaluator2 = new ElementSelector("h1");
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        Elements matchedElements1 = orEvaluator.select(root);
        assertTrue(matchedElements1.size() > 0);

        Evaluator evaluator3 = new ElementSelector("h2");
        CombiningEvaluator orEvaluatorWithThreeEvaluators = new CombiningEvaluator.Or(evaluator1, evaluator2, evaluator3);
        Elements matchedElements2 = orEvaluatorWithThreeEvaluators.select(root);
        assertTrue(matchedElements2.size() > 0); // At least one of evaluator1, evaluator2 or evaluator3 should match.
    }

    @Test
    public void testOrMatchesFalse() {
        Element root = new Element("html");
        Evaluator evaluator1 = new ElementSelector("body");
        Evaluator evaluator2 = new ElementSelector("p");
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(evaluator1, evaluator2);
        assertFalse(orEvaluator.matches(root, new Element("head")));
    }

    @Test
    public void testAddEvaluator() {
        Element root = new Element("html");
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or();
        Evaluator evaluator1 = new ElementSelector("body");
        Evaluator evaluator2 = new ElementSelector("h1");
        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);
        Elements matchedElements = orEvaluator.select(root);
        assertTrue(matchedElements.size() > 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEvaluatorToAnd() {
        Element root = new Element("html");
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And();
        Evaluator evaluator1 = new ElementSelector("body");
        andEvaluator.add(evaluator1);
    }

}