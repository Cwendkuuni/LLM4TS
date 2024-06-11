
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String baseUri;
    private ParseErrorList parseErrorList;
    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        baseUri = "http://example.com";
        parseErrorList = ParseErrorList.noTracking();
        parseSettings = ParseSettings.preserveCase;
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testParse_WithReaderAndBaseUri() {
        Reader input = new StringReader("<html></html>");
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParse_WithStringAndBaseUri() {
        String input = "<html></html>";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<html></html>");
        xmlTreeBuilder.initialiseParse(input, baseUri, parseErrorList, parseSettings);
        assertNotNull(xmlTreeBuilder.getStack());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("div");
        assertTrue(xmlTreeBuilder.process(token));

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
    }

    @Test
    public void testProcess_EndTag() {
        Element element = new Element(Tag.valueOf("div"), baseUri, parseSettings.normalizeAttributes(new Attributes()));
        xmlTreeBuilder.getStack().add(element);

        Token token = new Token.EndTag("div");
        assertTrue(xmlTreeBuilder.process(token));

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testProcess_Comment() {
        Token token = new Token.Comment("comment");
        assertTrue(xmlTreeBuilder.process(token));

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
    }

    @Test
    public void testProcess_Character() {
        Token token = new Token.Character("data");
        assertTrue(xmlTreeBuilder.process(token));

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("name", "publicId", "systemId");
        assertTrue(xmlTreeBuilder.process(doctype));

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), baseUri, parseSettings.normalizeAttributes(new Attributes()));
        xmlTreeBuilder.getStack().add(element);

        Node node = new Element(Tag.valueOf("span"), baseUri, parseSettings.normalizeAttributes(new Attributes()));
        xmlTreeBuilder.insertNode(node);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag startTag = new Token.StartTag("input", new Attributes(), true);
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertTrue(element.tag().isSelfClosing());
        assertTrue(element.tag().isKnownTag());
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes(), false);
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertFalse(element.tag().isSelfClosing());
        assertTrue(element.tag().isKnownTag());
        assertEquals("div", element.tagName());
    }

    @Test
    public void testInsert_CommentToken_Bogus() {
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");
        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof XmlDeclaration);
    }

    @Test
    public void testInsert_CommentToken_NotBogus() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Comment);
    }

    @Test
    public void testInsert_CharacterToken_TextNode() {
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof TextNode);
    }

    @Test
    public void testInsert_CharacterToken_CDataNode() {
        Token.Character characterToken = new Token.Character("<![CDATA[data]]>");
        xmlTreeBuilder.insert(characterToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof CDataNode);
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose_ElementFound() {
        Element element1 = new Element(Tag.valueOf("div"), baseUri, parseSettings.normalizeAttributes(new Attributes()));
        Element element2 = new Element(Tag.valueOf("span"), baseUri, parseSettings.normalizeAttributes(new Attributes()));
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        List<Element> stack = xmlTreeBuilder.getStack();
        assertEquals(1, stack.size());
        assertEquals(element2, stack.get(0));
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        Element element1 = new Element(Tag.valueOf("span"), baseUri, parseSettings.normalizeAttributes(new Attributes()));
        Element element2 = new Element(Tag.valueOf("div"), baseUri, parseSettings.normalizeAttributes(new Attributes()));
        xmlTreeBuilder.getStack().add(element1);
        xmlTreeBuilder.getStack().add(element2);

        Token.EndTag endTag = new Token.EndTag("p");
        xmlTreeBuilder.popStackToClose(endTag);

        List<Element> stack = xmlTreeBuilder.getStack();
        assertEquals(2, stack.size());
        assertEquals(element2, stack.get(0));
        assertEquals(element1, stack.get(1));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div></div>";
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parseErrorList, parseSettings);

        assertEquals(1, childNodes.size());
        assertTrue(childNodes.get(0) instanceof Element);
    }
}

