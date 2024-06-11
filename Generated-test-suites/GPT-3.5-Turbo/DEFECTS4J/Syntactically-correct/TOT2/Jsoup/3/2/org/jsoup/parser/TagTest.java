import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(false, tag1.isBlock());
        assertEquals(true, tag2.isBlock());
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("parent");
        Tag child1 = new Tag("child1");
        Tag child2 = new Tag("child2");

        parent.setContainInlineOnly();
        child1.setContainInlineOnly();
        child2.setContainDataOnly();

        assertEquals(true, parent.canContain(child1));
        assertEquals(false, parent.canContain(child2));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("testBlock");
        assertEquals(true, tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("testCanContainBlock");
        tag.setContainInlineOnly();
        assertEquals(false, tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("testInline");
        tag.setContainInlineOnly();
        assertEquals(true, tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("testData");
        tag.setContainDataOnly();
        assertEquals(true, tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("testEmpty");
        tag.setEmpty();
        assertEquals(true, tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("testPreserveWhitespace");
        tag.setPreserveWhitespace();
        assertEquals(true, tag.preserveWhitespace());
    }

}