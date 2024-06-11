import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {

    private Joiner joiner;
    
    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }
    
    @Test
    public void testAppendTo_withIterable_shouldReturnExpectedString() throws IOException {
        List<String> parts = Arrays.asList("Hello", "World");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts);
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withIterator_shouldReturnExpectedString() throws IOException {
        List<String> parts = Arrays.asList("Hello", "World");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts.iterator());
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withObjectArray_shouldReturnExpectedString() throws IOException {
        String[] parts = {"Hello", "World"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts);
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withObjects_shouldReturnExpectedString() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "Hello", "World");
        assertEquals("Hello,World", builder.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndIterable_shouldReturnExpectedStringBuilder() {
        List<String> parts = Arrays.asList("Hello", "World");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndIterator_shouldReturnExpectedStringBuilder() {
        List<String> parts = Arrays.asList("Hello", "World");
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts.iterator());
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndObjectArray_shouldReturnExpectedStringBuilder() {
        String[] parts = {"Hello", "World"};
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testAppendTo_withStringBuilderAndObjects_shouldReturnExpectedStringBuilder() {
        StringBuilder builder = new StringBuilder();
        StringBuilder result = joiner.appendTo(builder, "Hello", "World");
        assertEquals("Hello,World", result.toString());
    }
    
    @Test
    public void testJoin_withIterable_shouldReturnExpectedString() {
        List<String> parts = Arrays.asList("Hello", "World");
        String result = joiner.join(parts);
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withIterator_shouldReturnExpectedString() {
        List<String> parts = Arrays.asList("Hello", "World");
        String result = joiner.join(parts.iterator());
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withObjectArray_shouldReturnExpectedString() {
        String[] parts = {"Hello", "World"};
        String result = joiner.join(parts);
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testJoin_withObjects_shouldReturnExpectedString() {
        String result = joiner.join("Hello", "World");
        assertEquals("Hello,World", result);
    }
    
    @Test
    public void testUseForNull_shouldReturnNewJoinerInstance() {
        Joiner newJoiner = joiner.useForNull("NULL");
        assertNotSame(newJoiner, joiner);
    }
    
    @Test
    public void testSkipNulls_shouldReturnNewJoinerInstance() {
        Joiner newJoiner = joiner.skipNulls();
        assertNotSame(newJoiner, joiner);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_afterSkipNulls_shouldThrowUnsupportedOperationException() {
        joiner.skipNulls().useForNull("NULL");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_afterUseForNull_shouldThrowUnsupportedOperationException() {
        joiner.useForNull("NULL").skipNulls();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMapJoiner_shouldThrowUnsupportedOperationException() {
        joiner.skipNulls().withKeyValueSeparator(":");
    }
    
    @Test
    public void testWithKeyValueSeparator_shouldReturnNewMapJoinerInstance() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertNotSame(mapJoiner, joiner);
    }
    
    // Additional test cases can be added to further test the methods in Joiner class
    
}