import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testTagName() {
        Tag tag = new Tag("div");
        String expected = "div";
        String actual = tag.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("p");
        String expected = "p";
        String actual = tag.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        boolean expected = true;
        boolean actual = tag.isBlock();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        boolean expected = false;
        boolean actual = tag.isInline();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("script");
        boolean expected = true;
        boolean actual = tag.isData();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        boolean expected = false;
        boolean actual = tag.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("img");
        boolean expected = false;
        boolean actual = tag.isSelfClosing();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("div");
        boolean expected = true;
        boolean actual = Tag.isKnownTag("div");
        assertEquals(expected, actual);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        boolean expected = true;
        boolean actual = tag.preserveWhitespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("input");
        boolean expected = true;
        boolean actual = tag.isFormListed();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input");
        boolean expected = true;
        boolean actual = tag.isFormSubmittable();
        assertEquals(expected, actual);
    }

    // Edge cases and exception handling tests go here
}