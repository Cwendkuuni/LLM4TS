import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class CommentTest {
    @Test
    public void testCommentData() {
        String data = "This is a comment.";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testEmptyDataComment() {
        Comment comment = new Comment("");
        assertEquals("", comment.getData());
    }

    // Add other test cases for other methods

    @Test(expected = NullPointerException.class)
    public void testNullDataComment() {
        Comment comment = new Comment(null);
    }

    // Add other test cases for edge cases and exceptions

    @Test
    public void testLongDataComment() {
        String data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testSpecialCharactersDataComment() {
        String data = "This is a comment with <special> characters!";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testLeadingAndTrailingWhiteSpacesDataComment() {
        String data = " This is a comment.";
        Comment comment = new Comment(data);
        assertEquals(" This is a comment.", comment.getData());
    }

    @Test
    public void testXmlDeclarationComment() {
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Comment comment = new Comment(data);
        assertTrue(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructorWithNullBaseUriComment() {
        String data = "This is a comment.";
        Comment comment = new Comment(data, null);
    }

    // Add other test cases for other methods
}