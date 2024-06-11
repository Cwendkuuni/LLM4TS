import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testDoctype_getName() {
        Doctype doctype = new Doctype();
        doctype.name("html");
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testTag_name() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        assertEquals("div", startTag.name());
    }

    @Test
    public void testComment_getData() {
        Comment comment = new Comment();
        comment.data("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }
}