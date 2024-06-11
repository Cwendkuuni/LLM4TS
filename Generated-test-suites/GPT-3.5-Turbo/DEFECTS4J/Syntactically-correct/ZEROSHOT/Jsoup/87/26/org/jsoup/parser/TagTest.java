import org.jsoup.parser.ParseSettings;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("testTagName");
    }

    @Test
    public void getName() {
        assertEquals("testTagName", tag.getName());
    }

    @Test
    public void valueOfWithSettings() {
        Tag result = Tag.valueOf("testTagName", ParseSettings.preserveCase);
        assertEquals(tag, result);
    }

    @Test
    public void valueOf() {
        Tag result = Tag.valueOf("testTagName");
        assertEquals(tag, result);
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
        assertFalse(Tag.isKnownTag("testTagName"));
    }

    @Test
    public void preserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void isFormListed() {
        assertFalse(tag.isFormListed());
    }

    @Test
    public void isFormSubmittable() {
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void setSelfClosing() {
        Tag result = tag.setSelfClosing();
        assertTrue(result.isSelfClosing());
    }

    @Test
    public void equals() {
        Tag otherTag = new Tag("testTagName");
        assertTrue(tag.equals(otherTag));
    }

    @Test
    public void hashCode() {
        Tag otherTag = new Tag("testTagName");
        assertEquals(tag.hashCode(), otherTag.hashCode());
    }

    @Test
    public void toString() {
        assertEquals("testTagName", tag.toString());
    }
}