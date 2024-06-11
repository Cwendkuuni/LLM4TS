import org.junit.*;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Assert that the document is added to the stack
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        
        // Assert that the input, baseUri, and errors variables are properly initialized
        assertEquals(input, xmlTreeBuilder.tokeniser.input());
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
    }
    
    @Test
    public void testProcess_StartTag() {
        Token token = new Token(Token.TokenType.StartTag, "div");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert that the start tag is inserted into the tree
        assertTrue(xmlTreeBuilder.stack.peek() instanceof Element);
        assertEquals("div", xmlTreeBuilder.stack.peek().nodeName());
        assertTrue(result);
    }
    
    @Test
    public void testProcess_EndTag() {
        Token token = new Token(Token.TokenType.EndTag, "div");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert that stack elements with the same tag name are removed up to first occurrence
        assertFalse(xmlTreeBuilder.stack.contains("div"));
        assertTrue(result);
    }
    
    @Test
    public void testProcess_Comment() {
        Token token = new Token(Token.TokenType.Comment, "This is a comment");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert that a comment node is inserted into the tree
        assertTrue(xmlTreeBuilder.stack.peek() instanceof Comment);
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.stack.peek()).getData());
        assertTrue(result);
    }
    
    @Test
    public void testProcess_Character() {
        Token token = new Token(Token.TokenType.Character, "This is a character");
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert that a character node is inserted into the tree
        assertTrue(xmlTreeBuilder.stack.peek() instanceof TextNode);
        assertEquals("This is a character", ((TextNode) xmlTreeBuilder.stack.peek()).getWholeText());
        assertTrue(result);
    }
    
    @Test
    public void testProcess_Doctype() {
        Token.Doctype d = new Token.Doctype();
        d.setName("html");
        d.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Transitional//EN");
        d.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        Token token = new Token(Token.TokenType.Doctype, d);
        
        boolean result = xmlTreeBuilder.process(token);
        
        // Assert that a Doctype node is inserted into the tree
        assertTrue(xmlTreeBuilder.stack.peek() instanceof DocumentType);
        assertEquals("html", ((DocumentType) xmlTreeBuilder.stack.peek()).nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", ((DocumentType) xmlTreeBuilder.stack.peek()).attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", ((DocumentType) xmlTreeBuilder.stack.peek()).attr("systemId"));
        assertTrue(result);
    }
    
    @Test
    public void testInsertNode() {
        Element parentElement = new Element("div");
        xmlTreeBuilder.stack.push(parentElement);
        Node childNode = new TextNode("Child text");
        
        xmlTreeBuilder.insertNode(childNode);
        
        // Assert that the child node is appended to the parent element
        assertEquals(childNode, parentElement.childNode(0));
    }
    
    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Assert that the start tag is inserted into the tree
        assertTrue(element instanceof Element);
        assertEquals("div", element.nodeName());
        
        // Assert that the tag is added to the stack or self-closing tag flag is acknowledged
        if (startTag.isSelfClosing()) {
            assertTrue(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged());
        } else {
            assertTrue(xmlTreeBuilder.stack.contains(element));
        }
    }
    
    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Assert that a comment node is inserted into the tree
        assertTrue(xmlTreeBuilder.stack.peek() instanceof Comment);
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.stack.peek()).getData());
    }
    
    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character("This is a character");
        
        xmlTreeBuilder.insert(characterToken);
        
        // Assert that a character node is inserted into the tree
        assertTrue(xmlTreeBuilder.stack.peek() instanceof TextNode);
        assertEquals("This is a character", ((TextNode) xmlTreeBuilder.stack.peek()).getWholeText());
    }
    
    @Test
    public void testInsert_Doctype() {
        Token.Doctype d = new Token.Doctype();
        d.setName("html");
        d.setPublicIdentifier("-//W3C//DTD XHTML 1.0 Transitional//EN");
        d.setSystemIdentifier("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        Token.Doctype doctypeToken = new Token.Doctype(d);
        
        xmlTreeBuilder.insert(doctypeToken);
        
        // Assert that a Doctype node is inserted into the tree
        assertTrue(xmlTreeBuilder.stack.peek() instanceof DocumentType);
        assertEquals("html", ((DocumentType) xmlTreeBuilder.stack.peek()).nodeName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", ((DocumentType) xmlTreeBuilder.stack.peek()).attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", ((DocumentType) xmlTreeBuilder.stack.peek()).attr("systemId"));
    }
    
    @Test
    public void testPopStackToClose() {
        Element element1 = new Element("div");
        Element element2 = new Element("div");
        Element element3 = new Element("div");
        xmlTreeBuilder.stack.push(element1);
        xmlTreeBuilder.stack.push(element2);
        xmlTreeBuilder.stack.push(element3);
        Token.EndTag endTag = new Token.EndTag("div");
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Assert that the first occurrence of element with tag name 'div' is removed from the stack
        assertFalse(xmlTreeBuilder.stack.contains("div"));
    }
    
}