package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tag test file
 *
 * @author TestingBot
 */
public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("P");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.optionalClosing());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertEquals(new String[0], tag.getAncestors().toArray(new String[0]));
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("P");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.optionalClosing());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertEquals(new String[0], tag.getAncestors().toArray(new String[0]));

        Tag unknownTag = Tag.valueOf("UNKNOWN_TAG");
        assertNotSame(tag, unknownTag);
        assertEquals("unknown_tag", unknownTag.getName());
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.canContainBlock());
        assertTrue(unknownTag.canContainInline());
        assertFalse(unknownTag.optionalClosing());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.preserveWhitespace());
        assertEquals(new String[0], unknownTag.getAncestors().toArray(new String[0]));
    }

    @Test
    public void testTagCanContain() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        Tag imgTag = Tag.valueOf("IMG");
        Tag h1Tag = Tag.valueOf("H1");

        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(imgTag));
        assertTrue(pTag.canContain(h1Tag));

        Tag headTag = Tag.valueOf("HEAD");
        assertFalse(pTag.canContain(headTag));

        Tag dtTag = Tag.valueOf("DT");
        Tag ddTag = Tag.valueOf("DD");
        assertTrue(dtTag.canContain(ddTag));
        assertTrue(ddTag.canContain(dtTag));

        List<Tag> ancestors = new ArrayList<>();
        ancestors.add(h1Tag);
        Tag h2Tag = new Tag("H2");
        h2Tag.setAncestor(ancestors.toArray(new String[0]));
        assertTrue(pTag.canContain(h2Tag));
    }

    @Test
    public void testTagIsData() {
        Tag pTag = Tag.valueOf("P");
        Tag scriptTag = Tag.valueOf("SCRIPT");
        Tag metaTag = Tag.valueOf("META");

        assertFalse(pTag.isData());
        assertTrue(scriptTag.isData());
        assertTrue(metaTag.isData());
    }

    @Test
    public void testTagEqualsAndHashCode() {
        Tag pTag1 = Tag.valueOf("P");
        Tag pTag2 = Tag.valueOf("P");
        Tag unknownTag = Tag.valueOf("UNKNOWN_TAG");

        assertEquals(pTag1, pTag2);
        assertEquals(pTag1.hashCode(), pTag2.hashCode());

        assertNotEquals(pTag1, unknownTag);
        assertNotEquals(pTag1.hashCode(), unknownTag.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag pTag = Tag.valueOf("P");
        assertEquals("p", pTag.toString());
    }

    @Test
    public void testTagImplicitParentAndIsValidParent() {
        Tag divTag = Tag.valueOf("DIV");
        Tag pTag = Tag.valueOf("P");

        pTag.setAncestor(new String[]{"DIV"});
        assertEquals(divTag, pTag.getImplicitParent());

        assertTrue(divTag.isValidParent(pTag));
        assertTrue(pTag.isValidParent(divTag));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTagCanContainSelf() {
        Tag pTag = Tag.valueOf("P");
        assertTrue(pTag.canContain(pTag));
        fail("Illegal argument exception expected");
    }
}