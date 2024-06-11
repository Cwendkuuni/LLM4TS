import org.junit.Assert;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        Assert.assertEquals("Test", document.title());

        Element body = document.body();
        Assert.assertNotNull(body);
        
        Element h1 = body.selectFirst("h1");
        Assert.assertNotNull(h1);
        Assert.assertEquals("Hello World", h1.text());
    }
    
    @Test
    public void testParseBodyFragment() {
        String html = "<div>Hello World</div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragment(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        Element body = document.body();
        Assert.assertNotNull(body);
        
        Element div = body.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("Hello World", div.text());
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String html = "<div>Hello World</span>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parseBodyFragmentRelaxed(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        Element body = document.body();
        Assert.assertNotNull(body);
        
        Element div = body.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("Hello World", div.text());
    }
    
    @Test
    public void testParseComment() {
        String html = "<!-- Test Comment -->";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        Comment comment = (Comment) document.childNode(0);
        Assert.assertNotNull(comment);
        Assert.assertEquals(" Test Comment ", comment.getData());
    }
    
    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        XmlDeclaration decl = (XmlDeclaration) document.childNode(0);
        Assert.assertNotNull(decl);
        Assert.assertEquals("1.0", decl.attr("version"));
        Assert.assertEquals("UTF-8", decl.attr("encoding"));
    }
    
    @Test
    public void testParseEndTag() {
        String html = "<div><p>Hello World</p></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        Element div = document.selectFirst("div");
        Assert.assertNotNull(div);
        
        Element p = div.selectFirst("p");
        Assert.assertNotNull(p);
        Assert.assertEquals("Hello World", p.text());
    }
    
    @Test
    public void testParseStartTag() {
        String html = "<div class=\"container\"><p>Hello World</p></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        Element div = document.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("container", div.className());
        
        Element p = div.selectFirst("p");
        Assert.assertNotNull(p);
        Assert.assertEquals("Hello World", p.text());
    }
    
    @Test
    public void testParseTextNode() {
        String html = "<div>Hello World</div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        Element div = document.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("Hello World", div.text());
    }
    
    @Test
    public void testParseCdata() {
        String html = "<div><![CDATA[Hello <span>World</span>]]></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        Element div = document.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("Hello <span>World</span>", div.text());
    }
    
    @Test
    public void testParseAttribute() {
        String html = "<div class='container' id=\"myDiv\"></div>";
        String baseUri = "https://www.example.com";
        Document document = Parser.parse(html, baseUri);
        
        Assert.assertNotNull(document);
        Assert.assertEquals("https://www.example.com", document.baseUri());
        
        Element div = document.selectFirst("div");
        Assert.assertNotNull(div);
        Assert.assertEquals("container", div.className());
        Assert.assertEquals("myDiv", div.id());
    }
    
    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        parser.stack.add(new Element(Tag.valueOf("div"), ""));
        parser.stack.add(new Element(Tag.valueOf("p"), ""));
        
        Assert.assertTrue(parser.stackHasValidParent(Tag.valueOf("span")));
        Assert.assertFalse(parser.stackHasValidParent(Tag.valueOf("h1")));
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        parser.stack.add(new Element(Tag.valueOf("div"), ""));
        parser.stack.add(new Element(Tag.valueOf("p"), ""));
        
        Assert.assertEquals(Tag.valueOf("div"), parser.popStackToSuitableContainer(Tag.valueOf("div")).tag());
        Assert.assertEquals(Tag.valueOf("body"), parser.popStackToSuitableContainer(Tag.valueOf("h1")).tag());
    }
    
    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        parser.stack.add(new Element(Tag.valueOf("div"), ""));
        parser.stack.add(new Element(Tag.valueOf("p"), ""));
        
        Assert.assertEquals(Tag.valueOf("p"), parser.popStackToClose(Tag.valueOf("p")).tag());
        Assert.assertEquals(Tag.valueOf("div"), parser.popStackToClose(Tag.valueOf("div")).tag());
        Assert.assertNull(parser.popStackToClose(Tag.valueOf("h1")));
    }
    
    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        parser.stack.add(new Element(Tag.valueOf("body"), ""));
        parser.stack.add(new Element(Tag.valueOf("div"), ""));
        parser.stack.add(new Element(Tag.valueOf("p"), ""));
        
        Assert.assertEquals(Tag.valueOf("p"), parser.last().tag());
    }
}