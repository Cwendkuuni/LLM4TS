package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xmlString = "<root><element attr1=\"value1\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document xmlDocument = xmlTreeBuilder.parse(new StringReader(xmlString), "");

        assertNotNull(xmlDocument);
        assertEquals("root", xmlDocument.tagName());
        Element root = xmlDocument.rootElement();
        assertEquals("element", root.child(0).tagName());
        assertEquals("value1", root.child(0).attributeValues("attr1").get(0));
        assertEquals("Content", root.child(0).child(0).text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr1=\"value1\">Content</element>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(3, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value1", element.attributeValues("attr1").get(0));
        assertEquals("Content", element.child(0).text());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("element", null, null, true);
        Element expectedElement = new Element("element", "", null);

        Element actualElement = xmlTreeBuilder.insert(startTagToken);

        assertEquals(expectedElement, actualElement);
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token endTagToken = new Token.EndTag("element");

        xmlTreeBuilder.popStackToClose(endTagToken);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token commentToken = new Token.Comment("This is a comment.");

        xmlTreeBuilder.insert(commentToken);

        Comment expectedComment = new Comment("This is a comment.");
        Comment actualComment = (Comment) xmlTreeBuilder.doc.childNodes().get(xmlTreeBuilder.doc.childNodes().size() - 1);

        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token characterToken = new Token.Character('A');

        xmlTreeBuilder.insert(characterToken);

        TextNode expectedTextNode = new TextNode("A");
        TextNode actualTextNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(xmlTreeBuilder.doc.childNodes().size() - 1);

        assertEquals(expectedTextNode, actualTextNode);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token doctypeToken = new Token.Doctype("html", "", "");

        xmlTreeBuilder.insert(doctypeToken);

        DocumentType expectedDocumentType = new DocumentType("html", null, null);
        DocumentType actualDocumentType = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);

        assertEquals(expectedDocumentType, actualDocumentType);
    }
}