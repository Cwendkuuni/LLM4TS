import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParse() {
        // Test case 1
        String html1 = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        String baseUri1 = "http://test.com";
        Document doc1 = Parser.parse(html1, baseUri1);
        assertNotNull(doc1);
        assertEquals("Test", doc1.title());
        assertEquals("Hello World", doc1.body().select("h1").text());
        
        // Test case 2
        String html2 = "<html><head><title></title></head><body></body></html>";
        String baseUri2 = "http://test2.com";
        Document doc2 = Parser.parse(html2, baseUri2);
        assertNotNull(doc2);
        assertEquals("", doc2.title());
        assertEquals("", doc2.body().html());
    }
    
    @Test
    public void testParseBodyFragment() {
        // Test case 1
        String bodyHtml1 = "<h1>Hello World</h1>";
        String baseUri1 = "http://test.com";
        Document doc1 = Parser.parseBodyFragment(bodyHtml1, baseUri1);
        assertNotNull(doc1);
        assertEquals("Hello World", doc1.body().select("h1").text());
        
        // Test case 2
        String bodyHtml2 = "";
        String baseUri2 = "http://test2.com";
        Document doc2 = Parser.parseBodyFragment(bodyHtml2, baseUri2);
        assertNotNull(doc2);
        assertEquals("", doc2.body().html());
    }
    
    @Test
    public void testParseComment() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        parser.parseComment();
        assertEquals("", parser.doc.html());
        
        // Test case 2
        parser.tq.addFirst("<!--Hello-->");
        parser.parseComment();
        assertEquals("<!--Hello-->", parser.doc.html());
    }
    
    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        parser.parseXmlDecl();
        assertEquals("", parser.doc.html());
        
        // Test case 2
        parser.tq.addFirst("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        parser.parseXmlDecl();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", parser.doc.html());
    }
    
    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        parser.tq.addFirst("</h1>");
        parser.parseEndTag();
        assertEquals("", parser.doc.html());
        
        // Test case 2
        parser.tq.addFirst("</body>");
        parser.parseEndTag();
        assertEquals("", parser.doc.html());
    }
    
    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        parser.tq.addFirst("<title>");
        parser.parseStartTag();
        assertEquals("<title>", parser.doc.html());
        
        // Test case 2
        parser.tq.addFirst("<h1>");
        parser.parseStartTag();
        assertEquals("<title><h1></h1>", parser.doc.html());
    }
    
    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        parser.tq.addFirst("id=\"test\"");
        Attribute attribute1 = parser.parseAttribute();
        assertNotNull(attribute1);
        assertEquals("id", attribute1.getKey());
        assertEquals("test", attribute1.getValue());
        
        // Test case 2
        parser.tq.addFirst("class='test'");
        Attribute attribute2 = parser.parseAttribute();
        assertNotNull(attribute2);
        assertEquals("class", attribute2.getKey());
        assertEquals("test", attribute2.getValue());
    }
    
    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        parser.tq.addFirst("Hello");
        parser.parseTextNode();
        assertEquals("Hello", parser.doc.html());
        
        // Test case 2
        parser.tq.addFirst(" World");
        parser.parseTextNode();
        assertEquals("Hello World", parser.doc.html());
    }
    
    @Test
    public void testParseCdata() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        parser.tq.addFirst("<![CDATA[Hello World]]>");
        parser.parseCdata();
        assertEquals("<!--[CDATA[Hello World]]-->", parser.doc.html());
    }
    
    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        Element child1 = new Element(Tag.valueOf("h1"), "http://test.com", new Attributes());
        parser.addChildToParent(child1, false);
        assertNotNull(parser.stack.getLast());
        assertEquals(child1, parser.stack.getLast());
        
        // Test case 2
        Element child2 = new Element(Tag.valueOf("div"), "http://test2.com", new Attributes());
        parser.addChildToParent(child2, true);
        assertNotNull(parser.stack.getLast());
        assertEquals(child2, parser.stack.getLast());
    }
    
    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        Element parent1 = new Element(Tag.valueOf("body"), "http://test.com", new Attributes());
        parser.stack.add(parent1);
        Tag childTag1 = Tag.valueOf("h1");
        assertTrue(parser.stackHasValidParent(childTag1));
        
        // Test case 2
        Element parent2 = new Element(Tag.valueOf("body"), "http://test2.com", new Attributes());
        parser.stack.add(parent2);
        Tag childTag2 = Tag.valueOf("body");
        assertFalse(parser.stackHasValidParent(childTag2));
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        Element element1 = new Element(Tag.valueOf("h1"), "http://test.com", new Attributes());
        parser.stack.add(element1);
        Tag tag1 = Tag.valueOf("div");
        Element result1 = parser.popStackToSuitableContainer(tag1);
        assertEquals(element1, result1);
        
        // Test case 2
        Element element2 = new Element(Tag.valueOf("body"), "http://test2.com", new Attributes());
        parser.stack.add(element2);
        Tag tag2 = Tag.valueOf("title");
        Element result2 = parser.popStackToSuitableContainer(tag2);
        assertNull(result2);
    }
    
    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        Element element1 = new Element(Tag.valueOf("h1"), "http://test.com", new Attributes());
        parser.stack.add(element1);
        Tag tag1 = Tag.valueOf("h1");
        Element result1 = parser.popStackToClose(tag1);
        assertEquals(element1, result1);
        
        // Test case 2
        Element element2 = new Element(Tag.valueOf("div"), "http://test2.com", new Attributes());
        parser.stack.add(element2);
        Tag tag2 = Tag.valueOf("h1");
        Element result2 = parser.popStackToClose(tag2);
        assertNull(result2);
    }
    
    @Test
    public void testLast() {
        Parser parser = new Parser("", "", false);
        
        // Test case 1
        Element element1 = new Element(Tag.valueOf("h1"), "http://test.com", new Attributes());
        parser.stack.add(element1);
        Element result1 = parser.last();
        assertEquals(element1, result1);
        
        // Test case 2
        Element element2 = new Element(Tag.valueOf("div"), "http://test2.com", new Attributes());
        parser.stack.add(element2);
        Element result2 = parser.last();
        assertEquals(element2, result2);
    }
    
}