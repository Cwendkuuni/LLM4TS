import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testValueOf() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag headTag = Tag.valueOf("HEAD");
        Tag unknownTag = Tag.valueOf("UNKNOWN");

        assertEquals("p", pTag.getName());
        assertEquals("div", divTag.getName());
        assertEquals("BODY", bodyTag.getName());
        assertEquals("HEAD", headTag.getName());
        assertNotEquals("p", unknownTag.getName());

        // Check that an unknown tag is a generic one
        assertTrue(unknownTag.isBlock());
        assertTrue(unknownTag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");
        Tag bodyTag = Tag.valueOf("BODY");

        // p can contain span
        assertTrue(pTag.canContain(spanTag));

        // div can contain p
        assertTrue(divTag.canContain(pTag));

        // p cannot contain p
        assertFalse(pTag.canContain(pTag));

        // div cannot contain div
        assertFalse(divTag.canContain(divTag));

        // body can contain anything
        assertTrue(bodyTag.canContain(pTag));
        assertTrue(bodyTag.canContain(divTag));
        assertTrue(bodyTag.canContain(spanTag));
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
        assertFalse(spanTag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag spanTag = Tag.valueOf("span");

        assertTrue(divTag.canContainBlock());
        assertTrue(pTag.canContainBlock());
        assertFalse(spanTag.canContainBlock());
    }

    @Test
    public void testIsData() {
        Tag pTag = Tag.valueOf("p");
        Tag scriptTag = Tag.valueOf("script");

        assertFalse(pTag.isData());
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        Tag pTag = Tag.valueOf("p");

        assertFalse(imgTag.isEmpty());
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        Tag pTag = Tag.valueOf("p");

        assertFalse(pTag.preserveWhitespace());
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("BODY");

        assertEquals(pTag, pTag.getImplicitParent());
        assertEquals(divTag, pTag.getImplicitParent(divTag));
        assertEquals(bodyTag, pTag.getImplicitParent(bodyTag));
    }

    @Test
    public void testIsValidParent() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag bodyTag = Tag.valueOf("BODY");
        Tag headTag = Tag.valueOf("HEAD");

        // p is a valid parent for span
        assertTrue(pTag.isValidParent(Tag.valueOf("span")));

        // div is a valid parent for p
        assertTrue(divTag.isValidParent(pTag));

        // head cannot be a parent of body
        assertFalse(headTag.isValidParent(bodyTag));

        // body is not a valid parent for head
        assertFalse(bodyTag.isValidParent(headTag));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertEquals(pTag1, pTag2);
        assertEquals(pTag1.hashCode(), pTag2.hashCode());
        assertNotEquals(pTag1, divTag);
        assertNotEquals(pTag1.hashCode(), divTag.hashCode());
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertEquals("p", pTag.toString());
        assertEquals("div", divTag.toString());
    }

    @Test
    public void testCreateBlockTags() {
        List<String> tags = Arrays.asList("HTML", "HEAD", "BODY", "FRAMESET", "FRAME", "NOFRAMES", "FONT", "TT", "I", "B", "BIG", "SMALL",
                "EM", "STRONG", "DFN", "CODE", "SAMP", "KBD", "VAR", "CITE", "ABBR", "ACRONYM", "A", "IMG", "BR", "MAP", "Q", "SUB", "SUP",
                "SPAN", "BDO", "P", "H1", "H2", "H3", "H4", "H5", "H6", "UL", "OL", "LI", "DL", "DT", "DD", "TABLE", "CAPTION", "THEAD",
                "TFOOT", "TBODY", "COLGROUP", "COL", "TR", "TH", "TD");

        for (String tagName : tags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock() || tag.isInline());
        }
    }

    @Test
    public void testCreateInlineTags() {
        List<String> tags = Arrays.asList("SCRIPT", "NOSCRIPT", "STYLE", "META", "LINK", "OBJECT", "PARAM");

        for (String tagName : tags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertTrue(tag.canContainBlock());
        }
    }
}