package org.jsoup.parser;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {
    
    private ParseSettings parseSettings;
    
    @Before
    public void setup() {
        parseSettings = new ParseSettings(false, false);
    }
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testValueOf_caseSensitive() {
        Tag tag = Tag.valueOf("P");
        assertEquals("P", tag.getName());
    }
    
    @Test
    public void testValueOf_caseInsensitive() {
        Tag tag = Tag.valueOf("P", parseSettings);
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag());
    }
    
    @Test
    public void testIsKnownTag_notKnown() {
        Tag tag = Tag.valueOf("xyz");
        assertFalse(tag.isKnownTag());
    }
    
    @Test
    public void testIsKnownTag_caseSensitive() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag("p"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormSubmittable());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("p");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
}