import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root>Test</root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(input, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertEquals(baseUri, xmlTreeBuilder.getCurrentElement().baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");

        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals(Tag.valueOf("div"), element.tag());
        assertEquals("div", element.nodeName());
        assertEquals(xmlTreeBuilder.getBaseUri(), element.baseUri());
        assertTrue(element.attributes().isEmpty());
        assertNull(element.parent());
        assertFalse(element.isBlock());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.process(endTag);

        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
        Comment comment = (Comment) childNodes.get(0);
        assertEquals(commentToken.getData(), comment.getData());
        assertEquals(xmlTreeBuilder.getBaseUri(), comment.baseUri());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");

        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals(characterToken.getData(), textNode.getWholeText());
        assertEquals(xmlTreeBuilder.getBaseUri(), textNode.baseUri());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");

        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.getCurrentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) childNodes.get(0);
        assertEquals(doctype.getName(), documentType.getName());
        assertEquals(doctype.getPublicIdentifier(), documentType.getPublicIdentifier());
        assertEquals(doctype.getSystemIdentifier(), documentType.getSystemIdentifier());
        assertEquals(xmlTreeBuilder.getBaseUri(), documentType.baseUri());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Test</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
        Element element = (Element) childNodes.get(0);
        assertEquals(Tag.valueOf("p"), element.tag());
        assertEquals("p", element.nodeName());
        assertEquals(baseUri, element.baseUri());
        assertTrue(element.childNodes().get(0) instanceof TextNode);
        TextNode textNode = (TextNode) element.childNodes().get(0);
        assertEquals("Test", textNode.getWholeText());
        assertEquals(baseUri, textNode.baseUri());
    }

}