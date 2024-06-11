
import org.jsoup.parser.ParseSettings;
import org.jsoup.helper.Validate;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    private static final Map<String, Tag> tags = new HashMap<>(); // map of known tags
    private Tag tag;

    @Test
    public void testGetName() {
        tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        ParseSettings settings = new ParseSettings();
        tag = Tag.valueOf("div", settings);
        assertNotNull(tag);
    }

    @Test
    public void testValueOfCaseSensitive() {
        tag = Tag.valueOf("P");
        assertNotNull(tag);
    }

    @Test
    public void testIsBlock() {
        tag = new Tag("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        tag = new Tag("p");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        tag = new Tag("p");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        tag = new Tag("p");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        tag = new Tag("p");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        tag = new Tag("img");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        tag = new Tag("p");
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagFalse() {
        tag = new Tag("unknown");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagString() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testIsKnownTagStringFalse() {
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        tag = new Tag("input");
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        tag = new Tag("input");
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        tag = new Tag("div");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        tag = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag.equals(tag2));
    }

    @Test
    public void testHashCode() {
        tag = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        tag = new Tag("p");
        assertEquals("p", tag.toString());
    }
}
