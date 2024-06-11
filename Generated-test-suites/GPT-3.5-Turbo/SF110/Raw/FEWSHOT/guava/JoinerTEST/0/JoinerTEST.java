import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JoinerTest {
    
    // Test on(String separator) method
    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
    }
    
    // Test on(char separator) method
    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on('+');
        assertEquals("1+2+3", joiner.join(Arrays.asList(1, 2, 3)));
    }
    
    // Test appendTo(A appendable, Iterable<?> parts) method
    @Test
    public void testAppendToWithIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", builder.toString());
    }
    
    // Test appendTo(A appendable, Iterator<?> parts) method
    @Test
    public void testAppendToWithIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", builder.toString());
    }
    
    // Test appendTo(A appendable, Object[] parts) method
    @Test
    public void testAppendToWithArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[] {1, 2, 3});
        assertEquals("1,2,3", builder.toString());
    }
    
    // Test appendTo(A appendable, Object first, Object second, Object... rest) method
    @Test
    public void testAppendToWithObjects() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, 2, 3);
        assertEquals("1,2,3", builder.toString());
    }
    
    // Test appendTo(StringBuilder builder, Iterable<?> parts) method
    @Test
    public void testAppendToBuilderWithIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", builder.toString());
    }
    
    // Test appendTo(StringBuilder builder, Iterator<?> parts) method
    @Test
    public void testAppendToBuilderWithIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", builder.toString());
    }
    
    // Test appendTo(StringBuilder builder, Object[] parts) method
    @Test
    public void testAppendToBuilderWithArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[] {1, 2, 3});
        assertEquals("1,2,3", builder.toString());
    }
    
    // Test appendTo(StringBuilder builder, Object first, Object second, Object... rest) method
    @Test
    public void testAppendToBuilderWithObjects() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, 2, 3);
        assertEquals("1,2,3", builder.toString());
    }
    
    // Test join(Iterable<?> parts) method
    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
    }
    
    // Test join(Iterator<?> parts) method
    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3).iterator()));
    }
    
    // Test join(Object[] parts) method
    @Test
    public void testJoinWithArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(new Object[] {1, 2, 3}));
    }
    
    // Test join(Object first, Object second, Object... rest) method
    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(1, 2, 3));
    }
    
    // Test useForNull(String nullText) method
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        assertEquals("1,NULL,3", joiner.join(Arrays.asList(1, null, 3)));
    }
    
    // Test skipNulls() method
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertEquals("1,3", joiner.join(Arrays.asList(1, null, 3)));
    }
}