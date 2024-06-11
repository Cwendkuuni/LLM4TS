import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    
    @Test
    public void testName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("h1");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("ul");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("strong");
        assertTrue(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("code");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("img");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.isSelfClosing());
        assertFalse(tag2.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        String tagName1 = "p";
        String tagName2 = "foo";
        assertTrue(Tag.isKnownTag(tagName1));
        assertFalse(Tag.isKnownTag(tagName2));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag1 = Tag.valueOf("input");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.isFormListed());
        assertFalse(tag2.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag1 = Tag.valueOf("input");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.isFormSubmittable());
        assertFalse(tag2.isFormSubmittable());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2) && tag2.equals(tag1));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("h2");
        assertEquals("h2", tag.toString());
    }
    
    @Test
    public void testValueOfWithTagNameSettings() {
        Tag tag1 = Tag.valueOf("p", ParseSettings.preserveCase);
        Tag tag2 = Tag.valueOf("P", ParseSettings.preserveCase);
        Tag tag3 = Tag.valueOf("p", ParseSettings.normal);
        assertTrue(tag1.equals(tag2) && tag2.equals(tag3));
    }
    
    @Test
    public void testValueOfWithTagName() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("P");
        assertFalse(tag1.equals(tag2));
    }
}