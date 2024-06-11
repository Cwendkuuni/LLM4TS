
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Document document = xmlTreeBuilder.parse(new StringReader("<root></root>"), "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        Document document = xmlTreeBuilder.parse("<root></root>", "http://example.com");
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(new StringReader("<root></root>"), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessWithStartTag() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token token = new Token.StartTag("div", new Attributes());
        xmlTreeBuilder.stack.add(element);
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("div", xmlTreeBuilder.currentElement().children().get(0).nodeName());
    }

    @Test
    public void testProcessWithEndTag() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        Token token = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(element);
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(0, xmlTreeBuilder.currentElement().children().size());
    }

    @Test
    public void testProcessWithComment() {
        Token token = new Token.Comment("This is a comment");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.currentElement().children().get(0).outerHtml());
    }

    @Test
    public void testProcessWithCharacter() {
        Token token = new Token.Character("Hello World");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("Hello World", xmlTreeBuilder.currentElement().children().get(0).outerHtml());
    }

    @Test
    public void testProcessWithDoctype() {
        Token token = new Token.Doctype("html", "public", "system", "pubsys");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("<!DOCTYPE html PUBLIC \"public\" \"system\">", xmlTreeBuilder.currentElement().children().get(0).outerHtml());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(element);
        Node node = new Element(Tag.valueOf("span"));
        xmlTreeBuilder.insertNode(node);
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("span", xmlTreeBuilder.currentElement().children().get(0).nodeName());
    }

    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.nodeName());
        assertTrue(element.children().isEmpty());

        startTag.attributes().put("id", "myDiv");
        element = xmlTreeBuilder.insert(startTag);
        assertEquals(1, element.attributes().size());
        assertEquals("myDiv", element.attributes().get("id"));

        startTag.setSelfClosing();
        element = xmlTreeBuilder.insert(startTag);
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.currentElement().children().get(0).outerHtml());

        commentToken.bogus = true;
        xmlTreeBuilder.insert(commentToken);
        assertEquals(2, xmlTreeBuilder.currentElement().children().size());
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.currentElement().children().get(0).outerHtml());
        assertEquals("<!--This is a comment-->", xmlTreeBuilder.currentElement().children().get(1).outerHtml());
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("Hello World");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("Hello World", xmlTreeBuilder.currentElement().children().get(0).outerHtml());

        characterToken.setCData();
        xmlTreeBuilder.insert(characterToken);
        assertEquals(2, xmlTreeBuilder.currentElement().children().size());
        assertEquals("Hello World", xmlTreeBuilder.currentElement().children().get(0).outerHtml());
        assertTrue(xmlTreeBuilder.currentElement().children().get(1) instanceof CDataNode);
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "public", "system", "pubsys");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, xmlTreeBuilder.currentElement().children().size());
        assertEquals("<!DOCTYPE html PUBLIC \"public\" \"system\">", xmlTreeBuilder.currentElement().children().get(0).outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("div"));
        Element element2 = new Element(Tag.valueOf("span"));
        Element element3 = new Element(Tag.valueOf("div"));
        xmlTreeBuilder.stack.add(element3);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element1);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment() {
        List<Node> childNodes = xmlTreeBuilder.parseFragment("<div>Fragment</div>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, childNodes.size());
        assertEquals("div", childNodes.get(0).nodeName());
        assertEquals("Fragment", childNodes.get(0).child(0).outerHtml());
    }
}
