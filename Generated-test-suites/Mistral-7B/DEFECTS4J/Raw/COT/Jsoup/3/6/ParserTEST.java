package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.parser.Parser.parse;

/**
 * Test class for Parser.
 */
public class ParserTest {
    @Test
    public void testParser_HTML() {
        String html = "<html><head><title>Test</title></head>" +
                "<body><h1>Test Heading</h1><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc.rootElement();

        // Check root element is correct
        assertThat(root.tagName(), is(Elements.HTML.tagName()));

        // Check head element is present
        Element head = root.select("head").get(0);
        assertThat(head.tagName(), is(Elements.HEAD.tagName()));
        Assert.assertNotNull(head.title());
        assertThat(head.title().text(), is("Test"));

        // Check body element is present
        Element body = root.body();
        assertThat(body, is(notNullValue()));

        // Check h1 element is present
        Element h1 = root.select("h1").get(0);
        assertThat(h1.tagName(), is(Elements.H1.tagName()));
        assertThat(h1.text(), is("Test Heading"));

        // Check p element is present
        Element p = root.select("p").get(0);
        assertThat(p.tagName(), is(Elements.P.tagName()));
        assertThat(p.text(), is("Test Paragraph."));
    }

    @Test
    public void testParser_BodyFragment() {
        String bodyHtml = "<body><p>Test Paragraph.</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com");
        Element root = doc;

        // Check root element is empty document
        Assert.assertNotNull(root);
        Assert.assertNull(root.head());

        // Check p element is present
        Element p = root.body().select("p").get(0);
        assertThat(p.tagName(), is(Elements.P.tagName()));
        assertThat(p.text(), is("Test Paragraph."));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_NullHtml() {
        Parser.parse(null, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_NullBaseUri() {
        String html = "<html></html>";
        Parser.parse(html, null);
    }

    @Test
    public void testParser_ParseComment() {
        String html = "<html><!-- Test Comment -->";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        Comment comment = root.select("html comment").get(0);
        assertThat(comment.data(), is("Test Comment"));
    }

    @Test
    public void testParser_ParseXmlDecl() {
        String html = "<html xmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'>" +
                "<head><title>Test</title></head>" +
                "<body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        XmlDeclaration xmlDeclaration = root.select("xml-declaration").get(0);
        Map<String, String> attributes = xmlDeclaration.attributes();
        List<String> names = Arrays.asList("xmlns:xsi");
        List<String> expectedValues = Arrays.asList("http://www.w3.org/2001/XMLSchema-instance");
        Assert.assertEquals(names.size(), attributes.size());
        for (String name : names) {
            String value = attributes.get(name);
            Assert.assertEquals(expectedValues.get(Collections.indexOfList(names, name)), value);
        }
    }

    @Test
    public void testParser_ParseEndTag() {
        String html = "<html><body><p>Test Paragraph.<br></p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        Element p = root.body().select("p").get(0);
        Element br = p.child(1);

        Parser.popStackToClose(htmlTag);
        Assert.assertNull(root.body());
        Assert.assertNull(p);
        Assert.assertNull(br);
    }

    @Test
    public void testParser_ParseStartTag() {
        String html = "<html><body><p><span>Test Span</span></p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        Element p = root.body().select("p").get(0);
        Element span = p.child(0).child(0);

        Tag spanTag = span.tag();
        assertThat(spanTag.tagName(), is(Elements.SPAN.tagName()));
        assertThat(span.text(), is("Test Span"));

        Parser.popStackToClose(pTag);
        Assert.assertNull(p);
        Assert.assertNull(span);
    }

    @Test
    public void testParser_ParseAttribute() {
        String html = "<html><body><p id='test' class='test'><span title='Test Title'>Test Content</span></p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        Element p = root.body().select("p").get(0);
        Attribute idAttr = p.attr("id");
        Attribute titleAttr = p.select("span").get(0).attr("title");

        Map<String, String> idAttrMap = idAttr.attributes();
        Map<String, String> titleAttrMap = titleAttr.attributes();
        List<String> idAttrNames = Arrays.asList("id");
        List<String> idAttrValues = Arrays.asList("test");
        List<String> titleAttrNames = Arrays.asList("title");
        List<String> titleAttrValues = Arrays.asList("Test Title");

        Assert.assertEquals(idAttrNames.size(), idAttrMap.size());
        for (String name : idAttrNames) {
            String value = idAttrMap.get(name);
            Assert.assertEquals(idAttrValues.get(Collections.indexOfList(idAttrNames, name)), value);
        }

        Assert.assertEquals(titleAttrNames.size(), titleAttrMap.size());
        for (String name : titleAttrNames) {
            String value = titleAttrMap.get(name);
            Assert.assertEquals(titleAttrValues.get(Collections.indexOfList(titleAttrNames, name)), value);
        }

        Parser.popStackToClose(pTag);
        Assert.assertNull(p);
        Assert.assertNull(titleAttr);
        Assert.assertNull(idAttr);
    }

    @Test
    public void testParser_ParseTextNode() {
        String html = "<html><body><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        Element p = root.body().select("p").get(0);
        TextNode textNode = (TextNode) p.child(0);

        String text = textNode.text();
        String expectedText = "Test Paragraph.";
        Assert.assertEquals(expectedText, text);

        Parser.popStackToClose(pTag);
        Assert.assertNull(p);
        Assert.assertNull(textNode);
    }

    @Test
    public void testParser_ParseCdata() {
        String html = "<html><body><![CDATA[Test CData]]></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        TextNode textNode = (TextNode) root.body().child(0);
        String text = textNode.text();
        String expectedText = "Test CData";
        Assert.assertEquals(expectedText, text);

        Parser.popStackToClose(pTag);
        Assert.assertNull(root.body());
        Assert.assertNull(textNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_EmptyInput() {
        Parser.parse("", "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_UnexpectedEndTag() {
        String html = "<html><body><p><p>Test Paragraph.</p></p></body></html>";
        Parser.parse(html, "http://test.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_UnmatchedEndTag() {
        String html = "<html><body><p><p></p></div></body></html>";
        Parser.parse(html, "http://test.com");
    }

    @Test
    public void testParser_ParseCommandInput() {
        String html = "<html><body>&lt;Test&gt;</body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        Element p = root.body();
        TextNode textNode = (TextNode) p.child(0);
        String text = textNode.text();

        String expectedText = "&lt;Test&gt;";
        Assert.assertEquals(expectedText, text);

        Parser.popStackToClose(pTag);
        Assert.assertNull(p);
        Assert.assertNull(textNode);
    }

    @Test
    public void testParser_ParseBaseTag() {
        String html = "<html><head><base href='/test'></head><body><p>Test Paragraph.</p></body></html>";
        Document doc = Parser.parse(html, "http://test.com");
        Element root = doc;

        Attribute baseAttr = root.head().select("base").get(0).attr("href");
        String expectedBase = "/test";
        Map<String, String> baseAttrMap = baseAttr.attributes();
        List<String> baseAttrNames = Arrays.asList("href");
        List<String> baseAttrValues = Arrays.asList(expectedBase);

        Assert.assertEquals(baseAttrNames.size(), baseAttrMap.size());
        for (String name : baseAttrNames) {
            String value = baseAttrMap.get(name);
            Assert.assertEquals(baseAttrValues.get(Collections.indexOfList(baseAttrNames, name)), value);
        }

        String baseUri = doc.baseUri();
        Assert.assertEquals(expectedBase, baseUri);

        Parser.popStackToClose(htmlTag);
        Assert.assertNull(root);
    }
}