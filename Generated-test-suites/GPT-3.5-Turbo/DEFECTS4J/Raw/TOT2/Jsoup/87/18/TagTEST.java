import org.jsoup.parser.Tag;
import org.jsoup.parser.ParseSettings;
import org.jsoup.helper.Validate;
import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }
    
    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);
        assertNotNull(tag);
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("testTag");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("testTag");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("testTag");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isKnownTag());
    }
    
    @Test
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("testTag"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isFormSubmittable());
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
        assertEquals(tag.getName().hashCode(), tag.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.toString());
    }
    
}