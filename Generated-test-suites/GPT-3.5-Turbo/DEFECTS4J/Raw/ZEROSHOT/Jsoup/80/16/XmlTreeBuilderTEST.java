import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader reader = new StringReader("<test>123</test>");
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(document);
        assertEquals("<test>123</test>", document.toString());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<test>123</test>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("<test>123</test>", document.toString());
    }

    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("<test>123</test>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.getStack());
        assertTrue(xmlTreeBuilder.getStack().size() > 0);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("test", "name", "value");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("test", element.tagName());
        assertEquals("name", element.attr("name"));
        assertEquals("value", element.attr("value"));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("test");
        Element element = new Element(Tag.valueOf("test"), "http://example.com");
        xmlTreeBuilder.getStack().add(element);
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessCommentToken() {
        Token.Comment commentToken = new Token.Comment("test comment");
        xmlTreeBuilder.insert(commentToken);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertTrue(childNodes.get(0) instanceof Comment);
        Comment comment = (Comment) childNodes.get(0);
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testProcessCharacterToken() {
        Token.Character characterToken = new Token.Character("123");
        xmlTreeBuilder.insert(characterToken);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertTrue(childNodes.get(0) instanceof TextNode);
        TextNode textNode = (TextNode) childNodes.get(0);
        assertEquals("123", textNode.text());
    }

    @Test
    public void testProcessDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("test", "public", "system");
        xmlTreeBuilder.insert(doctypeToken);
        List<Node> childNodes = xmlTreeBuilder.getDoc().childNodes();
        assertTrue(childNodes.get(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) childNodes.get(0);
        assertEquals("test", doctype.name());
        assertEquals("public", doctype.publicId());
        assertEquals("system", doctype.systemId());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<test>123</test>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("test", element.tagName());
        assertEquals("123", element.text());
    }

}