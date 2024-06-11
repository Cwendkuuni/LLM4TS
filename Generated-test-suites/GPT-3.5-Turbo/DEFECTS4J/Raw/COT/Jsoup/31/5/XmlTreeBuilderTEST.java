
import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        treeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert that document is added to the stack
        assertEquals(1, treeBuilder.getStack().size());
        assertEquals(treeBuilder.getDoc(), treeBuilder.getStack().get(0));
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        
        treeBuilder.process(startTag);
        
        // Assert that element is inserted into the document
        Element element = treeBuilder.getCurrentElement();
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "", null);
        treeBuilder.getStack().add(element);
        Token.EndTag endTag = new Token.EndTag("div");
        
        treeBuilder.process(endTag);
        
        // Assert that element is removed from the stack
        assertEquals(0, treeBuilder.getStack().size());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
        
        treeBuilder.process(comment);
        
        // Assert that comment node is inserted into the document
        Element element = treeBuilder.getCurrentElement();
        assertNotNull(element);
        Node commentNode = element.childNodes().get(0);
        assertNotNull(commentNode);
        assertTrue(commentNode instanceof Comment);
        assertEquals("This is a comment", ((Comment) commentNode).getData());
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Hello, world!");
        
        treeBuilder.process(character);
        
        // Assert that text node is inserted into the document
        Element element = treeBuilder.getCurrentElement();
        assertNotNull(element);
        Node textNode = element.childNodes().get(0);
        assertNotNull(textNode);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Hello, world!", ((TextNode) textNode).getWholeText());
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        
        treeBuilder.process(doctype);
        
        // Assert that doctype node is inserted into the document
        Element element = treeBuilder.getCurrentElement();
        assertNotNull(element);
        Node doctypeNode = element.childNodes().get(0);
        assertNotNull(doctypeNode);
        assertTrue(doctypeNode instanceof DocumentType);
        assertEquals("html", ((DocumentType) doctypeNode).name());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element div1 = new Element(Tag.valueOf("div"), "", null);
        Element div2 = new Element(Tag.valueOf("div"), "", null);
        treeBuilder.getStack().add(div1);
        treeBuilder.getStack().add(div2);
        Token.EndTag endTag = new Token.EndTag("div");
        
        treeBuilder.popStackToClose(endTag);
        
        // Assert that the element with the given name is removed from the stack
        assertEquals(0, treeBuilder.getStack().size());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "", null);
        TextNode textNode = new TextNode("Hello, world!", "");
        
        treeBuilder.setCurrentElement(element);
        treeBuilder.insertNode(textNode);
        
        // Assert that the text node is appended to the current element
        assertEquals(1, element.childNodes().size());
        assertEquals(textNode, element.childNodes().get(0));
    }
    
    @Test
    public void testInsertStartElement() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        
        treeBuilder.insert(startTag);
        
        // Assert that element is inserted into the document
        Element element = treeBuilder.getCurrentElement();
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("This is a comment");
        
        treeBuilder.insert(comment);
        
        // Assert that comment node is inserted into the document
        Element element = treeBuilder.getCurrentElement();
        assertNotNull(element);
        Node commentNode = element.childNodes().get(0);
        assertNotNull(commentNode);
        assertTrue(commentNode instanceof Comment);
        assertEquals("This is a comment", ((Comment) commentNode).getData());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Hello, world!");
        
        treeBuilder.insert(character);
        
        // Assert that text node is inserted into the document
        Element element = treeBuilder.getCurrentElement();
        assertNotNull(element);
        Node textNode = element.childNodes().get(0);
        assertNotNull(textNode);
        assertTrue(textNode instanceof TextNode);
        assertEquals("Hello, world!", ((TextNode) textNode).getWholeText());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        
        treeBuilder.insert(doctype);
        
        // Assert that doctype node is inserted into the document
        Element element = treeBuilder.getCurrentElement();
        assertNotNull(element);
        Node doctypeNode = element.childNodes().get(0);
        assertNotNull(doctypeNode);
        assertTrue(doctypeNode instanceof DocumentType);
        assertEquals("html", ((DocumentType) doctypeNode).name());
    }
}
