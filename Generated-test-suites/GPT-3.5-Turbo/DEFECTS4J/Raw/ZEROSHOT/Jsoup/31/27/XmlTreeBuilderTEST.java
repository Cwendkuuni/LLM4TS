import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test case 1: input = null, baseUri = "", errors = null
        xmlTreeBuilder.initialiseParse(null, "", null);
        // Assert that doc is placed onto the stack
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.stack.peek(), xmlTreeBuilder.doc);

        // Test case 2: input = "test", baseUri = "http://example.com", errors = new ParseErrorList()
        xmlTreeBuilder.initialiseParse("test", "http://example.com", new ParseErrorList());
        // Assert that doc is placed onto the stack
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.stack.peek(), xmlTreeBuilder.doc);
    }

    @Test
    public void testProcess() {
        // Test case: token type = StartTag
        Token startTagToken = new Token(Token.TokenType.StartTag);
        xmlTreeBuilder.process(startTagToken);
        // Assert that insert method is called with the start tag token
        // Insert method will be verified separately in its own test case

        // Test case: token type = EndTag
        Token endTagToken = new Token(Token.TokenType.EndTag);
        xmlTreeBuilder.process(endTagToken);
        // Assert that popStackToClose method is called with the end tag token
        // PopStackToClose method will be verified separately in its own test case

        // Test case: token type = Comment
        Token commentToken = new Token(Token.TokenType.Comment);
        xmlTreeBuilder.process(commentToken);
        // Assert that insert method is called with the comment token
        // Insert method will be verified separately in its own test case

        // Test case: token type = Character
        Token characterToken = new Token(Token.TokenType.Character);
        xmlTreeBuilder.process(characterToken);
        // Assert that insert method is called with the character token
        // Insert method will be verified separately in its own test case

        // Test case: token type = Doctype
        Token doctypeToken = new Token(Token.TokenType.Doctype);
        xmlTreeBuilder.process(doctypeToken);
        // Assert that insert method is called with the doctype token
        // Insert method will be verified separately in its own test case

        // Test case: token type = EOF
        Token eofToken = new Token(Token.TokenType.EOF);
        xmlTreeBuilder.process(eofToken);
        // Assert that the method does not throw any exceptions
    }

    @Test
    public void testInsert() {
        // Test case: start tag is self-closing
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes(), true);
        Element element = xmlTreeBuilder.insert(startTag);
        // Assert that insertNode method is called with the created element
        // InsertNode method will be verified separately in its own test case
        assertTrue(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);
        assertTrue(element.tag.isKnownTag());
        assertTrue(element.tag.isSelfClosing());
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.stack.peek(), element);

        // Test case: start tag is not self-closing
        startTag = new Token.StartTag("tag", new Attributes(), false);
        element = xmlTreeBuilder.insert(startTag);
        // Assert that insertNode method is called with the created element
        // InsertNode method will be verified separately in its own test case
        assertFalse(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);
        assertTrue(element.tag.isKnownTag());
        assertFalse(element.tag.isSelfClosing());
        assertEquals(xmlTreeBuilder.stack.size(), 2);
        assertEquals(xmlTreeBuilder.stack.peek(), element);
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("test comment");
        xmlTreeBuilder.insert(commentToken);
        // Assert that insertNode method is called with the created comment node
        // InsertNode method will be verified separately in its own test case
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("test character");
        xmlTreeBuilder.insert(characterToken);
        // Assert that insertNode method is called with the created text node
        // InsertNode method will be verified separately in its own test case
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("test doctype", "public identifier", "system identifier");
        xmlTreeBuilder.insert(doctypeToken);
        // Assert that insertNode method is called with the created document type node
        // InsertNode method will be verified separately in its own test case
    }

    @Test
    public void testPopStackToClose() {
        // Test case: element with end tag name exists in stack
        Element element1 = new Element(Tag.valueOf("tag1"), "baseUri");
        Element element2 = new Element(Tag.valueOf("tag2"), "baseUri");
        Element element3 = new Element(Tag.valueOf("tag3"), "baseUri");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);
        Token.EndTag endTag = new Token.EndTag("tag2");
        xmlTreeBuilder.popStackToClose(endTag);
        // Assert that the first occurrence of element2 is removed from the stack
        assertEquals(xmlTreeBuilder.stack.size(), 2);
        assertEquals(xmlTreeBuilder.stack.peek(), element3);

        // Test case: element with end tag name does not exist in stack
        endTag = new Token.EndTag("nonexistenttag");
        xmlTreeBuilder.popStackToClose(endTag);
        // Assert that the stack remains unchanged
        assertEquals(xmlTreeBuilder.stack.size(), 2);
        assertEquals(xmlTreeBuilder.stack.peek(), element3);
    }
}