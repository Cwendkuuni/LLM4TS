import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

    @Test
    public void testParse() {
        String cssQuery = "div#id.class[attr=value]:containsOwn(text)";
        
        Document doc = Jsoup.parse("<div id=\"id\" class=\"class\" attr=\"value\">Contains text</div>");
        Elements elements = doc.select(cssQuery);
        
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("div", elements.get(0).tagName());
        Assert.assertEquals("Contains text", elements.get(0).text());
    }
    
    @Test
    public void testParse_invalidQuery() {
        String cssQuery = "div#id.class[attr=value]:containsOwn(text";
        
        Document doc = Jsoup.parse("<div id=\"id\" class=\"class\" attr=\"value\">Contains text</div>");
        Elements elements = doc.select(cssQuery);
        
        Assert.assertEquals(0, elements.size());
    }
    
    @Test
    public void testParse_combinator() {
        String cssQuery = "div > .class + [attr=value] ~ span";
        
        Document doc = Jsoup.parse("<div><div><div><div class=\"class\"></div><div attr=\"value\"></div><span></span></div></div></div>");
        Elements elements = doc.select(cssQuery);
        
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("span", elements.get(0).tagName());
    }
    
    @Test
    public void testParse_pseudoSelector() {
        String cssQuery = "div:contains(text)";
        
        Document doc = Jsoup.parse("<div id=\"id\" class=\"class\">Contains text</div>");
        Elements elements = doc.select(cssQuery);
        
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("div", elements.get(0).tagName());
        Assert.assertEquals("Contains text", elements.get(0).text());
    }
    
    @Test
    public void testParse_attributeSelector() {
        String cssQuery = "div[attr=value]";
        
        Document doc = Jsoup.parse("<div attr=\"value\">Contains text</div>");
        Elements elements = doc.select(cssQuery);
        
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("div", elements.get(0).tagName());
    }
    
    @Test
    public void testParse_idSelector() {
        String cssQuery = "div#id";
        
        Document doc = Jsoup.parse("<div id=\"id\">Contains text</div>");
        Elements elements = doc.select(cssQuery);
        
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("div", elements.get(0).tagName());
        Assert.assertEquals("id", elements.get(0).id());
    }

}