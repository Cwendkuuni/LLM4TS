
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <h1>Hello World</h1>\n </body>\n</html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html><head></head><body><div>\n <p>Hello World</p>\n</div></body></html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Hello World</p></div>";
        String baseUri = "http://example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html><head></head><body><div>\n <p>Hello World</p>\n</div></body></html>", doc.outerHtml());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <!-- This is a comment -->\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello World</p>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<html><body><p>Hello World</p></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <p>Hello World</p>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "<html><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  Hello World\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<html><body><![CDATA[ Hello World ]]></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  \n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html><body>Hello World</body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  Hello World\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<html><body><div class=\"container\" id=\"main\">Hello World</div></body></html>";
        String baseUri = "http://example.com";
        Document doc = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <div class=\"container\" id=\"main\">Hello World</div>\n </body>\n</html>", doc.outerHtml());
    }
    
    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        
        Assert.assertTrue(parser.stackHasValidParent(Tag.valueOf("p")));
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        parser.stack.add(new Element(Tag.valueOf("div"), ""));
        parser.stack.add(new Element(Tag.valueOf("p"), ""));
        
        Assert.assertEquals(Tag.valueOf("div"), parser.popStackToSuitableContainer(Tag.valueOf("p")));
    }
    
    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        parser.stack.add(new Element(Tag.valueOf("div"), ""));
        parser.stack.add(new Element(Tag.valueOf("p"), ""));
        
        Assert.assertEquals(Tag.valueOf("p"), parser.popStackToClose(Tag.valueOf("p")));
        Assert.assertEquals(1, parser.stack.size());
    }
    
    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        
        Assert.assertEquals(Tag.valueOf("body"), parser.last().tag());
    }
}
