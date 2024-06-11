import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("test");
    }

    @Test
    public void getName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void valueOf() {
        Tag customTag = Tag.valueOf("custom");
        assertNotNull(customTag);
    }

    @Test
    public void isBlock() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void formatAsBlock() {
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void canContainBlock() {
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void isInline() {
        assertFalse(tag.isInline());
    }

    @Test
    public void isData() {
        assertFalse(tag.isData());
    }

    @Test
    public void isEmpty() {
        assertFalse(tag.isEmpty());
    }

    @Test
    public void isSelfClosing() {
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void isKnownTag() {
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void isKnownTagWithName() {
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void preserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void setSelfClosing() {
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void equals() {
        Tag otherTag = new Tag("test");
        assertEquals(tag, otherTag);
    }

    @Test
    public void hashCodeTest() {
        Tag otherTag = new Tag("test");
        assertEquals(tag.hashCode(), otherTag.hashCode());
    }

    @Test
    public void toStringTest() {
        assertEquals("test", tag.toString());
    }
}