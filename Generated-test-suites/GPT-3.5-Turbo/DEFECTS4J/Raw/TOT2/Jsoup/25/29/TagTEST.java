
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNullTagName() {
        Tag.valueOf(null);
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("img");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }
}
