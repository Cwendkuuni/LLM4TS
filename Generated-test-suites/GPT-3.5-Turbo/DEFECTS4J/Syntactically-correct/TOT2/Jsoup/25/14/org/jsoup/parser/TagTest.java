import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("script");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("script"));
        assertFalse(Tag.isKnownTag("unknown"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
}