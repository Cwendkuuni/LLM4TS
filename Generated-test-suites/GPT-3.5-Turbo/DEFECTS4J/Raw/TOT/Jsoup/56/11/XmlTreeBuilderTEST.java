import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParse() {
        String input = "<root><child>data</child></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("root", document.nodeName());
        assertEquals(1, document.childNodeSize());
        assertEquals("child", document.child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child>data</child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(false, false);
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(0, errors.size());
        assertEquals(settings, xmlTreeBuilder.settings());
    }

    @Test
    public void testProcess() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("root");
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("root");
        xmlTreeBuilder.process(startTag);
        xmlTreeBuilder.process(endTag);
        Element rootElement = xmlTreeBuilder.currentElement();
        assertNotNull(rootElement);
        assertEquals(0, xmlTreeBuilder.stack.size());
        assertEquals("root", rootElement.nodeName());
    }

    @Test
    public void testInsertNode() {
        Element rootElement = new Element("root");
        Element childElement = new Element("child");
        xmlTreeBuilder.stack.add(rootElement);
        xmlTreeBuilder.insertNode(childElement);
        assertEquals(1, rootElement.childNodeSize());
        assertEquals(childElement, rootElement.child(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("root");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("root", element.nodeName());
        assertEquals(1, element.attributes().size());
        assertEquals("", element.baseUri());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Element rootElement = xmlTreeBuilder.currentElement();
        assertEquals(1, rootElement.childNodeSize());
        assertEquals("<!--This is a comment-->", rootElement.child(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("data");
        xmlTreeBuilder.insert(characterToken);
        Element rootElement = xmlTreeBuilder.currentElement();
        assertEquals(1, rootElement.childNodeSize());
        assertEquals("data", rootElement.child(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        xmlTreeBuilder.insert(doctype);
        Element rootElement = xmlTreeBuilder.currentElement();
        assertEquals(1, rootElement.childNodeSize());
        assertEquals("<!DOCTYPE html>", rootElement.child(0).toString());
    }

    @Test
    public void testPopStackToClose() {
        Token.StartTag startTag1 = new Token.StartTag();
        startTag1.name("root");
        Token.StartTag startTag2 = new Token.StartTag();
        startTag2.name("child");
        xmlTreeBuilder.insert(startTag1);
        xmlTreeBuilder.insert(startTag2);
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("root");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("child", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<child>data</child>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(false, false);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(1, nodes.size());
        assertEquals("child", nodes.get(0).nodeName());
        assertEquals("data", ((Element) nodes.get(0)).text());
    }
}