import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_nullMessage() {
        String message = null;
        String expectedResult = null;
        String result = HTMLFilter.filter(message);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilter_emptyMessage() {
        String message = "";
        String expectedResult = "";
        String result = HTMLFilter.filter(message);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilter_noSpecialCharacters() {
        String message = "This is a test message";
        String expectedResult = "This is a test message";
        String result = HTMLFilter.filter(message);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilter_withSpecialCharacters() {
        String message = "<script>alert('This is a test');</script>";
        String expectedResult = "&lt;script&gt;alert(&#039;This is a test&#039;);&lt;/script&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilter_multipleSpecialCharacters() {
        String message = "<h1>Title</h1><p>This is a paragraph</p>";
        String expectedResult = "&lt;h1&gt;Title&lt;/h1&gt;&lt;p&gt;This is a paragraph&lt;/p&gt;";
        String result = HTMLFilter.filter(message);
        assertEquals(expectedResult, result);
    }
}