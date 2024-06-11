import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("BODY");
        assertEquals("BODY", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag parent1 = new Tag("BODY");
        Tag child1 = new Tag("div");
        assertTrue(parent1.canContain(child1));

        Tag parent2 = new Tag("p");
        Tag child2 = new Tag("span");
        assertTrue(parent2.canContain(child2));

        Tag parent3 = new Tag("p");
        Tag child3 = new Tag("div");
        assertFalse(parent3.canContain(child3));
    }

    @Test
    public void testisBlock() {
        Tag tag = new Tag("p");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testcanContainBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testisInline() {
        Tag tag = new Tag("p");
        assertTrue(tag.isInline());
    }

    @Test
    public void testisData() {
        Tag tag1 = new Tag("p");
        assertFalse(tag1.isData());

        Tag tag2 = new Tag("textarea");
        assertTrue(tag2.isData());

        Tag tag3 = new Tag("img");
        assertFalse(tag3.isData());
    }

    @Test
    public void testisEmpty() {
        Tag tag1 = new Tag("p");
        assertFalse(tag1.isEmpty());

        Tag tag2 = new Tag("img");
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testpreserveWhitespace() {
        Tag tag1 = new Tag("p");
        assertFalse(tag1.preserveWhitespace());

        Tag tag2 = new Tag("pre");
        assertTrue(tag2.preserveWhitespace());
    }

    @Test
    public void testgetImplicitParent() {
        Tag parent = new Tag("p");
        Tag child = new Tag("span");
        child.setAncestor(parent.getName());

        assertEquals(parent, child.getImplicitParent());
    }

    @Test
    public void testisValidParent() {
        Tag parent1 = new Tag("BODY");
        Tag child1 = new Tag("div");
        assertTrue(parent1.isValidParent(child1));

        Tag parent2 = new Tag("p");
        Tag child2 = new Tag("span");
        assertFalse(parent2.isValidParent(child2));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));

        Tag tag3 = new Tag("p");
        Tag tag4 = new Tag("span");
        assertFalse(tag3.equals(tag4));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.toString());
    }

    @Test
    public void testTagsInitialization() {
        Tag tag = Tag.valueOf("unknown");
        assertEquals("unknown", tag.getName());

        Tag defaultAncestor = tag.getImplicitParent();
        assertEquals("BODY", defaultAncestor.getName());
    }
}