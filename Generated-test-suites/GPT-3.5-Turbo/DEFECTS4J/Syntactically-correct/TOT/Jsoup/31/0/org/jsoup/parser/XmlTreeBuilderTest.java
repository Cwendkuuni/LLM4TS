import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Verify that the document is placed onto the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("html", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcess_StartTag_InsertsElement() {
        Token.StartTag startTag = new Token.StartTag("div", null);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Verify that the element is inserted as a child of the current element
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }

    @Test
    public void testProcess_StartTag_SelfClosingTag() {
        Token.StartTag startTag = new Token.StartTag("img", null);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Verify that the element is inserted as a child of the current element
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));

        // Verify that the tag is marked as self-closing
        assertEquals(true, startTag.isSelfClosing());
    }

    @Test
    public void testProcess_StartTag_UnknownTag() {
        Token.StartTag startTag = new Token.StartTag("customTag", null);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        // Verify that the element is inserted as a child of the current element
        assertEquals(element, xmlTreeBuilder.currentElement().child(0));

        // Verify that the tag is marked as self-closing for unknown tags
        assertEquals(true, startTag.isSelfClosing());
        assertEquals(true, element.tag().isSelfClosing());
    }

    @Test
    public void testProcess_Comment_InsertsCommentNode() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        // Verify that the comment node is inserted as a child of the current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals(NodeType.COMMENT, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNode(0)).getData());
    }

    @Test
    public void testProcess_Character_InsertsTextNode() {
        Token.Character characterToken = new Token.Character("Some text");
        
        xmlTreeBuilder.insert(characterToken);
        
        // Verify that the text node is inserted as a child of the current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals(NodeType.TEXT, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        assertEquals("Some text", ((TextNode) xmlTreeBuilder.currentElement().childNode(0)).getWholeText());
    }

    @Test
    public void testProcess_Doctype_InsertsDoctypeNode() {
        Token.Doctype doctype = new Token.Doctype("html", null, null);
        
        xmlTreeBuilder.insert(doctype);
        
        // Verify that the doctype node is inserted as a child of the current element
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals(NodeType.DOCTYPE, xmlTreeBuilder.currentElement().childNode(0).nodeType());
        assertEquals("html", ((DocumentType) xmlTreeBuilder.currentElement().childNode(0)).getName());
    }

    @Test
    public void testPopStackToClose_ExistingElement_PopsStack() {
        Token.EndTag endTag = new Token.EndTag("h1");
        
        // Add a test element to the stack
        Element testElement = new Element(Tag.valueOf("h1"), "http://example.com");
        xmlTreeBuilder.stack.add(testElement);

        // Add another element to the stack (not the one we want to pop)
        Element otherElement = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(otherElement);

        xmlTreeBuilder.popStackToClose(endTag);
        
        // Verify that the test element is removed from the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("div", xmlTreeBuilder.stack.peek().nodeName());
    }

    @Test
    public void testPopStackToClose_NonExistingElement_NoAction() {
        Token.EndTag endTag = new Token.EndTag("p");
        
        // Add an element to the stack (not the one we want to pop)
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(element);

        xmlTreeBuilder.popStackToClose(endTag);
        
        // Verify that the stack is not modified
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("div", xmlTreeBuilder.stack.peek().nodeName());
    }

    @Test
    public void testProcess_UnexpectedToken_ThrowsException() {
        Token unexpectedToken = new Token(TokenType.Unknown);
        
        try {
            xmlTreeBuilder.process(unexpectedToken);
        } catch(Exception e) {
            // Verify that a failure exception is thrown
            assertEquals("Unexpected token type: Unknown", e.getMessage());
        }
    }
}