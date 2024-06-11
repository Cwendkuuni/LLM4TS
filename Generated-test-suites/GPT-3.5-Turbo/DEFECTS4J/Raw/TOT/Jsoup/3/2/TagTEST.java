import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setup() {
        tag = new Tag("div");
    }

    @Test
    public void testGetName() {
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        assertEquals("p", tag1.getName());

        Tag tag2 = Tag.valueOf("nonexistent");
        assertEquals("nonexistent", tag2.getName());

        Tag tag3 = Tag.valueOf("");
        assertEquals("", tag3.getName());
    }

    @Test
    public void testCanContain() {
        assertFalse(tag.canContain(null));

        Tag blockTag1 = new Tag("h1");
        Tag inlineTag1 = new Tag("i");
        assertTrue(tag.canContain(blockTag1));
        assertTrue(tag.canContain(inlineTag1));

        Tag blockTag2 = new Tag("ul");
        Tag inlineTag2 = new Tag("span");
        tag.setContainInlineOnly();
        assertFalse(tag.canContain(blockTag2));
        assertTrue(tag.canContain(inlineTag2));

        Tag blockTag3 = new Tag("p");
        Tag inlineTag3 = new Tag("strong");
        tag.setContainDataOnly();
        assertFalse(tag.canContain(blockTag3));
        assertFalse(tag.canContain(inlineTag3));

        Tag blockTag4 = new Tag("table");
        Tag inlineTag4 = new Tag("b");
        tag.setEmpty();
        assertFalse(tag.canContain(blockTag4));
        assertFalse(tag.canContain(inlineTag4));
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
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
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag childTag1 = new Tag("div");
        Tag childTag2 = new Tag("span");
        assertTrue(tag.isValidParent(childTag1));
        assertTrue(tag.isValidParent(childTag2));

        Tag childTag3 = new Tag("p");
        Tag childTag4 = new Tag("i");
        tag.setContainInlineOnly();
        assertFalse(tag.isValidParent(childTag3));
        assertFalse(tag.isValidParent(childTag4));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        assertTrue(tag.equals(tag1));

        Tag tag2 = new Tag("span");
        assertFalse(tag.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        assertEquals(tag.hashCode(), tag1.hashCode());

        Tag tag2 = new Tag("span");
        assertNotEquals(tag.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("div", tag.toString());
    }

    @Test
    public void testTagInitialization() {
        Tag defaultAncestor = Tag.valueOf("BODY");
        Tag tag1 = Tag.valueOf("h1");
        Tag tag2 = Tag.valueOf("li");
        Tag tag3 = Tag.valueOf("form");

        assertTrue(tag1.canContain(defaultAncestor));
        assertFalse(tag1.isValidParent(tag2));
        assertTrue(tag3.canContain(tag2));
    }
}