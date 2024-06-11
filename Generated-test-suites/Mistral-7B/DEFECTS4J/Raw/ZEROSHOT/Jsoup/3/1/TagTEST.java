package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("p");
        assertNotNull(tag);
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.optionalClosing());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertEquals("p", tag.getName());

        Tag emptyTag = new Tag("img");
        assertTrue(emptyTag.isEmpty());

        Tag inlineTag = new Tag("i");
        assertFalse(inlineTag.isBlock());
        assertTrue(inlineTag.canContainInlineOnly());

        Tag dataTag = new Tag("script");
        assertFalse(dataTag.isBlock());
        assertFalse(dataTag.canContainInline());
        assertTrue(dataTag.canContainDataOnly());

        Tag optionalTag = new Tag("a");
        assertTrue(optionalTag.optionalClosing());

        Tag preserveTag = new Tag("pre");
        assertTrue(preserveTag.preserveWhitespace());
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.optionalClosing());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());

        Tag unknownTag = Tag.valueOf("unknownTag");
        assertNotNull(unknownTag);
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.canContainBlock());
        assertTrue(unknownTag.canContainInline());
        assertFalse(unknownTag.optionalClosing());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.preserveWhitespace());

        Tag emptyKnownTag = Tag.valueOf("img");
        assertTrue(emptyKnownTag.isEmpty());

        Tag inlineKnownTag = Tag.valueOf("i");
        assertFalse(inlineKnownTag.isBlock());
        assertTrue(inlineKnownTag.canContainInlineOnly());

        Tag dataKnownTag = Tag.valueOf("script");
        assertFalse(dataKnownTag.isBlock());
        assertFalse(dataKnownTag.canContainInline());
        assertTrue(dataKnownTag.canContainDataOnly());

        Tag optionalKnownTag = Tag.valueOf("a");
        assertTrue(optionalKnownTag.optionalClosing());

        Tag preserveKnownTag = Tag.valueOf("pre");
        assertTrue(preserveKnownTag.preserveWhitespace());
    }

    @Test
    public void testCanContain() {
        Tag p = new Tag("p");
        Tag div = new Tag("div");
        Tag span = new Tag("span");
        Tag img = new Tag("img");
        Tag br = new Tag("br");
        Tag p2 = new Tag("p");
        Tag p3 = new Tag("p");

        assertTrue(p.canContain(div));
        assertTrue(p.canContain(span));
        assertFalse(p.canContain(img));
        assertFalse(p.canContain(br));

        assertTrue(div.canContain(p));
        assertFalse(div.canContain(div));
        assertTrue(div.canContain(span));
        assertFalse(div.canContain(img));
        assertFalse(div.canContain(br));

        assertTrue(span.canContain(p));
        assertFalse(span.canContain(div));
        assertFalse(span.canContain(span));
        assertTrue(span.canContain(img));
        assertFalse(span.canContain(br));

        assertTrue(img.canContain(p));
        assertFalse(img.canContain(div));
        assertFalse(img.canContain(span));
        assertFalse(img.canContain(img));
        assertFalse(img.canContain(br));

        assertFalse(br.canContain(p));
        assertFalse(br.canContain(div));
        assertFalse(br.canContain(span));
        assertFalse(br.canContain(img));
        assertFalse(br.canContain(br));

        assertTrue(p.canContain(p2));
        assertTrue(p.canContain(p3));
        assertTrue(p2.canContain(p3));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag p1 = new Tag("p");
        Tag p2 = new Tag("p");
        Tag div = new Tag("div");

        assertTrue(p1.equals(p2));
        assertTrue(p1.hashCode() == p2.hashCode());

        assertFalse(p1.equals(div));
        assertFalse(p1.hashCode() == div.hashCode());

        Tag p3 = new Tag("p");
        assertTrue(p1.equals(p3));
        assertTrue(p1.hashCode() == p3.hashCode());

        Tag p4 = new Tag("P");
        assertFalse(p1.equals(p4));
        assertFalse(p1.hashCode() == p4.hashCode());

        Tag p5 = new Tag("p");
        p5.setOptionalClosing(true);
        assertFalse(p1.equals(p5));
        assertFalse(p1.hashCode() == p5.hashCode());

        Tag p6 = new Tag("p");
        p6.setTagName("qqq");
        assertFalse(p1.equals(p6));
        assertFalse(p1.hashCode() == p6.hashCode());
    }

    @Test
    public void testToString() {
        Tag p = new Tag("p");
        assertEquals("p", p.toString());
    }

    @Test
    public void testCanContainHead() {
        Tag head = new Tag("head");
        Tag body = new Tag("body");
        Tag html = new Tag("html");
        Tag script = new Tag("script");
        Tag title = new Tag("title");
        Tag link = new Tag("link");
        Tag style = new Tag("style");
        Tag base = new Tag("base");
        Tag object = new Tag("object");

        assertTrue(head.canContain(script));
        assertTrue(head.canContain(title));
        assertTrue(head.canContain(link));
        assertTrue(head.canContain(style));
        assertTrue(head.canContain(base));
        assertTrue(head.canContain(object));

        assertTrue(head.canContain(html));
        assertTrue(html.canContain(head));

        assertFalse(head.canContain(body));
        assertFalse(body.canContain(head));
    }

    @Test
    public void testCanContainDTDDD() {
        Tag dl = new Tag("dl");
        Tag dt = new Tag("dt");
        Tag dd = new Tag("dd");

        assertTrue(dl.canContain(dt));
        assertTrue(dl.canContain(dd));
        assertTrue(dt.canContain(dd));
        assertFalse(dt.canContain(dt));
        assertFalse(dd.canContain(dt));
        assertFalse(dd.canContain(dd));
    }

    @Test
    public void testIsValidParent() {
        Tag p = new Tag("p");
        Tag div = new Tag("div");
        Tag span = new Tag("span");
        Tag img = new Tag("img");
        Tag br = new Tag("br");
        Tag p2 = new Tag("p");
        Tag p3 = new Tag("p");

        assertTrue(p.isValidParent(p2));
        assertTrue(p.isValidParent(p3));
        assertTrue(p.isValidParent(div));
        assertFalse(p.isValidParent(p));
        assertFalse(p.isValidParent(span));
        assertFalse(p.isValidParent(img));
        assertFalse(p.isValidParent(br));
    }
}