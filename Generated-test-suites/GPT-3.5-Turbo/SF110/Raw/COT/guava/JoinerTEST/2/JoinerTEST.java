import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class JoinerTest {
    
    // Method: on(String separator)
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }
    
    // Method: on(char separator)
    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
    }
    
    // Method: <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException
    @Test
    public void testAppendToWithIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<?> parts = Arrays.asList("abc", "def", "ghi");
        try {
            StringBuilder result = joiner.appendTo(builder, parts);
            assertEquals("abc,def,ghi", result.toString());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }
    
    // Method: <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException
    @Test
    public void testAppendToWithIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterator<?> parts = Arrays.asList("abc", "def", "ghi").iterator();
        try {
            StringBuilder result = joiner.appendTo(builder, parts);
            assertEquals("abc,def,ghi", result.toString());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }
    
    // Method: final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException
    @Test
    public void testAppendToWithArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"abc", "def", "ghi"};
        try {
            StringBuilder result = joiner.appendTo(builder, parts);
            assertEquals("abc,def,ghi", result.toString());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }
    
    // Method: final <A extends Appendable> A appendTo(A appendable, @Nullable Object first, @Nullable Object second, Object... rest) throws IOException
    @Test
    public void testAppendToWithParams() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object first = "abc";
        Object second = "def";
        Object[] rest = {"ghi", "jkl"};
        try {
            StringBuilder result = joiner.appendTo(builder, first, second, rest);
            assertEquals("abc,def,ghi,jkl", result.toString());
        } catch (IOException e) {
            fail("IOException was thrown: " + e.getMessage());
        }
    }
    
    // Method: final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts)
    @Test
    public void testAppendToStringBuilderWithIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<?> parts = Arrays.asList("abc", "def", "ghi");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("abc,def,ghi", result.toString());
    }
    
    // Method: final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts)
    @Test
    public void testAppendToStringBuilderWithIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterator<?> parts = Arrays.asList("abc", "def", "ghi").iterator();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("abc,def,ghi", result.toString());
    }
    
    // Method: final StringBuilder appendTo(StringBuilder builder, Object[] parts)
    @Test
    public void testAppendToStringBuilderWithArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"abc", "def", "ghi"};
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("abc,def,ghi", result.toString());
    }
    
    // Method: final StringBuilder appendTo(StringBuilder builder, @Nullable Object first, @Nullable Object second, Object... rest)
    @Test
    public void testAppendToStringBuilderWithParams() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object first = "abc";
        Object second = "def";
        Object[] rest = {"ghi", "jkl"};
        StringBuilder result = joiner.appendTo(builder, first, second, rest);
        assertEquals("abc,def,ghi,jkl", result.toString());
    }
    
    // Method: final String join(Iterable<?> parts)
    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        Iterable<?> parts = Arrays.asList("abc", "def", "ghi");
        String result = joiner.join(parts);
        assertEquals("abc,def,ghi", result);
    }
    
    // Method: final String join(Iterator<?> parts)
    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<?> parts = Arrays.asList("abc", "def", "ghi").iterator();
        String result = joiner.join(parts);
        assertEquals("abc,def,ghi", result);
    }
    
    // Method: final String join(Object[] parts)
    @Test
    public void testJoinWithArray() {
        Joiner joiner = Joiner.on(",");
        Object[] parts = {"abc", "def", "ghi"};
        String result = joiner.join(parts);
        assertEquals("abc,def,ghi", result);
    }
    
    // Method: final String join(@Nullable Object first, @Nullable Object second, Object... rest)
    @Test
    public void testJoinWithParams() {
        Joiner joiner = Joiner.on(",");
        Object first = "abc";
        Object second = "def";
        Object[] rest = {"ghi", "jkl"};
        String result = joiner.join(first, second, rest);
        assertEquals("abc,def,ghi,jkl", result);
    }
    
    // Method: Joiner useForNull(String nullText)
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertNotNull(joiner);
    }
    
    // Method: Joiner skipNulls()
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertNotNull(joiner);
    }
    
    // Method: MapJoiner withKeyValueSeparator(String keyValueSeparator)
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        assertNotNull(joiner);
    }
}