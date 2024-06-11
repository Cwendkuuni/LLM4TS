package org.jsoup.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.Jsoup.connect;
import static org.jsoup.Jsoup.parse;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Xml;
import org.jsoup.nodes.XmlDeclaration;

/**
 * Test for Parser class
 *
 * @author John Doe
 */
public class ParserTest {

    @Test
    public void testParse() throws IOException {
        String html = " <html><head><title>Test Title</title></head>" +
                      "<body><h1>Test Heading</h1>" +
                      "<p>Test Paragraph <a href=\"http://www.example.com\">Link</a></p>" +
                      "<script type=\"text/javascript\">console.log('JavaScript');</script>" +
                      "</body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");
        Element root = doc.body();

        // Test base uri
        assertThat(doc.baseUri(), is("http://www.example.com"));

        // Test elements
        Element head = root.selectFirst("head");
        Element title = head.selectFirst("title");
        Element h1 = root.selectFirst("h1");
        Element p = root.selectFirst("p");
        Element link = p.selectFirst("a");
        Xml script = root.selectFirst("script");

        // Test title
        assertThat(title.text(), is("Test Title"));

        // Test elements' attributes
        Attributes titleAttributes = title.attributes();
        Attribute titleId = titleAttributes.get("id");
        Attribute titleClass = titleAttributes.get("class");
        assertThat(titleId, is(nullValue()));
        assertThat(titleClass, is(nullValue()));

        // Test h1
        assertThat(h1.text(), is("Test Heading"));

        // Test p
        assertThat(p.text(), is("Test Paragraph Link"));

        // Test link
        assertThat(link.attr("href"), is("http://www.example.com"));

        // Test script
        XmlDeclaration xmlDecl = (XmlDeclaration) script.child(0);
        assertThat(xmlDecl.version(), is("1.0"));
        String scriptText = script.text();
        assertThat(scriptText, is("console.log('JavaScript');"));

        // Test parse empty tag
        String emptyTagHtml = "<img src=\"foo.png\" alt=\"Foo\" />";
        Document emptyTagDoc = Parser.parse(emptyTagHtml, "http://www.example.com");
        Element emptyTagImage = emptyTagDoc.body().selectFirst("img");
        assertThat(emptyTagImage.text(), is(""));

        // Test parse text node
        String textNodeHtml = "Test Test";
        Document textNodeDoc = Parser.parse(textNodeHtml, "http://www.example.com");
        Element textNodeElement = textNodeDoc.body();
        TextNode textNode = (TextNode) textNodeElement.child(0);
        assertThat(textNode.text(), is("Test Test"));

        // Test parse comment
        String commentHtml = "<!-- Test Comment -->";
        Document commentDoc = Parser.parse(commentHtml, "http://www.example.com");
        Comment comment = commentDoc.body().child(0);
        assertThat(comment.data(), is("Test Comment"));

        // Test parse CDATA
        String cdataHtml = "<![CDATA[Test CDATA]]>";
        Document cdataDoc = Parser.parse(cdataHtml, "http://www.example.com");
        DataNode cdataNode = (DataNode) cdataDoc.body().child(0);
        assertThat(cdataNode.text(), is("Test CDATA"));

        // Test parse invalid HTML
        String invalidHtml = "<html><body><script>alert('Invalid HTML');</script>";
        try {
            Parser.parse(invalidHtml, "http://www.example.com");
            fail("Expected ParseException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBodyFragment() throws IOException {
        String bodyHtml = "<body><p>Test Paragraph<p>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "http://www.example.com");

        // Test parse empty tag
        Element emptyTagImage = doc.body().selectFirst("img");
        assertThat(emptyTagImage, is(nullValue()));

        // Test parse text nodes
        TextNode textNode1 = (TextNode) doc.body().child(0);
        TextNode textNode2 = (TextNode) doc.body().child(1);
        assertThat(textNode1.text(), is(""));
        assertThat(textNode2.text(), is("Test Paragraph"));

        // Test parse comment
        Comment comment = (Comment) doc.body().child(2);
        assertThat(comment.data(), is(""));

        // Test parse CDATA
        DataNode cdataNode = (DataNode) doc.body().child(3);
        assertThat(cdataNode.text(), is(""));
    }

    @Test
    public void testParseAttributes() {
        String html = "<p id=\"testId\" class=\"testClass\"><a href=\"http://www.example.com\" title=\"Test Title\">Test Link</a></p>";
        Document doc = Parser.parse(html, "http://www.example.com");
        Element p = doc.selectFirst("p");
        Element a = p.selectFirst("a");

        Attributes pAttributes = p.attributes();
        Attribute pId = pAttributes.get("id");
        Attribute pClass = pAttributes.get("class");
        List<Attribute> pClasses = pAttributes.list();

        Attributes aAttributes = a.attributes();
        Attribute aHref = aAttributes.get("href");
        Attribute aTitle = aAttributes.get("title");

        // Test id attribute
        assertThat(pId.value(), is("testId"));

        // Test class attribute
        assertThat(pClass.value(), is("testClass"));

        // Test class attribute as list
        List<String> classes = pClasses.stream().map(Attribute::value).toList();
        assertThat(classes, is(List.of("testClass")));

        // Test href attribute
        assertThat(aHref.value(), is("http://www.example.com"));

        // Test title attribute
        assertThat(aTitle.value(), is("Test Title"));
    }

    @Test
    public void testParseInvalidAttribute() {
        String html = "<p><a title=\"Test Title\" bad-attribute=\"Invalid Attribute\" href=\"http://www.example.com\">Test Link</a></p>";
        Document doc = Parser.parse(html, "http://www.example.com");
        Element p = doc.selectFirst("p");
        Element a = p.selectFirst("a");

        Attributes aAttributes = a.attributes();
        List<Attribute> aAttributesList = aAttributes.list();

        // Test invalid attribute
        Attribute badAttribute = null;
        for (Attribute attribute : aAttributesList) {
            if (attribute.key().equals("bad-attribute")) {
                badAttribute = attribute;
                break;
            }
        }

        // Test invalid attribute is null
        assertThat(badAttribute, is(nullValue()));
    }

    @Test
    public void testParseValidParent() {
        String html = "<html><head><title>Test Title</title></head>" +
                      "<body><div><p>Test Paragraph</p></div></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");
        Element p = doc.selectFirst("p");
        Element div = p.parent();

        // Test div is valid parent for p
        assertThat(div.tagName(), is("div"));
        Tag pTag = p.tag();
        boolean validParent = pTag.isValidParent(div.tag());
        assertThat(validParent, is(true));
    }

    @Test
    public void testParseInvalidParent() {
        String html = "<html><head><title>Test Title</title></head>" +
                      "<body><div><p>Test Paragraph</p></div>" +
                      "<ul><li>Test List Item</li></ul></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");
        Element p = doc.selectFirst("p");
        Element li = doc.selectFirst("li");

        // Test li is not valid parent for p
        Tag pTag = p.tag();
        boolean validParent = pTag.isValidParent(li.tag());
        assertThat(validParent, is(false));
    }

    @Test
    public void testParseImplicitParent() {
        String html = "<html><head><title>Test Title</title></head>" +
                      "<body><img src=\"foo.png\" alt=\"Foo\" /></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");
        Element img = doc.body().selectFirst("img");

        // Test img has implicit parent div
        Element div = new Element(Tag.valueOf("div"), "http://www.example.com");
        Element parent = Parser.parse(img.outerHtml(), "http://www.example.com").body();
        Stack<Element> parentStack = parent.stack();
        Element stackTop = parentStack.peek();
        assertThat(stackTop, is(div));
    }

    @Test
    public void testParseImplicitParentForScriptTag() {
        String html = "<html><head><title>Test Title</title></head>" +
                      "<body><script src=\"foo.js\"></script></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");
        Element script = doc.body().selectFirst("script");

        // Test script has implicit parent div
        Element div = new Element(Tag.valueOf("div"), "http://www.example.com");
        Element parent = Parser.parse(script.outerHtml(), "http://www.example.com").body();
        Stack<Element> parentStack = parent.stack();
        Element stackTop = parentStack.peek();
        assertThat(stackTop, is(div));
    }

    @Test
    public void testParseUnknownTag() {
        String html = "<html><head><title>Test Title</title></head>" +
                      "<body><unknown></unknown></body></html>";

        Document doc = Parser.parse(html, "http://www.example.com");

        // Test unknown tag is ignored
        List<Element> elements = doc.body().children();
        assertThat(elements.isEmpty(), is(true));
    }
}