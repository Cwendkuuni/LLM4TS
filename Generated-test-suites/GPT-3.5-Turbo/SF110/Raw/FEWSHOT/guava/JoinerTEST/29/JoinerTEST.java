import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JoinerTest {
    
    @Test
    public void testOnWithStringSeparator() {
        Joiner joiner = Joiner.on(":");
        assertNotNull(joiner);
    }
    
    @Test
    public void testOnWithCharSeparator() {
        Joiner joiner = Joiner.on(';');
        assertNotNull(joiner);
    }
    
    @Test
    public void testAppendToWithAppendableAndIterable() throws Exception {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Iterable<?> parts = Arrays.asList("foo", "bar", "baz");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("foo:bar:baz", result.toString());
    }
    
    @Test
    public void testAppendToWithAppendableAndIterator() throws Exception {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Iterator<?> parts = Arrays.asList("foo", "bar", "baz").iterator();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("foo:bar:baz", result.toString());
    }
    
    @Test
    public void testAppendToWithAppendableAndArray() throws Exception {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"foo", "bar", "baz"};
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("foo:bar:baz", result.toString());
    }
    
    @Test
    public void testAppendToWithAppendableAndObjects() throws Exception {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Object first = "foo";
        Object second = "bar";
        Object[] rest = {"baz", "qux"};
        StringBuilder result = joiner.appendTo(builder, first, second, rest);
        assertEquals("foo:bar:baz:qux", result.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilderAndIterable() {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Iterable<?> parts = Arrays.asList("foo", "bar", "baz");
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("foo:bar:baz", result.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilderAndIterator() {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Iterator<?> parts = Arrays.asList("foo", "bar", "baz").iterator();
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("foo:bar:baz", result.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilderAndArray() {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"foo", "bar", "baz"};
        StringBuilder result = joiner.appendTo(builder, parts);
        assertEquals("foo:bar:baz", result.toString());
    }
    
    @Test
    public void testAppendToWithStringBuilderAndObjects() {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Object first = "foo";
        Object second = "bar";
        Object[] rest = {"baz", "qux"};
        StringBuilder result = joiner.appendTo(builder, first, second, rest);
        assertEquals("foo:bar:baz:qux", result.toString());
    }
    
    @Test
    public void testJoinWithIterable() {
        Joiner joiner = Joiner.on(":");
        Iterable<?> parts = Arrays.asList("foo", "bar", "baz");
        String result = joiner.join(parts);
        assertEquals("foo:bar:baz", result);
    }
    
    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(":");
        Iterator<?> parts = Arrays.asList("foo", "bar", "baz").iterator();
        String result = joiner.join(parts);
        assertEquals("foo:bar:baz", result);
    }
    
    @Test
    public void testJoinWithArray() {
        Joiner joiner = Joiner.on(":");
        Object[] parts = {"foo", "bar", "baz"};
        String result = joiner.join(parts);
        assertEquals("foo:bar:baz", result);
    }
    
    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on(":");
        Object first = "foo";
        Object second = "bar";
        Object[] rest = {"baz", "qux"};
        String result = joiner.join(first, second, rest);
        assertEquals("foo:bar:baz:qux", result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(":").useForNull("null");
        assertNotNull(joiner);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(":").skipNulls();
        assertNotNull(joiner);
    }
    
    @Test
    public void testMapJoinerWithAppendableAndMap() throws Exception {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Map<?, ?> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "qux");
        StringBuilder result = joiner.withKeyValueSeparator("=").appendTo(builder, map);
        assertEquals("foo=bar:baz=qux", result.toString());
    }
    
    @Test
    public void testMapJoinerWithStringBuilderAndMap() {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Map<?, ?> map = new HashMap<>();
        map.put("foo", "bar");
        map.put("baz", "qux");
        StringBuilder result = joiner.withKeyValueSeparator("=").appendTo(builder, map);
        assertEquals("foo=bar:baz=qux", result.toString());
    }
    
    @Test
    public void testMapJoinerWithIterable() {
        Joiner joiner = Joiner.on(":");
        Iterable<? extends Map.Entry<?, ?>> entries = new HashMap<String, String>() {{
            put("foo", "bar");
            put("baz", "qux");
        }}.entrySet();
        String result = joiner.withKeyValueSeparator("=").join(entries);
        assertEquals("foo=bar:baz=qux", result);
    }
    
    @Test
    public void testMapJoinerWithIterator() {
        Joiner joiner = Joiner.on(":");
        Iterator<? extends Map.Entry<?, ?>> entries = new HashMap<String, String>() {{
            put("foo", "bar");
            put("baz", "qux");
        }}.entrySet().iterator();
        String result = joiner.withKeyValueSeparator("=").join(entries);
        assertEquals("foo=bar:baz=qux", result);
    }
    
    @Test
    public void testMapJoinerWithMap() {
        Joiner joiner = Joiner.on(":");
        Map<?, ?> map = new HashMap<String, String>() {{
            put("foo", "bar");
            put("baz", "qux");
        }};
        String result = joiner.withKeyValueSeparator("=").join(map);
        assertEquals("foo=bar:baz=qux", result);
    }
    
    @Test
    public void testMapJoinerUseForNull() {
        Joiner joiner = Joiner.on(":").withKeyValueSeparator("=").useForNull("null");
        assertNotNull(joiner);
    }

}