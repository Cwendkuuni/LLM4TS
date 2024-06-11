import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class JoinerTest {

    //Tests for the on(String separator) method
    @Test
    public void testOn_StringSeparator_ValidSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }

    @Test(expected = NullPointerException.class)
    public void testOn_StringSeparator_NullSeparator() {
        Joiner joiner = Joiner.on(null);
    }

    //Tests for the on(char separator) method
    @Test
    public void testOn_CharSeparator_ValidSeparator() {
        Joiner joiner = Joiner.on(',');
        assertNotNull(joiner);
    }

    //Tests for the appendTo(Appendable appendable, Iterable<?> parts) method
    @Test
    public void testAppendTo_AppendableIterable_ValidParts() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("a", "b", "c");
        Appendable appended = Joiner.on(",").appendTo(builder, parts);
        assertEquals("a,b,c", appended.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_AppendableIterable_NullAppendable() throws IOException {
        List<String> parts = Arrays.asList("a", "b", "c");
        Joiner.on(",").appendTo(null, parts);
    }

    //Tests for the appendTo(Appendable appendable, Iterator<?> parts) method
    @Test
    public void testAppendTo_AppendableIterator_ValidParts() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("a", "b", "c");
        Appendable appended = Joiner.on(",").appendTo(builder, parts.iterator());
        assertEquals("a,b,c", appended.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_AppendableIterator_NullAppendable() throws IOException {
        List<String> parts = Arrays.asList("a", "b", "c");
        Joiner.on(",").appendTo(null, parts.iterator());
    }

    //Tests for the appendTo(Appendable appendable, Object[] parts) method
    @Test
    public void testAppendTo_AppendableObjectArray_ValidParts() throws IOException {
        StringBuilder builder = new StringBuilder();
        String[] parts = {"a", "b", "c"};
        Appendable appended = Joiner.on(",").appendTo(builder, parts);
        assertEquals("a,b,c", appended.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_AppendableObjectArray_NullAppendable() throws IOException {
        String[] parts = {"a", "b", "c"};
        Joiner.on(",").appendTo(null, parts);
    }

    //Tests for the appendTo(Appendable appendable, Object first, Object second, Object... rest) method
    @Test
    public void testAppendTo_AppendableObjects_ValidParts() throws IOException {
        StringBuilder builder = new StringBuilder();
        Appendable appended = Joiner.on(",").appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", appended.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_AppendableObjects_NullAppendable() throws IOException {
        Joiner.on(",").appendTo(null, "a", "b", "c");
    }

    //Tests for the appendTo(StringBuilder builder, Iterable<?> parts) method
    @Test
    public void testAppendTo_StringBuilderIterable_ValidParts() {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("a", "b", "c");
        StringBuilder appended = Joiner.on(",").appendTo(builder, parts);
        assertEquals("a,b,c", appended.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_StringBuilderIterable_NullBuilder() {
        List<String> parts = Arrays.asList("a", "b", "c");
        Joiner.on(",").appendTo(null, parts);
    }

    //Tests for the appendTo(StringBuilder builder, Iterator<?> parts) method
    @Test
    public void testAppendTo_StringBuilderIterator_ValidParts() {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("a", "b", "c");
        StringBuilder appended = Joiner.on(",").appendTo(builder, parts.iterator());
        assertEquals("a,b,c", appended.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_StringBuilderIterator_NullBuilder() {
        List<String> parts = Arrays.asList("a", "b", "c");
        Joiner.on(",").appendTo(null, parts.iterator());
    }

    //Tests for the appendTo(StringBuilder builder, Object[] parts) method
    @Test
    public void testAppendTo_StringBuilderObjectArray_ValidParts() {
        StringBuilder builder = new StringBuilder();
        String[] parts = {"a", "b", "c"};
        StringBuilder appended = Joiner.on(",").appendTo(builder, parts);
        assertEquals("a,b,c", appended.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_StringBuilderObjectArray_NullBuilder() {
        String[] parts = {"a", "b", "c"};
        Joiner.on(",").appendTo(null, parts);
    }

    //Tests for the appendTo(StringBuilder builder, Object first, Object second, Object... rest) method
    @Test
    public void testAppendTo_StringBuilderObjects_ValidParts() {
        StringBuilder builder = new StringBuilder();
        StringBuilder appended = Joiner.on(",").appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", appended.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAppendTo_StringBuilderObjects_NullBuilder() {
        Joiner.on(",").appendTo(null, "a", "b", "c");
    }

    //Tests for the join(Iterable<?> parts) method
    @Test
    public void testJoin_Iterable_ValidParts() {
        List<String> parts = Arrays.asList("a", "b", "c");
        String joined = Joiner.on(",").join(parts);
        assertEquals("a,b,c", joined);
    }

    //Tests for the join(Iterator<?> parts) method
    @Test
    public void testJoin_Iterator_ValidParts() {
        List<String> parts = Arrays.asList("a", "b", "c");
        String joined = Joiner.on(",").join(parts.iterator());
        assertEquals("a,b,c", joined);
    }

    //Tests for the join(Object[] parts) method
    @Test
    public void testJoin_ObjectArray_ValidParts() {
        String[] parts = {"a", "b", "c"};
        String joined = Joiner.on(",").join(parts);
        assertEquals("a,b,c", joined);
    }

    //Tests for the join(Object first, Object second, Object... rest) method
    @Test
    public void testJoin_Objects_ValidParts() {
        String joined = Joiner.on(",").join("a", "b", "c");
        assertEquals("a,b,c", joined);
    }

    //Tests for the useForNull(String nullText) method
    @Test
    public void testUseForNull_ValidNullText() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        assertNotNull(joiner);
    }

    @Test(expected = NullPointerException.class)
    public void testUseForNull_NullNullText() {
        Joiner joiner = Joiner.on(",").useForNull(null);
    }

    //Tests for the skipNulls() method
    @Test
    public void testSkipNulls_ValidSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        assertNotNull(joiner);
    }

    //Tests for the withKeyValueSeparator(String keyValueSeparator) method
    @Test
    public void testWithKeyValueSeparator_ValidSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        assertNotNull(mapJoiner);
    }

    //Complete test case using multiple methods together
    @Test
    public void testCompleteJoinerFunctionality() throws IOException {
        StringBuilder builder = new StringBuilder();
        List<String> parts = Arrays.asList("a", "b", "c");

        //Append parts to builder, skipping null values
        StringBuilder appended = Joiner.on(",")
                .skipNulls()
                .appendTo(builder, parts.iterator());
        assertEquals("a,b,c", appended.toString());

        //Use alternative null text instead of null
        appended = Joiner.on(",")
                .useForNull("null")
                .appendTo(builder, parts);
        assertEquals("a,b,cnullbnullc", appended.toString());

        //Join parts with key-value separator for Map
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        String joined = Joiner.on(", ")
                .withKeyValueSeparator("=")
                .join(map.entrySet());
        assertEquals("key1=value1, key2=value2", joined);
    }
}