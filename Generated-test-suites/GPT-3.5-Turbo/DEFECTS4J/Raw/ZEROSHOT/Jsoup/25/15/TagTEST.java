import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("span");
        
        assertTrue(tag1.isKnownTag());
        assertFalse(tag2.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.isBlock());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.formatAsBlock());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.canContainBlock());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isInline());
        
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isData());
        
        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag2.isData());
        
        Tag tag3 = Tag.valueOf("p");
        assertTrue(tag3.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isEmpty());
        
        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isSelfClosing());
        
        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("span"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("pre");
        assertTrue(tag1.preserveWhitespace());
        
        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.preserveWhitespace());
    }
}