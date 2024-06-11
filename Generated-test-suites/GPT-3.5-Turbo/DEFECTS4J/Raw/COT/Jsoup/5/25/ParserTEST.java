
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head><body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals("Test Title", doc.title());
        Assert.assertEquals("Test Heading", doc.body().select("h1").text());
        Assert.assertEquals("Test Paragraph", doc.body().select("p").text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph</p>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        Assert.assertEquals("Test Heading", doc.body().select("h1").text());
        Assert.assertEquals("Test Paragraph", doc.body().select("p").text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test Heading</h1><p>Test Paragraph</p>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        Assert.assertEquals("Test Heading", doc.body().select("h1").text());
        Assert.assertEquals("Test Paragraph", doc.body().select("p").text());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- test comment -->";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<!-- test comment -->", doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<h1>Test Heading</h1>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals(1, doc.body().select("h1").size());
        Assert.assertEquals("Test Heading", doc.body().select("h1").text());

        html = "<h1></h1>";
        doc = Parser.parse(html, baseUri);
        Assert.assertEquals(1, doc.body().select("h1").size());
        Assert.assertEquals("", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<h1>Test Heading</h1>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals(1, doc.body().select("h1").size());
        Assert.assertEquals("Test Heading", doc.body().select("h1").text());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "Test Text";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals(1, doc.body().childNodes().size());
        Assert.assertEquals("Test Text", doc.body().childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<![CDATA[Test CDATA]]>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals(1, doc.childNodes().size());
        Assert.assertEquals("<![CDATA[Test CDATA]]>", doc.childNodes().get(0).outerHtml());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<div class=\"test\" id='testId' data-key=dataValue></div>";
        String baseUri = "https://www.example.com/";
        Document doc = Parser.parse(html, baseUri);
        Assert.assertEquals(1, doc.select("div").size());
        Assert.assertEquals("test", doc.select("div").attr("class"));
        Assert.assertEquals("testId", doc.select("div").attr("id"));
        Assert.assertEquals("dataValue", doc.select("div").attr("data-key"));
    }
    
    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        Element parent = new Element(Tag.valueOf("head"), "");
        parser.stack.add(parent);
        
        Tag childTag = Tag.valueOf("title");
        boolean result = parser.stackHasValidParent(childTag);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        Element parent = new Element(Tag.valueOf("head"), "");
        Element child = new Element(Tag.valueOf("title"), "");
        parser.stack.add(parent);
        parser.stack.add(child);
        
        Element result = parser.popStackToSuitableContainer(Tag.valueOf("title"));
        Assert.assertEquals(parent, result);
        Assert.assertEquals(1, parser.stack.size());
        Assert.assertEquals(child, parser.stack.get(0));
    }
    
    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        Element element1 = new Element(Tag.valueOf("div"), "");
        Element element2 = new Element(Tag.valueOf("span"), "");
        parser.stack.add(element1);
        parser.stack.add(element2);
        
        Tag tag = Tag.valueOf("div");
        Element result = parser.popStackToClose(tag);
        Assert.assertEquals(element1, result);
        Assert.assertEquals(0, parser.stack.size());
    }
    
    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        Element element = new Element(Tag.valueOf("div"), "");
        parser.stack.add(element);
        
        Element result = parser.last();
        Assert.assertEquals(element, result);
    }
}

