package org.jsoup.parser;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

/**
 * Tag test file using JUnit 4.
 */
public class TagTest {

    private static Tag htmlTag;
    private static Tag bodyTag;

    @BeforeClass
    public static void setUp() {
        htmlTag = Tag.valueOf("HTML");
        bodyTag = Tag.valueOf("BODY");
    }

    @Test
    public void testTagName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("HTML");
        assertEquals(htmlTag, tag);

        tag = Tag.valueOf("BODY");
        assertEquals(bodyTag, tag);

        tag = Tag.valueOf("unknownTag");
        assertNotSame(htmlTag, tag);
        assertNotSame(bodyTag, tag);
        assertEquals("unknownTag", tag.getName());
    }

    @Test
    public void testCanContain() {
        // Test that HTML can contain any tag
        for (Tag tag : Tag.values()) {
            assertTrue(htmlTag.canContain(tag));
        }

        // Test that body can contain most tags
        for (Tag tag : Tag.values()) {
            if (tag.isEmpty() || tag.isData() || tag.isBlock() && !bodyTag.canContainBlock()) {
                continue;
            }
            assertTrue(bodyTag.canContain(tag));
        }

        // Test that some tags cannot contain others
        Tag pTag = Tag.valueOf("P");
        Tag h1Tag = Tag.valueOf("H1");
        assertFalse(pTag.canContain(h1Tag));

        Tag imgTag = Tag.valueOf("IMG");
        assertFalse(bodyTag.canContain(imgTag));
        assertFalse(imgTag.canContain(bodyTag));
    }

    @Test
    public void testIsBlock() {
        assertTrue(htmlTag.isBlock());
        assertTrue(bodyTag.isBlock());

        Tag pTag = Tag.valueOf("P");
        assertTrue(pTag.isBlock());

        Tag imgTag = Tag.valueOf("IMG");
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(htmlTag.canContainBlock());
        assertTrue(bodyTag.canContainBlock());

        Tag pTag = Tag.valueOf("P");
        assertTrue(pTag.canContainBlock());

        Tag imgTag = Tag.valueOf("IMG");
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(htmlTag.isInline());
        assertFalse(bodyTag.isInline());

        Tag pTag = Tag.valueOf("P");
        assertFalse(pTag.isInline());

        Tag imgTag = Tag.valueOf("IMG");
        assertTrue(imgTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag metaTag = Tag.valueOf("META");
        assertTrue(metaTag.isData());

        Tag pTag = Tag.valueOf("P");
        assertFalse(pTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("IMG");
        assertTrue(imgTag.isEmpty());

        Tag pTag = Tag.valueOf("P");
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("PRE");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("P");
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag pTag = Tag.valueOf("P");
        Tag bodyTag = pTag.getImplicitParent();
        assertEquals(bodyTag.getName(), "BODY");
    }

    @Test
    public void testIsValidParent() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        assertTrue(pTag.isValidParent(divTag));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidParentWithInvalidParent() {
        Tag pTag = Tag.valueOf("P");
        Tag pTag2 = Tag.valueOf("P");
        pTag.isValidParent(pTag2);
    }

    @Test
    public void testEquals() {
        Tag pTag1 = Tag.valueOf("P");
        Tag pTag2 = Tag.valueOf("P");
        assertTrue(pTag1.equals(pTag2));

        Tag pTag3 = Tag.valueOf("DIV");
        assertFalse(pTag1.equals(pTag3));

        Tag pTag4 = new Tag("P");
        assertNotEquals(pTag1, pTag4);
    }

    @Test
    public void testHashCode() {
        Tag pTag1 = Tag.valueOf("P");
        Tag pTag2 = Tag.valueOf("P");
        assertEquals(pTag1.hashCode(), pTag2.hashCode());

        Tag pTag3 = Tag.valueOf("DIV");
        int hashCode1 = pTag1.hashCode();
        int hashCode2 = pTag3.hashCode();
        assertNotEquals(hashCode1, hashCode2);

        Tag pTag4 = new Tag("P");
        int hashCode3 = pTag1.hashCode();
        int hashCode4 = pTag4.hashCode();
        assertNotEquals(hashCode3, hashCode4);
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("P");
        assertEquals(pTag.toString(), "P");
    }

    @Test
    public void testCreateBlock() {
        Tag htmlTag = Tag.createBlock("HTML");
        assertEquals(htmlTag.getName(), "HTML");
        assertEquals(htmlTag.isBlock(), true);

        Tag bodyTag = Tag.createBlock("BODY");
        assertEquals(bodyTag.getName(), "BODY");
        assertEquals(bodyTag.isBlock(), true);

        Tag h1Tag = Tag.createBlock("H1");
        assertEquals(h1Tag.getName(), "H1");
        assertEquals(h1Tag.isBlock(), true);

        Tag ulTag = Tag.createBlock("UL");
        assertEquals(ulTag.getName(), "UL");
        assertEquals(ulTag.isBlock(), true);

        Tag spanTag = Tag.createInline("SPAN");
        assertEquals(spanTag.getName(), "SPAN");
        assertEquals(spanTag.isBlock(), false);
    }

    @Test
    public void testCreateInline() {
        Tag imgTag = Tag.createInline("IMG");
        assertEquals(imgTag.getName(), "IMG");
        assertEquals(imgTag.isBlock(), false);

        Tag aTag = Tag.createInline("A");
        assertEquals(aTag.getName(), "A");
        assertEquals(aTag.isBlock(), false);
        assertTrue(aTag.isOptionalClosing());

        Tag inputTag = Tag.createInline("INPUT");
        assertEquals(inputTag.getName(), "INPUT");
        assertEquals(inputTag.isBlock(), false);
    }

    @Test
    public void testRegister() {
        Tag pTag = new Tag("P");
        Tag registeredPTag = Tag.register(pTag);
        assertEquals(pTag, registeredPTag);
    }

    @Test
    public void testSetContainInlineOnly() {
        Tag pTag = Tag.valueOf("P");
        pTag.setContainInlineOnly();
        assertFalse(pTag.canContainBlock());
        assertTrue(pTag.canContainInline());
    }

    @Test
    public void testSetContainDataOnly() {
        Tag metaTag = Tag.valueOf("META");
        metaTag.setContainDataOnly();
        assertFalse(metaTag.canContainBlock());
        assertFalse(metaTag.canContainInline());
        assertTrue(metaTag.preserveWhitespace());
    }

    @Test
    public void testSetEmpty() {
        Tag imgTag = Tag.valueOf("IMG");
        imgTag.setEmpty();
        assertTrue(imgTag.isEmpty());
    }

    @Test
    public void testSetOptionalClosing() {
        Tag aTag = Tag.valueOf("A");
        aTag.setOptionalClosing();
        assertTrue(aTag.isOptionalClosing());
    }

    @Test
    public void testSetPreserveWhitespace() {
        Tag preTag = Tag.valueOf("PRE");
        preTag.setPreserveWhitespace();
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testSetAncestor() {
        Tag divTag = Tag.valueOf("DIV");
        divTag.setAncestor("HTML", "HEAD", "BODY");
        List<Tag> expectedAncestors = Arrays.asList(Tag.valueOf("HTML"), Tag.valueOf("HEAD"), Tag.valueOf("BODY"), Tag.valueOf("DIV"));
        List<Tag> actualAncestors = divTag.getAncestors();
        assertEquals(expectedAncestors, actualAncestors);
    }

    @Test
    public void testGetAncestors() {
        Tag divTag = Tag.valueOf("DIV");
        List<Tag> expectedAncestors = Arrays.asList(Tag.valueOf("BODY"));
        List<Tag> actualAncestors = divTag.getAncestors();
        assertEquals(expectedAncestors, actualAncestors);
    }
}