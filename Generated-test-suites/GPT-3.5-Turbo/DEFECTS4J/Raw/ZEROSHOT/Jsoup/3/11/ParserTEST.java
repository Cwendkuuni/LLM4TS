import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head></head><body><h1>Hello</h1><p>World</p></body></html>";
        Document doc = Parser.parse(html, "http://example.com");
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        
        Element htmlTag = doc.getElementsByTag("html").first();
        Element headTag = doc.getElementsByTag("head").first();
        Element bodyTag = doc.getElementsByTag("body").first();
        
        Assert.assertNotNull(htmlTag);
        Assert.assertNotNull(headTag);
        Assert.assertNotNull(bodyTag);
        
        Element h1Tag = doc.getElementsByTag("h1").first();
        Element pTag = doc.getElementsByTag("p").first();
        
        Assert.assertNotNull(h1Tag);
        Assert.assertNotNull(pTag);
        
        Assert.assertEquals("Hello", h1Tag.text());
        Assert.assertEquals("World", pTag.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello</h1><p>World</p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        
        Element bodyTag = doc.getElementsByTag("body").first();
        
        Assert.assertNotNull(bodyTag);
        
        Element h1Tag = doc.getElementsByTag("h1").first();
        Element pTag = doc.getElementsByTag("p").first();
        
        Assert.assertNotNull(h1Tag);
        Assert.assertNotNull(pTag);
        
        Assert.assertEquals("Hello", h1Tag.text());
        Assert.assertEquals("World", pTag.text());
    }

}