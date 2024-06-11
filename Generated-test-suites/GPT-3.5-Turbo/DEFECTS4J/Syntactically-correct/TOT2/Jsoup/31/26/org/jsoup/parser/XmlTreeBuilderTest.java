import org.jsoup.parser.*;
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
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "root");
    }

    @Test
    public void testProcess() {
        Token startTagToken = new Token(Token.TokenType.StartTag, "<div>");
        assertTrue(xmlTreeBuilder.process(startTagToken));
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "div");

        Token endTagToken = new Token(Token.TokenType.EndTag, "</div>");
        assertTrue(xmlTreeBuilder.process(endTagToken));
        assertNull(xmlTreeBuilder.currentElement());

        Token commentToken = new Token(Token.TokenType.Comment, "<!-- comment -->");
        assertTrue(xmlTreeBuilder.process(commentToken));

        Token characterToken = new Token(Token.TokenType.Character, "text");
        assertTrue(xmlTreeBuilder.process(characterToken));

        Token doctypeToken = new Token(Token.TokenType.Doctype, "<!DOCTYPE html>");
        assertTrue(xmlTreeBuilder.process(doctypeToken));

        Token eofToken = new Token(Token.TokenType.EOF, "");
        assertTrue(xmlTreeBuilder.process(eofToken));

        Token invalidToken = new Token(Token.TokenType.Unknown, "");
        try {
            xmlTreeBuilder.process(invalidToken);
            fail("Expected Validate.fail to be called");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Unexpected token type"));
        }
    }

    @Test
    public void testInsertNode() {
        Element parentElement = new Element(Tag.valueOf("parent"), "http://example.com");
        Element childElement = new Element(Tag.valueOf("child"), "http://example.com");
        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.insertNode(childElement);
        assertEquals(parentElement.childNode(0), childElement);
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag("<div>");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals(element.nodeName(), "div");

        Token.StartTag selfClosingTag = new Token.StartTag("<img/>");
        Element selfClosingElement = xmlTreeBuilder.insert(selfClosingTag);
        assertTrue(selfClosingElement.tag().isSelfClosing());

        Token.Comment commentToken = new Token.Comment("comment");
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(comment.getText(), "comment");

        Token.Character characterToken = new Token.Character("text");
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(1);
        assertEquals(textNode.text(), "text");

        Token.Doctype doctypeToken = new Token.Doctype("<!DOCTYPE html>");
        DocumentType doctype = (DocumentType) xmlTreeBuilder.currentElement().childNode(2);
        assertEquals(doctype.nodeName(), "html");
    }

    @Test
    public void testPopStackToClose() {
        Element parentElement = new Element(Tag.valueOf("parent"), "http://example.com");
        Element childElement = new Element(Tag.valueOf("child"), "http://example.com");
        xmlTreeBuilder.stack.add(parentElement);
        xmlTreeBuilder.stack.add(childElement);
        
        Token.EndTag endTag = new Token.EndTag("</child>");
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.currentElement().nodeName(), "parent");
    }
}