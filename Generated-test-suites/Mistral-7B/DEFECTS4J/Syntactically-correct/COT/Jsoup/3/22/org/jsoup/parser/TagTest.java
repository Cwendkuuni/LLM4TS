package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class TagTest {

    @Test
    public void testTagNameGetters() {
        Tag tag = new Tag("TEST");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("DIV");
        assertEquals("div", tag.getName());

        Tag unknownTag = Tag.valueOf("UNKNOWN");
        assertFalse(unknownTag.isBlock());
        assertEquals(Arrays.asList("UNKNOWN"), unknownTag.ancestors);
    }

    @Test
    public void testCanContain() {
        Tag pTag = new Tag("P");
        Tag divTag = new Tag("DIV");
        Tag spanTag = new Tag("SPAN");

        assertTrue(pTag.canContain(divTag));
        assertTrue(pTag.canContain(spanTag));
        assertFalse(spanTag.canContain(divTag));
        assertFalse(pTag.canContain(pTag));
    }

    @Test
    public void testIsBlockAndIsInline() {
        Tag pTag = new Tag("P");
        Tag divTag = new Tag("DIV");

        assertTrue(pTag.isBlock());
        assertTrue(divTag.isBlock());
        assertFalse(divTag.isInline());
        assertFalse(pTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag pTag = new Tag("P");
        Tag scriptTag = new Tag("SCRIPT");

        assertFalse(pTag.isData());
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = new Tag("IMG");
        Tag pTag = new Tag("P");

        assertFalse(imgTag.isEmpty());
        assertFalse(pTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = new Tag("PRE");
        Tag pTag = new Tag("P");

        assertFalse(pTag.preserveWhitespace());
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag pTag = new Tag("P").setAncestor("DIV");
        List<Tag> expectedAncestors = Arrays.asList(new Tag("DIV"), new Tag("BODY"), new Tag("HTML"));
        assertEquals(expectedAncestors, pTag.getAncestors());
        assertEquals(expectedAncestors.get(0), pTag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag divTag = new Tag("DIV");
        List<Tag> childAncestors = Arrays.asList(new Tag("HTML"), new Tag("BODY"), new Tag("DIV"));
        Tag pTag = new Tag("P").setAncestors(childAncestors);

        assertTrue(divTag.isValidParent(pTag));
        assertTrue(pTag.isValidParent(divTag));
        assertFalse(divTag.isValidParent(pTag));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullTagName() {
        new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyTagName() {
        new Tag("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorTagNameWithEmptyList() {
        new Tag("DIV", new String[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorTagNameWithNonEmptyList() {
        new Tag("DIV", Arrays.asList("HTML", "BODY"));
    }

    @Test(expected = NullPointerException.class)
    public void testValueOfNullTagName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyTagName() {
        Tag.valueOf("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfTagNameWithEmptyList() {
        Tag tag = Tag.valueOf("DIV", new String[0]);
        assertFalse(tag.isBlock());
        assertEquals(Arrays.asList(), tag.ancestors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfTagNameWithNonEmptyList() {
        Tag tag = Tag.valueOf("DIV", Arrays.asList("HTML", "BODY"));
        assertFalse(tag.isBlock());
        assertEquals(Arrays.asList("HTML", "BODY"), tag.ancestors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanContainNullTag() {
        Tag pTag = new Tag("P");
        pTag.canContain(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanContainSelf() {
        Tag pTag = new Tag("P");
        assertFalse(pTag.canContain(pTag));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("DIV");
        Tag tag2 = new Tag("DIV");
        assertEquals(tag1, tag2);

        Tag tag3 = new Tag("SPAN");
        assertFalse(tag1.equals(tag3));

        Tag tag4 = new Tag("DIV");
        tag4.ancestors = Arrays.asList("HTML", "BODY");
        assertFalse(tag1.equals(tag4));

        Tag tag5 = new Tag("DIV");
        tag5.setAncestor("HTML");
        assertEquals(tag1, tag5);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("DIV");
        Tag tag2 = new Tag("DIV");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = new Tag("SPAN");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());

        Tag tag4 = new Tag("DIV");
        tag4.ancestors = Arrays.asList("HTML", "BODY");
        assertNotEquals(tag1.hashCode(), tag4.hashCode());

        Tag tag5 = new Tag("DIV");
        tag5.setAncestor("HTML");
        assertEquals(tag1.hashCode(), tag5.hashCode());
    }

    @Test
    public void testRegisterTag() {
        Tag tag = new Tag("TAG");
        synchronized (Map.class) {
            Map<String, Tag> tags = Tag.tags;
            assertNull(tags.get("TAG"));
            assertEquals(tag, Tag.register(tag));
            assertEquals(tag, tags.get("TAG"));
        }
    }

    @Test
    public void testRegisterEmptyListAncestors() {
        Tag tag = new Tag("TAG").setAncestors(Collections.emptyList());
        assertEquals(Collections.emptyList(), tag.ancestors);
    }

    @Test
    public void testRegisterNonEmptyListAncestors() {
        Tag tag = new Tag("TAG").setAncestors(Arrays.asList("HTML", "BODY"));
        assertEquals(Arrays.asList("HTML", "BODY"), tag.ancestors);
    }

    @Test
    public void testCreateBlock() {
        Tag htmlTag = Tag.createBlock("HTML");
        List<Tag> expectedAncestors = Arrays.asList(new Tag("HTML"));
        assertEquals(expectedAncestors, htmlTag.ancestors);
        assertFalse(htmlTag.isBlock());
    }

    @Test
    public void testCreateInline() {
        Tag fontStyleTag = Tag.createInline("FONT");
        List<Tag> expectedAncestors = new ArrayList<>();
        assertEquals(expectedAncestors, fontStyleTag.ancestors);
        assertFalse(fontStyleTag.isBlock());
    }

    @Test
    public void testCreateBlockWithOptionalClosing() {
        Tag aTag = Tag.createInline("A").setOptionalClosing();
        List<Tag> expectedAncestors = Arrays.asList(new Tag("HTML"), new Tag("BODY"));
        assertEquals(expectedAncestors, aTag.ancestors);
        assertTrue(aTag.optionalClosing);
    }

    @Test
    public void testCreateBlockAndSetAncestor() {
        Tag divTag = Tag.createBlock("DIV");
        divTag.setAncestor("BODY");
        List<Tag> expectedAncestors = Arrays.asList("BODY");
        assertEquals(expectedAncestors, divTag.ancestors);
    }

    @Test
    public void testCreateBlockWithAncestors() {
        Tag divTag = Tag.createBlock("DIV").setAncestors(Arrays.asList("HTML", "BODY"));
        List<Tag> expectedAncestors = Arrays.asList("HTML", "BODY");
        assertEquals(expectedAncestors, divTag.ancestors);
    }

    @Test
    public void testRegisterTagWithAncestor() {
        Tag divTag = new Tag("DIV").setAncestor("BODY");
        assertEquals(Arrays.asList("BODY"), divTag.ancestors);
    }

    @Test
    public void testSetContainInlineOnly() {
        Tag divTag = new Tag("DIV");
        divTag.setContainInlineOnly();
        assertFalse(divTag.canContainBlock());
        assertTrue(divTag.canContainInline());
    }

    @Test
    public void testSetContainDataOnly() {
        Tag scriptTag = new Tag("SCRIPT");
        scriptTag.setContainDataOnly();
        assertFalse(scriptTag.canContainBlock());
        assertFalse(scriptTag.canContainInline());
        assertTrue(scriptTag.preserveWhitespace);
    }

    @Test
    public void testSetEmpty() {
        Tag imgTag = new Tag("IMG");
        imgTag.setEmpty();
        assertFalse(imgTag.canContainBlock());
        assertFalse(imgTag.canContainInline());
        assertTrue(imgTag.empty);
    }

    @Test
    public void testSetOptionalClosing() {
        Tag pTag = new Tag("P");
        pTag.setOptionalClosing();
        assertTrue(pTag.optionalClosing);
    }

    @Test
    public void testSetPreserveWhitespace() {
        Tag preTag = new Tag("PRE");
        preTag.setPreserveWhitespace();
        assertTrue(preTag.preserveWhitespace);
    }

    @Test
    public void testSetAncestor() {
        Tag divTag = new Tag("DIV");
        List<Tag> expectedAncestors = Arrays.asList("BODY");
        divTag.setAncestor("BODY");
        assertEquals(expectedAncestors, divTag.ancestors);

        divTag.setAncestor(Arrays.asList("HTML", "BODY"));
        List<Tag> updatedAncestors = Arrays.asList("HTML", "BODY");
        assertEquals(updatedAncestors, divTag.ancestors);
    }
}