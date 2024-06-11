import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import org.jsoup.select.Evaluator;
import org.jsoup.select.QueryParser;

public class QueryParserTest {

    @Test
    public void testParse() {
        String query = "div#myId.class[attr=value]:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        String html = "<div id=\"myId\" class=\"myClass\" attr=\"value\">Hello World</div>";
        Document document = Jsoup.parse(html);
        Assert.assertTrue(evaluator.matches(document.getElementsByTag("div").first()));
    }

    @Test
    public void testCombinator() {
        String query = "div > span";
        Evaluator evaluator = QueryParser.parse(query);
        String html = "<div><span></span></div>";
        Document document = Jsoup.parse(html);
        Assert.assertTrue(evaluator.matches(document.getElementsByTag("span").first()));
    }

    @Test
    public void testAttributeSelector() {
        String query = "div[attr=value]";
        Evaluator evaluator = QueryParser.parse(query);
        String html = "<div attr=\"value\"></div>";
        Document document = Jsoup.parse(html);
        Assert.assertTrue(evaluator.matches(document.getElementsByTag("div").first()));
    }

    @Test
    public void testPseudoSelector() {
        String query = "div:contains(text)";
        Evaluator evaluator = QueryParser.parse(query);
        String html = "<div>Hello World</div>";
        Document document = Jsoup.parse(html);
        Assert.assertTrue(evaluator.matches(document.getElementsByTag("div").first()));
    }
}