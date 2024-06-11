import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("testTag");
    }

    @Test
    public void testGetName() {
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf_knownTag() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());
        assertFalse(pTag.canContainBlock());
    }

    @Test
    public void testValueOf_unknownTag() {
        Tag newTag = Tag.valueOf("newTag");
        assertFalse(newTag.isBlock());
        assertTrue(newTag.canContainBlock());
    }

    @Test
    public void testCanContain_knownTag() {
        Tag bodyTag = Tag.valueOf("body");
        Tag divTag = Tag.valueOf("div");
        assertTrue(bodyTag.canContain(divTag));
    }

    @Test
    public void testCanContain_unknownTag() {
        Tag unknownTag = new Tag("unknown");
        Tag divTag = Tag.valueOf("div");
        assertFalse(unknownTag.canContain(divTag));
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
    public void testGetImplicitParent_noParent() {
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testGetImplicitParent_withParent() {
        Tag parentTag = new Tag("parent");
        tag.setAncestor("parent");
        assertEquals(parentTag, tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent_validParent() {
        Tag parentTag = new Tag("parent");
        tag.setAncestor("parent");
        assertTrue(tag.isValidParent(parentTag));
    }

    @Test
    public void testIsValidParent_invalidParent() {
        Tag parentTag = new Tag("parent");
        assertFalse(tag.isValidParent(parentTag));
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(tag.equals(tag));
    }

    @Test
    public void testEquals_nullObject() {
        assertFalse(tag.equals(null));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(tag.equals("testTag"));
    }

    @Test
    public void testEquals_differentTagName() {
        Tag newTag = new Tag("newTag");
        assertFalse(tag.equals(newTag));
    }

    @Test
    public void testHashCode() {
        Tag newTag = new Tag("testTag");
        assertEquals(tag.hashCode(), newTag.hashCode());
    }

}