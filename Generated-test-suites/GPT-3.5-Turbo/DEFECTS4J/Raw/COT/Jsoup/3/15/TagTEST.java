import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {
    
    private Tag tag;
    
    @BeforeEach
    public void setUp() {
        tag = new Tag("TAG");
    }

    @Test
    public void testGetName() {
        assertEquals("TAG", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
        assertEquals("p", tag.getName());
    }
    
    @Test(expected = NullPointerException.class)
    public void testValueOfWithNull() {
        Tag tag = Tag.valueOf(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWithEmptyString() {
        Tag tag = Tag.valueOf("");
    }
    
    @Test
    public void testCanContain() {
        Tag child = new Tag("CHILD");
        assertTrue(tag.canContain(child));
    }
    
    @Test
    public void testCanContainWithBlockChild() {
        Tag child = new Tag("CHILD");
        child.isBlock = true;
        tag.canContainBlock = false;
        assertFalse(tag.canContain(child));
    }
    
    @Test
    public void testCanContainWithInlineChild() {
        Tag child = new Tag("CHILD");
        child.isBlock = false;
        tag.canContainInline = false;
        assertFalse(tag.canContain(child));
    }
    
    // Add more test cases for the remaining methods
    
}