
import static org.junit.Assert.*;

import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class TagTest {
    
    private Tag testTag;
    
    @Before
    public void setUp() {
        testTag = new Tag("testTag");
    }

    @Test
    public void testGetName() {
        assertEquals("testTag", testTag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("testTag");
        assertEquals(testTag, tag);
        
        tag = Tag.valueOf("unknownTag");
        assertFalse(testTag.equals(tag));
        assertFalse(testTag == tag);
    }
    
    @Test
    public void testIsBlock() {
        assertTrue(testTag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        assertTrue(testTag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        assertTrue(testTag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        assertFalse(testTag.isInline());
    }
    
    @Test
    public void testIsData() {
        assertFalse(testTag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(testTag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        assertFalse(testTag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertFalse(testTag.isKnownTag());
    }
    
    @Test
    public void testIsKnownTagWithKnownTag() {
        assertTrue(Tag.isKnownTag("html"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        assertFalse(testTag.preserveWhitespace());
    }

}
