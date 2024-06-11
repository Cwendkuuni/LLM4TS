
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class JoinerTest {
    
    @Test
    public void testOn_StringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }
    
    @Test
    public void testOn_CharacterSeparator() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
    }
    
    @Test
    public void testAppendTo_Iterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendTo_Iterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendTo_ObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, 2, 3);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendTo_ObjectArrayWithNulls() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, null, 3);
        assertEquals("1,null,3", builder.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilderWithIterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = joiner.appendTo(new StringBuilder(), Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilderWithIterator() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = joiner.appendTo(new StringBuilder(), Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testAppendTo_StringBuilderWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = joiner.appendTo(new StringBuilder(), 1, 2, 3);
        assertEquals("1,2,3", builder.toString());
    }
    
    @Test
    public void testJoin_Iterable() {
        Joiner joiner = Joiner.on(",");
        String joined = joiner.join(Arrays.asList(1, 2, 3));
        assertEquals("1,2,3", joined);
    }
    
    @Test
    public void testJoin_Iterator() {
        Joiner joiner = Joiner.on(",");
        String joined = joiner.join(Arrays.asList(1, 2, 3).iterator());
        assertEquals("1,2,3", joined);
    }
    
    @Test
    public void testJoin_ObjectArray() {
        Joiner joiner = Joiner.on(",");
        String joined = joiner.join(1, 2, 3);
        assertEquals("1,2,3", joined);
    }
    
    @Test
    public void testJoin_ObjectArrayWithNulls() {
        Joiner joiner = Joiner.on(",");
        String joined = joiner.join(1, null, 3);
        assertEquals("1,null,3", joined);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, null, 3);
        assertEquals("1,NULL,3", builder.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_ThrowsException() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        joiner.useForNull("NULL");
    }
    
    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",").skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, 1, null, 3);
        assertEquals("1,3", builder.toString());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_ThrowsException() {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.useForNull("NULL");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_WithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").skipNulls();
        joiner.withKeyValueSeparator(":");
    }
    
    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        assertNotNull(mapJoiner);
    }
    
    @Test
    public void testMapJoiner_AppendTo() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, Map.of("key1", "value1", "key2", "value2"));
        assertEquals("key1:value1,key2:value2", builder.toString());
    }
    
    @Test
    public void testMapJoiner_Join() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        String joined = mapJoiner.join(Map.of("key1", "value1", "key2", "value2"));
        assertEquals("key1:value1,key2:value2", joined);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoiner_UseForNull() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator(":");
        mapJoiner.useForNull("NULL");
    }
}

