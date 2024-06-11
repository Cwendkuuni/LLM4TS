
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("Test");
        assertEquals("Test", tag.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testValueOfInvalidTagName() {
        Tag.valueOf(null);
    }

    @Test
    public void testIsDataForEmptyTagWithInlineContent() {
        Tag tag = new Tag("Test");
        tag.setEmpty();
        tag.setContainInlineOnly();
        assertFalse(tag.isData());
    }
}
