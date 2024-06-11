import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

    @Test
    public void testAndMethod() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                Arrays.asList(
                        new ElementSelector("p"),
                        new ElementSelector("h1")
                )
        );

        String html = "<html><body><p id=\"p1\">Test1</p><h1 id=\"h1\">Test2</h1></body></html>";
        Document document = Jsoup.parse(html);
        Element root = document.body();

        List<Element> elements = andEvaluator.findAll(root);
        assertTrue(elements.size() > 0);
        assertEquals(2, elements.size());
    }

    @Test
    public void testAndMethod_NoMatch() {
        CombiningEvaluator andEvaluator = new CombiningEvaluator.And(
                Arrays.asList(
                        new ElementSelector("p"),
                        new ElementSelector("h2")
                )
        );

        String html = "<html><body><p id=\"p1\">Test1</p><h2 id=\"h2\">Test3</h2></body></html>";
        Document document = Jsoup.parse(html);
        Element root = document.body();

        List<Element> elements = andEvaluator.findAll(root);
        assertTrue(elements.isEmpty());
    }

    @Test
    public void testOrMethod() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new ElementSelector("p"),
                        new ElementSelector("h1")
                )
        );

        String html = "<html><body><p id=\"p1\">Test1</p><h1 id=\"h1\">Test2</h1></body></html>";
        Document document = Jsoup.parse(html);
        Element root = document.body();

        List<Element> elements = orEvaluator.findAll(root);
        assertTrue(elements.size() >= 1);
    }

    @Test
    public void testOrMethod_MultipleMatches() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        new ElementSelector("p"),
                        new ElementSelector("h1"),
                        new ElementSelector("h2")
                )
        );

        String html = "<html><body><p id=\"p1\">Test1</p><h1 id=\"h1\">Test2</h1><h2 id=\"h2\">Test3</h2></body></html>";
        Document document = Jsoup.parse(html);
        Element root = document.body();

        List<Element> elements = orEvaluator.findAll(root);
        assertTrue(elements.size() >= 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrMethod_EmptyList() {
        new CombiningEvaluator.Or(new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrMethod_NullElement() {
        CombiningEvaluator orEvaluator = new CombiningEvaluator.Or(
                Arrays.asList(
                        null,
                        new ElementSelector("p")
                )
        );
    }
}