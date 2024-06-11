import org.junit.Test;

public class TagTest {
    // Test case for getName() method
    @Test
    public void testGetName() {
        Tag tag = new Tag("tag");
        assertEqual(tag.getName(), "tag");
    }

    // Test case for valueOf() method
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("tag");
        assertEqual(tag.getName(), "tag");
    }

    // Test case for isBlock() method
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("tag");
        assertEqual(tag.isBlock(), true);
    }

    // Test case for formatAsBlock() method
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("tag");
        assertEqual(tag.formatAsBlock(), true);
    }

    // Test case for canContainBlock() method
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("tag");
        assertEqual(tag.canContainBlock(), true);
    }

    // Test case for isInline() method
    @Test
    public void testIsInline() {
        Tag tag = new Tag("tag");
        assertEqual(tag.isInline(), false);
    }

    // Test case for isData() method
    @Test
    public void testIsData() {
        Tag tag = new Tag("tag");
        assertEqual(tag.isData(), false);
    }

    // Test case for isEmpty() method
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("tag");
        assertEqual(tag.isEmpty(), false);
    }

    // Test case for isSelfClosing() method
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("tag");
        assertEqual(tag.isSelfClosing(), false);
    }

    // Test case for isKnownTag() method
    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("tag");
        assertEqual(tag.isKnownTag(), false);
    }

    // Test case for isKnownTag(String tagName) method
    @Test
    public void testIsKnownTagWithName() {
        assertEqual(Tag.isKnownTag("tag"), false);
    }

    // Test case for preserveWhitespace() method
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("tag");
        assertEqual(tag.preserveWhitespace(), false);
    }
}