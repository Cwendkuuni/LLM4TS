
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml>Test</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Check that the document is added to the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        
        // Check that the output settings syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessStartTag() {
        // Create a StartTag token
        Token.StartTag startTag = new Token.StartTag("p", new Attributes());
        
        // Call the insert method with the StartTag token
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Check that the element is inserted into the current element
        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
        
        // Check that the element is added to the stack
        assertEquals(element, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
    }
    
    @Test
    public void testProcessEndTag() {
        // Create an EndTag token
        Token.EndTag endTag = new Token.EndTag("p");
        
        // Add an element to the stack
        Element element = new Element("p");
        xmlTreeBuilder.stack.add(element);
        
        // Call the popStackToClose method with the EndTag token
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Check that the element is removed from the stack
        assertEquals(0, xmlTreeBuilder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        // Create a Comment token
        Token.Comment commentToken = new Token.Comment("Test");
        
        // Call the insert method with the Comment token
        xmlTreeBuilder.insert(commentToken);
        
        // Check that the comment is inserted into the current element
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Test", comment.getData());
        assertEquals(xmlTreeBuilder.baseUri, comment.baseUri());
    }
    
    @Test
    public void testProcessCharacter() {
        // Create a Character token
        Token.Character characterToken = new Token.Character("Test");
        
        // Call the insert method with the Character token
        xmlTreeBuilder.insert(characterToken);
        
        // Check that the text node is inserted into the current element
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Test", textNode.getWholeText());
        assertEquals(xmlTreeBuilder.baseUri, textNode.baseUri());
    }
    
    @Test
    public void testProcessDoctype() {
        // Create a Doctype token
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        
        // Call the insert method with the Doctype token
        xmlTreeBuilder.insert(doctype);
        
        // Check that the doctype node is inserted into the current element
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", doctypeNode.getName());
        assertEquals(xmlTreeBuilder.baseUri, doctypeNode.baseUri());
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Test</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        // Check that the fragment is parsed correctly
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.nodeName());
        assertEquals("Test", element.text());
        assertEquals(baseUri, element.baseUri());
    }
}

