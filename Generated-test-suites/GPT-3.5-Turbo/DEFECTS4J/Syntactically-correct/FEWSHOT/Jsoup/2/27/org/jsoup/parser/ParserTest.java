package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Tag;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.TokenQueue;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser("", "", true);
    }
    
    @Test
    public void testParse() {
        Document doc = parser.parse();
        Elements elements = doc.getAllElements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
        Document doc = parser.doc();
        Elements elements = doc.getAllElements();
        assertEquals(1, elements.size());

        Node node = elements.get(0);
        assertEquals(Node.COMMENT_NODE, node.getNodeType());
        assertEquals("<!--test-->", node.outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
        Document doc = parser.doc();
        Elements elements = doc.getAllElements();
        assertEquals(1, elements.size());

        Node node = elements.get(0);
        assertEquals(Node.XML_DECLARATION_NODE, node.getNodeType());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", node.outerHtml());
    }

    @Test
    public void testParseEndTag() {
        parser.tq(new TokenQueue("</html>>"));
        parser.parseEndTag();
        Document doc = parser.doc();
        Elements elements = doc.getAllElements();
        assertEquals(1, elements.size());

        Node node = elements.get(0);
        assertEquals(Node.ELEMENT_NODE, node.getNodeType());
        assertEquals("<html>></html>", node.outerHtml());
    }
    
    @Test
    public void testParseStartTag() {
        parser.tq(new TokenQueue("<div>"));
        parser.parseStartTag();
        Document doc = parser.doc();
        Elements elements = doc.getAllElements();
        assertEquals(1, elements.size());

        Node node = elements.get(0);
        assertEquals(Node.ELEMENT_NODE, node.getNodeType());
        assertEquals("<div></div>", node.outerHtml());
    }

    @Test
    public void testParseAttribute() {
        parser.tq(new TokenQueue("<div class=\"test\" id=\"divId\">"));
        Attribute attribute = parser.parseAttribute();
        assertEquals("class", attribute.getKey());
        assertEquals("test", attribute.getValue());

        attribute = parser.parseAttribute();
        assertEquals("id", attribute.getKey());
        assertEquals("divId", attribute.getValue());
    }

    @Test
    public void testParseTextNode() {
        parser.tq(new TokenQueue("Test Text"));
        parser.parseTextNode();
        Document doc = parser.doc();
        Elements elements = doc.getAllElements();
        assertEquals(1, elements.size());

        Node node = elements.get(0);
        assertEquals(Node.TEXT_NODE, node.getNodeType());
        assertEquals("Test Text", ((TextNode)node).text());
    }

    @Test
    public void testParseCdata() {
        parser.tq(new TokenQueue("<![CDATA[Test CDATA]]>"));
        parser.parseCdata();
        Document doc = parser.doc();
        Elements elements = doc.getAllElements();
        assertEquals(1, elements.size());

        Node node = elements.get(0);
        assertEquals(Node.TEXT_NODE, node.getNodeType());
        assertEquals("Test CDATA", ((TextNode)node).text());
    }

    @Test
    public void testAddChildToParent() {
        Element parent = new Element(Tag.valueOf("div"), "");
        Element child = new Element(Tag.valueOf("p"), "");
        boolean isChildEmpty = false;

        parser.stack().add(parent);
        Element root = parser.addChildToParent(child, isChildEmpty);
        assertEquals(parent, root);

        Elements children = parent.children();
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Element parent1 = new Element(Tag.valueOf("div"), "");
        Element parent2 = new Element(Tag.valueOf("body"), "");
        Element child = new Element(Tag.valueOf("p"), "");

        parser.stack().add(parent1);
        parser.stack().add(parent2);
        parser.popStackToSuitableContainer(child.tag());

        Elements children = parent1.children();
        assertEquals(0, children.size());
        
        children = parent2.children();
        assertEquals(1, children.size());
        assertEquals(child, children.get(0));
    }

    @Test
    public void testPopStackToClose() {
        Element parent1 = new Element(Tag.valueOf("div"), "");
        Element parent2 = new Element(Tag.valueOf("body"), "");
        Element child = new Element(Tag.valueOf("p"), "");

        parser.stack().add(parent1);
        parser.stack().add(parent2);
        parser.stack().add(child);
        Element elToClose = parser.popStackToClose(child.tag());

        assertNull(elToClose);

        Elements children = parent1.children();
        assertEquals(0, children.size());

        children = parent2.children();
        assertEquals(0, children.size());
    }

    @Test
    public void testStackHasValidParent() {
        Element parent1 = new Element(Tag.valueOf("html"), "");
        Element parent2 = new Element(Tag.valueOf("body"), "");
        Element child = new Element(Tag.valueOf("p"), "");

        parser.stack().add(parent1);
        parser.stack().add(parent2);
        boolean hasValidParent = parser.stackHasValidParent(child.tag());

        assertEquals(true, hasValidParent);
    }

    @Test
    public void testLast() {
        Element parent = new Element(Tag.valueOf("div"), "");
        parser.stack().add(parent);

        Element lastElement = parser.last();
        assertEquals(parent, lastElement);
    }

    //Other test methods can be added to comprehensively test all methods in Parser class
    
}