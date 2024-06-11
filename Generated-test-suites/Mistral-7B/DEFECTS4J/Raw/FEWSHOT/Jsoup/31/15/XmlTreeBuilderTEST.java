package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private static final String XML_SAMPLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<root>\n" +
            "  <element1 attr1=\"value1\" attr2=\"value2\">\n" +
            "    <subelement1/>\n" +
            "    <subelement2/>\n" +
            "  </element1>\n" +
            "  <element2 attr3=\"value3\">\n" +
            "    <subelement3/>\n" +
            "  </element2>\n" +
            "</root>";

    @Test
    public void testParseXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(XML_SAMPLE);

        assertEquals("root", doc.tagName());
        Element root = doc.child(0);
        assertEquals(2, root.childNodeSize());

        Element element1 = root.child(0);
        assertEquals("element1", element1.tagName());
        assertEquals("value1", element1.attr("attr1"));
        assertEquals("value2", element1.attr("attr2"));
        assertNotNull(element1.child(0));
        assertEquals("subelement1", element1.child(0).tagName());
        assertNotNull(element1.child(1));
        assertEquals("subelement2", element1.child(1).tagName());

        Element element2 = root.child(1);
        assertEquals("element2", element2.tagName());
        assertEquals("value3", element2.attr("attr3"));
        assertNotNull(element2.child(0));
        assertEquals("subelement3", element2.child(0).tagName());
    }

    @Test
    public void testParseEmptyXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals("", doc.outerHtml());
    }

    @Test
    public void testParseMalformedXml() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        Document doc = builder.parse("<root", errors);
        assertNull(doc);
        assertEquals(1, errors.size());
        assertEquals("Error parsing XML: not well-formed (syntax error)", errors.get(0).getMessage());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.stack.push(root);

        Element element1 = new Element("element1");
        root.appendChild(element1);
        builder.stack.push(element1);

        Element subelement1 = new Element("subelement1");
        element1.appendChild(subelement1);
        builder.stack.push(subelement1);

        Token.EndTag endTag = new Token.EndTag("element1");
        builder.popStackToClose(endTag);

        assertEquals(2, builder.stack.size());
        assertEquals(element1, builder.stack.peek());
    }

    @Test
    public void testPopStackToCloseMissingElement() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        builder.stack.push(root);

        Element element1 = new Element("element1");
        root.appendChild(element1);
        builder.stack.push(element1);

        Token.EndTag endTag = new Token.EndTag("element2"); // does not exist
        builder.popStackToClose(endTag);

        assertEquals(2, builder.stack.size());
        assertEquals(root, builder.stack.peek());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token startTag = new Token.StartTag("element1");
        builder.process(startTag);

        assertEquals(Element.class, builder.currentElement().getClass());
        assertEquals("element1", builder.currentElement().tagName());
        assertTrue(builder.stack.contains(builder.currentElement()));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element element1 = new Element("element1");
        root.appendChild(element1);
        builder.stack.push(root);
        builder.stack.push(element1);

        Token endTag = new Token.EndTag("element1");
        builder.process(endTag);

        assertEquals(1, builder.stack.size());
        assertEquals(root, builder.stack.peek());
    }
}