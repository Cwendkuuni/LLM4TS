package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html>" +
                "<head>" +
                "<title>Test</title>" +
                "</head>" +
                "<body>" +
                "<p>Hello World!</p>" +
                "</body>" +
                "</html>";

        Document doc = Parser.parse(html, null);

        Element root = doc.body();
        assertEquals(Element.Tag.BODY, root.tag());

        Element title = doc.head().getElementsByTag("title").first();
        assertEquals("Test", title.text());

        Element p = root.getElementsByTag("p").first();
        assertEquals("Hello World!", p.text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div id=\"content\">Test</div>";

        Document doc = Parser.parseBodyFragment(bodyHtml, null);

        Element root = doc;
        assertEquals(Element.Tag.HTML, root.tag());
        assertEquals("", root.attr("id"));

        Element div = root.getElementsByTag("div").first();
        assertEquals("content", div.attr("id"));
        assertEquals("Test", div.text());
    }

    @Test
    public void testParseEmpty() {
        String html = "";

        Document doc = Parser.parse(html, null);

        assertEquals(Element.Tag.HTML, doc.tag());
        assertEquals(0, doc.body().childElementCount());
    }

    @Test
    public void testParseInvalidHtml() {
        String html = "<html<tag";

        try {
            Parser.parse(html, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("-- Test <comment>", null, false);
        parser.parseComment();

        Element comment = parser.last();
        assertEquals("Test <comment>", comment.text());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<xml version='1.0' encoding='UTF-8'>", null, false);
        parser.parseXmlDecl();

        XmlDeclaration xmlDecl = parser.last();
        assertEquals("UTF-8", xmlDecl.encoding());
        assertEquals("UTF-8", xmlDecl.encodingDeclared());
        assertFalse(xmlDecl.isStandalone());
        assertEquals("1.0", xmlDecl.version());
        assertEquals("1.0", xmlDecl.versionDeclared());
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("<p><div>Test</div></p>", null, false);

        parser.parseEndTag();
        parser.parseEndTag();

        Element p = parser.last();
        assertEquals(Element.Tag.P, p.tag());
        assertEquals("", p.text());
        assertEquals(1, p.childElementCount());

        Element div = p.child(0);
        assertEquals(Element.Tag.DIV, div.tag());
        assertEquals("Test", div.text());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<p><div id=\"content\">Test</div></p>", null, false);

        parser.parseStartTag();
        parser.parseAttribute();
        parser.parseAttribute();
        parser.parseAttribute();

        Element p = parser.last();
        assertEquals(Element.Tag.P, p.tag());
        assertEquals("", p.attr("id"));

        Element div = p.lastChild();
        assertEquals(Element.Tag.DIV, div.tag());
        assertEquals("content", div.attr("id"));
        assertEquals("Test", div.text());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("<p>Test</p>", null, false);

        parser.parseTextNode();

        Element p = parser.last();
        assertEquals(Element.Tag.P, p.tag());
        assertEquals("Test", p.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseCdata() {
        Parser parser = new Parser("<![CDATA[Test]]>", null, false);
        parser.parseCdata();

        fail();
    }

    @Test
    public void testParseDataNode() {
        Parser parser = new Parser("<script type=\"text/javascript\">Test</script>", null, false);
        parser.parseStartTag();
        parser.parseAttribute();
        parser.parseTextNode();
        parser.parseEndTag();

        Element script = parser.last();
        assertEquals(Element.Tag.SCRIPT, script.tag());
        assertEquals("text/javascript", script.attr("type"));
        assertEquals("Test", script.getElementsByTag("data").first().text());
    }

    @Test
    public void testParseBaseTag() {
        Parser parser = new Parser("<base href=\"/test\">", null, false);
        parser.parseStartTag();
        parser.parseAttribute();
        parser.parseEndTag();

        assertEquals("/test", parser.baseUri);
    }

    @Test
    public void testParseBodyFragmentWithBaseTag() {
        String bodyHtml = "<base href=\"/test\">" +
                "<div id=\"content\">Test</div>";

        Document doc = Parser.parseBodyFragment(bodyHtml, null);

        assertEquals("/test", doc.baseUri());
        assertEquals("Test", doc.body().getElementsByTag("div").first().text());
    }

    @Test
    public void testParseEmptyElement() {
        Parser parser = new Parser("<img src=\"test.png\" />", null, false);
        parser.parseStartTag();
        parser.parseAttribute();
        parser.parseEndTag();

        Element img = parser.last();
        assertEquals(Element.Tag.IMG, img.tag());
        assertTrue(img.isEmpty());
    }
}