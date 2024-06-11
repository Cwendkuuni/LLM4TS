import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {
    @Test
    public void testgetName() {
        Tag tag = new Tag("div");
        String expected = "div";
        String actual = tag.getName();
        assert(expected.equals(actual));
    }
}