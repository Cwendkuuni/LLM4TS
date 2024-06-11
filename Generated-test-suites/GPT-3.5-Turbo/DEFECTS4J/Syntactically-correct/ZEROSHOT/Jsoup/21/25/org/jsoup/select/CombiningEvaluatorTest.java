import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {
    private CombiningEvaluator.And andEvaluator;
    private CombiningEvaluator.Or orEvaluator;

    @Before
    public void setup() {
        andEvaluator = new CombiningEvaluator.And();
        orEvaluator = new CombiningEvaluator.Or();
    }

    @Test
    public void testAndMatches() {
        Document doc = Jsoup.parse("<body><p>Hello World</p></body>");

        Element root = doc.body();
        Element node = doc.selectFirst("p");

        Assert.assertTrue(andEvaluator.matches(root, node));
    }

    @Test
    public void testAndDoesNotMatch() {
        Document doc = Jsoup.parse("<body><p>Hello World</p></body>");

        Element root = doc.body();
        Element node = doc.selectFirst("div");

        Assert.assertFalse(andEvaluator.matches(root, node));
    }

    @Test
    public void testOrMatches() {
        Document doc = Jsoup.parse("<body><p>Hello World</p></body>");

        Element root = doc.body();
        Element node = doc.selectFirst("p");

        Assert.assertTrue(orEvaluator.matches(root, node));
    }

    @Test
    public void testOrDoesNotMatch() {
        Document doc = Jsoup.parse("<body><p>Hello World</p></body>");

        Element root = doc.body();
        Element node = doc.selectFirst("div");

        Assert.assertFalse(orEvaluator.matches(root, node));
    }

    @Test
    public void testAndToString() {
        CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
                new Evaluator.Tag("p"),
                new Evaluator.ContainsText("Hello")
        );

        String expected = "p :contains(Hello)";
        Assert.assertEquals(expected, andEvaluator.toString());
    }

    @Test
    public void testOrToString() {
        CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
                new Evaluator.Tag("p"),
                new Evaluator.Tag("div")
        );

        String expected = ":or[p, div]";
        Assert.assertEquals(expected, orEvaluator.toString());
    }

    @Test
    public void testOrAdd() {
        Evaluator evaluator1 = new Evaluator.Tag("p");
        Evaluator evaluator2 = new Evaluator.Tag("div");

        orEvaluator.add(evaluator1);
        orEvaluator.add(evaluator2);

        List<Evaluator> expected = Arrays.asList(evaluator1, evaluator2);
        Assert.assertEquals(expected, orEvaluator.getEvaluators());
    }
}