package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFragmentEmpty() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragmentXMLDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<root>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertNotNull(nodes);
        assertEquals(2, nodes.size());

        Node firstNode = nodes.get(0);
        assertNotNull(firstNode);
        assertEquals(Xml.XmlDeclaration.class, firstNode.getClass());

        Node secondNode = nodes.get(1);
        assertNotNull(secondNode);
        Element root = (Element) secondNode;
        assertEquals("root", root.nodeName());
    }

    @Test
    public void testParseFragmentRootElementWithAttributes() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root att1=\"val1\" att2=\"val2\"/>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertNotNull(node);
        Element root = (Element) node;
        assertEquals("root", root.nodeName());
        assertEquals(2, root.attributeSize());
        assertEquals("val1", root.attribute("att1"));
        assertEquals("val2", root.attribute("att2"));
    }

    @Test
    public void testParseFragmentElementWithChildren() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root><child1>Text 1</child1><child2><grandchild>Text 3</grandchild></child2></root>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node rootNode = nodes.get(0);
        assertNotNull(rootNode);
        Element root = (Element) rootNode;
        assertEquals("root", root.nodeName());
        assertEquals(2, root.childNodeSize());

        Node child1Node = root.childNode(0);
        assertNotNull(child1Node);
        Element child1 = (Element) child1Node;
        assertEquals("child1", child1.nodeName());
        assertEquals(Text.class, child1.childNode(0).getClass());
        assertEquals("Text 1", child1.childNode(0).text());

        Node child2Node = root.childNode(1);
        assertNotNull(child2Node);
        Element child2 = (Element) child2Node;
        assertEquals("child2", child2.nodeName());
        assertEquals(1, child2.childNodeSize());

        Node grandchildNode = child2.childNode(0);
        assertNotNull(grandchildNode);
        Element grandchild = (Element) grandchildNode;
        assertEquals("grandchild", grandchild.nodeName());
        assertEquals(Text.class, grandchild.childNode(0).getClass());
        assertEquals("Text 3", grandchild.childNode(0).text());
    }

    @Test
    public void testParseFragmentSelfClosingTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<img src=\"image.png\" />";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertNotNull(node);
        Element img = (Element) node;
        assertEquals("img", img.nodeName());
        assertFalse(img.isTagClosed());
    }

    @Test
    public void testParseFragmentEmptyComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<!-- -->";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertNotNull(node);
        assertEquals(Comment.class, node.getClass());
        assertEquals("", ((Comment) node).data());
    }

    @Test
    public void testParseFragmentCommentWithData() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<!-- This is a comment -->";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertNotNull(nodes);
        assertEquals(1, nodes.size());

        Node node = nodes.get(0);
        assertNotNull(node);
        assertEquals(Comment.class, node.getClass());
        assertEquals("This is a comment", ((Comment) node).data());
    }

    @Test
    public void testParseFragmentEndTagWithMismatchedStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<root><child1><child2></child2></child1></child2>";
        String baseUri = "https://example.com/";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertNotNull(nodes);
        assertEquals(0, nodes.size());
        assertFalse(errors.isEmpty());
    }
}