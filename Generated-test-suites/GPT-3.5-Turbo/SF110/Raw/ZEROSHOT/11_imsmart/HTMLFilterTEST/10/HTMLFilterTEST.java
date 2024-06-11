import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullInput_ReturnNull() {
        String result = HTMLFilter.filter(null);
        assertEquals(null, result);
    }

    @Test
    public void testFilter_EmptyInput_ReturnEmptyString() {
        String result = HTMLFilter.filter("");
        assertEquals("", result);
    }

    @Test
    public void testFilter_NoTagsInput_ReturnOriginalString() {
        String input = "Hello World";
        String result = HTMLFilter.filter(input);
        assertEquals(input, result);
    }

    @Test
    public void testFilter_TagInput_ReturnFilteredString() {
        String input = "<script>console.log('Hello World');</script>";
        String expected = "&lt;script&gt;console.log('Hello World');&lt;/script&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_MultipleTagsInput_ReturnFilteredString() {
        String input = "<h1>Welcome</h1><p>This is a test</p>";
        String expected = "&lt;h1&gt;Welcome&lt;/h1&gt;&lt;p&gt;This is a test&lt;/p&gt;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }

    @Test
    public void testFilter_SpecialCharactersInput_ReturnFilteredString() {
        String input = "This is & that's \"cool\"";
        String expected = "This is &amp; that&apos;s &quot;cool&quot;";
        String result = HTMLFilter.filter(input);
        assertEquals(expected, result);
    }
}