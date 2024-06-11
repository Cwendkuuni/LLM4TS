import org.junit.Test;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testAppendTo_withIterable_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("one", "two", "three");

        joiner.appendTo(builder, iterable);

        assertEquals("one-two-three", builder.toString());
    }

    @Test
    public void testAppendTo_withIterator_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();

        joiner.appendTo(builder, iterator);

        assertEquals("one-two-three", builder.toString());
    }

    @Test
    public void testAppendTo_withObjectArray_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"one", "two", "three"};

        joiner.appendTo(builder, parts);

        assertEquals("one-two-three", builder.toString());
    }

    @Test
    public void testAppendTo_withObjects_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();

        joiner.appendTo(builder, "one", "two", "three");

        assertEquals("one-two-three", builder.toString());
    }

    @Test
    public void testAppendTo_withNulls_shouldAppendToAppendable() throws IOException {
        Joiner joiner = Joiner.on("-");
        joiner = joiner.useForNull("null");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"one", null, "three"};

        joiner.appendTo(builder, parts);

        assertEquals("one-null-three", builder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilderAndIterable_shouldAppendToStringBuilder() {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Iterable<String> iterable = Arrays.asList("one", "two", "three");

        joiner.appendTo(builder, iterable);

        assertEquals("one-two-three", builder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilderAndIterator_shouldAppendToStringBuilder() throws IOException {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();

        joiner.appendTo(builder, iterator);

        assertEquals("one-two-three", builder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilderAndObjectArray_shouldAppendToStringBuilder() {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"one", "two", "three"};

        joiner.appendTo(builder, parts);

        assertEquals("one-two-three", builder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilderAndObjects_shouldAppendToStringBuilder() {
        Joiner joiner = Joiner.on("-");
        StringBuilder builder = new StringBuilder();

        joiner.appendTo(builder, "one", "two", "three");

        assertEquals("one-two-three", builder.toString());
    }

    @Test
    public void testJoin_withIterable_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on("-");
        Iterable<String> iterable = Arrays.asList("one", "two", "three");

        String result = joiner.join(iterable);

        assertEquals("one-two-three", result);
    }

    @Test
    public void testJoin_withIterator_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on("-");
        Iterator<String> iterator = Arrays.asList("one", "two", "three").iterator();

        String result = joiner.join(iterator);

        assertEquals("one-two-three", result);
    }

    @Test
    public void testJoin_withObjectArray_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on("-");
        Object[] parts = {"one", "two", "three"};

        String result = joiner.join(parts);

        assertEquals("one-two-three", result);
    }

    @Test
    public void testJoin_withObjects_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on("-");

        String result = joiner.join("one", "two", "three");

        assertEquals("one-two-three", result);
    }

    @Test
    public void testJoin_withNulls_shouldReturnJoinedString() {
        Joiner joiner = Joiner.on("-").useForNull("null");
        Object[] parts = {"one", null, "three"};

        String result = joiner.join(parts);

        assertEquals("one-null-three", result);
    }

    @Test
    public void testUseForNull_shouldReturnNewJoinerWithNullText() {
        Joiner joiner = Joiner.on("-");
        joiner = joiner.useForNull("null");

        assertEquals("null", joiner.toString(null));
    }

    @Test
    public void testSkipNulls_shouldReturnNewJoinerWithoutNulls() throws IOException {
        Joiner joiner = Joiner.on("-").skipNulls();
        StringBuilder builder = new StringBuilder();
        Object[] parts = {"one", null, "three"};

        joiner.appendTo(builder, parts);

        assertEquals("one-three", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withUseForNull_throwUnsupportedOperationException() {
        Joiner joiner = Joiner.on("-").skipNulls();

        joiner.useForNull("null");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withWithKeyValueSeparator_throwUnsupportedOperationException() {
        Joiner joiner = Joiner.on("-").skipNulls();

        joiner.withKeyValueSeparator(":");
    }

    @Test
    public void testWithKeyValueSeparator_shouldReturnMapJoinerWithKeyValueSeparator() {
        Joiner joiner = Joiner.on("-");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");

        assertEquals("one:1-two:2-three:3", mapJoiner.join(createMap()));
    }

    private Map<String, Integer> createMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        return map;
    }
}