import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
        String input = "<html></html>";
        Reader reader = new StringReader(input);
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(reader, baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("<html></html>", doc.toString());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertEquals("<html></html>", doc.toString());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "http://example.com";
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, ParseErrorList.noTracking(), settings);
        assertEquals(baseUri, xmlTreeBuilder.baseUri());
        assertFalse(xmlTreeBuilder.errors().hasErrors());
        assertEquals(settings, xmlTreeBuilder.settings());
    }

    @Test
    public void testProcessStartTag() {
        Tag tag = Tag.valueOf("div", xmlTreeBuilder.settings());
        Token.StartTag startTag = new Token.StartTag(tag, "http://example.com", new Attributes());
        Element el = xmlTreeBuilder.insert(startTag);
        assertEquals(tag, el.tag());
        assertEquals("http://example.com", el.baseUri());
        assertEquals(0, el.attributes().size());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertTrue(el.parent() instanceof Document);
    }

    @Test
    public void testProcessEndTag() {
        Tag tag = Tag.valueOf("div", xmlTreeBuilder.settings());
        Token.StartTag startTag = new Token.StartTag(tag, "http://example.com", new Attributes());
        Element el = xmlTreeBuilder.insert(startTag);
        Token.EndTag endTag = new Token.EndTag(tag);
        xmlTreeBuilder.process(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
        assertFalse(el.parent() instanceof Document);
    }

    @Test
    public void testProcessComment() {
        String data = "comment";
        Token.Comment commentToken = new Token.Comment(data);
        xmlTreeBuilder.insert(commentToken);
        assertEquals(data, xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        String data = "character";
        Token.Character characterToken = new Token.Character(data);
        xmlTreeBuilder.insert(characterToken);
        assertEquals(data, xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        String name = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/loose.dtd";
        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        xmlTreeBuilder.insert(doctype);
        DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(name.toUpperCase(), doctypeNode.name());
        assertEquals(publicIdentifier, doctypeNode.publicIdentifier());
        assertEquals(systemIdentifier, doctypeNode.systemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Tag divTag = Tag.valueOf("div", xmlTreeBuilder.settings());
        Token.StartTag divStartTag = new Token.StartTag(divTag, "http://example.com", new Attributes());
        xmlTreeBuilder.insert(divStartTag);

        Tag spanTag = Tag.valueOf("span", xmlTreeBuilder.settings());
        Token.StartTag spanStartTag = new Token.StartTag(spanTag, "http://example.com", new Attributes());
        xmlTreeBuilder.insert(spanStartTag);

        Token.EndTag divEndTag = new Token.EndTag(divTag);
        xmlTreeBuilder.popStackToClose(divEndTag);
        assertFalse(xmlTreeBuilder.stack.isEmpty());
        assertTrue(xmlTreeBuilder.stack.getLast().tag().equals(spanTag));
    }

    @Test
    public void testParseFragment() {
        String input = "<div>test</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}