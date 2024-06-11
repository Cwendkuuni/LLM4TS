import org.jsoup.helper.Validate;
import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public class TagTest {

    private Tag tag;

    @Before
    public void setup() {
        tag = new Tag("testTag");
    }

    @Test
    public void testName() {
        String tagName = "testTag";
        String actual = tag.getName();
        assertEquals(tagName, actual);
    }

    @Test
    public void testValueOfWithSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag expected = tag;
        Tag actual = Tag.valueOf("testTag", settings);
        assertEquals(expected, actual);
    }

    @Test
    public void testValueOf() {
        Tag expected = tag;
        Tag actual = Tag.valueOf("testTag");
        assertEquals(expected, actual);
    }

    @Test
    public void testIsBlock() {
        boolean expected = true;
        boolean actual = tag.isBlock();
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatAsBlock() {
        boolean expected = true;
        boolean actual = tag.formatAsBlock();
        assertEquals(expected, actual);
    }

    @Test
    public void testCanContainBlock() {
        boolean expected = true;
        boolean actual = tag.canContainBlock();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsInline() {
        boolean expected = false;
        boolean actual = tag.isInline();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsData() {
        boolean expected = false;
        boolean actual = tag.isData();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty() {
        boolean expected = false;
        boolean actual = tag.isEmpty();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsSelfClosing() {
        boolean expected = false;
        boolean actual = tag.isSelfClosing();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsKnownTag() {
        boolean expected = false;
        boolean actual = tag.isKnownTag();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsKnownTagWithTagName() {
        String tagName = "testTag";
        boolean expected = false;
        boolean actual = Tag.isKnownTag(tagName);
        assertEquals(expected, actual);
    }

    @Test
    public void testPreserveWhitespace() {
        boolean expected = false;
        boolean actual = tag.preserveWhitespace();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsFormListed() {
        boolean expected = false;
        boolean actual = tag.isFormListed();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsFormSubmittable() {
        boolean expected = false;
        boolean actual = tag.isFormSubmittable();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetSelfClosing() {
        Tag expected = tag;
        Tag actual = tag.setSelfClosing();
        assertEquals(expected, actual);
        assertTrue(actual.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("testTag");
        Tag tag2 = new Tag("testTag");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("testTag");
        Tag tag2 = new Tag("testTag");
        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        String expected = "testTag";
        String actual = tag.toString();
        assertEquals(expected, actual);
    }
}