import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnCharSeparator() {
        // Typical use case
        Splitter splitter = Splitter.on(',');
        assertNotNull(splitter);
        
        // Edge case - separator is not found in the sequence
        Splitter splitterNotFound = Splitter.on(':');
        assertNotNull(splitterNotFound);
    }

    @Test
    public void testOnCharMatcherSeparator() {
        // Typical use case
        Splitter splitter = Splitter.on(CharMatcher.anyOf(":;"));
        assertNotNull(splitter);
        
        // Edge case - separator is a special character
        Splitter splitterSpecial = Splitter.on(CharMatcher.is('$'));
        assertNotNull(splitterSpecial);
    }

    @Test
    public void testOnStringSeparator() {
        // Typical use case
        Splitter splitter = Splitter.on(";");
        assertNotNull(splitter);
        
        // Edge case - empty separator
        try {
            Splitter splitterEmpty = Splitter.on("");
            fail("Expected IllegalArgumentException for empty separator");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testOnPatternSeparator() {
        // Typical use case
        Splitter splitter = Splitter.on(Pattern.compile("\\s+"));
        assertNotNull(splitter);
        
        // Edge case - pattern matches empty string
        try {
            Splitter splitterEmptyPattern = Splitter.on(Pattern.compile(""));
            fail("Expected IllegalArgumentException for pattern matching empty string");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFixedLength() {
        // Typical use case
        Splitter splitter = Splitter.fixedLength(3);
        assertNotNull(splitter);
        
        // Edge case - length less than 1
        try {
            Splitter splitterInvalidLength = Splitter.fixedLength(0);
            fail("Expected IllegalArgumentException for length less than 1");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testOmitEmptyStrings() {
        // Typical use case
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        assertNotNull(splitter);
    }

    @Test
    public void testLimit() {
        // Typical use case
        Splitter splitter = Splitter.on(',').limit(3);
        assertNotNull(splitter);
        
        // Edge case - limit less than 1
        try {
            Splitter splitterInvalidLimit = Splitter.on(',').limit(0);
            fail("Expected IllegalArgumentException for limit less than 1");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testTrimResults() {
        // Typical use case
        Splitter splitter = Splitter.on(',').trimResults();
        assertNotNull(splitter);
    }

    // Add more test cases for other methods as needed

}