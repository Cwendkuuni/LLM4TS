import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("parent");
        Tag childBlock = Tag.valueOf("childBlock");
        childBlock.isBlock = true;
        Tag childInline = Tag.valueOf("childInline");
        childInline.isBlock = false;
        assertTrue(parent.canContain(childBlock));
        assertFalse(parent.canContain(childInline));
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = new Tag("block");
        assertTrue(blockTag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag blockTag = new Tag("blockTag");
        assertTrue(blockTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag inlineTag = new Tag("inlineTag");
        inlineTag.isBlock = false;
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag dataTag = new Tag("dataTag");
        dataTag.canContainInline = false;
        assertFalse(dataTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag emptyTag = new Tag("emptyTag");
        emptyTag.setEmpty();
        assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag whitespaceTag = new Tag("whitespaceTag");
        whitespaceTag.setPreserveWhitespace();
        assertTrue(whitespaceTag.preserveWhitespace());
    }

}