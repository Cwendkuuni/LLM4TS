import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }

    @Test
    public void testAppendTo_withAppendableAndIterable_shouldAppendValuesToAppendable() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Iterable<Integer> values = Arrays.asList(1, 2, 3);

        joiner.appendTo(stringBuilder, values);

        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withAppendableAndIterator_shouldAppendValuesToAppendable() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> values = Arrays.asList(1, 2, 3).iterator();

        joiner.appendTo(stringBuilder, values);

        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withAppendableAndObjectArray_shouldAppendValuesToAppendable() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Object[] values = {1, 2, 3};

        joiner.appendTo(stringBuilder, values);

        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withAppendableAndObjects_shouldAppendValuesToAppendable() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        joiner.appendTo(stringBuilder, 1, 2, 3);

        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilderAndIterable_shouldAppendValuesToStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterable<Integer> values = Arrays.asList(1, 2, 3);

        joiner.appendTo(stringBuilder, values);

        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilderAndIterator_shouldAppendValuesToStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> values = Arrays.asList(1, 2, 3).iterator();

        joiner.appendTo(stringBuilder, values);

        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilderAndObjectArray_shouldAppendValuesToStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        Object[] values = {1, 2, 3};

        joiner.appendTo(stringBuilder, values);

        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withStringBuilderAndObjects_shouldAppendValuesToStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();

        joiner.appendTo(stringBuilder, 1, 2, 3);

        assertEquals("1,2,3", stringBuilder.toString());
    }

    @Test
    public void testJoin_withIterable_shouldJoinValues() {
        Iterable<Integer> values = Arrays.asList(1, 2, 3);

        String joinedString = joiner.join(values);

        assertEquals("1,2,3", joinedString);
    }

    @Test
    public void testJoin_withIterator_shouldJoinValues() {
        Iterator<Integer> values = Arrays.asList(1, 2, 3).iterator();

        String joinedString = joiner.join(values);

        assertEquals("1,2,3", joinedString);
    }

    @Test
    public void testJoin_withObjectArray_shouldJoinValues() {
        Object[] values = {1, 2, 3};

        String joinedString = joiner.join(values);

        assertEquals("1,2,3", joinedString);
    }

    @Test
    public void testJoin_withObjects_shouldJoinValues() {
        String joinedString = joiner.join(1, 2, 3);

        assertEquals("1,2,3", joinedString);
    }

    @Test
    public void testUseForNull_shouldReplaceNullValues() {
        Joiner modifiedJoiner = joiner.useForNull("NULL");

        String joinedString = modifiedJoiner.join(1, null, 3);

        assertEquals("1,NULL,3", joinedString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_twice_shouldThrowException() {
        Joiner modifiedJoiner = joiner.useForNull("NULL");
        modifiedJoiner.useForNull("OTHER_NULL");
    }

    @Test
    public void testSkipNulls_shouldSkipNullValues() throws IOException {
        Joiner modifiedJoiner = joiner.skipNulls();
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> values = Arrays.asList(1, null, 3).iterator();

        modifiedJoiner.appendTo(stringBuilder, values);

        assertEquals("1,3", stringBuilder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_useForNull_shouldThrowException() {
        Joiner modifiedJoiner = joiner.skipNulls();
        modifiedJoiner.useForNull("NULL");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMapJoiner_shouldThrowException() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        mapJoiner.skipNulls();
    }

    @Test
    public void testMapJoiner_appendTo_withAppendableAndMap_shouldAppendKeyValuePairsToAppendable() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        StringBuilder stringBuilder = new StringBuilder();
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        mapJoiner.appendTo(stringBuilder, map);

        assertEquals("1=one,2=two,3=three", stringBuilder.toString());
    }

    @Test
    public void testMapJoiner_appendTo_withStringBuilderAndMap_shouldAppendKeyValuePairsToStringBuilder() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        StringBuilder stringBuilder = new StringBuilder();
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        mapJoiner.appendTo(stringBuilder, map);

        assertEquals("1=one,2=two,3=three", stringBuilder.toString());
    }

    @Test
    public void testMapJoiner_join_withMap_shouldJoinKeyValuePairs() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        String joinedString = mapJoiner.join(map);

        assertEquals("1=one,2=two,3=three", joinedString);
    }

    @Test
    public void testMapJoiner_appendTo_withIterable_shouldAppendKeyValuePairsToAppendable() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        StringBuilder stringBuilder = new StringBuilder();
        Iterable<Map.Entry<Integer, String>> entries = Arrays.asList(
                new AbstractMap.SimpleEntry<>(1, "one"),
                new AbstractMap.SimpleEntry<>(2, "two"),
                new AbstractMap.SimpleEntry<>(3, "three")
        );

        mapJoiner.appendTo(stringBuilder, entries);

        assertEquals("1=one,2=two,3=three", stringBuilder.toString());
    }

    @Test
    public void testMapJoiner_appendTo_withIterator_shouldAppendKeyValuePairsToAppendable() throws IOException {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<Integer, String>> entries = Arrays.asList(
                new AbstractMap.SimpleEntry<>(1, "one"),
                new AbstractMap.SimpleEntry<>(2, "two"),
                new AbstractMap.SimpleEntry<>(3, "three")
        ).iterator();

        mapJoiner.appendTo(stringBuilder, entries);

        assertEquals("1=one,2=two,3=three", stringBuilder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMapJoiner_appendTo_twice_shouldThrowException() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        StringBuilder stringBuilder = new StringBuilder();
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        mapJoiner.appendTo(stringBuilder, map);
        mapJoiner.appendTo(stringBuilder, map);
    }

    @Test
    public void testMapJoiner_join_withIterable_shouldJoinKeyValuePairs() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        Iterable<Map.Entry<Integer, String>> entries = Arrays.asList(
                new AbstractMap.SimpleEntry<>(1, "one"),
                new AbstractMap.SimpleEntry<>(2, "two"),
                new AbstractMap.SimpleEntry<>(3, "three")
        );

        String joinedString = mapJoiner.join(entries);

        assertEquals("1=one,2=two,3=three", joinedString);
    }

    @Test
    public void testMapJoiner_join_withIterator_shouldJoinKeyValuePairs() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");

        Iterator<Map.Entry<Integer, String>> entries = Arrays.asList(
                new AbstractMap.SimpleEntry<>(1, "one"),
                new AbstractMap.SimpleEntry<>(2, "two"),
                new AbstractMap.SimpleEntry<>(3, "three")
        ).iterator();

        String joinedString = mapJoiner.join(entries);

        assertEquals("1=one,2=two,3=three", joinedString);
    }
}