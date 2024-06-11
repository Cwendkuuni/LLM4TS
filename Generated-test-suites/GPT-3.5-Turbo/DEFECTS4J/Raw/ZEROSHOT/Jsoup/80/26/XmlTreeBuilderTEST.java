import org.jsoup.helper.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Test
    void testDefaultSettings() {
        xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    void testParseReaderString() {
        xmlTreeBuilder = new XmlTreeBuilder();
        Reader reader = new StringReader("<xml><tag>Test</tag></xml>");
        Document document = xmlTreeBuilder.parse(reader, "https://example.com");
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    void testParseStringString() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag>Test</tag></xml>";
        Document document = xmlTreeBuilder.parse(input, "https://example.com");
        assertNotNull(document);
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    void testInitialiseParse() {
        xmlTreeBuilder = new XmlTreeBuilder();
        Reader reader = new StringReader("<xml><tag>Test</tag></xml>");
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(reader, "https://example.com", errors, settings);
        Document document = xmlTreeBuilder.getDocument();
        assertNotNull(document);
        assertNotNull(xmlTreeBuilder.getStack());
    }

    @Test
    void testProcess() {
        xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("tag", null, null);
        assertTrue(xmlTreeBuilder.process(startTagToken));
        Token endTagToken = new Token.EndTag("tag");
        assertTrue(xmlTreeBuilder.process(endTagToken));
        Token commentToken = new Token.Comment("Test Comment");
        assertTrue(xmlTreeBuilder.process(commentToken));
        Token characterToken = new Token.Character("Test Character");
        assertTrue(xmlTreeBuilder.process(characterToken));
        Token doctypeToken = new Token.Doctype("doctype");
        assertTrue(xmlTreeBuilder.process(doctypeToken));
        Token eofToken = new Token.EOF();
        assertTrue(xmlTreeBuilder.process(eofToken));
    }

    @Test
    void testInsertNode() {
        xmlTreeBuilder = new XmlTreeBuilder();
        Document document = new Document("");
        xmlTreeBuilder.setDocument(document);
        Element element = new Element("tag");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, document.body().child(0));
    }

    @Test
    void testInsertStartTag() {
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.setBaseUri("https://example.com");
        Token.StartTag startTag = new Token.StartTag("tag", null, null);
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals(element, xmlTreeBuilder.currentElement());
        assertFalse(element.isKnownTag());
    }

    @Test
    void testInsertComment() {
        xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("Test Comment");
        xmlTreeBuilder.insert(commentToken);
        Node commentNode = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Test Comment", commentNode.outerHtml());
    }

    @Test
    void testInsertCharacter() {
        xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Test Character");
        xmlTreeBuilder.insert(characterToken);
        Node characterNode = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("Test Character", characterNode.outerHtml());
    }

    @Test
    void testInsertDoctype() {
        xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctypeToken);
        Node doctypeNode = xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("doctype", ((DocumentType) doctypeNode).getName());
    }

    @Test
    void testPopStackToClose() {
        xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.setBaseUri("https://example.com");

        Element element1 = new Element("tag");
        xmlTreeBuilder.getStack().add(element1);
        Element element2 = new Element("tag");
        xmlTreeBuilder.getStack().add(element2);

        Token.EndTag endTagToken = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTagToken);
        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    void testParseFragment() {
        xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<tag>Test Fragment</tag>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "https://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("tag", ((Element) nodes.get(0)).tagName());
    }
}