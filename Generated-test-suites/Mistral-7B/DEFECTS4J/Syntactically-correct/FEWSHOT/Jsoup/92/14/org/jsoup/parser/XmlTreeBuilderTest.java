package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeToken;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TextToken;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element attr=\"value\">Content</element>\n<element2>\n<sub-element attr2=\"value2\"/>\n</element2>\n</root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));

        assertEquals(1, doc.childNodes().size());
        assertEquals("root", doc.tagName());

        Element root = doc.child(0);
        assertEquals(2, root.childNodes().size());

        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals(1, element.childNodes().size());
        assertEquals("Content", element.child(0).text());

        Element element2 = root.child(1);
        assertEquals("element2", element2.tagName());
        assertEquals(1, element2.childNodes().size());

        Element subElement = element2.child(0);
        assertEquals("sub-element", subElement.tagName());
        assertEquals("value2", subElement.attr("attr2"));
        assertEquals(0, subElement.childNodes().size());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        List<Node> fragment = new XmlTreeBuilder().parseFragment(xmlFragment, null, null, null);

        assertEquals(1, fragment.size());
        Element element = (Element) fragment.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals(1, element.childNodes().size());
        assertEquals("Content", element.child(0).text());
    }

    @Test
    public void testComment() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<!-- comment -->\n</root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));

        assertEquals(1, doc.childNodes().size());
        Element root = doc.child(0);
        assertEquals(1, root.childNodes().size());

        Comment comment = (Comment) root.child(0);
        assertEquals("comment", comment.data());
    }

    @Test
    public void testDoctype() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<!DOCTYPE root [ <!ATTLIST element attr CDATA #REQUIRED >]\n</root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));

        assertEquals(1, doc.childNodes().size());
        Element root = doc.child(0);
        assertEquals(1, root.childNodes().size());

        DocumentType doctype = (DocumentType) root.child(0);
        assertEquals("root", doctype.nodeName());
        assertEquals("root", doctype.tagName());
        assertEquals("root", doctype.systemId());
        assertEquals("", doctype.publicId());

        List<Attribute> attributes = doctype.attributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get(0);
        assertEquals("element", attribute.name());
        assertEquals("attr", attribute.name());
        assertEquals("CDATA", attribute.type());
        assertEquals("REQUIRED", attribute.specified());
    }

    @Test
    public void testSelfClosingStartTag() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element xmlns:x=\"http://example.com\" />\n</root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));

        assertEquals(1, doc.childNodes().size());
        Element root = doc.child(0);
        assertEquals(1, root.childNodes().size());

        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertTrue(element.isSelfClosing());
        assertEquals(0, element.childNodes().size());
    }

    @Test
    public void testUnknownStartTag() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<unknown>\n</root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));

        assertEquals(1, doc.childNodes().size());
        Element root = doc.child(0);
        assertEquals(1, root.childNodes().size());

        Element element = root.child(0);
        assertFalse(element.isSelfClosing()); // unknown tag should not be self closing by default
        assertEquals(0, element.childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>\n<element>\n<element>\n</element>\n</element>\n</element>\n</root>";
        Document doc = new XmlTreeBuilder().parse(new StringReader(xml));

        assertEquals(1, doc.childNodes().size());
        Element root = doc.child(0);
        assertEquals(1, root.childNodes().size());

        Element element = root.child(0);
        assertEquals(3, element.childNodes().size());

        Element innerElement = element.child(2);
        String innerElementTag = innerElement.tagName();

        tokenize("<root><element><element><element name=\"innerElement\" /></element></element></root>"); // test with a known tag name
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document parsedDoc = builder.parse(new StringReader(input));
        Element root2 = parsedDoc.selectFirst("root > element > element > element");
        assertEquals(innerElementTag, root2.tagName());

        tokenize("<root><element><element name=\"innerElement\" /></element></element></root>"); // test with an unknown tag name
        builder = new XmlTreeBuilder();
        parsedDoc = builder.parse(new StringReader(input));
        root2 = parsedDoc.selectFirst("root > element > element > element");
        assertTrue(root2.isSelfClosing()); // unknown tag should be treated as self closing
    }

    @Test
    public void testParseFragmentWithContext() {
        String xmlFragment = "<element attr=\"value\">Content</element>";
        Element context = new Element("root");
        List<Node> fragment = new XmlTreeBuilder().parseFragment(xmlFragment, context, null, null);

        assertEquals(1, fragment.size());
        Element element = (Element) fragment.get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));
        assertEquals(1, element.childNodes().size());
        assertEquals("Content", element.child(0).text());
        assertEquals(context, element.parent());
    }

    private void tokenize(String input) {
        Parser parser = new Parser(new XmlTreeBuilder());
        List<Token> tokens = parser.parse(input, "", null).body();
        this.input = new StringReader(input);
    }
}