package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag html = Tag.valueOf("html");
        assertTrue(Tag.isKnownTag(html.getName()));

        Tag unknown = new Tag("unknown");
        assertFalse(Tag.isKnownTag(unknown.getName()));
    }

    @Test
    public void testValueOf() {
        Tag html = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");

        assertEquals(html, Tag.valueOf("HTML"));
        assertEquals(html, Tag.valueOf("html", ParseSettings.preserveCase));
        assertEquals(html, Tag.valueOf("HTML", ParseSettings.ignoreCase));
        assertEquals(html, Tag.valueOf("HTML", ParseSettings.ignoreCaseAndNormalize));

        assertEquals(div, Tag.valueOf("DIV"));
        assertEquals(div, Tag.valueOf("div"));
        assertNotEquals(html, Tag.valueOf("DIV"));

        assertEquals(p, Tag.valueOf("p"));
        assertEquals(p, Tag.valueOf("P"));
        assertNotEquals(html, Tag.valueOf("p"));
    }

    @Test
    public void testIsBlock() {
        Tag html = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");
        Tag span = Tag.valueOf("span");

        assertTrue(html.isBlock());
        assertTrue(div.isBlock());
        assertTrue(p.isBlock());
        assertFalse(span.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag html = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");
        Tag span = Tag.valueOf("span");

        assertTrue(html.formatAsBlock());
        assertTrue(div.formatAsBlock());
        assertFalse(p.formatAsBlock());
        assertFalse(span.formatAsBlock());
    }

    @Test
    public void testIsInline() {
        Tag html = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");
        Tag span = Tag.valueOf("span");

        assertFalse(html.isInline());
        assertFalse(div.isInline());
        assertTrue(p.isInline());
        assertTrue(span.isInline());
    }

    @Test
    public void testIsData() {
        Tag html = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");
        Tag p = Tag.valueOf("p");
        Tag script = Tag.valueOf("script");

        assertFalse(html.isData());
        assertFalse(div.isData());
        assertFalse(p.isData());
        assertTrue(script.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag img = Tag.valueOf("img");
        Tag br = Tag.valueOf("br");
        Tag input = Tag.valueOf("input");
        Tag p = Tag.valueOf("p");

        assertFalse(img.isEmpty());
        assertTrue(br.isEmpty());
        assertFalse(input.isEmpty());
        assertFalse(p.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag img = Tag.valueOf("img");
        Tag br = Tag.valueOf("br");
        Tag input = Tag.valueOf("input");
        Tag hr = Tag.valueOf("hr");
        Tag selfClosingTag = new Tag("selfclosingtag");

        assertFalse(img.isSelfClosing());
        assertTrue(br.isSelfClosing());
        assertFalse(input.isSelfClosing());
        assertTrue(hr.isSelfClosing());
        assertTrue(selfClosingTag.setSelfClosing().isSelfClosing());
    }

    @Test
    public void testIsKnownTagArray() {
        Tag.register(new Tag("customtag"));
        assertTrue(Tag.isKnownTag("customtag"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNull() {
        Tag.valueOf(null);
    }

    @Test
    public void testEquals() {
        Tag html = Tag.valueOf("html");
        Tag html2 = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");
        Tag div2 = new Tag("div");

        assertTrue(html.equals(html2));
        assertFalse(html.equals(div));
        assertFalse(html.equals(div2));
        assertTrue(div.equals(div2));
    }

    @Test
    public void testHashCode() {
        Tag html = Tag.valueOf("html");
        Tag html2 = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");
        Tag div2 = new Tag("div");

        int hashCodeHtml = html.hashCode();
        int hashCodeHtml2 = html2.hashCode();
        int hashCodeDiv = div.hashCode();
        int hashCodeDiv2 = div2.hashCode();

        assertEquals(hashCodeHtml, hashCodeHtml2);
        assertNotEquals(hashCodeHtml, hashCodeDiv);
        assertNotEquals(hashCodeHtml, hashCodeDiv2);
        assertEquals(hashCodeDiv, hashCodeDiv2);
    }

    @Test
    public void testToString() {
        Tag html = Tag.valueOf("html");
        Tag div = Tag.valueOf("div");

        assertEquals("html", html.toString());
        assertEquals("div", div.toString());
    }
}