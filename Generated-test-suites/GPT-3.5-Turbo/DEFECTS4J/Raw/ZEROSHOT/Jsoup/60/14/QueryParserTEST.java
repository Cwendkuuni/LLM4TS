import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse_id() {
        Document doc = Jsoup.parse("<div id=\"test\"></div>");
        Elements elements = QueryParser.parse("#test").select(doc);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("test", elements.get(0).id());
    }

    @Test
    public void testParse_class() {
        Document doc = Jsoup.parse("<div class=\"test\"></div>");
        Elements elements = QueryParser.parse(".test").select(doc);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("test", elements.get(0).className());
    }

    @Test
    public void testParse_tag() {
        Document doc = Jsoup.parse("<div></div>");
        Elements elements = QueryParser.parse("div").select(doc);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("div", elements.get(0).tagName());
    }

    @Test
    public void testParse_attribute() {
        Document doc = Jsoup.parse("<div data-value=\"123\"></div>");
        Elements elements = QueryParser.parse("[data-value]").select(doc);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("123", elements.get(0).attr("data-value"));
    }

    @Test
    public void testParse_combinator() {
        Document doc = Jsoup.parse("<div><span>test</span></div>");
        Elements elements = QueryParser.parse("div span").select(doc);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("test", elements.get(0).text());
    }

    @Test
    public void testParse_pseudo_selector() {
        Document doc = Jsoup.parse("<ul><li>Item 1</li><li>Item 2</li></ul>");
        Elements elements = QueryParser.parse("li:first-child").select(doc);
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("Item 1", elements.get(0).text());
    }
}