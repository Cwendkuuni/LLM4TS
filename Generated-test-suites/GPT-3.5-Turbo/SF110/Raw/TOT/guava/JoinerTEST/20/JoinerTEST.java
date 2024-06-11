import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import com.google.common.base.*;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setUp() {
        joiner = Joiner.on(", ");
    }

    @Test
    public void testAppendTo_withIterable_shouldAppendAllElements() throws IOException {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        assertEquals("apple, banana, cherry", builder.toString());
    }

    @Test
    public void testAppendTo_withIterator_shouldAppendAllElements() throws IOException {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());
        assertEquals("apple, banana, cherry", builder.toString());
    }

    @Test
    public void testAppendTo_withObjectArray_shouldAppendAllElements() throws IOException {
        String[] array = {"apple", "banana", "cherry"};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        assertEquals("apple, banana, cherry", builder.toString());
    }

    @Test
    public void testAppendTo_withVarArgs_shouldAppendAllElements() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "apple", "banana", "cherry");
        assertEquals("apple, banana, cherry", builder.toString());
    }

    @Test
    public void testAppendTo_withEmptyIterable_shouldNotAppendAnything() throws IOException {
        List<String> list = Collections.emptyList();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list);
        assertEquals("", builder.toString());
    }

    @Test
    public void testAppendTo_withEmptyIterator_shouldNotAppendAnything() throws IOException {
        List<String> list = Collections.emptyList();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, list.iterator());
        assertEquals("", builder.toString());
    }

    @Test
    public void testAppendTo_withEmptyObjectArray_shouldNotAppendAnything() throws IOException {
        String[] array = {};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, array);
        assertEquals("", builder.toString());
    }

    @Test
    public void testAppendTo_withEmptyVarArgs_shouldNotAppendAnything() throws IOException {
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder);
        assertEquals("", builder.toString());
    }

    @Test
    public void testJoin_withIterable_shouldReturnJoinedString() {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        String result = joiner.join(list);
        assertEquals("apple, banana, cherry", result);
    }

    @Test
    public void testJoin_withIterator_shouldReturnJoinedString() {
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        String result = joiner.join(list.iterator());
        assertEquals("apple, banana, cherry", result);
    }

    @Test
    public void testJoin_withObjectArray_shouldReturnJoinedString() {
        String[] array = {"apple", "banana", "cherry"};
        String result = joiner.join(array);
        assertEquals("apple, banana, cherry", result);
    }

    @Test
    public void testJoin_withVarArgs_shouldReturnJoinedString() {
        String result = joiner.join("apple", "banana", "cherry");
        assertEquals("apple, banana, cherry", result);
    }

    @Test
    public void testJoin_withEmptyIterable_shouldReturnEmptyString() {
        List<String> list = Collections.emptyList();
        String result = joiner.join(list);
        assertEquals("", result);
    }

    @Test
    public void testJoin_withEmptyIterator_shouldReturnEmptyString() {
        List<String> list = Collections.emptyList();
        String result = joiner.join(list.iterator());
        assertEquals("", result);
    }

    @Test
    public void testJoin_withEmptyObjectArray_shouldReturnEmptyString() {
        String[] array = {};
        String result = joiner.join(array);
        assertEquals("", result);
    }

    @Test
    public void testJoin_withEmptyVarArgs_shouldReturnEmptyString() {
        String result = joiner.join();
        assertEquals("", result);
    }

    @Test
    public void testUseForNull_withNullText_shouldReturnNewJoiner() {
        String nullText = "null";
        Joiner newJoiner = joiner.useForNull(nullText);
        assertNotNull(newJoiner);
        assertNotSame(joiner, newJoiner);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_withNullText_whenAlreadySpecified_shouldThrowException() {
        String nullText = "null";
        joiner.useForNull(nullText).useForNull(nullText);
    }

    @Test
    public void testSkipNulls_shouldReturnNewJoiner() {
        Joiner newJoiner = joiner.skipNulls();
        assertNotNull(newJoiner);
        assertNotSame(joiner, newJoiner);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_whenAlreadySpecified_shouldThrowException() {
        joiner.skipNulls().skipNulls();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withWithKeyValueSeparator_shouldThrowException() {
        joiner.skipNulls().withKeyValueSeparator("=");
    }

    @Test
    public void testWithKeyValueSeparator_shouldReturnMapJoiner() {
        String keyValueSeparator = "=";
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(keyValueSeparator);
        assertNotNull(mapJoiner);
        assertEquals(keyValueSeparator, mapJoiner.keyValueSeparator);
    }

    @Test
    public void testAppendTo_withMap_shouldAppendAllEntries() throws IOException {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("cherry", 3);
        StringBuilder builder = new StringBuilder();
        joiner.withKeyValueSeparator(": ").appendTo(builder, map);
        assertEquals("apple: 1, banana: 2, cherry: 3", builder.toString());
    }

    @Test
    public void testAppendTo_withEmptyMap_shouldNotAppendAnything() throws IOException {
        Map<String, Integer> map = Collections.emptyMap();
        StringBuilder builder = new StringBuilder();
        joiner.withKeyValueSeparator(": ").appendTo(builder, map);
        assertEquals("", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoiner_useForNull_withNullText_shouldThrowException() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");
        String nullText = "null";
        mapJoiner.useForNull(nullText);
    }

    @Test
    public void testMapJoiner_useForNull_shouldReturnNewMapJoiner() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(": ");
        Joiner.MapJoiner newMapJoiner = mapJoiner.useForNull("null");
        assertNotNull(newMapJoiner);
        assertNotSame(mapJoiner, newMapJoiner);
    }

}