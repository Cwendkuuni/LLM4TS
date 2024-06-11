package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<root><element attr=\"value\">Hello World</element></root>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://example.com");
        assertEquals("root", doc.tagName());

        Element root = doc.getElementsByTag("root").get(0);
        assertEquals(1, root.childElementCount());
        Element element = root.getElementsByTag("element").get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<element attr=\"value\">Hello World</element>";
        List<Node> fragments = new XmlTreeBuilder().parseFragment(xmlFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(fragments);
        assertSame(1, fragments.size());
        Element fragment = (Element) fragments.get(0);
        assertEquals("element", fragment.tagName());
        assertEquals("value", fragment.attribute("attr"));
        assertEquals("Hello World", fragment.text());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element attr=\"value\">Hello World</element></root>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(xml, "http://example.com", errors, settings);

        Token token = builder.tokeniser.next();
        Element root = builder.insert(token.asStartTag());
        assertNotNull(root);
        assertEquals("root", root.tagName());

        token = builder.tokeniser.next();
        Element element = builder.insert(token.asStartTag());
        assertNotNull(element);
        assertEquals("element", element.tagName());

        token = builder.tokeniser.next();
        builder.popStackToClose(token.asEndTag());

        token = builder.tokeniser.next();
        assertEquals(token.type, Token.Type.EOF);
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element attr=\"value\">Hello World</element></root>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(xml, "http://example.com", errors, settings);

        Token token = builder.tokeniser.next();
        Element root = builder.insert(token.asStartTag());
        assertNotNull(root);
        assertEquals("root", root.tagName());

        token = builder.tokeniser.next();
        Element element = builder.insert(token.asStartTag());
        assertNotNull(element);
        assertEquals("element", element.tagName());

        token = builder.tokeniser.next();
        builder.popStackToClose(token.asEndTag());

        token = builder.tokeniser.next();
        token = builder.tokeniser.next(); // skip root end tag

        assertEquals(token.type, Token.Type.EOF);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<!-- This is a comment --><root><element attr=\"value\">Hello World</element></root>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(xml, "http://example.com", errors, settings);

        Token token = builder.tokeniser.next();
        Comment comment = (Comment) builder.insert(token);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());

        token = builder.tokeniser.next();
        Element root = builder.insert(token.asStartTag());
        assertNotNull(root);
        assertEquals("root", root.tagName());

        token = builder.tokeniser.next();
        Element element = builder.insert(token.asStartTag());
        assertNotNull(element);
        assertEquals("element", element.tagName());

        token = builder.tokeniser.next();
        builder.popStackToClose(token.asEndTag());

        token = builder.tokeniser.next();
        assertEquals(token.type, Token.Type.EOF);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><element>Hello World</element></root>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(xml, "http://example.com", errors, settings);

        Token token = builder.tokeniser.next();
        Element root = builder.insert(token.asStartTag());
        assertNotNull(root);
        assertEquals("root", root.tagName());

        token = builder.tokeniser.next();
        Element element = builder.insert(token.asStartTag());
        assertNotNull(element);
        assertEquals("element", element.tagName());

        token = builder.tokeniser.next();
        TextNode text = (TextNode) builder.insert(token);
        assertNotNull(text);
        assertEquals("Hello World", text.text());

        token = builder.tokeniser.next();
        builder.popStackToClose(token.asEndTag());

        token = builder.tokeniser.next();
        assertEquals(token.type, Token.Type.EOF);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<!DOCTYPE root SYSTEM \"http://example.com/root.dtd\"><root><element>Hello World</element></root>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(xml, "http://example.com", errors, settings);

        Token token = builder.tokeniser.next();
        DocumentType doctype = (DocumentType) builder.insert(token);
        assertNotNull(doctype);
        assertEquals("root", doctype.nodeName());
        assertEquals("http://example.com/root.dtd", doctype.systemIdentifier());

        token = builder.tokeniser.next();
        Element root = builder.insert(token.asStartTag());
        assertNotNull(root);
        assertEquals("root", root.tagName());

        token = builder.tokeniser.next();
        Element element = builder.insert(token.asStartTag());
        assertNotNull(element);
        assertEquals("element", element.tagName());

        token = builder.tokeniser.next();
        TextNode text = (TextNode) builder.insert(token);
        assertNotNull(text);
        assertEquals("Hello World", text.text());

        token = builder.tokeniser.next();
        builder.popStackToClose(token.asEndTag());

        token = builder.tokeniser.next();
        assertEquals(token.type, Token.Type.EOF);
    }

    @Test(expected = AssertionError.class)
    public void testUnexpectedTokenType() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String xml = "<root><!-- This is a comment --><element>Hello World</element></root>";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(xml, "http://example.com", errors, settings);

        Token token = builder.tokeniser.next();
        Comment comment = (Comment) builder.insert(token);
        assertNotNull(comment);
        assertEquals("This is a comment", comment.text());

        token = builder.tokeniser.next();
        Element element = builder.insert(token.asStartTag());
        assertNotNull(element);
        assertEquals("element", element.tagName());

        token = builder.tokeniser.next();
        Validate.fail("Unexpected token type: " + token.type);
    }
}