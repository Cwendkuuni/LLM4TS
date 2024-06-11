package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr='value'><sub-element attr2='value2'/><another-element attr3='value3'/></element></root>";
        Document doc = XmlTreeBuilder.parse(new StringReader(xml), "/");
        assertEquals("root", doc.tagName());
        Element root = doc.element("root");
        assertEquals(3, root.children().size());
        Element element = root.child(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attr("attr"));

        assertEquals(2, element.children().size());
        Element subElement = element.child(0);
        assertEquals("sub-element", subElement.tagName());
        assertEquals("value2", subElement.attr("attr2"));

        Element anotherElement = element.child(1);
        assertEquals("another-element", anotherElement.tagName());
        assertEquals("value3", anotherElement.attr("attr3"));
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr='value'><sub-element attr2='value2'/><another-element attr3='value3'/></element>";
        List<Node> fragments = XmlTreeBuilder.parseFragment(xmlFragment, "/", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document doc = Jsoup.parse(fragments.get(0).outerHtml(), "/");
        assertEquals("element", doc.tagName());
        Element root = doc.element("element");
        assertEquals(2, root.children().size());

        Element subElement = root.child(0);
        assertEquals("sub-element", subElement.tagName());
        assertEquals("value2", subElement.attr("attr2"));

        Element anotherElement = root.child(1);
        assertEquals("another-element", anotherElement.tagName());
        assertEquals("value3", anotherElement.attr("attr3"));
    }

    @Test
    public void testProcessUnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer("<unknown-tag attr='value'>content</unknown-tag>");
        Token token;
        while ((token = tokenizer.next()) != null) {
            builder.process(token);
        }

        assertEquals(1, builder.stack.size());
        Element unknownTag = (Element) builder.stack.get(0);
        assertEquals("unknown-tag", unknownTag.tagName());
        assertNotNull(unknownTag.attr("attr"));
        assertEquals("value", unknownTag.attr("attr"));
        assertEquals(1, unknownTag.children().size());
        Text content = (Text) unknownTag.child(0);
        assertEquals("content", content.text());
    }

    @Test
    public void testProcessSelfClosingTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer("<self-closing-tag/>");
        Token token;
        while ((token = tokenizer.next()) != null) {
            builder.process(token);
        }

        assertEquals(0, builder.stack.size());
        assertNull(builder.doc);
    }

    @Test(expected = RuntimeException.class)
    public void testProcessUnexpectedToken() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Tokenizer tokenizer = new Tokenizer("<html><body><!DOCTYPE html>");
        Token token;
        while ((token = tokenizer.next()) != null) {
            builder.process(token);
        }

        // The unexpected comment token should throw a RuntimeException
    }
}