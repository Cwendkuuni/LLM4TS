import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
  
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }
  
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
  
        tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
    }
  
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.formatAsBlock());
  
        tag = Tag.valueOf("span");
        assertTrue(tag.formatAsBlock());
    }
  
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
  
        tag = Tag.valueOf("span");
        assertTrue(tag.canContainBlock());
    }
  
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());
  
        tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }
  
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isData());
  
        tag = Tag.valueOf("span");
        assertFalse(tag.isData());
    }
  
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());
  
        tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }
  
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
  
        tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
    }
  
    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("custom"));
  
        assertTrue(Tag.isKnownTag("div"));
    }
  
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());
  
        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
}