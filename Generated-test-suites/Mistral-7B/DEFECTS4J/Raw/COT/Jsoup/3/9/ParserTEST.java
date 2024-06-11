package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Parser class.
 */
public class ParserTest {

    private static final String HTML_WITH_COMMENT = "<html><!-- This is a comment --><body></body></html>";
    private static final String XML_DECL_HTML = "<!-- This is a XML declaration --><!DOCTYPE html SYSTEM \"about:legacy-compat\">";
    private static final String HTML_WITHOUT_DOCTYPE = "<html><body></body></html>";
    private static final String HTML_WITH_EMPTY_TAG = "<html><img src=\"image.png\"/></html>";
    private static final String HTML_WITH_SELF_CLOSED_TAG = "<html><hr/></html>";
    private static final String HTML_WITH_TEXTAREA = "<html><body><textarea name=\"textarea\"></textarea></body></html>";
    private static final String HTML_WITH_BASE_TAG = "<html><head><base href=\"baseurl\"/></head><body></body></html>";
    private static final String HTML_WITH_INVALID_TAG = "<html><invalidtag></invalidtag></html>";

    private Document parsedHtml;
    private Document parsedHtmlWithComment;
    private Document parsedHtmlXmlDecl;
    private Document parsedHtmlWithoutDoctype;
    private Document parsedHtmlWithEmptyTag;
    private Document parsedHtmlWithSelfClosedTag;
    private Document parsedHtmlWithTextarea;
    private Document parsedHtmlWithBaseTag;
    private Document parsedHtmlWithInvalidTag;

    @Before
    public void setup() {
        parsedHtml = Parser.parse(HTML_WITH_COMMENT, "http://example.com");
        parsedHtmlWithComment = Parser.parse(XML_DECL_HTML, "http://example.com");
        parsedHtmlWithoutDoctype = Parser.parse(HTML_WITHOUT_DOCTYPE, "http://example.com");
        parsedHtmlWithEmptyTag = Parser.parse(HTML_WITH_EMPTY_TAG, "http://example.com");
        parsedHtmlWithSelfClosedTag = Parser.parse(HTML_WITH_SELF_CLOSED_TAG, "http://example.com");
        parsedHtmlWithTextarea = Parser.parse(HTML_WITH_TEXTAREA, "http://example.com");
        parsedHtmlWithBaseTag = Parser.parse(HTML_WITH_BASE_TAG, "http://example.com");
        parsedHtmlWithInvalidTag = Parser.parse(HTML_WITH_INVALID_TAG, "http://example.com");
    }

    @Test
    public void testParserBasic() {
        assertNotNull(parsedHtml);
        assertEquals("html", parsedHtml.tagName());
        assertEquals("http://example.com", parsedHtml.baseUri());
        assertEquals(1, parsedHtml.childNodes().size());

        Element body = parsedHtml.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(0, body.childNodes().size());

        assertEquals(1, parsedHtml.numHeaders());
        assertEquals(1, parsedHtml.numBlocks());
        assertEquals(1, parsedHtml.numParas());
        assertEquals(0, parsedHtml.numLinks());
        assertEquals(0, parsedHtml.numImages());
        assertEquals(1, parsedHtml.numScripts());
    }

    @Test
    public void testParserHtmlWithComment() {
        assertNotNull(parsedHtmlWithComment);
        assertEquals("html", parsedHtmlWithComment.tagName());
        assertEquals("http://example.com", parsedHtmlWithComment.baseUri());
        assertEquals(1, parsedHtmlWithComment.childNodes().size());

        Element comment = parsedHtmlWithComment.getElementsByTag("comment").get(0);
        assertNotNull(comment);
        assertEquals("comment", comment.tagName());
        assertEquals("This is a comment ", comment.text());
    }

    @Test
    public void testParserHtmlXmlDecl() {
        assertNotNull(parsedHtmlXmlDecl);
        assertEquals("html", parsedHtmlXmlDecl.tagName());
        assertEquals("http://example.com", parsedHtmlXmlDecl.baseUri());
        assertEquals(1, parsedHtmlXmlDecl.childNodes().size());

        Element xmlDeclaration = parsedHtmlXmlDecl.getElementsByTag("xml-decl").get(0);
        assertNotNull(xmlDeclaration);
        assertEquals("xml-decl", xmlDeclaration.tagName());
        String xmlDeclarationData = xmlDeclaration.data();
        assertTrue(xmlDeclarationData.contains("encoding=\"UTF-8\""));
        assertTrue(xmlDeclarationData.contains("standalone=\"yes\""));
    }

    @Test
    public void testParserHtmlWithoutDoctype() {
        assertNotNull(parsedHtmlWithoutDoctype);
        assertEquals("html", parsedHtmlWithoutDoctype.tagName());
        assertEquals("http://example.com", parsedHtmlWithoutDoctype.baseUri());
        assertEquals(1, parsedHtmlWithoutDoctype.childNodes().size());

        Element body = parsedHtmlWithoutDoctype.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(0, body.childNodes().size());
    }

    @Test
    public void testParserHtmlWithEmptyTag() {
        assertNotNull(parsedHtmlWithEmptyTag);
        assertEquals("html", parsedHtmlWithEmptyTag.tagName());
        assertEquals("http://example.com", parsedHtmlWithEmptyTag.baseUri());
        assertEquals(1, parsedHtmlWithEmptyTag.childNodes().size());

        Element img = parsedHtmlWithEmptyTag.getElementsByTag("img").get(0);
        assertNotNull(img);
        assertEquals("img", img.tagName());
        assertEquals(0, img.childNodes().size());
        assertNull(img.data());
    }

    @Test
    public void testParserHtmlWithSelfClosedTag() {
        assertNotNull(parsedHtmlWithSelfClosedTag);
        assertEquals("html", parsedHtmlWithSelfClosedTag.tagName());
        assertEquals("http://example.com", parsedHtmlWithSelfClosedTag.baseUri());
        assertEquals(1, parsedHtmlWithSelfClosedTag.childNodes().size());

        Element hr = parsedHtmlWithSelfClosedTag.getElementsByTag("hr").get(0);
        assertNotNull(hr);
        assertEquals("hr", hr.tagName());
        assertEquals(0, hr.childNodes().size());
        assertNull(hr.data());
    }

    @Test
    public void testParserHtmlWithTextarea() {
        assertNotNull(parsedHtmlWithTextarea);
        assertEquals("html", parsedHtmlWithTextarea.tagName());
        assertEquals("http://example.com", parsedHtmlWithTextarea.baseUri());
        assertEquals(1, parsedHtmlWithTextarea.childNodes().size());

        Element body = parsedHtmlWithTextarea.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
        assertEquals(1, body.childNodes().size());

        Element textarea = body.getElementsByTag("textarea").get(0);
        assertNotNull(textarea);
        assertEquals("textarea", textarea.tagName());
        assertEquals(0, textarea.childNodes().size());
        assertNull(textarea.data());
    }

    @Test
    public void testParserHtmlWithBaseTag() {
        assertNotNull(parsedHtmlWithBaseTag);
        assertEquals("html", parsedHtmlWithBaseTag.tagName());
        assertEquals("http://example.com", parsedHtmlWithBaseTag.baseUri());

        Element head = parsedHtmlWithBaseTag.getElementsByTag("head").get(0);
        assertNotNull(head);
        assertEquals("head", head.tagName());
        assertEquals(1, head.childNodes().size());

        Element base = head.getElementsByTag("base").get(0);
        assertNotNull(base);
        assertEquals("base", base.tagName());
        assertEquals("baseurl", base.absUrl("href"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParserHtmlWithInvalidTag() {
        assertNotNull(parsedHtmlWithInvalidTag);
        assertEquals("html", parsedHtmlWithInvalidTag.tagName());
        assertEquals("http://example.com", parsedHtmlWithInvalidTag.baseUri());

        Element invalidTag = parsedHtmlWithInvalidTag.getElementsByTag("invalidtag").get(0);
        assertNull(invalidTag);
    }

    @Test
    public void testParserAttributes() {
        String htmlWithAttributes = " <html <title>Test</title> <meta charset=\"UTF-8\"> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/> <body class=\"test-class\"> <h1 id=\"test-header\" class=\"test-class test-header\" title=\"Test header\">Test</h1> </body> </html>";
        Document parsedHtmlWithAttributes = Parser.parse(htmlWithAttributes, "http://example.com");

        Element title = parsedHtmlWithAttributes.title();
        assertEquals("Test", title.text());
        List<Attribute> titleAttributes = title.attributes();
        assertEquals(0, titleAttributes.size());

        Element metaCharset = parsedHtmlWithAttributes.getElementsByTag("meta").get(0);
        Map<String, String> metaCharsetAttributes = new HashMap<>(MetaTag.parseAttributes(metaCharset.attributes()));
        assertEquals("UTF-8", metaCharsetAttributes.get("charset"));

        Element metaViewport = parsedHtmlWithAttributes.getElementsByTag("meta").get(1);
        Map<String, String> metaViewportAttributes = new HashMap<>(MetaTag.parseAttributes(metaViewport.attributes()));
        String[] expectedViewportValues = {"width=device-width", "initial-scale=1.0"};
        List<String> viewportValues = Collections.list(metaViewportAttributes.values().toArray(new String[0]));
        for (String expectedValue : expectedViewportValues) {
            assertTrue(viewportValues.contains(expectedValue));
        }

        Element body = parsedHtmlWithAttributes.body();
        List<String> bodyClasses = new ArrayList<>();
        for (Attribute attribute : body.attributes()) {
            if (attribute.getKey().equals("class")) {
                bodyClasses.addAll(Arrays.asList(attribute.getValue().split("\\s+")));
                break;
            }
        }
        List<String> expectedBodyClasses = Arrays.asList("test-class", "test-header");
        Collections.sort(bodyClasses);
        Collections.sort(expectedBodyClasses);
        assertEquals(expectedBodyClasses, bodyClasses);

        List<String> expectedHeaderClasses = Arrays.asList("test-class", "test-header", "test-header");
        Element header = parsedHtmlWithAttributes.getElementsByTag("h1").get(0);
        List<String> headerClasses = new ArrayList<>();
        for (Attribute attribute : header.attributes()) {
            if (attribute.getKey().equals("class")) {
                headerClasses.addAll(Arrays.asList(attribute.getValue().split("\\s+")));
                break;
            }
        }
        Collections.sort(headerClasses);
        Collections.sort(expectedHeaderClasses);
        assertEquals(expectedHeaderClasses, headerClasses);
    }

    @Test
    public void testParseTextNode() {
        String htmlWithTextNode = "<html><body><p>Test text node</p></body></html>";
        Document parsedHtmlWithTextNode = Parser.parse(htmlWithTextNode, "http://example.com");

        Element textNodeParent = parsedHtmlWithTextNode.body();
        Element textNode = textNodeParent.getElementsByTag("p").get(0).childNodes().get(0);

        assertEquals("Test text node", textNode.text());
    }

    @Test
    public void testParseCdata() {
        String htmlWithCdata = "<html><body><![CDATA[Test cdata]]></body></html>";
        Document parsedHtmlWithCdata = Parser.parse(htmlWithCdata, "http://example.com");

        Element cdataParent = parsedHtmlWithCdata.body();
        Element cdata = cdataParent.getElementsByTag("body").get(0).childNodes().get(0);
        Node cdataNode = cdata.getElementsByTag("![CDATA[").get(0).childNodes().get(0);

        assertEquals("Test cdata", cdataNode.text());
    }

    @Test
    public void testParseComment() {
        String htmlWithComment = "<html><!--This is a comment--><body></body></html>";
        Document parsedHtmlWithComment = Parser.parse(htmlWithComment, "http://example.com");

        List<Comment> comments = parsedHtmlWithComment.getElementsByTag("comment");
        assertEquals(1, comments.size());

        Comment comment = comments.get(0);
        assertEquals("This is a comment", comment.data());
    }

    @Test
    public void testParserXmlDecl() {
        String xmlDeclHtml = "<!-- This is a XML declaration --><!DOCTYPE html SYSTEM \"about:legacy-compat\">";
        Document parsedXmlDeclHtml = Parser.parse(xmlDeclHtml, "http://example.com");

        Element xmlDeclaration = parsedXmlDeclHtml.getElementsByTag("xml-decl").get(0);

        String xmlDeclarationData = xmlDeclaration.data();
        assertTrue(xmlDeclarationData.contains("DOCTYPE html"));
        assertTrue(xmlDeclarationData.contains("SYSTEM \"about:legacy-compat\""));
    }

    @Test
    public void testParserEndTag() {
        String htmlWithImplicitParent = "<html><body><div class=\"test\"><span>Test</span></div></body></html>";
        Document parsedHtmlWithImplicitParent = Parser.parse(htmlWithImplicitParent, "http://example.com");

        Element div = parsedHtmlWithImplicitParent.body();
        Element span = div.getElementsByTag("span").get(0);
        assertEquals(1, div.childNodes().size());
        assertEquals("div", div.tagName());
        assertEquals("test", div.classes().get(0));

        Element parent = span.parent();
        assertEquals(div, parent);

        assertTrue(parent.childNodes().contains(span));

        String htmlWithInvalidEndTag = "<html><body><div class=\"test\"><span>Test</span></div></span></body></html>";
        Document parsedHtmlWithInvalidEndTag = Parser.parse(htmlWithInvalidEndTag, "http://example.com");

        Element parentWithInvalidEndTag = parsedHtmlWithInvalidEndTag.body();
        Element spanWithInvalidEndTag = parentWithInvalidEndTag.getElementsByTag("span").get(0);

        assertFalse(parentWithInvalidEndTag.childNodes().contains(spanWithInvalidEndTag));
    }

    @Test
    public void testParserParseCommentWithCdata() {
        String htmlWithCommentAndCdata = "<html><!--This is a comment--><![CDATA[Test cdata]]></body></html>";
        Document parsedHtmlWithCommentAndCdata = Parser.parse(htmlWithCommentAndCdata, "http://example.com");

        List<Comment> comments = parsedHtmlWithCommentAndCdata.getElementsByTag("comment");
        assertEquals(1, comments.size());

        Comment comment = comments.get(0);
        assertEquals("This is a comment", comment.data());

        Element cdataParent = parsedHtmlWithCommentAndCdata.body();
        Element cdata = cdataParent.getElementsByTag("![CDATA[").get(0).childNodes().get(0);

        assertEquals("Test cdata", cdata.text());
    }
}