
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div.myClass";
        Document doc = Jsoup.parse("<div class=\"myClass\"></div>");
        Elements elements = doc.select(query);
        assertEquals(1, elements.size());
        assertEquals("div", elements.get(0).tagName());
        assertEquals("myClass", elements.get(0).className());
    }

    @Test
    public void testParse_withInvalidQuery() {
        String query = "div.myClass]";
        Document doc = Jsoup.parse("<div class=\"myClass\"></div>");
        Elements elements = doc.select(query);
        assertEquals(0, elements.size());
    }

    @Test
    public void testParse_withCombinators() {
        String query = "div > p";
        Document doc = Jsoup.parse("<div><p>Hello World</p></div>");
        Elements elements = doc.select(query);
        assertEquals(1, elements.size());
        assertEquals("p", elements.get(0).tagName());
    }

    @Test
    public void testParse_withAttributes() {
        String query = "div[class=\"myClass\"]";
        Document doc = Jsoup.parse("<div class=\"myClass\"></div>");
        Elements elements = doc.select(query);
        assertEquals(1, elements.size());
        assertEquals("div", elements.get(0).tagName());
        assertEquals("myClass", elements.get(0).className());
    }

    @Test
    public void testParse_withPseudoSelectors() {
        String query = "div:contains(\"Hello\")";
        Document doc = Jsoup.parse("<div>Hello World</div>");
        Elements elements = doc.select(query);
        assertEquals(1, elements.size());
        assertEquals("div", elements.get(0).tagName());
        assertEquals("Hello World", elements.get(0).text());
    }

    @Test
    public void testParse_withNthChild() {
        String query = "div:nth-child(2n+1)";
        Document doc = Jsoup.parse("<div></div><div></div><div></div><div></div>");
        Elements elements = doc.select(query);
        assertEquals(2, elements.size());
        assertEquals("div", elements.get(0).tagName());
        assertEquals("div", elements.get(1).tagName());
    }
}

