package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeImpl;
import org.jsoup.parser.Token.CommentImpl;
import org.jsoup.parser.Token.EndTagImpl;
import org.jsoup.parser.Token.StartTagImpl;
import org.jsoup.parser.Token.TextImpl;
import org.jsoup.parser.Tokenizer;

public class XmlTreeBuilderTest {

    @Test
    public void testParseXml() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "  <element attr1=\"value1\" attr2='value2'>\n" +
                "    <sub-element attr3=\"value3\"/>\n" +
                "  </element>\n" +
                "</root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml));

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());

        List<Attribute> attrs = element.attributes();
        assertEquals(2, attrs.size());
        Attribute attr1 = attrs.get(0);
        assertEquals("attr1", attr1.getKey());
        assertEquals("value1", attr1.getValue());

        Attribute attr2 = attrs.get(1);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());

        Element subElement = element.child(0);
        assertNotNull(subElement);
        assertEquals("sub-element", subElement.tagName());

        List<Attribute> subElementAttrs = subElement.attributes();
        assertEquals(1, subElementAttrs.size());
        Attribute subElementAttr = subElementAttrs.get(0);
        assertEquals("attr3", subElementAttr.getKey());
        assertEquals("value3", subElementAttr.getValue());
    }

    @Test
    public void testParseEmptyXml() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml));

        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseXmlWithComment() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "  <!-- This is a comment -->\n" +
                "</root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml));

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        List<Node> rootChildren = root.childNodes();
        assertEquals(0, rootChildren.size());

        Comment comment = (Comment) root.traverseNextSibling();
        assertNotNull(comment);
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testParseXmlWithCDataSection() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "<element>\n" +
                "<![CDATA[This is some CData text]]>\n" +
                "</element>\n" +
                "</root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml));

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());

        List<Node> elementChildren = element.childNodes();
        assertEquals(1, elementChildren.size());

        CDataNode cdata = (CDataNode) elementChildren.get(0);
        assertNotNull(cdata);
        assertEquals("This is some CData text", cdata.getText());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE root SYSTEM \"root.dtd\">\n" +
                "<root>\n" +
                "<element>\n" +
                "</root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml));

        DocumentType doctype = doc.doctype();
        assertNotNull(doctype);
        assertEquals("root", doctype.name());
        assertEquals("root.dtd", doctype.systemId());

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
    }

    @Test
    public void testParseXmlWithEmptyElement() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "<element/>\n" +
                "</root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml));

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals(0, element.childNodes().size());
    }

    @Test
    public void testParseXmlWithSelfClosingElement() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "<element/>\n" +
                "<selfClosingElement/>\n" +
                "</root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml));

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals(0, element.childNodes().size());

        Element selfClosingElement = root.child(1);
        assertNotNull(selfClosingElement);
        assertEquals("selfClosingElement", selfClosingElement.tagName());
        assertEquals(0, selfClosingElement.childNodes().size());
    }

    @Test
    public void testParseXmlWithEmptyDocument() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader(xml));

        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseXmlWithInvalidXml() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "<element>\n" +
                "<!-- This is a comment -->\n" +
                "<![CDATA[This is some CData text]]>\n" +
                "</element>\n" +
                "</root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        try {
            Document doc = builder.parse(new StringReader(xml));
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseXmlFragment() {
        String xmlFragment = "<element attr1=\"value1\" attr2='value2'><sub-element attr3=\"value3\"/></element>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(xmlFragment, null, null, null);

        assertEquals(1, nodes.size());

        Element root = (Element) nodes.get(0);
        assertEquals("element", root.tagName());

        List<Attribute> attrs = root.attributes();
        assertEquals(2, attrs.size());
        Attribute attr1 = attrs.get(0);
        assertEquals("attr1", attr1.getKey());
        assertEquals("value1", attr1.getValue());

        Attribute attr2 = attrs.get(1);
        assertEquals("attr2", attr2.getKey());
        assertEquals("value2", attr2.getValue());

        Element subElement = (Element) root.child(0);
        assertEquals("sub-element", subElement.tagName());

        List<Attribute> subElementAttrs = subElement.attributes();
        assertEquals(1, subElementAttrs.size());
        Attribute subElementAttr = subElementAttrs.get(0);
        assertEquals("attr3", subElementAttr.getKey());
        assertEquals("value3", subElementAttr.getValue());
    }

    @Test
    public void testParseXmlFragmentWithEmptyElement() {
        String xmlFragment = "<element/>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(xmlFragment, null, null, null);

        assertEquals(1, nodes.size());

        Element root = (Element) nodes.get(0);
        assertEquals("element", root.tagName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlFragmentWithSelfClosingElement() {
        String xmlFragment = "<element/>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(xmlFragment, null, null, null);

        assertEquals(1, nodes.size());

        Element root = (Element) nodes.get(0);
        assertEquals("element", root.tagName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlFragmentWithEmptyDocument() {
        String xmlFragment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(xmlFragment, null, null, null);

        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseXmlFragmentWithInvalidXml() {
        String xmlFragment = "<root><!-- This is a comment --><![CDATA[This is some CData text]]></root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        try {
            List<Node> nodes = builder.parseFragment(xmlFragment, null, null, null);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testParseXmlFragmentWithXmlDeclaration() {
        String xmlFragment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "<element>\n" +
                "</root>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment(xmlFragment, null, null, null);

        assertEquals(1, nodes.size());

        Document doc = (Document) nodes.get(0);
        assertNotNull(doc);
        assertNotNull(doc.outputSettings().xmlDeclaration());

        Element root = doc.rootElement();
        assertNotNull(root);
        assertEquals("root", root.tagName());

        Element element = root.child(0);
        assertNotNull(element);
        assertEquals("element", element.tagName());
        assertEquals(0, element.childNodes().size());
    }
}