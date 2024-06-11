import org.junit.Test;
import org.jsoup.parser.Tag;
import org.jsoup.parser.TagRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testValueOfValidTag() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.tagName);
        assertTrue(tag.isInline());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertNull(tag.ancestors);
    }

    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("XXX");
        assertEquals("XXX", tag.tagName);
        assertFalse(tag.isBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.optionalClosing);
        assertNotNull(tag.ancestors);
        assertTrue(tag.ancestors.isEmpty());
    }

    @Test
    public void testCanContainEmpty() {
        Tag parent = new Tag("p");
        Tag child = new Tag("br");

        assertTrue(parent.canContain(child));
    }

    @Test
    public void testCannotContainSameTag() {
        Tag parent = new Tag("p");
        Tag child = new Tag("p");

        assertFalse(parent.canContain(child));
    }

    @Test
    public void testCannotContainBlockInInline() {
        Tag parent = new Tag("span");
        Tag child = new Tag("div");

        assertFalse(parent.canContain(child));
    }

    @Test
    public void testCannotContainInlineInBlock() {
        Tag parent = new Tag("p");
        Tag child = new Tag("img");

        assertFalse(parent.canContain(child));
    }

    @Test
    public void testInvalidParent() {
        Tag child = new Tag("p");
        Tag parent = new Tag("h1");

        assertFalse(child.isValidParent(parent));
    }

    @Test
    public void testValidParent() {
        Tag child = new Tag("p");
        Tag parent = new Tag("div");

        assertTrue(child.isValidParent(parent));
    }

    @Test
    public void testEqualsObjects() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEqualsDifferentTags() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("div");

        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testHashCodeEquality() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testHashCodeNonEquality() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("div");

        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }

    @Test
    public void testCreateBlockTags() {
        Tag html = Tag.createBlock("HTML");
        Tag head = Tag.createBlock("HEAD");
        Tag body = Tag.createBlock("BODY");
        Tag frameSet = Tag.createBlock("FRAMESET");

        assertSame(html, tagRegistry.getValueOf("HTML"));
        assertSame(head, tagRegistry.getValueOf("HEAD"));
        assertSame(body, tagRegistry.getValueOf("BODY"));
        assertSame(frameSet, tagRegistry.getValueOf("FRAMESET"));
    }

    @Test
    public void testCreateInlineTags() {
        Tag font = Tag.createInline("FONT");
        Tag tt = Tag.createInline("TT");
        Tag i = Tag.createInline("I");
        Tag b = Tag.createInline("B");
        Tag big = Tag.createInline("BIG");
        Tag small = Tag.createInline("SMALL");

        assertSame(font, tagRegistry.getValueOf("FONT"));
        assertSame(tt, tagRegistry.getValueOf("TT"));
        assertSame(i, tagRegistry.getValueOf("I"));
        assertSame(b, tagRegistry.getValueOf("B"));
        assertSame(big, tagRegistry.getValueOf("BIG"));
        assertSame(small, tagRegistry.getValueOf("SMALL"));
    }

    @Test
    public void testRegisterTags() {
        Tag p = new Tag("p");
        Tag h1 = new Tag("h1");

        TagRegistry tagRegistry = new TagRegistry();
        TagRegistry.register(tagRegistry, p);
        TagRegistry.register(tagRegistry, h1);

        assertSame(p, tagRegistry.getValueOf("p"));
        assertSame(h1, tagRegistry.getValueOf("h1"));
    }

    @Test
    public void testSetContainInlineOnly() {
        Tag tag = new Tag("p");
        tag.setContainInlineOnly();

        assertFalse(tag.canContainBlock());
        assertTrue(tag.canContainInline());
    }

    @Test
    public void testSetContainDataOnly() {
        Tag tag = new Tag("script");
        tag.setContainDataOnly();

        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertTrue(tag.preserveWhitespace);
    }

    @Test
    public void testSetEmpty() {
        Tag tag = new Tag("img");
        tag.setEmpty();

        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testSetOptionalClosing() {
        Tag tag = new Tag("p");
        tag.setOptionalClosing();

        assertTrue(tag.optionalClosing);
    }

    @Test
    public void testSetPreserveWhitespace() {
        Tag tag = new Tag("pre");
        tag.setPreserveWhitespace();

        assertTrue(tag.preserveWhitespace);
    }

    @Test
    public void testSetAncestor() {
        Tag p = new Tag("p");
        Tag div = new Tag("div");

        p.setAncestor("div");
        assertEquals(Arrays.asList(new Tag("div")), p.ancestors);

        p.setAncestor("body", "div");
        assertEquals(Arrays.asList(new Tag("body"), new Tag("div")), p.ancestors);
    }

    @Test
    public void testSetEmptyAncestors() {
        Tag tag = new Tag("p");
        tag.setAncestor(Collections.emptyList());

        assertNull(tag.ancestors);
    }
}