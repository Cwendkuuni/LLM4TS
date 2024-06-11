
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><p>Test</p></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        // Verify the stack contains the document element
        assertEquals("Expecting size 1", 1, xmlTreeBuilder.stack.size());
        // Verify the document element is the same as the one added to the stack
        assertSame("Expecting the same document element", xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcess_StartTag() {
        // Create a token that represents a start tag
        Token token = new Token(Token.TokenType.StartTag, "p");

        // Invoke the process method with the token
        boolean result = xmlTreeBuilder.process(token);

        // Verify that the process method returns true
        assertTrue("Expecting true", result);
        // Verify that the stack now contains an element with the start tag name
        assertEquals("Expecting size 2", 2, xmlTreeBuilder.stack.size());
        // Verify that the new element has the same tag name as the start tag
        assertEquals("Expecting 'p'", "p", xmlTreeBuilder.stack.get(1).nodeName());
    }

    @Test
    public void testProcess_EndTag() {
        // Create a token that represents an end tag
        Token token = new Token(Token.TokenType.EndTag, "p");

        // Add an element to the stack before invoking the process method
        xmlTreeBuilder.stack.add(new Element("p"));

        // Invoke the process method with the token
        boolean result = xmlTreeBuilder.process(token);

        // Verify that the process method returns true
        assertTrue("Expecting true", result);
        // Verify that the stack no longer contains the element with the end tag name
        assertEquals("Expecting size 0", 0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        // Create a token that represents a comment
        Token token = new Token(Token.TokenType.Comment, "Test comment");

        // Invoke the process method with the token
        boolean result = xmlTreeBuilder.process(token);

        // Verify that the process method returns true
        assertTrue("Expecting true", result);
        // Verify that the stack contains a comment node
        assertEquals("Expecting size 1", 1, xmlTreeBuilder.stack.size());
        assertEquals("Expecting 'Test comment'", "Test comment", xmlTreeBuilder.stack.get(0).toString());
    }

    @Test
    public void testProcess_Character() {
        // Create a token that represents a character
        Token token = new Token(Token.TokenType.Character, "Test character");

        // Invoke the process method with the token
        boolean result = xmlTreeBuilder.process(token);

        // Verify that the process method returns true
        assertTrue("Expecting true", result);
        // Verify that the stack contains a text node
        assertEquals("Expecting size 1", 1, xmlTreeBuilder.stack.size());
        assertEquals("Expecting 'Test character'", "Test character", xmlTreeBuilder.stack.get(0).toString());
    }

    @Test
    public void testProcess_Doctype() {
        // Create a token that represents a doctype
        Token token = new Token(Token.TokenType.Doctype, "html");

        // Invoke the process method with the token
        boolean result = xmlTreeBuilder.process(token);

        // Verify that the process method returns true
        assertTrue("Expecting true", result);
        // Verify that the stack contains a document type node
        assertEquals("Expecting size 1", 1, xmlTreeBuilder.stack.size());
        assertEquals("Expecting 'html'", "html", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testInsert_StartTag() {
        // Create a start tag token
        Token.StartTag startTag = new Token.StartTag("p");

        // Invoke the insert method with the start tag token
        Element result = xmlTreeBuilder.insert(startTag);

        // Verify that the method returns an element
        assertNotNull("Expecting non-null value", result);
        // Verify that the stack contains the new element
        assertEquals("Expecting size 2", 2, xmlTreeBuilder.stack.size());
        // Verify that the new element has the same tag name as the start tag
        assertEquals("Expecting 'p'", "p", xmlTreeBuilder.stack.get(1).nodeName());
    }

    @Test
    public void testInsert_Comment() {
        // Create a comment token
        Token.Comment comment = new Token.Comment("Test comment");

        // Invoke the insert method with the comment token
        xmlTreeBuilder.insert(comment);

        // Verify that the stack contains a comment node
        assertEquals("Expecting size 1", 1, xmlTreeBuilder.stack.size());
        assertEquals("Expecting 'Test comment'", "Test comment", xmlTreeBuilder.stack.get(0).toString());
    }

    @Test
    public void testInsert_Character() {
        // Create a character token
        Token.Character character = new Token.Character("Test character");

        // Invoke the insert method with the character token
        xmlTreeBuilder.insert(character);

        // Verify that the stack contains a text node
        assertEquals("Expecting size 1", 1, xmlTreeBuilder.stack.size());
        assertEquals("Expecting 'Test character'", "Test character", xmlTreeBuilder.stack.get(0).toString());
    }

    @Test
    public void testInsert_Doctype() {
        // Create a doctype token
        Token.Doctype doctype = new Token.Doctype("html");

        // Invoke the insert method with the doctype token
        xmlTreeBuilder.insert(doctype);

        // Verify that the stack contains a document type node
        assertEquals("Expecting size 1", 1, xmlTreeBuilder.stack.size());
        assertEquals("Expecting 'html'", "html", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        // Create an end tag token
        Token.EndTag endTag = new Token.EndTag("p");

        // Add an element to the stack before invoking the popStackToClose method
        xmlTreeBuilder.stack.add(new Element("p"));

        // Invoke the popStackToClose method with the end tag token
        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the stack no longer contains the element with the end tag name
        assertEquals("Expecting size 0", 0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToClose_NoMatchingElement() {
        // Create an end tag token
        Token.EndTag endTag = new Token.EndTag("p");

        // Add an element to the stack before invoking the popStackToClose method
        xmlTreeBuilder.stack.add(new Element("div"));

        // Invoke the popStackToClose method with the end tag token
        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the stack still contains the element with a different tag name
        assertEquals("Expecting size 1", 1, xmlTreeBuilder.stack.size());
        assertEquals("Expecting 'div'", "div", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testPopStackToClose_MultipleMatchingElements() {
        // Create an end tag token
        Token.EndTag endTag = new Token.EndTag("p");

        // Add multiple elements to the stack with the same tag name
        xmlTreeBuilder.stack.add(new Element("p"));
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.stack.add(new Element("p"));

        // Invoke the popStackToClose method with the end tag token
        xmlTreeBuilder.popStackToClose(endTag);

        // Verify that the stack no longer contains the first occurrence of the element with the end tag name
        assertEquals("Expecting size 2", 2, xmlTreeBuilder.stack.size());
        assertEquals("Expecting 'p'", "p", xmlTreeBuilder.stack.get(0).nodeName());
        assertEquals("Expecting 'div'", "div", xmlTreeBuilder.stack.get(1).nodeName());
    }
}

