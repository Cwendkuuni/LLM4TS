
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList errors;
    private ParseSettings settings;
    private String baseUri;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        errors = ParseErrorList.track(errors);
        settings = xmlTreeBuilder.defaultSettings();
        baseUri = "https://example.com";
    }

    @Test
    public void testParse_withReaderAndBaseUri_shouldReturnDocumentObject() {
        String html = "<xml><element>test</element></xml>";
        Document doc = xmlTreeBuilder.parse(new StringReader(html), baseUri);
        assertTrue(doc != null);
        assertEquals("xml", doc.nodeName());
    }

    @Test
    public void testParse_withStringAndBaseUri_shouldReturnDocumentObject() {
        String html = "<element>test</element>";
        Document doc = xmlTreeBuilder.parse(html, baseUri);
        assertTrue(doc != null);
        assertEquals("element", doc.nodeName());
    }

    @Test
    public void testInitialiseParse_withReaderAndBaseUri_shouldSetDocumentAndOutputSettings() {
        xmlTreeBuilder.initialiseParse(new StringReader("<element>test</element>"), baseUri, errors, settings);
        Document doc = xmlTreeBuilder.doc;
        assertTrue(doc != null);
        assertEquals("element", doc.nodeName());
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_withStartTagToken_shouldInsertNode() {
        Token.StartTag startTag = new Token.StartTag("element");
        xmlTreeBuilder.stack.add(new Element("parent"));

        xmlTreeBuilder.process(startTag);

        Element element = xmlTreeBuilder.currentElement();
        assertTrue(element != null);
        assertTrue(element.childNodeSize() == 1);
        assertEquals("element", element.childNode(0).nodeName());
    }

    @Test
    public void testProcess_withEndTagToken_shouldPopStackToClose() {
        xmlTreeBuilder.stack.add(new Element("parent"));
        xmlTreeBuilder.stack.add(new Element("element"));
        xmlTreeBuilder.stack.add(new Element("other"));

        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.process(endTag);

        List<Element> stack = xmlTreeBuilder.stack;
        assertEquals(1, stack.size());
        assertEquals("parent", stack.get(0).nodeName());
    }

    @Test
    public void testProcess_withCommentToken_shouldInsertNode() {
        Token.Comment comment = new Token.Comment("example comment");
        xmlTreeBuilder.stack.add(new Element("parent"));

        xmlTreeBuilder.process(comment);

        Element parent = xmlTreeBuilder.currentElement();
        assertTrue(parent != null);
        assertTrue(parent.childNodeSize() == 1);
        assertEquals("#comment", parent.childNode(0).nodeName());
    }

    @Test
    public void testProcess_withCharacterToken_shouldInsertTextNode() {
        Token.Character character = new Token.Character("example text");
        xmlTreeBuilder.stack.add(new Element("parent"));

        xmlTreeBuilder.process(character);

        Element parent = xmlTreeBuilder.currentElement();
        assertTrue(parent != null);
        assertTrue(parent.childNodeSize() == 1);
        assertEquals("#text", parent.childNode(0).nodeName());
        assertEquals("example text", parent.childNode(0).toString());
    }

    @Test
    public void testProcess_withDoctypeToken_shouldInsertDoctypeNode() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.stack.add(new Element("parent"));

        xmlTreeBuilder.process(doctype);

        Element parent = xmlTreeBuilder.currentElement();
        assertTrue(parent != null);
        assertTrue(parent.childNodeSize() == 1);
        assertEquals("!DOCTYPE", parent.childNode(0).nodeName());
    }

    @Test
    public void testInsert_withStartTag_shouldInsertElementNodeAndSetSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("element");
        startTag.setSelfClosing(true);

        Element element = xmlTreeBuilder.insert(startTag);

        assertTrue(element != null);
        assertTrue(element.childNodeSize() == 0);
        assertTrue(element.isSelfClosing());
    }

    @Test
    public void testInsert_withCommentToken_shouldInsertCommentNode() {
        Token.Comment comment = new Token.Comment("example comment");

        xmlTreeBuilder.insert(comment);

        Element parent = xmlTreeBuilder.currentElement();
        assertTrue(parent != null);
        assertTrue(parent.childNodeSize() == 1);
        assertEquals("#comment", parent.childNode(0).nodeName());
    }

    @Test
    public void testInsert_withCharacterToken_shouldInsertTextNode() {
        Token.Character character = new Token.Character("example text");

        xmlTreeBuilder.insert(character);

        Element parent = xmlTreeBuilder.currentElement();
        assertTrue(parent != null);
        assertTrue(parent.childNodeSize() == 1);
        assertEquals("#text", parent.childNode(0).nodeName());
        assertEquals("example text", parent.childNode(0).toString());
    }

    @Test
    public void testInsert_withDoctypeToken_shouldInsertDoctypeNode() {
        Token.Doctype doctype = new Token.Doctype("html");

        xmlTreeBuilder.insert(doctype);

        Element parent = xmlTreeBuilder.currentElement();
        assertTrue(parent != null);
        assertTrue(parent.childNodeSize() == 1);
        assertEquals("!DOCTYPE", parent.childNode(0).nodeName());
    }

    @Test
    public void testPopStackToClose_withEndTag_shouldRemoveFirstOccurrenceFromStack() {
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element1);

        Token.EndTag endTag = new Token.EndTag("element1");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element2, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment_withInputFragment_shouldReturnListOfChildNodes() {
        String inputFragment = "<element>test</element>";

        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertTrue(childNodes != null);
        assertEquals(1, childNodes.size());
        assertEquals("element", childNodes.get(0).nodeName());
    }
}

