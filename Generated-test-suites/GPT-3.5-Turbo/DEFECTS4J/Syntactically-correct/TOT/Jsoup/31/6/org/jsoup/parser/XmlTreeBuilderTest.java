
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Typical case
        String input = "<root>Test</root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "root");

        // Edge case: Empty input
        input = "";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "root");

        // Error scenario: Invalid input
        input = "<root>Test";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(errors.size(), 1);
    }

    @Test
    public void testProcess() {
        // Typical case: StartTag
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        xmlTreeBuilder.process(startTag);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "div");

        // Typical case: EndTag
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        xmlTreeBuilder.process(endTag);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "root");

        // Typical case: Comment
        Token.Comment comment = new Token.Comment();
        comment.data("Test comment");
        xmlTreeBuilder.process(comment);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0).nodeName(), "#comment");

        // Typical case: Character
        Token.Character character = new Token.Character();
        character.data("Test character");
        xmlTreeBuilder.process(character);
        assertEquals(xmlTreeBuilder.currentElement().childNode(1).nodeName(), "#text");

        // Typical case: Doctype
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("");
        doctype.systemIdentifier("");
        xmlTreeBuilder.process(doctype);
        assertEquals(xmlTreeBuilder.currentElement().childNode(2).nodeName(), "#doctype");

        // Edge case: EOF
        Token token = new Token();
        token.type(Token.TokenType.EOF);
        xmlTreeBuilder.process(token);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "root");

        // Error scenario: Unexpected token type
        Token unexpectedToken = new Token();
        unexpectedToken.type(Token.TokenType.StartTag); // Assuming this is unexpected
        Validate.fail();
        xmlTreeBuilder.process(unexpectedToken); // This line won't be reached due to Validate.fail()
    }

    @Test
    public void testInsertNode() {
        // Typical case
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), element);

        // Edge case: Node is a Comment
        Comment comment = new Comment("Test comment");
        xmlTreeBuilder.insertNode(comment);
        assertEquals(xmlTreeBuilder.currentElement().childNode(1), comment);

        // Edge case: Node is a TextNode
        TextNode textNode = new TextNode("Test text");
        xmlTreeBuilder.insertNode(textNode);
        assertEquals(xmlTreeBuilder.currentElement().childNode(2), textNode);

        // Edge case: Node is a DocumentType
        DocumentType documentType = new DocumentType("html");
        xmlTreeBuilder.insertNode(documentType);
        assertEquals(xmlTreeBuilder.currentElement().childNode(3), documentType);
    }

    @Test
    public void testInsert() {
        // Typical case: StartTag
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals(element.nodeName(), "div");

        // Typical case: Self-closing StartTag
        startTag.attributes.put("id", "test");
        startTag.selfClosing(true);
        element = xmlTreeBuilder.insert(startTag);
        assertEquals(element.nodeName(), "div");
        assertEquals(element.attr("id"), "test");

        // Typical case: Comment
        Token.Comment comment = new Token.Comment();
        comment.data("Test comment");
        xmlTreeBuilder.insert(comment);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0).nodeName(), "#comment");

        // Typical case: Character
        Token.Character character = new Token.Character();
        character.data("Test character");
        xmlTreeBuilder.insert(character);
        assertEquals(xmlTreeBuilder.currentElement().childNode(1).nodeName(), "#text");

        // Typical case: Doctype
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("");
        doctype.systemIdentifier("");
        xmlTreeBuilder.insert(doctype);
        assertEquals(xmlTreeBuilder.currentElement().childNode(2).nodeName(), "#doctype");
    }

    @Test
    public void testPopStackToClose() {
        // Typical case: Element found
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        xmlTreeBuilder.process(new Token.StartTag().name("div")); // Add an element to the stack
        xmlTreeBuilder.process(new Token.StartTag().name("p")); // Add another element to the stack
        xmlTreeBuilder.process(new Token.StartTag().name("div")); // Add another element to the stack
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(xmlTreeBuilder.stack.size(), 1); // Only one element should be left in the stack

        // Edge case: Element not found
        endTag.name("span");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(xmlTreeBuilder.stack.size(), 1); // No elements should be removed from the stack

        // Edge case: Stack is empty
        xmlTreeBuilder.stack.clear(); // Clear the stack
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(xmlTreeBuilder.stack.size(), 0); // Stack should remain empty
    }
}
