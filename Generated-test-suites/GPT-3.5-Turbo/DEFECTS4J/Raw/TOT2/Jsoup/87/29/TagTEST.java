import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {
    
    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("testTag");
    }

    @Test
    public void testGetName() {
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag parsedTag = Tag.valueOf("p");
        
        assertNotNull(parsedTag);
        assertTrue(parsedTag.isBlock());
        assertFalse(parsedTag.isEmpty());
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertFalse(tag.isFormSubmittable());
    }
    
}