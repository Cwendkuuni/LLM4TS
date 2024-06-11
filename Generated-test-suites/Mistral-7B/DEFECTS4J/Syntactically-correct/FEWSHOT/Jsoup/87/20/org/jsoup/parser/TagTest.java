package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    @Test
    public void testIsKnownTag() {
        Tag tagP = Tag.valueOf("p");
        assertTrue(Tag.isKnownTag("p"));
        assertEquals(tagP, Tag.valueOf("p"));

        Tag tagUnknown = new Tag("unknown");
        assertFalse(Tag.isKnownTag("unknown"));
        assertNotEquals(tagUnknown, Tag.valueOf("p"));
    }

    @Test
    public void testIsKnownTagCaseInsensitive() {
        Tag tagP = Tag.valueOf("p");
        assertTrue(Tag.isKnownTag("P"));
        assertEquals(tagP, Tag.valueOf("P"));
    }

    @Test
    public void testIsBlockTag() {
        Tag tagP = Tag.valueOf("p");
        assertTrue(tagP.isBlock());

        Tag tagInput = Tag.valueOf("input");
        assertFalse(tagInput.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tagP = Tag.valueOf("p");
        assertTrue(tagP.formatAsBlock());

        Tag tagInput = Tag.valueOf("input");
        assertFalse(tagInput.formatAsBlock());
    }

    @Test
    public void testIsDataTag() {
        Tag tagP = Tag.valueOf("p");
        assertFalse(tagP.isData());

        Tag tagScript = Tag.valueOf("script");
        assertTrue(tagScript.isData());
    }

    @Test
    public void testIsEmptyTag() {
        Tag tagImg = Tag.valueOf("img");
        assertTrue(tagImg.isEmpty());

        Tag tagP = Tag.valueOf("p");
        assertFalse(tagP.isEmpty());
    }

    @Test
    public void testIsSelfClosingTag() {
        Tag tagImg = Tag.valueOf("img");
        assertTrue(tagImg.isSelfClosing());

        Tag tagP = Tag.valueOf("p");
        assertFalse(tagP.isSelfClosing());
    }

    @Test
    public void testIsKnownFormListedTag() {
        Tag tagInput = Tag.valueOf("input");
        assertTrue(tagInput.isFormListed());

        Tag tagDiv = Tag.valueOf("div");
        assertFalse(tagDiv.isFormListed());
    }

    @Test
    public void testIsKnownFormSubmittableTag() {
        Tag tagInput = Tag.valueOf("input");
        assertTrue(tagInput.isFormSubmittable());

        Tag tagDiv = Tag.valueOf("div");
        assertFalse(tagDiv.isFormSubmittable());
    }

    @Test
    public void testRegisterTag() {
        Map<String, Tag> tags = new HashMap<>();
        Tag tagNew = new Tag("newTag");
        tagNew.register(tags);
        Tag tagNewFromMap = tags.get("newTag");
        assertEquals(tagNew, tagNewFromMap);
    }
}