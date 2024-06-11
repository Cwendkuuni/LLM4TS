
import org.junit.*;

public class TagTest {

    // Testing getName() method

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        Assert.assertEquals("p", tag.getName());
    }

    // Testing valueOf() method

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        Assert.assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        Assert.assertEquals("unknown", tag.getName());
    }

    // Testing canContain() method

    @Test
    public void testCanContain() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("p");

        Assert.assertTrue(tag1.canContain(tag2));
    }

    @Test
    public void testCanContainWithBlockChild() {
        Tag tag1 = new Tag("a");
        Tag tag2 = new Tag("div");

        Assert.assertFalse(tag1.canContain(tag2));
    }

    @Test
    public void testCanContainWithInlineChild() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("a");

        Assert.assertFalse(tag1.canContain(tag2));
    }

    // Testing isBlock() method

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        Assert.assertTrue(tag.isBlock());
    }

    // Testing canContainBlock() method

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        Assert.assertTrue(tag.canContainBlock());
    }

    // Testing isInline() method

    @Test
    public void testIsInline() {
        Tag tag = new Tag("a");
        Assert.assertTrue(tag.isInline());
    }

    // Testing isData() method

    @Test
    public void testIsData() {
        Tag tag = new Tag("script");
        Assert.assertTrue(tag.isData());
    }

    // Testing isEmpty() method

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        Assert.assertTrue(tag.isEmpty());
    }

    // Testing preserveWhitespace() method

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        Assert.assertTrue(tag.preserveWhitespace());
    }

    // Testing getImplicitParent() method

    @Test
    public void testGetImplicitParent() {
        Tag tag1 = new Tag("a");
        Tag tag2 = new Tag("div");

        tag2.addAncestor(tag1);

        Assert.assertEquals(tag1, tag2.getImplicitParent());
    }

    // Testing isValidParent() method

    @Test
    public void testIsValidParentWithValidParent() {
        Tag parent = new Tag("div");
        Tag child = new Tag("a");

        child.addAncestor(parent);

        Assert.assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testIsValidParentWithInvalidParent() {
        Tag parent = new Tag("p");
        Tag child = new Tag("a");

        child.addAncestor(new Tag("div"));

        Assert.assertFalse(parent.isValidParent(child));
    }

    @Test
    public void testIsValidParentWithNoAncestors() {
        Tag parent = new Tag("div");
        Tag child = new Tag("a");

        Assert.assertTrue(parent.isValidParent(child));
    }
}

