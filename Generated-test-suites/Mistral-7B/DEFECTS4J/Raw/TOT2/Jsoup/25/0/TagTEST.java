import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testTagConstructor() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
    }

    // ... (Other test cases for getter methods)
}