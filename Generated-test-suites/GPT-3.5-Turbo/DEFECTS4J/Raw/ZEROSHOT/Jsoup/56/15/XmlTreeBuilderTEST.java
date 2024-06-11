import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private ParseSettings parseSettings;
    private Document doc;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        parseSettings = xmlTreeBuilder.defaultSettings();
        doc = Jsoup.parse("<html></html>");
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();

        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParse() {
        String input = "<html></html>";
        String baseUri = "http://example.com";

        Document parsedDoc = xmlTreeBuilder.parse(input, baseUri);

        assertEquals(doc, parsedDoc);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, parseSettings);

        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "testDiv");

        xmlTreeBuilder.process(startTag);

        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
        assertEquals("testDiv", xmlTreeBuilder.currentElement().attr("id"));
    }

    @Test
    public void testProcessEndTag() {
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.stack.add(element);

        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.process(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("Test comment");

        xmlTreeBuilder.process(comment);

        assertEquals("Test comment", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("Test character");

        xmlTreeBuilder.process(character);

        assertEquals("Test character", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", null, null);

        xmlTreeBuilder.process(doctype);

        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().outerHtml());
    }

    @Test
    public void testInsertNode() {
        Node node = new TextNode("Test node", "http://example.com");

        xmlTreeBuilder.insertNode(node);

        assertEquals("Test node", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "testDiv");
        Tag tag = Tag.valueOf("div", parseSettings);

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals(tag, element.tag());
        assertEquals(baseUri, element.baseUri());
        assertEquals("testDiv", element.attr("id"));
        assertFalse(element.isSelfClosing());
        assertEquals(element, xmlTreeBuilder.stack.get(0));
    }
    
    @Test
    public void testInsertStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "testDiv");
        startTag.selfClosing = true;
        Tag tag = Tag.valueOf("div", parseSettings);
        tag.setSelfClosing();
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(tag, element.tag());
        assertEquals(baseUri, element.baseUri());
        assertEquals("testDiv", element.attr("id"));
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("Test comment");

        xmlTreeBuilder.insert(comment);

        assertEquals("<!--Test comment-->", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("Test character");

        xmlTreeBuilder.insert(character);

        assertEquals("Test character", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");

        xmlTreeBuilder.insert(doctype);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", xmlTreeBuilder.currentElement().outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element(Tag.valueOf("div"), "http://example.com");
        Element element2 = new Element(Tag.valueOf("span"), "http://example.com");
        Element element3 = new Element(Tag.valueOf("div"), "http://example.com");

        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(element2, xmlTreeBuilder.stack.get(0));
        assertEquals(element3, xmlTreeBuilder.stack.get(1));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div class=\"test\"><p>Test fragment</p></div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, parseSettings);

        assertEquals(1, nodes.size());
        assertEquals("<div class=\"test\"><p>Test fragment</p></div>", nodes.get(0).outerHtml());
    }
}