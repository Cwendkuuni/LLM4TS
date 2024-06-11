
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParseWithReader() {
        StringReader reader = new StringReader("test");
        Document document = xmlTreeBuilder.parse(reader, "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseWithString() {
        String input = "test";
        Document document = xmlTreeBuilder.parse(input, "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        StringReader reader = new StringReader("test");
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(reader, "http://example.com", errors, settings);

        List<Node> stack = xmlTreeBuilder.stack();
        assertNotNull(stack);
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Document);

        Document doc = xmlTreeBuilder.doc();
        assertNotNull(doc);
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("test", "attribute");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("test", element.nodeName());
        assertEquals("http://example.com", element.absUrl("attribute"));
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("test");
        xmlTreeBuilder.insert(commentToken);

        List<Node> children = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("test");
        xmlTreeBuilder.insert(characterToken);

        List<Node> children = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);

        List<Node> children = xmlTreeBuilder.currentElement().childNodes();
        assertNotNull(children);
        assertEquals(1, children.size());
        assertTrue(children.get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("test");
        xmlTreeBuilder.popStackToClose(endTag);

        List<Element> stack = xmlTreeBuilder.stack();
        assertNotNull(stack);
        assertEquals(0, stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<b>hello</b>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}

