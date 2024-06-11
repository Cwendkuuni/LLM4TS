
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    // Test for getName() method
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    // Test for isBlock() method
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }

    // Test for formatAsBlock() method
    @Test
    public void testFormatAsBlock(){
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.formatAsBlock());

        Tag tag2 = Tag.valueOf("pre");
        assertTrue(tag2.formatAsBlock());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.formatAsBlock());
    }

    // Test for canContainBlock() method
    @Test
    public void testCanContainBlock(){
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());

        Tag tag2 = Tag.valueOf("pre");
        assertTrue(tag2.canContainBlock());

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag3.canContainBlock());
    }

    // Test for isInline() method
    @Test
    public void testIsInline(){
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());

        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isInline());
    }

    // Test for isData() method
    @Test
    public void testIsData(){
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isData());

        Tag tag2 = Tag.valueOf("textarea");
        assertFalse(tag2.isData());

        Tag tag3 = Tag.valueOf("img");
        assertFalse(tag3.isData());

        Tag tag4 = Tag.valueOf("span");
        assertFalse(tag4.isData());
    }

    // Test for isEmpty() method
    @Test
    public void testIsEmpty(){
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isEmpty());

        Tag tag3 = Tag.valueOf("input");
        assertTrue(tag3.isEmpty());
    }

    // Test for isSelfClosing() method
    @Test
    public void testIsSelfClosing(){
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isSelfClosing());

        Tag tag3 = Tag.valueOf("input");
        assertTrue(tag3.isSelfClosing());
    }

    // Test for isKnownTag() method
    @Test
    public void testIsKnownTag(){
        assertFalse(Tag.isKnownTag("div"));

        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isKnownTag());
    }

    // Test for preserveWhitespace() method
    @Test
    public void testPreserveWhitespace(){
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());

        Tag tag2 = Tag.valueOf("pre");
        assertTrue(tag2.preserveWhitespace());

        Tag tag3 = Tag.valueOf("plaintext");
        assertTrue(tag3.preserveWhitespace());
    }
}
