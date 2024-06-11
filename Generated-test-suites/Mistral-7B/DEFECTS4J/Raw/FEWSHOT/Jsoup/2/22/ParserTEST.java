package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.*;

public class ParserTest {

    @Test
    public void testParser_emptyHtml_createsEmptyDocument() {
        String html = "";
        Parser parser = new Parser(html, "", false);
        Document doc = parser.parse();
        assertEquals(Tag.HTML, doc.tagName());
        assertEquals(0, doc.children().size());
    }

    @Test
    public void testParser_simpleHtml_createsDocumentWithElementChildren() {
        String html = "<html><head/><body><h1>Hello</h1></body></html>";
        Parser parser = new Parser(html, "", false);
        Document doc = parser.parse();
        Element root = doc.getElementsByTag("html").get(0);
        assertEquals(3, root.children().size());

        Element head = root.getElementsByTag("head").get(0);
        assertEquals(0, head.children().size());

        Element body = root.getElementsByTag("body").get(0);
        assertEquals(1, body.children().size());

        Element h1 = body.getElementsByTag("h1").get(0);
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParser_fragmentHtml_createsDocumentWithBodyFragment() {
        String html = "<html><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parseBodyFragment(html, "");
        assertEquals(Tag.BODY, doc.tagName());
        assertEquals(1, doc.children().size());
        Element body = doc.getElementsByTag("body").get(0);
        assertEquals(1, body.children().size());
        Element h1 = body.getElementsByTag("h1").get(0);
        assertEquals("Hello", h1.text());
    }

    @Test
    public void testParser_htmlWithBaseTag_setsBaseUri() {
        String html = "<html><head><base href='/test'/></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "");
        assertEquals("/test", doc.baseUri());
    }

    @Test
    public void testParser_htmlWithInvalidBaseTag_ignoresIt() {
        String html = "<html><head><base target='_blank'/></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "");
        assertNull(doc.baseUri());
    }

    @Test
    public void testParser_htmlWithBaseTagAndUnknownTag_setsBaseUriOnDocument() {
        String html = "<html><head><base href='/test'/><x/></head><body><h1>Hello</h1></body></html>";
        Document doc = Parser.parse(html, "");
        assertEquals("/test", doc.baseUri());
    }

    @Test
    public void testParser_htmlWithScriptTag_parsesContentAsTextNode() {
        String html = "<html><head></head><body><script type='text/javascript'>window.hello = 'world';</script></body></html>";
        Document doc = Parser.parse(html, "");
        Element script = doc.getElementsByTag("script").get(0);
        assertEquals(1, script.children().size());
        TextNode textNode = (TextNode) script.child(0);
        assertEquals("window.hello = 'world';", textNode.text());
    }

    @Test
    public void testParser_htmlWithTextareaTag_parsesContentAsTextNode() {
        String html = "<html><head></head><body><textarea>Hello World</textarea></body></html>";
        Document doc = Parser.parse(html, "");
        Element textarea = doc.getElementsByTag("textarea").get(0);
        assertEquals(1, textarea.children().size());
        TextNode textNode = (TextNode) textarea.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testParser_htmlWithComment_parsesCommentAsCommentNode() {
        String html = "<html><!-- This is a comment --> <body></body></html>";
        Document doc = Parser.parse(html, "");
        assertEquals(1, doc.child(0).children().size());
        Comment comment = (Comment) doc.child(0).child(0);
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testParser_htmlWithCDATA_parsesContentAsTextNode() {
        String html = "<html><![CDATA[ <script type='text/javascript'>window.hello = 'world';</script> ]]> <body></body></html>";
        Document doc = Parser.parse(html, "");
        Element script = doc.getElementsByTag("script").get(0);
        assertEquals(1, script.children().size());
        TextNode textNode = (TextNode) script.child(0);
        assertEquals(" <script type='text/javascript'>window.hello = 'world';</script>", textNode.text());
    }

    @Test
    public void testParser_htmlWithEmptyTag_parsesAsEmptyElement() {
        String html = "<html><head></head><body><img src=''/></body></html>";
        Document doc = Parser.parse(html, "");
        Element img = doc.getElementsByTag("img").get(0);
        assertEquals(0, img.children().size());
    }

    @Test
    public void testParser_htmlWithImplicitParentTag_createsImplicitParent() {
        String html = "<html><body><img src=''/><span></span></body></html>";
        Document doc = Parser.parse(html, "");
        Element img = doc.getElementsByTag("img").get(0);
        assertEquals(1, img.children().size());
        Element span = img.getElementsByTag("span").get(0);
        assertEquals(1, span.children().size());
    }

    @Test
    public void testParser_htmlWithInvalidParentTag_createsImplicitParent() {
        String html = "<html><body><h1>Hello</h1><script src='script.js'></script></body></html>";
        Document doc = Parser.parse(html, "");
        Element h1 = doc.getElementsByTag("h1").get(0);
        assertEquals(1, h1.children().size());
        Element script = h1.getElementsByTag("script").get(0);
        assertEquals(0, script.children().size());
    }

    @Test
    public void testParser_htmlWithInvalidTag_ignoresIt() {
        String html = "<html><body><foo/></body></html>";
        Document doc = Parser.parse(html, "");
        assertEquals(0, doc.children().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_htmlWithNullHtml_throwsException() {
        Parser parser = new Parser(null, "", false);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_htmlWithNullBaseUri_throwsException() {
        Parser parser = new Parser("", null, false);
        parser.parse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParser_htmlWithEmptyBaseUri_throwsException() {
        Parser parser = new Parser("", "", false);
        parser.parse();
    }
}