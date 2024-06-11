
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TagTest {

    // Test getName() method
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    // Test canContain() method
    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("span");
        assertEquals(true, parent.canContain(child));
    }

    // Test isBlock() method
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertEquals(true, tag.isBlock());
    }

    // Test canContainBlock() method
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertEquals(true, tag.canContainBlock());
    }

    // Test isInline() method
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertEquals(true, tag.isInline());
    }

    // Test isData() method
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("script");
        assertEquals(true, tag.isData());
    }

    // Test isEmpty() method
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertEquals(true, tag.isEmpty());
    }

    // Test preserveWhitespace() method
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertEquals(true, tag.preserveWhitespace());
    }

    // Test getImplicitParent() method
    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("p");
        assertEquals(null, tag.getImplicitParent());
    }

    // Test isValidParent() method
    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        assertEquals(true, parent.isValidParent(child));
    }
}

