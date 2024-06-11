import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.helper.Validate;
import org.jsoup.helper.Validate.Fail;

public class XmlTreeBuilderTest {

    // Test initialiseParse method
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        builder.initialiseParse(input, baseUri, errors);
        
        assertEquals(builder.input, input);
        assertEquals(builder.baseUri, baseUri);
        assertEquals(builder.errors, errors);
        assertNotNull(builder.stack);
        assertEquals(builder.stack.size(), 1);
    }
    
    // Test process method for StartTag token
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.StartTag startTag = new Token.StartTag("tag", "attributes");
        
        builder.stack.add(el);
        builder.process(startTag);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 0);
    }
    
    // Test process method for EndTag token
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.EndTag endTag = new Token.EndTag("tag");
        
        builder.stack.add(el);
        builder.process(endTag);
        
        assertEquals(builder.stack.size(), 0);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 0);
    }
    
    // Test process method for Comment token
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.Comment comment = new Token.Comment("comment");
        
        builder.stack.add(el);
        builder.process(comment);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 1);
        assertTrue(el.childNode(0) instanceof Comment);
        assertEquals(((Comment) el.childNode(0)).getData(), "comment");
    }
    
    // Test process method for Character token
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.Character character = new Token.Character("character");
        
        builder.stack.add(el);
        builder.process(character);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 1);
        assertTrue(el.childNode(0) instanceof TextNode);
        assertEquals(((TextNode) el.childNode(0)).getWholeText(), "character");
    }
    
    // Test process method for Doctype token
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier");
        
        builder.stack.add(el);
        builder.process(doctype);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 1);
        assertTrue(el.childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) el.childNode(0);
        assertEquals(doctypeNode.getName(), "name");
        assertEquals(doctypeNode.getPublicIdentifier(), "publicIdentifier");
        assertEquals(doctypeNode.getSystemIdentifier(), "systemIdentifier");
    }
    
    // Test insertNode method
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Node node = new TextNode("text", "http://example.com");
        
        builder.stack.add(el);
        builder.insertNode(node);
        
        assertNotNull(el.children());
        assertEquals(el.children().size(), 1);
        assertEquals(el.childNode(0), node);
    }
    
    // Test insert method for StartTag token
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.StartTag startTag = new Token.StartTag("tag", "attributes");
        
        builder.stack.add(el);
        Element newEl = builder.insert(startTag);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 1);
        assertEquals(el.childNode(0), newEl);
        assertTrue(newEl instanceof Element);
        assertEquals(newEl.tagName(), "tag");
        assertEquals(newEl.baseUri(), "http://example.com");
        assertEquals(newEl.attributes(), "attributes");
        assertFalse(newEl.isSelfClosing());
    }
    
    // Test insert method for Comment token
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.Comment comment = new Token.Comment("comment");
        
        builder.stack.add(el);
        builder.insert(comment);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 1);
        assertTrue(el.childNode(0) instanceof Comment);
        assertEquals(((Comment) el.childNode(0)).getData(), "comment");
    }
    
    // Test insert method for Character token
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.Character character = new Token.Character("character");
        
        builder.stack.add(el);
        builder.insert(character);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 1);
        assertTrue(el.childNode(0) instanceof TextNode);
        assertEquals(((TextNode) el.childNode(0)).getWholeText(), "character");
    }
    
    // Test insert method for Doctype token
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("tag"), "http://example.com", null);
        Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier");
        
        builder.stack.add(el);
        builder.insert(doctype);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el);
        assertNotNull(el.children());
        assertEquals(el.children().size(), 1);
        assertTrue(el.childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) el.childNode(0);
        assertEquals(doctypeNode.getName(), "name");
        assertEquals(doctypeNode.getPublicIdentifier(), "publicIdentifier");
        assertEquals(doctypeNode.getSystemIdentifier(), "systemIdentifier");
    }
    
    // Test popStackToClose method
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el1 = new Element(Tag.valueOf("tag1"), "http://example.com", null);
        Element el2 = new Element(Tag.valueOf("tag2"), "http://example.com", null);
        Token.EndTag endTag = new Token.EndTag("tag1");
        
        builder.stack.add(el1);
        builder.stack.add(el2);
        builder.popStackToClose(endTag);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.stack.getFirst(), el2);
    }
}