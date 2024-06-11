
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test case for initialiseParse method
        String input = "<html><body></body></html>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        treeBuilder.initialiseParse(input, baseUri, errors);
        // Assert the stack has the document element
        assertEquals(treeBuilder.stack.peek(), treeBuilder.doc);
    }

    @Test
    public void testProcess() {
        // Test cases for process method with different token types
        // Test StartTag
        Token startTagToken = new Token.StartTag("div");
        boolean result = treeBuilder.process(startTagToken);
        assertTrue(result);
        // Test EndTag
        Token endTagToken = new Token.EndTag("div");
        result = treeBuilder.process(endTagToken);
        assertTrue(result);
        // Test Comment
        Token commentToken = new Token.Comment("Sample comment");
        result = treeBuilder.process(commentToken);
        assertTrue(result);
        // Test Character
        Token characterToken = new Token.Character("Sample text");
        result = treeBuilder.process(characterToken);
        assertTrue(result);
        // Test Doctype
        Token doctypeToken = new Token.Doctype("html", "", "", "");
        result = treeBuilder.process(doctypeToken);
        assertTrue(result);
        // Test EOF
        Token eofToken = new Token.EOF();
        result = treeBuilder.process(eofToken);
        assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        // Test case for insertNode method
        Element element = new Element("div");
        treeBuilder.insertNode(element);
        // Assert the current element has the inserted node as child
        assertEquals(treeBuilder.currentElement().childNode(0), element);
    }

    @Test
    public void testInsertStartTag() {
        // Test case for insert method with StartTag token
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = treeBuilder.insert(startTag);
        // Assert the inserted element has the correct tag name
        assertEquals(element.tagName(), "div");
        // Assert the stack has the inserted element
        assertEquals(treeBuilder.stack.peek(), element);
    }

    @Test
    public void testInsertComment() {
        // Test case for insert method with Comment token
        Token.Comment comment = new Token.Comment("Sample comment");
        treeBuilder.insert(comment);
        // Assert the current element has the inserted comment as child
        assertEquals(treeBuilder.currentElement().childNode(0).nodeName(), "#comment");
    }

    @Test
    public void testInsertCharacter() {
        // Test case for insert method with Character token
        Token.Character character = new Token.Character("Sample text");
        treeBuilder.insert(character);
        // Assert the current element has the inserted text node as child
        assertEquals(treeBuilder.currentElement().childNode(0).nodeName(), "#text");
    }

    @Test
    public void testInsertDoctype() {
        // Test case for insert method with Doctype token
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        treeBuilder.insert(doctype);
        // Assert the current element has the inserted doctype as child
        assertEquals(treeBuilder.currentElement().childNode(0).nodeName(), "#doctype");
    }

    @Test
    public void testPopStackToClose() {
        // Test case for popStackToClose method
        Element divElement = new Element("div");
        treeBuilder.stack.push(divElement);
        treeBuilder.popStackToClose(new Token.EndTag("div"));
        // Assert the stack is empty after calling popStackToClose
        assertTrue(treeBuilder.stack.isEmpty());
    }
}

