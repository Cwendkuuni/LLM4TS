package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder treeBuilder;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = treeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParse() {
        String input = "<xml><tag>hello world</tag></xml>";
        String baseUri = "http://example.com";
        Document document = treeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals("<xml><tag>hello world</tag></xml>", document.outerHtml());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag>hello world</tag></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        treeBuilder.initialiseParse(input, baseUri, errors, settings);

        List<Node> stack = treeBuilder.getStack();
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Document);

        Document document = (Document) stack.get(0);
        assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "xml", "attr=\"value\"");
        Element element = treeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("tag", element.tagName());
        assertEquals("xml", element.baseUri());
        assertNull(element.parent());
        assertEquals(1, element.siblingIndex());
        assertNotNull(element.attributes());
        assertEquals(1, element.attributes().size());
        assertEquals("value", element.attributes().get("attr"));
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        treeBuilder.insert(endTag);

        List<Node> stack = treeBuilder.getStack();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("This is a comment");
        treeBuilder.insert(comment);

        List<Node> stack = treeBuilder.getStack();
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof Comment);

        Comment insertedComment = (Comment) stack.get(0);
        assertEquals("This is a comment", insertedComment.getData());
        assertNull(insertedComment.parent());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("hello world");
        treeBuilder.insert(character);

        List<Node> stack = treeBuilder.getStack();
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof TextNode);

        TextNode textNode = (TextNode) stack.get(0);
        assertEquals("hello world", textNode.getWholeText());
        assertNull(textNode.parent());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "-//W3C//DTD HTML 4.01//EN", null);
        treeBuilder.insert(doctype);

        List<Node> stack = treeBuilder.getStack();
        assertEquals(1, stack.size());
        assertTrue(stack.get(0) instanceof DocumentType);

        DocumentType documentType = (DocumentType) stack.get(0);
        assertEquals("html", documentType.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.getPublicIdentifier());
        assertNull(documentType.getParent());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element element1 = new Element("div");
        Element element2 = new Element("tag");
        Element element3 = new Element("tag");

        treeBuilder.getStack().add(element1);
        treeBuilder.getStack().add(element2);
        treeBuilder.getStack().add(element3);

        treeBuilder.popStackToClose(endTag);

        List<Node> stack = treeBuilder.getStack();
        assertEquals(1, stack.size());
        assertSame(element1, stack.get(0));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>hello world</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertTrue(node instanceof Element);

        Element element = (Element) node;
        assertEquals("tag", element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertNull(element.parent());
    }
}