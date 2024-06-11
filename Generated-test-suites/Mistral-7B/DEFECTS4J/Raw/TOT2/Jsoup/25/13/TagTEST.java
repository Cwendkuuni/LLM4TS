
package com.example.junit4test;

import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testTagName() {
        String tagName = "div";
        Tag tag = new Tag(tagName);
        String actualTagName = tag.getName();
        assertEquals("div", actualTagName);
    }

    @Test
    public void testNullTagName() {
        String tagName = null;
        Tag tag = new Tag(tagName);
        assertNotNull(tag); // Ensure that a default tag is created
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTagName() {
        String tagName = "";
        new Tag(tagName);
    }

    @Test
    public void testTagNameWithWhiteSpace() {
        String tagName = "   p   ";
        Tag tag = new Tag(tagName);
        String actualTagName = tag.getName();
        assertEquals("p", actualTagName);
    }

    @Test
    public void testRegisteringTagWithSameName() {
        String tagName = "div";
        Tag tag1 = new Tag(tagName);
        Tag tag2 = new Tag(tagName);
        assertEquals(tag1, tag2); // They should be the same tag instance
    }

    @Test
    public void testUnknownTag() {
        String unknownTagName = "mytag";
        Tag tag = Tag.valueOf(unknownTagName);
        assertNotNull(tag); // Ensure that a default tag is created
    }

    // Test other methods here...

}