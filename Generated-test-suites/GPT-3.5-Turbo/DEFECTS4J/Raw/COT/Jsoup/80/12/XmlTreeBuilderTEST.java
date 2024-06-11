import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(parseSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String xml = "<root><element>value</element></root>";
        Reader reader = new StringReader(xml);
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(document);
        assertEquals(xml, document.outerHtml());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String xml = "<root><element>value</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(xml, baseUri);
        assertNotNull(document);
        assertEquals(xml, document.outerHtml());
        assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        assertNotNull(xmlTreeBuilder.doc);
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc.outputSettings());
    }

    @Test
    public void testProcess() {
        assertTrue(xmlTreeBuilder.process(new Token.StartTag("tag")));
        assertTrue(xmlTreeBuilder.process(new Token.EndTag("tag")));
        assertTrue(xmlTreeBuilder.process(new Token.Comment("comment")));
        assertTrue(xmlTreeBuilder.process(new Token.Character("data")));
        assertTrue(xmlTreeBuilder.process(new Token.Doctype("doctype")));
        assertFalse(xmlTreeBuilder.process(new Token.EOF()));
        assertFalse(xmlTreeBuilder.process(new Token.Unknown()));
    }

    @Test
    public void testInsertNode() {
        Document document = new Document("");
        xmlTreeBuilder.doc = document;
        Element element = new Element("element");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, document.body().child(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes().put("attr", "value");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertTrue(element.hasAttr("attr"));
        assertEquals("value", element.attr("attr"));
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals("<!--comment-->", xmlTreeBuilder.doc.outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);
        assertEquals("data", xmlTreeBuilder.doc.text());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals("<!DOCTYPE doctype>", xmlTreeBuilder.doc.outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        Document document = new Document("");
        Element element1 = new Element("element");
        Element element2 = new Element("element");
        Element element3 = new Element("element");
        document.appendChild(element1);
        document.body().appendChild(element2);
        document.body().appendChild(element3);
        xmlTreeBuilder.doc = document;

        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, document.childNodes().size());
        assertEquals(element1, document.child(0));
    }

    @Test
    public void testParseFragment() {
        String fragment = "<element>value</element>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> nodes = xmlTreeBuilder.parseFragment(fragment, baseUri, errors, settings);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
    }
}