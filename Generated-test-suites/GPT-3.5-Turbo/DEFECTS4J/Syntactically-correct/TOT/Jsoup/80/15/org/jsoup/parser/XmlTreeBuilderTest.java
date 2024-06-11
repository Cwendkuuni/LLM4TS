import org.jsoup.Jsoup;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParseReaderAndBaseUri() throws IOException {
        String input = "<html><head><title>Test Page</title></head><body><h1>This is a test</h1></body></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(reader, baseUri);

        // Verify document structure
        assertEquals("html", document.nodeName());
        assertEquals(1, document.children().size());

        Element htmlElement = document.child(0);
        assertEquals("head", htmlElement.child(0).nodeName());
        assertEquals("body", htmlElement.child(1).nodeName());
    }

    @Test
    public void testParseStringAndBaseUri() throws IOException {
        String input = "<html><head><title>Test Page</title></head><body><h1>This is a test</h1></body></html>";
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Verify document structure
        assertEquals("html", document.nodeName());
        assertEquals(1, document.children().size());

        Element htmlElement = document.child(0);
        assertEquals("head", htmlElement.child(0).nodeName());
        assertEquals("body", htmlElement.child(1).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        // Test that initialiseParse method sets the document onto the stack
        String input = "<test><tag></tag></test>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";

        Document document = xmlTreeBuilder.parse(reader, baseUri);

        assertEquals(document, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("class", "test");

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("div", element.nodeName());
        assertEquals(1, element.children().size());
    }

    @Test
    public void testProcessEndTag() {
        // Test that popStackToClose method removes the correct element from the stack
        Token.StartTag startTag1 = new Token.StartTag("div");
        Token.StartTag startTag2 = new Token.StartTag("p");
        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.insert(startTag1);
        xmlTreeBuilder.insert(startTag2);
        xmlTreeBuilder.process(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("p", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a text");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("This is a text", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");

        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");

        xmlTreeBuilder.insertNode(element);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertEquals("div", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<h1>This is a test</h1>";
        String baseUri = "http://example.com";

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("h1", nodes.get(0).nodeName());
    }

}