import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {
    private Joiner joiner;
    
    @Before
    public void setUp() {
        joiner = new Joiner(";");
    }
    
    @Test
    public void testOnSeparator() {
        assertEquals("[\"a;b;c\"]", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testOnCharSeparator() {
        Joiner charJoiner = Joiner.on(',');
        assertEquals("[\"1,2,3\"]", charJoiner.join(Arrays.asList(1, 2, 3)));
    }
    
    @Test
    public void testAppendToIterable() throws IOException {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("a", "b", "c"));
        assertEquals("a;b;c", sb.toString());
    }
    
    @Test
    public void testAppendToIterator() throws IOException {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a;b;c", sb.toString());
    }
    
    @Test
    public void testAppendToObjectArray() throws IOException {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, new Object[]{"a", "b", "c"});
        assertEquals("a;b;c", sb.toString());
    }
    
    @Test
    public void testAppendToVarargs() throws IOException {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, "a", "b", "c");
        assertEquals("a;b;c", sb.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterable() {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("a", "b", "c"));
        assertEquals("a;b;c", sb.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterator() {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a;b;c", sb.toString());
    }
    
    @Test
    public void testAppendToStringBuilderObjectArray() {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, new Object[]{"a", "b", "c"});
        assertEquals("a;b;c", sb.toString());
    }
    
    @Test
    public void testAppendToStringBuilderVarargs() {
        StringBuilder sb = new StringBuilder();
        joiner.appendTo(sb, "a", "b", "c");
        assertEquals("a;b;c", sb.toString());
    }
    
    @Test
    public void testJoinIterable() {
        assertEquals("a;b;c", joiner.join(Arrays.asList("a", "b", "c")));
    }
    
    @Test
    public void testJoinIterator() {
        assertEquals("a;b;c", joiner.join(Arrays.asList("a", "b", "c").iterator()));
    }
    
    @Test
    public void testJoinObjectArray() {
        assertEquals("a;b;c", joiner.join(new Object[]{"a", "b", "c"}));
    }
    
    @Test
    public void testJoinVarargs() {
        assertEquals("a;b;c", joiner.join("a", "b", "c"));
    }
    
    @Test
    public void testUseForNull() throws IOException {
        Joiner nullJoiner = joiner.useForNull("null");
        StringBuilder sb = new StringBuilder();
        nullJoiner.appendTo(sb, Arrays.asList("a", null, "c"));
        assertEquals("a;null;c", sb.toString());
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        Joiner skipNullsJoiner = joiner.skipNulls();
        StringBuilder sb = new StringBuilder();
        skipNullsJoiner.appendTo(sb, Arrays.asList("a", null, "c"));
        assertEquals("a;c", sb.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsTwice() {
        joiner.skipNulls().skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullTwice() {
        joiner.useForNull("null").useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMap() {
        joiner.skipNulls().withKeyValueSeparator(":");
    }
}