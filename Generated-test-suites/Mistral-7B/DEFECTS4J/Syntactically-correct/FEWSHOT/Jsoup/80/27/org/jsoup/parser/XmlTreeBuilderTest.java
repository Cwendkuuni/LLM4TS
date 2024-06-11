package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlInput = "<root><child1>Hello World</child1><child2><subchild>Test</subchild></child2></root>";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

        Element root = xmlDoc.getRootElement();
        assertEquals("root", root.tagName());

        List<Node> children = root.children();
        assertEquals(2, children.size());

        Element child1 = (Element) children.get(0);
        assertEquals("child1", child1.tagName());
        Text textNode = (Text) child1.child(0);
        assertEquals("Hello World", textNode.text());

        Element child2 = (Element) children.get(1);
        assertEquals("child2", child2.tagName());

        List<Node> child2Children = child2.children();
        assertEquals(1, child2Children.size());

        Element subchild = (Element) child2Children.get(0);
        assertEquals("subchild", subchild.tagName());
        Text subchildText = (Text) subchild.child(0);
        assertEquals("Test", subchildText.text());
    }

    @Test
    public void testProcessStartTag() {
        String xmlInput = "<root><child1><subchild></subchild></child1></root>";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Test processStartTag for Root tag
        Token rootToken = new Token.StartTag("root");
        Element root = xmlTreeBuilder.insert(rootToken);
        assertEquals("root", root.tagName());

        // Test processStartTag for Child1 tag
        Token child1Token = new Token.StartTag("child1");
        Element child1 = xmlTreeBuilder.insert(child1Token);
        assertEquals("child1", child1.tagName());
        assertEquals(root, child1.parent());

        // Test processStartTag for Subchild tag
        Token subchildToken = new Token.StartTag("subchild");
        Element subchild = xmlTreeBuilder.insert(subchildToken);
        assertEquals("subchild", subchild.tagName());
        assertEquals(child1, subchild.parent());
    }

    @Test
    public void testProcessComment() {
        String xmlInput = "<!-- This is a comment -->";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Token commentToken = new Token.Comment("This is a comment");
        Comment comment = xmlTreeBuilder.insert(commentToken);
        assertEquals(commentToken.getData(), comment.data());
    }

    @Test
    public void testProcessCharacter() {
        String xmlInput = "<root>&#123;</root>";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Token characterToken = new Token.Character("&#123;");
        TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);
        assertEquals("&#123;", textNode.text());
    }

    @Test
    public void testProcessEndTag() {
        String xmlInput = "<root><child><subchild/></child></root>";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        Document xmlDoc = xmlTreeBuilder.parse(new StringReader(xmlInput), baseUri);

        Element root = xmlDoc.getRootElement();
        Element child = root.getChild(0);
        Element subchild = child.getChild(0);

        Token endChildToken = new Token.EndTag("subchild");
        xmlTreeBuilder.popStackToClose(endChildToken);

        List<Node> childNodes = child.childNodes();
        assertEquals(0, childNodes.size());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child><subchild/></child></root>";
        String baseUri = "http://example.com";

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(xmlFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);

        Element root = (Element) parsedNodes.get(0);
        assertEquals("root", root.tagName());

        Element child = (Element) root.getChild(0);
        assertEquals("child", child.tagName());

        List<Node> childNodes = child.childNodes();
        assertEquals(0, childNodes.size());
    }
}