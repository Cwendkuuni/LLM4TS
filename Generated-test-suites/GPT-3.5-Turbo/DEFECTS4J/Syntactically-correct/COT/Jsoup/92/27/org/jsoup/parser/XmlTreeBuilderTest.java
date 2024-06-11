
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        
        // Assert that the default settings are as expected
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        // Call the constructor with sample input
        builder.initialiseParse(reader, baseUri, parser);
        
        // Assert that the stack contains the document
        assertTrue(builder.stack.contains(builder.doc));
        
        // Assert that the document's output settings have been set to XML syntax
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax);
    }
    
    @Test
    public void testParseWithReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(reader, baseUri);
        
        // Assert that the returned document is not null
        assertNotNull(document);
    }
    
    @Test
    public void testParseWithString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document document = builder.parse(input, baseUri);
        
        // Assert that the returned document is not null
        assertNotNull(document);
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", attributes, false);
        
        // Call the method with the start tag token
        boolean result = builder.process(startTag);
        
        // Assert that the method returns true
        assertTrue(result);
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        
        // Call the method with the end tag token
        boolean result = builder.process(endTag);
        
        // Assert that the method returns true
        assertTrue(result);
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        
        // Call the method with the comment token
        boolean result = builder.process(comment);
        
        // Assert that the method returns true
        assertTrue(result);
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        
        // Call the method with the character token
        boolean result = builder.process(character);
        
        // Assert that the method returns true
        assertTrue(result);
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype");
        
        // Call the method with the doctype token
        boolean result = builder.process(doctype);
        
        // Assert that the method returns true
        assertTrue(result);
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Node();
        
        // Call the method with the node
        builder.insertNode(node);
        
        // Assert that the current element has the node as its child
        assertEquals(node, builder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", attributes, true);
        
        // Call the method with the start tag token
        Element element = builder.insert(startTag);
        
        // Assert that the returned element is not null
        assertNotNull(element);
        
        // Assert that the element is appended to the current element
        assertEquals(element, builder.currentElement().childNode(0));
        
        // Assert that the element is added to the stack
        assertTrue(builder.stack.contains(element));
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        
        // Call the method with the comment token
        builder.insert(comment);
        
        // Assert that a comment node is added to the current element
        assertTrue(builder.currentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        
        // Call the method with the character token
        builder.insert(character);
        
        // Assert that a text node is added to the current element
        assertTrue(builder.currentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype");
        
        // Call the method with the doctype token
        builder.insert(doctype);
        
        // Assert that a document type node is added to the current element
        assertTrue(builder.currentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        
        // Call the method with the end tag token
        builder.popStackToClose(endTag);
        
        // Assert that the stack is empty
        assertTrue(builder.stack.isEmpty());
    }
    
    @Test
    public void testParseFragmentWithString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        
        // Assert that the returned list of nodes is not null
        assertNotNull(nodes);
    }
    
    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);
        
        // Assert that the returned list of nodes is not null
        assertNotNull(nodes);
    }
}
