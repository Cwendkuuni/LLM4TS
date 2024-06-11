package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.XmlTreeBuilder;

import org.junit.Test;

/**
 * XmlTreeBuilderTest.java
 */
public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<root><child/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, null, null);
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals("<root><child/></root>", xmlTreeBuilder.doc.outerHtml());
    }

    @Test
    public void testProcess_StartTag() {
        String input = "<root><child/><child2/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, null, null);

        List<Node> nodes = xmlTreeBuilder.doc.childNodes();
        Element root = (Element) nodes.get(0);
        Element child1 = (Element) nodes.get(1);
        Element child2 = (Element) nodes.get(2);

        assertEquals("root", root.nodeName());
        assertEquals("child", child1.nodeName());
        assertEquals("child2", child2.nodeName());
        assertEquals(2, root.childNodes().size());
    }

    @Test
    public void testProcess_EndTag() {
        String input = "<root><child/></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, null, null);

        List<Node> nodes = xmlTreeBuilder.doc.childNodes();
        Element root = (Element) nodes.get(0);
        Element child = (Element) nodes.get(1);

        xmlTreeBuilder.popStackToClose(new EndTag("child"));
        nodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(0, nodes.size());
        assertNull(child.getParentNode());
    }

    @Test
    public void testProcess_Comment() {
        String input = "<!-- Comment -->";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, null, null);

        List<Node> nodes = xmlTreeBuilder.doc.childNodes();
        Comment comment = (Comment) nodes.get(0);
        assertEquals("Comment", comment.text());
    }

    @Test
    public void testProcess_Character() {
        String input = "<root>Text</root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, null, null);

        List<Node> nodes = xmlTreeBuilder.doc.childNodes();
        Element root = (Element) nodes.get(0);
        TextNode textNode = (TextNode) root.childNodes().get(0);
        assertEquals("Text", textNode.text());
    }

    @Test
    public void testProcess_Doctype() {
        String input = "<!DOCTYPE root [ <!ATTLIST root attr CDATA #REQUIRED > ]><root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, null, null);

        List<Node> nodes = xmlTreeBuilder.doc.childNodes();
        DocumentType doctypeNode = (DocumentType) nodes.get(0);
        assertEquals("root", doctypeNode.nodeName());

        assertEquals("<!ATTLIST root attr CDATA #REQUIRED>", doctypeNode.data());
    }

    @Test(expected = RuntimeException.class)
    public void testProcess_UnexpectedTokenType() {
        String input = "Unexpected"; // this should not be a valid input for the parser
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, null, null);
    }

    @Test
    public void testInsertNode() {
        String input = "<root></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parseFragment(input, null, null);
        Element root = (Element) xmlTreeBuilder.doc.childNodes().get(0);

        Element child = new Element("child");
        xmlTreeBuilder.insertNode(child);
        root.appendChild(child);

        assertEquals("child", child.nodeName());
        assertEquals(1, root.childNodes().size());
    }
}