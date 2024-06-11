
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
  
public class ParserTest {
  
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Elements titleElements = document.getElementsByTag("title");
        Element titleElement = titleElements.first();
        String title = titleElement.text();
        Assert.assertEquals("Test", title);
    }
  
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
        Elements pElements = document.getElementsByTag("p");
        Element pElement = pElements.first();
        String text = pElement.text();
        Assert.assertEquals("Hello, World!", text);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello, World!</p></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Elements pElements = document.getElementsByTag("p");
        Element pElement = pElements.first();
        String text = pElement.text();
        Assert.assertEquals("Hello, World!", text);
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Elements commentElements = document.getAllElements();
        Element commentElement = commentElements.first();
        String comment = commentElement.data();
        Assert.assertEquals(" This is a comment ", comment);
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Elements xmlDeclElements = document.getAllElements();
        Element xmlDeclElement = xmlDeclElements.first();
        String xmlDecl = xmlDeclElement.data();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDecl);
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Elements pElements = document.getElementsByTag("p");
        Assert.assertEquals(1, pElements.size());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<div><p>Hello, World!</p></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Elements divElements = document.getElementsByTag("div");
        Assert.assertEquals(1, divElements.size());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "<p>Hello, World!</p>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Elements pElements = document.getElementsByTag("p");
        Element pElement = pElements.first();
        String text = pElement.text();
        Assert.assertEquals("Hello, World!", text);
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is CDATA]]>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Elements cdataElements = document.getAllElements();
        Element cdataElement = cdataElements.first();
        String cdata = cdataElement.data();
        Assert.assertEquals("This is CDATA", cdata);
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<a href=\"https://www.example.com\">Link</a>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        Elements aElements = document.getElementsByTag("a");
        Element aElement = aElements.first();        
        String href = aElement.attr("href");
        Assert.assertEquals("https://www.example.com", href);
    }
    
    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        boolean validParent = parser.stackHasValidParent(parser.headTag);
        Assert.assertTrue(validParent);
    }
    
    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        Element el = new Element(parser.bodyTag, "");
        parser.stack.add(el);
        Element closedEl = parser.popStackToClose(parser.bodyTag);
        Assert.assertEquals(parser.bodyTag, closedEl.tag());
    }
    
    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        Element el = new Element(parser.bodyTag, "");
        parser.stack.add(el);
        Element lastEl = parser.last();
        Assert.assertEquals(parser.bodyTag, lastEl.tag());
    }
  
}
