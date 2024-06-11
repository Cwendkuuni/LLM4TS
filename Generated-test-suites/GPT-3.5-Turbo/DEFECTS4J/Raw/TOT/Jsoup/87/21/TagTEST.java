import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    
    private ParseSettings parseSettings;
    
    @Before
    public void setUp() {
        parseSettings = ParseSettings.htmlDefault; // using default parse settings
    }
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div", parseSettings);
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        // Testing an unknown tag
        Tag tag1 = Tag.valueOf("unknownTag");
        assertTrue(!tag1.isBlock() && !tag1.formatAsBlock() && tag1.isInline());
        
        // Testing a known tag
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag2.isBlock() && tag2.formatAsBlock() && !tag2.isInline());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isData());
        
        tag = Tag.valueOf("script");
        assertTrue(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
        
        tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
        
        tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
        
        tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormListed());
        
        tag = Tag.valueOf("div");
        assertFalse(tag.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormSubmittable());
        
        tag = Tag.valueOf("div");
        assertFalse(tag.isFormSubmittable());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
        
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
        
        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag1.equals(tag3));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        Tag tag3 = Tag.valueOf("span");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
    
}