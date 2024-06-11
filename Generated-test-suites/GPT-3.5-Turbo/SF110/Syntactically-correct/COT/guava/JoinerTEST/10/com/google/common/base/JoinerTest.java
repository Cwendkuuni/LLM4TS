import static org.junit.Assert.*;
import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(',');
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }
    
    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> parts = Arrays.asList(1, 2, 3);
        joiner.appendTo(builder, parts);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendToIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> parts = Arrays.asList(1, 2, 3).iterator();
        joiner.appendTo(builder, parts);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {1, 2, 3};
        joiner.appendTo(builder, parts);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendToFirstSecondRest() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, 2, 3);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> parts = Arrays.asList(1, 2, 3);
        joiner.appendTo(builder, parts);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterator<Integer> parts = Arrays.asList(1, 2, 3).iterator();
        joiner.appendTo(builder, parts);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {1, 2, 3};
        joiner.appendTo(builder, parts);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendToStringBuilderFirstSecondRest() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, 2, 3);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        Iterable<Integer> parts = Arrays.asList(1, 2, 3);
        String result = joiner.join(parts);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        Iterator<Integer> parts = Arrays.asList(1, 2, 3).iterator();
        String result = joiner.join(parts);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinObjectArray() {
        Joiner joiner = Joiner.on(",");
        Object[] parts = {1, 2, 3};
        String result = joiner.join(parts);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testJoinFirstSecondRest() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join(1, 2, 3);
        assertEquals("1,2,3", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.useForNull("null");
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> parts = Arrays.asList(1, null, 3);
        joiner.appendTo(builder, parts);
        assertEquals("1,null,3", builder.toString());
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.skipNulls();
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> parts = Arrays.asList(1, null, 3);
        joiner.appendTo(builder, parts);
        assertEquals("1,3", builder.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNullAlreadySpecified() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.useForNull("null");
        joiner.useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsAlreadySpecified() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.skipNulls();
        joiner.useForNull("null");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithMaps() {
        Joiner joiner = Joiner.on(",");
        joiner = joiner.skipNulls();
        joiner.withKeyValueSeparator(":");
    }
    
}