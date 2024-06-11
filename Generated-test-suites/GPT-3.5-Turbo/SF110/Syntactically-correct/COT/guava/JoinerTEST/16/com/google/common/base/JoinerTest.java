import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {
    
    // Test public static Joiner on(String separator)
    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assert(joiner instanceof Joiner);
    }
    
    // Test public static Joiner on(char separator)
    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(',');
        assert(joiner instanceof Joiner);
    }
    
    // Test public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException
    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("Hello", "World"));
        assert(builder.toString().equals("Hello,World"));
    }
    
    // Test public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException
    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("Hello", "World").iterator());
        assert(builder.toString().equals("Hello,World"));
    }
    
    // Test public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException
    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[] {"Hello", "World"});
        assert(builder.toString().equals("Hello,World"));
    }
    
    // Test public final StringBuilder appendTo(final StringBuilder builder, final Iterable<?> parts)
    @Test
    public void testStringBuilderAppendToIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("Hello", "World"));
        assert(builder.toString().equals("Hello,World"));
    }
    
    // Test public final StringBuilder appendTo(final StringBuilder builder, final Iterator<?> parts)
    @Test
    public void testStringBuilderAppendToIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("Hello", "World").iterator());
        assert(builder.toString().equals("Hello,World"));
    }
    
    // Test public final StringBuilder appendTo(final StringBuilder builder, final Object[] parts)
    @Test
    public void testStringBuilderAppendToObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[] {"Hello", "World"});
        assert(builder.toString().equals("Hello,World"));
    }
    
    // Test public final String join(final Iterable<?> parts)
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("Hello", "World"));
        assert(result.equals("Hello,World"));
    }
    
    // Test public final String join(final Iterator<?> parts)
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(Arrays.asList("Hello", "World").iterator());
        assert(result.equals("Hello,World"));
    }
    
    // Test public final String join(final Object[] parts)
    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(new Object[] {"Hello", "World"});
        assert(result.equals("Hello,World"));
    }
    
    // Test public final String join(final Object first, final Object second, final Object... rest)
    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("Hello", "World", "!");
        assert(result.equals("Hello,World,!"));
    }
    
    // Test @CheckReturnValue public Joiner useForNull(final String nullText)
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        String result = joiner.join(new Object[] {"Hello", null, "World"});
        assert(result.equals("Hello,NULL,World"));
    }
    
    // Test @CheckReturnValue public Joiner skipNulls()
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        String result = joiner.join(new Object[] {null, "Hello", null, "World"});
        assert(result.equals("Hello,World"));
    }
    
    // Test @CheckReturnValue public MapJoiner withKeyValueSeparator(final String keyValueSeparator)
    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, Integer> map = new HashMap<>();
        map.put("One", 1);
        map.put("Two", 2);
        String result = joiner.join(map);
        assert(result.equals("One:1,Two:2"));
    }

    // Additional test for toString(final Object part)
    @Test
    public void testToString() {
        Joiner joiner = Joiner.on(",");
        String result1 = joiner.toString("Hello");
        assert(result1.equals("Hello"));

        String result2 = joiner.toString(123);
        assert(result2.equals("123"));
    }

}