
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("testTag");
        tag.isBlock = true;
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("testTag");
        tag.formatAsBlock = true;
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("testTag");
        tag.canContainBlock = true;
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = new Tag("testTag");
        tag.isBlock = false;
        assertTrue(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("testTag");
        tag.canContainInline = false;
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("testTag");
        tag.empty = true;
        assertTrue(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("testTag");
        tag.empty = true;
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("testTag"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("testTag");
        tag.preserveWhitespace = true;
        assertTrue(tag.preserveWhitespace());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("testTag");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("testTag");
        Tag tag2 = new Tag("testTag");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag".hashCode(), tag.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.toString());
    }
}
