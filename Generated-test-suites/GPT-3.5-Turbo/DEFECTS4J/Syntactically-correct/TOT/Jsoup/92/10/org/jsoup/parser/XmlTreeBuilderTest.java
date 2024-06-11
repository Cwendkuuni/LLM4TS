
package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
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
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testInitialiseParse_withReaderBaseUriParser() {
        // Test empty input
        String input = "";
        String baseUri = "http://example.com";
        Parser parser = new Parser(this);
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parser);
        // Verify stack contains the document
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));

        // Test non-empty input
        input = "<xml></xml>";
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parser);
        // Verify stack contains the document
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParse_withReaderBaseUri() {
        // Test empty input
        String input = "";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        assertNotNull(document);
        assertTrue(document.childNodes().isEmpty());

        // Test non-empty input
        input = "<xml></xml>";
        document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
    }

    @Test
    public void testParse_withInputBaseUri() {
        // Test empty input
        String input = "";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertTrue(document.childNodes().isEmpty());

        // Test non-empty input
        input = "<xml></xml>";
        document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(1, document.childNodes().size());
    }

    @Test
    public void testProcess_withStartTagToken() {
        // Test valid start tag token
        Token token = new Token.StartTag("test", "attr", "value");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Element element = (Element) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("test", element.tagName());
        assertEquals("attr", element.attributes().asList().get(0).getKey());
        assertEquals("value", element.attributes().asList().get(0).getValue());

        // Test empty start tag token
        token = new Token.StartTag("", null, null);
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("", xmlTreeBuilder.currentElement().childNodes().get(1).nodeName());
    }

    @Test
    public void testProcess_withEndTagToken() {
        // Test valid end tag token
        Token token = new Token.EndTag("test");
        assertTrue(xmlTreeBuilder.process(token));
        assertTrue(xmlTreeBuilder.stack.isEmpty());

        // Test end tag token when stack is empty
        token = new Token.EndTag("test");
        assertTrue(xmlTreeBuilder.process(token));

        // Test end tag token that doesn't match any element in the stack
        token = new Token.EndTag("test");
        XmlTreeBuilder.Element element = xmlTreeBuilder.new Element(Tag.valueOf("other"), "http://example.com", null);
        xmlTreeBuilder.stack.add(element);
        assertFalse(xmlTreeBuilder.process(token));
    }

    @Test
    public void testProcess_withCommentToken() {
        // Test valid comment token
        Token token = new Token.Comment("test");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("test", node.toString());
    }

    @Test
    public void testProcess_withCharacterToken() {
        // Test character token
        Token token = new Token.Character("test");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("test", node.toString());

        // Test character token with CDATA
        token = new Token.Character("test", true);
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(1) instanceof CDataNode);
        node = xmlTreeBuilder.currentElement().childNodes().get(1);
        assertEquals("test", node.toString());
    }

    @Test
    public void testProcess_withDoctypeToken() {
        // Test valid doctype token
        Token token = new Token.Doctype("test", "public", "system", "pubSysKey");
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("test", doctype.nodeName());
        assertEquals("public", doctype.publicIdentifier());
        assertEquals("system", doctype.systemIdentifier());
        assertEquals("pubSysKey", doctype.pubSysKey());

        // Test empty doctype token
        token = new Token.Doctype("", null, null, null);
        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("", xmlTreeBuilder.currentElement().childNodes().get(1).nodeName());
    }

    @Test(expected = IllegalStateException.class)
    public void testProcess_withUnexpectedTokenType() {
        // Test unexpected token type
        Token token = new Token.EOF();
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        // Test insert node
        Node node = xmlTreeBuilder.new Element(Tag.valueOf("test"), "http://example.com", null);
        xmlTreeBuilder.currentElement().appendChild(node);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Element element = (Element) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("test", element.tagName());
    }

    @Test
    public void testInsert_withStartTagToken() {
        // Test insert start tag
        Token.StartTag startTag = new Token.StartTag("test", "attr", "value");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("test", element.tagName());
        assertEquals("attr", element.attributes().asList().get(0).getKey());
        assertEquals("value", element.attributes().asList().get(0).getValue());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));

        // Test insert self-closing tag
        startTag = new Token.StartTag("test", "attr", "value", true);
        element = xmlTreeBuilder.insert(startTag);
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_withCommentToken() {
        // Test insert comment token
        Token.Comment commentToken = new Token.Comment("test");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("test", comment.getData());

        // Test insert comment token with bogus xml declaration
        commentToken = new Token.Comment("<?xml version=\"1.0\"?>");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(1) instanceof XmlDeclaration);
        XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlTreeBuilder.currentElement().childNodes().get(1);
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testInsert_withCharacterToken() {
        // Test insert character token
        Token.Character characterToken = new Token.Character("test");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        Node node = xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("test", node.toString());

        // Test insert character token with CDATA
        characterToken = new Token.Character("test", true);
        xmlTreeBuilder.insert(characterToken);
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(1) instanceof CDataNode);
        node = xmlTreeBuilder.currentElement().childNodes().get(1);
        assertEquals("test", node.toString());
    }

    @Test
    public void testInsert_withDoctypeToken() {
        // Test insert doctype token
        Token.Doctype doctypeToken = new Token.Doctype("test", "public", "system", "pubSysKey");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        DocumentType doctype = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("test", doctype.nodeName());
        assertEquals("public", doctype.publicIdentifier());
        assertEquals("system", doctype.systemIdentifier());
        assertEquals("pubSysKey", doctype.pubSysKey());

        // Test insert empty doctype token
        doctypeToken = new Token.Doctype("", null, null, null);
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(2, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("", xmlTreeBuilder.currentElement().childNodes().get(1).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        // Set up stack
        XmlTreeBuilder.Element element1 = xmlTreeBuilder.new Element(Tag.valueOf("test"), "http://example.com", null);
        XmlTreeBuilder.Element element2 = xmlTreeBuilder.new Element(Tag.valueOf("test"), "http://example.com", null);
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);

        // Test pop stack to close
        Token.EndTag endTag = new Token.EndTag("test");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));

        // Test end tag not found in stack
        endTag = new Token.EndTag("other");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void testParseFragment_withInputFragmentBaseUriParser() {
        // Test parse fragment without context
        String inputFragment = "<test></test>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(this);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("test", element.tagName());

        // Test parse fragment with context
        Element context = xmlTreeBuilder.new Element(Tag.valueOf("parent"), "http://example.com", null);
        nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        element = (Element) nodes.get(0);
        assertEquals("test", element.tagName());
        assertEquals(context, element.parent());
    }

    @Test
    public void testParseFragment_withInputFragmentContextBaseUriParser() {
        // Test parse fragment without context
        String inputFragment = "<test></test>";
        Element context = xmlTreeBuilder.new Element(Tag.valueOf("context"), "http://example.com", null);
        String baseUri = "http://example.com";
        Parser parser = new Parser(this);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("test", element.tagName());
        assertEquals(context, element.parent());
    }

    @Test
    public void testParseFragment_withInvalidInputFragment() {
        // Test parse fragment with invalid input
        String inputFragment = "<test>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(this);
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        assertTrue(nodes.isEmpty());
    }
}

