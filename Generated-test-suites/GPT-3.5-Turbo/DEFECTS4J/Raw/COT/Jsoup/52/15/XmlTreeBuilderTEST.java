import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml></xml>", "http://example.com", new ParseErrorList());
        
        assertNotNull(builder.stack);
        assertNotNull(builder.doc);
        assertNotNull(builder.tokeniser);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.StartTag startTag = new Token.StartTag("child", new Attributes());
        builder.process(startTag);
        
        assertEquals(1, builder.currentElement().children().size());
        assertEquals("child", builder.currentElement().child(0).tagName());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        builder.stack.add(new Element(Tag.valueOf("child"), "http://example.com"));
        Token.EndTag endTag = new Token.EndTag("child");
        builder.process(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals("root", builder.stack.get(0).tagName());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Comment commentToken = new Token.Comment("This is a comment", "http://example.com");
        builder.process(commentToken);
        
        assertEquals(1, builder.currentElement().children().size());
        assertTrue(builder.currentElement().child(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) builder.currentElement().child(0)).getData());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Character characterToken = new Token.Character("This is a character", "http://example.com");
        builder.process(characterToken);
        
        assertEquals(1, builder.currentElement().children().size());
        assertTrue(builder.currentElement().child(0) instanceof TextNode);
        assertEquals("This is a character", ((TextNode) builder.currentElement().child(0)).getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://example.com");
        builder.process(doctype);
        
        assertEquals(1, builder.currentElement().children().size());
        assertTrue(builder.currentElement().child(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) builder.currentElement().child(0)).getName());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Node node = new Element(Tag.valueOf("child"), "http://example.com");
        builder.insertNode(node);
        
        assertEquals(1, builder.currentElement().children().size());
        assertEquals("child", builder.currentElement().child(0).tagName());
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.StartTag startTag = new Token.StartTag("child", new Attributes());
        builder.insert(startTag);
        
        assertEquals(1, builder.currentElement().children().size());
        assertEquals("child", builder.currentElement().child(0).tagName());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Comment commentToken = new Token.Comment("This is a comment", "http://example.com");
        builder.insert(commentToken);
        
        assertEquals(1, builder.currentElement().children().size());
        assertTrue(builder.currentElement().child(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) builder.currentElement().child(0)).getData());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Character characterToken = new Token.Character("This is a character", "http://example.com");
        builder.insert(characterToken);
        
        assertEquals(1, builder.currentElement().children().size());
        assertTrue(builder.currentElement().child(0) instanceof TextNode);
        assertEquals("This is a character", ((TextNode) builder.currentElement().child(0)).getWholeText());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://example.com");
        builder.insert(doctype);
        
        assertEquals(1, builder.currentElement().children().size());
        assertTrue(builder.currentElement().child(0) instanceof DocumentType);
        assertEquals("html", ((DocumentType) builder.currentElement().child(0)).getName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        Element child = new Element(Tag.valueOf("child"), "http://example.com");
        builder.stack.add(root);
        builder.stack.add(child);
        builder.stack.add(child);
        
        Token.EndTag endTag = new Token.EndTag("child");
        builder.popStackToClose(endTag);
        
        assertEquals(1, builder.stack.size());
        assertEquals(root, builder.stack.get(0));
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<tag>Content</tag>", "http://example.com", new ParseErrorList());
        
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).tagName());
        assertEquals("Content", ((TextNode) ((Element) nodes.get(0)).childNode(0)).getWholeText());
    }
}