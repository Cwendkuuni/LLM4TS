
import org.jsoup.parser.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p", ParseSettings.preserveCase);
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = new Tag("p");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("p");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("p");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("p");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("random"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("p");
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("p");
        assertFalse(tag.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("p");
        assertFalse(tag.isFormSubmittable());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("p");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }
  
}
