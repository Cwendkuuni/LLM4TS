package org.junit.test;

import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testTagName() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
    }

    @Test
    public void testValueOfWithNullTagName() {
        assertThrows(NullPointerException.class, () -> Tag.valueOf(null));
    }

    @Test
    public void testValueOfWithEmptyTagName() {
        assertThrows(IllegalArgumentException.class, () -> Tag.valueOf(""));
    }

    @Test
    public void testValueOfWithDuplicateTagName() {
        Tag pTag = Tag.valueOf("p");
        Tag newPTag = new Tag("p");
        Tag newPTag2 = new Tag("p");

        Tag registeredPTag = Tag.register(pTag);
        Tag newPTag3 = Tag.valueOf("p");

        assertEquals("p", registeredPTag.getName());
        assertEquals(registeredPTag, newPTag3);
    }

    @Test
    public void testValueOfWithSpecialCharacters() {
        Tag tag = Tag.valueOf("tag-with-#%&*'()_+={}[];?@,./~`!");
        assertEquals("tag-with-#%&*'()_+={}[];?@,./~`!", tag.getName());
    }

    @Test
    public void testValueOfWithUnknownTagName() {
        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals("unknown", unknownTag.getName());
        assertEquals(false, unknownTag.isBlock());
        assertEquals(true, unknownTag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag imgTag = Tag.valueOf("img");

        assertEquals(true, pTag.isBlock());
        assertEquals(false, imgTag.isBlock());
    }
}