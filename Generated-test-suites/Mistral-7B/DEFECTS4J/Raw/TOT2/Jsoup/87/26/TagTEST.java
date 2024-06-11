import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testValue() {
        String tagName = "p";
        ParseSettings settings = ParseSettings.preserveCase; // Default settings
        Tag tag = Tag.valueOf(tagName, settings);
        String expectedTagName = "p";
        assertEquals(expectedTagName, tag.getName());
    }
}