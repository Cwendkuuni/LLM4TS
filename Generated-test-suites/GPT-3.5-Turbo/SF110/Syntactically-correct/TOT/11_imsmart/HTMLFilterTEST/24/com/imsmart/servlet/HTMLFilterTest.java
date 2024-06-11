import static org.junit.Assert.*;
import org.junit.Test;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_typicalInput() {
        String message = "Hello <b>World</b> & \"Universe\"";
        String expected = "Hello &lt;b&gt;World&lt;/b&gt; &amp; &quot;Universe&quot;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFilter_emptyInput() {
        String message = "";
        String expected = "";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFilter_nullInput() {
        String message = null;
        String expected = null;
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFilter_edgeCases() {
        String message = "<&>";
        String expected = "&lt;&amp;&gt;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFilter_specialCharacters() {
        String message = "!@#$%^*()";
        String expected = "!@#$%^*()";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFilter_multipleSpecialCharacters() {
        String message = "\"<>&";
        String expected = "&quot;&lt;&gt;&amp;";
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFilter_largeInput() {
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            messageBuilder.append("<>");
        }
        String message = messageBuilder.toString();
        StringBuilder expectedBuilder = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            expectedBuilder.append("&lt;&gt;");
        }
        String expected = expectedBuilder.toString();
        String actual = HTMLFilter.filter(message);
        assertEquals(expected, actual);
    }
}