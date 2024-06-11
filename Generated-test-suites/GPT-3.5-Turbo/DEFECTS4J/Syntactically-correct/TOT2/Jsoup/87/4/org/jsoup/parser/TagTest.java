import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }
    
    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("p", ParseSettings.preserveCase);
        assertNotNull(tag);
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("test");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("unknown"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("test");
        assertFalse(tag.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("test");
        assertFalse(tag.isFormSubmittable());
    }

}