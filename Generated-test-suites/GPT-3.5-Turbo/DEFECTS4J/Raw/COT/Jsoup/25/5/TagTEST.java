import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Tag;
import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("div"); // create a new Tag instance for testing
    }

    @Test
    public void testGetName() {
        String name = tag.getName();
        assertEquals("div", name);
    }

    @Test
    public void testIsBlock() {
        boolean isBlock = tag.isBlock();
        assertTrue(isBlock);
    }

    @Test
    public void testFormatAsBlock() {
        boolean formatAsBlock = tag.formatAsBlock();
        assertTrue(formatAsBlock);
    }

    @Test
    public void testCanContainBlock() {
        boolean canContainBlock = tag.canContainBlock();
        assertTrue(canContainBlock);
    }

    @Test
    public void testIsInline() {
        boolean isInline = tag.isInline();
        assertFalse(isInline);
    }

    @Test
    public void testIsData() {
        boolean isData = tag.isData();
        assertFalse(isData);
    }

    @Test
    public void testIsEmpty() {
        boolean isEmpty = tag.isEmpty();
        assertFalse(isEmpty);
    }

    @Test
    public void testIsSelfClosing() {
        boolean isSelfClosing = tag.isSelfClosing();
        assertFalse(isSelfClosing);
    }

    @Test
    public void testIsKnownTag() {
        boolean isKnownTag = tag.isKnownTag();
        assertFalse(isKnownTag);
    }

    @Test
    public void testPreserveWhitespace() {
        boolean preserveWhitespace = tag.preserveWhitespace();
        assertFalse(preserveWhitespace);
    }

}