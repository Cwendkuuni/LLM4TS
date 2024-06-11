import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    private String baseUrl;
    private ParseErrorList errors;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = Document.createShell("", baseUrl);
        errors = ParseErrorList.tracking(Validate.DoNothing);
    }
    
    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse("", baseUrl, errors);
        
        // Test the stack contains the document
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(doc, xmlTreeBuilder.getStack().peek());
        
        // Test the document's output settings syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcess_withStartTagToken() {
        // Create a start tag token
        Token.StartTag startTag = new Token.StartTag("tag", "name", new Attributes());
        Token token = Token.createStartTag(startTag);
        
        // Add an existing element to the stack
        Element existingElement = new Element(Tag.valueOf("existingTag"));
        xmlTreeBuilder.getStack().push(existingElement);
        
        // Process the token
        boolean result = xmlTreeBuilder.process(token);
        
        // Test the token is inserted as a new element with the specified tag name
        Node insertedNode = existingElement.childNode(0);
        assertTrue(insertedNode instanceof Element);
        assertEquals("tag", ((Element) insertedNode).tagName());
        
        // Test the existing element is still in the stack
        assertEquals(2, xmlTreeBuilder.getStack().size());
        assertEquals(existingElement, xmlTreeBuilder.getStack().pop());
        
        // Test the result is true
        assertTrue(result);
    }
    
    @Test
    public void testProcess_withEndTagToken() {
        // Create an end tag token
        Token.EndTag endTag = new Token.EndTag("tag", "name");
        Token token = Token.createEndTag(endTag);
        
        // Add an element with the same tag name to the stack
        Element element = new Element(Tag.valueOf("tag"));
        xmlTreeBuilder.getStack().push(element);
        
        // Process the token
        boolean result = xmlTreeBuilder.process(token);
        
        // Test the element with the same tag name is popped from the stack
        assertEquals(0, xmlTreeBuilder.getStack().size());
        
        // Test the result is true
        assertTrue(result);
    }
    
    @Test
    public void testProcess_withCommentToken() {
        // Create a comment token
        Token.Comment commentToken = new Token.Comment("some comment");
        Token token = Token.createComment(commentToken);
        
        // Process the token
        boolean result = xmlTreeBuilder.process(token);
        
        // Test a comment node is inserted with the comment data
        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof Comment);
        assertEquals("some comment", ((Comment) insertedNode).getData());
        
        // Test the result is true
        assertTrue(result);
    }
    
    @Test
    public void testProcess_withCharacterToken() {
        // Create a character token
        Token.Character characterToken = new Token.Character("text");
        Token token = Token.createCharacter(characterToken);
        
        // Process the token
        boolean result = xmlTreeBuilder.process(token);
        
        // Test a text node is inserted with the character data
        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof TextNode);
        assertEquals("text", ((TextNode) insertedNode).getWholeText());
        
        // Test the result is true
        assertTrue(result);
    }
    
    @Test
    public void testProcess_withDoctypeToken() {
        // Create a doctype token
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", baseUrl);
        Token token = Token.createDoctype(doctypeToken);
        
        // Process the token
        boolean result = xmlTreeBuilder.process(token);
        
        // Test a doctype node is inserted with the specified name and baseUri
        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) insertedNode;
        assertEquals("html", doctypeNode.getName());
        assertEquals("", doctypeNode.getPublicIdentifier());
        assertEquals("", doctypeNode.getSystemIdentifier());
        assertEquals(baseUrl, doctypeNode.baseUri());
        
        // Test the result is true
        assertTrue(result);
    }
    
    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"));
        Element currentElement = xmlTreeBuilder.getCurrentElement();
        currentElement.appendChild(element);
        
        // Test the element is inserted into the current element
        assertEquals(element, currentElement.childNode(0));
    }
    
    @Test
    public void testInsert_withStartTagToken() {
        // Create a start tag token
        Token.StartTag startTag = new Token.StartTag("tag", "name", new Attributes());
        Token token = Token.createStartTag(startTag);
        
        // Insert the token
        Element insertedElement = xmlTreeBuilder.insert(token.asStartTag());
        
        // Test the inserted element has the specified tag name
        assertEquals("tag", insertedElement.tagName());
        
        // Test the tokeniser's self-closing flag is acknowledged if the tag is self-closing
        assertTrue(xmlTreeBuilder.getTokeniser().selfClosingFlagAcknowledged);
        
        // Test the tag is set to self-closing if it is unknown
        if (!Tag.isKnownTag("tag")) {
            assertTrue(insertedElement.tag().isSelfClosing());
        }
        
        // Test the element is added to the stack if the tag is not self-closing
        if (!startTag.isSelfClosing()) {
            assertEquals(insertedElement, xmlTreeBuilder.getStack().peek());
        }
    }
    
    @Test
    public void testInsert_withCommentToken() {
        // Create a comment token
        Token.Comment commentToken = new Token.Comment("some comment");
        Token token = Token.createComment(commentToken);
        
        // Insert the token
        xmlTreeBuilder.insert(token.asComment());
        
        // Test a comment node is inserted with the comment data
        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof Comment);
        assertEquals("some comment", ((Comment) insertedNode).getData());
    }
    
    @Test
    public void testInsert_withCharacterToken() {
        // Create a character token
        Token.Character characterToken = new Token.Character("text");
        Token token = Token.createCharacter(characterToken);
        
        // Insert the token
        xmlTreeBuilder.insert(token.asCharacter());
        
        // Test a text node is inserted with the character data
        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof TextNode);
        assertEquals("text", ((TextNode) insertedNode).getWholeText());
    }
    
    @Test
    public void testInsert_withDoctypeToken() {
        // Create a doctype token
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", baseUrl);
        Token token = Token.createDoctype(doctypeToken);
        
        // Insert the token
        xmlTreeBuilder.insert(token.asDoctype());
        
        // Test a doctype node is inserted with the specified name and baseUri
        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) insertedNode;
        assertEquals("html", doctypeNode.getName());
        assertEquals("", doctypeNode.getPublicIdentifier());
        assertEquals("", doctypeNode.getSystemIdentifier());
        assertEquals(baseUrl, doctypeNode.baseUri());
    }
    
    @Test
    public void testPopStackToClose() {
        // Create an end tag token
        Token.EndTag endTag = new Token.EndTag("tag", "name");
        Token token = Token.createEndTag(endTag);
        
        // Add two elements with the same tag name to the stack
        Element element1 = new Element(Tag.valueOf("tag"));
        Element element2 = new Element(Tag.valueOf("tag"));
        xmlTreeBuilder.getStack().push(element1);
        xmlTreeBuilder.getStack().push(element2);
        
        // Pop the stack to close the element with the same tag name as the end tag token
        xmlTreeBuilder.popStackToClose(token.asEndTag());
        
        // Test the first element with the same tag name is popped from the stack
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(element1, xmlTreeBuilder.getStack().pop());
    }
    
    @Test
    public void testParseFragment() {
        // Parse the input fragment
        List<Node> nodes = xmlTreeBuilder.parseFragment("<p>fragment</p>", baseUrl, errors);
        
        // Test the parsed fragment has one child node
        assertEquals(1, nodes.size());
        
        // Test the child node is an element
        Node childNode = nodes.get(0);
        assertTrue(childNode instanceof Element);
        
        // Test the element has the tag name "p"
        Element element = (Element) childNode;
        assertEquals("p", element.tagName());
    }
}