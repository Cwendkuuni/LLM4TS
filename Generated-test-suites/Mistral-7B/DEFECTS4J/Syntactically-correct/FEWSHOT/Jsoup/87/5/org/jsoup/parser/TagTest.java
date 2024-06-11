package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testKnownTags() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags
        Tag html = Tag.valueOf("html");
        Tag body = Tag.valueOf("body");
        Tag p = Tag.valueOf("p");
        Tag div = Tag.valueOf("div");

        assertEquals(html, tags.put(html.getName(), html));
        assertEquals(body, tags.put(body.getName(), body));
        assertEquals(p, tags.put(p.getName(), p));
        assertEquals(div, tags.put(div.getName(), div));

        Tag tagFromMap = tags.get("p");
        assertEquals(p, tagFromMap);

        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("body"));
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testUnknownTags() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags

        Tag unknownTag = Tag.valueOf("unknownTag");

        assertNotNull(unknownTag);
        assertTrue(unknownTag.isSelfClosing());
        assertTrue(unknownTag.isEmpty());

        assertFalse(Tag.isKnownTag("unknownTag"));
        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());
        assertFalse(unknownTag.canContainInline());
        assertFalse(unknownTag.preserveWhitespace());
        assertFalse(unknownTag.formList());
        assertFalse(unknownTag.formSubmit());
    }

    @Test
    public void testBlockTags() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags

        Tag html = Tag.valueOf("html");
        Tag p = Tag.valueOf("p");
        Tag table = Tag.valueOf("table");

        assertTrue(html.isBlock());
        assertTrue(html.formatAsBlock());
        assertTrue(p.isBlock());
        assertTrue(p.formatAsBlock());
        assertFalse(table.isBlock());
        assertFalse(table.formatAsBlock());

        assertTrue(Tag.isBlock(html));
        assertTrue(Tag.isBlock(p));
        assertFalse(Tag.isBlock(table));
    }

    @Test
    public void testInlineTags() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags

        Tag script = Tag.valueOf("script");
        Tag input = Tag.valueOf("input");

        assertFalse(script.isBlock());
        assertFalse(script.formatAsBlock());
        assertTrue(input.isBlock());
        assertFalse(input.formatAsBlock());

        assertTrue(Tag.isInline(script));
        assertFalse(Tag.isInline(input));
    }

    @Test
    public void testEmptyTags() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags

        Tag img = Tag.valueOf("img");
        Tag br = Tag.getValueByTagName("br");

        assertTrue(img.isEmpty());
        assertTrue(img.canContainInline);
        assertFalse(img.preserveWhitespace);
        assertFalse(img.isBlock());
        assertFalse(img.formatAsBlock());

        assertTrue(br.isEmpty());
        assertFalse(br.canContainInline);
        assertFalse(br.preserveWhitespace);
        assertFalse(br.isBlock());
        assertFalse(br.formatAsBlock());

        assertTrue(Tag.isEmpty(img));
        assertTrue(Tag.isEmpty(br));
    }

    @Test
    public void testSelfClosingTags() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags

        Tag img = Tag.valueOf("img");
        Tag input = Tag.valueOf("input");
        Tag imgSelfClosing = Tag.valueOf("img").setSelfClosing();

        assertFalse(imgSelfClosing.isEmpty());
        assertTrue(imgSelfClosing.isSelfClosing());
        assertFalse(imgSelfClosing.canContainInline);
        assertFalse(imgSelfClosing.preserveWhitespace);
        assertFalse(imgSelfClosing.isBlock());
        assertFalse(imgSelfClosing.formatAsBlock());

        assertFalse(input.isSelfClosing());
        assertFalse(input.isEmpty());
        assertFalse(input.canContainInline);
        assertFalse(input.preserveWhitespace);
        assertTrue(input.isBlock());
        assertTrue(input.formatAsBlock);

        assertTrue(Tag.isSelfClosing("img"));
        assertFalse(Tag.isSelfClosing("input"));
    }

    @Test
    public void testFormListedTags() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags

        Tag input = Tag.valueOf("input");
        Tag textarea = Tag.valueOf("textarea");

        assertTrue(input.isFormListed());
        assertTrue(input.isFormSubmittable());
        assertTrue(textarea.isFormListed());
        assertTrue(textarea.isFormSubmittable());

        assertTrue(Tag.isFormListed("input"));
        assertTrue(Tag.isFormSubmittable("input"));
        assertTrue(Tag.isFormListed("textarea"));
        assertTrue(Tag.isFormSubmittable("textarea"));
    }

    @Test
    public void testFormSubmitTags() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags

        Tag input = Tag.valueOf("input");
        Tag select = Tag.valueOf("select");

        assertTrue(input.isFormSubmittable());
        assertTrue(input.isFormListed());
        assertTrue(select.isFormSubmittable());
        assertTrue(select.isFormListed());

        assertTrue(Tag.isFormSubmittable("input"));
        assertTrue(Tag.isFormSubmittable("select"));
        assertFalse(Tag.isFormSubmittable("p"));
        assertFalse(Tag.isFormSubmittable("div"));
    }

    @Test
    public void testEqualsAndHashCode() {
        Map<String, Tag> tags = new HashMap<>(); // map of known tags

        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        Tag tag3 = Tag.valueOf("div");

        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertNotEquals(tag1, tag3);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }
}