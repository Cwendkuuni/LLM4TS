
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }

    @Test
    public void appendToTest_withIterable() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        StringBuilder builder = joiner.appendTo(new StringBuilder(), list);

        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void appendToTest_withIterator() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        StringBuilder builder = joiner.appendTo(new StringBuilder(), list.iterator());

        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void appendToTest_withArray() throws IOException {
        Object[] array = {"a", "b", "c"};

        StringBuilder builder = joiner.appendTo(new StringBuilder(), array);

        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void appendToTest_withObjects() throws IOException {
        StringBuilder builder = joiner.appendTo(new StringBuilder(), "a", "b", "c");

        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void joinTest_withIterable() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        String joinedString = joiner.join(list);

        assertEquals("a,b,c", joinedString);
    }

    @Test
    public void joinTest_withIterator() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        String joinedString = joiner.join(list.iterator());

        assertEquals("a,b,c", joinedString);
    }

    @Test
    public void joinTest_withArray() {
        Object[] array = {"a", "b", "c"};

        String joinedString = joiner.join(array);

        assertEquals("a,b,c", joinedString);
    }

    @Test
    public void joinTest_withObjects() {
        String joinedString = joiner.join("a", "b", "c");

        assertEquals("a,b,c", joinedString);
    }

    @Test
    public void useForNullTest() {
        Joiner nullJoiner = joiner.useForNull("NULL");

        StringBuilder builder = nullJoiner.appendTo(new StringBuilder(), "a", null, "b", null, "c");

        assertEquals("a,NULL,b,NULL,c", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void useForNullTest_withAlreadySpecifiedUseForNull() {
        joiner.useForNull("NULL").useForNull("NULL");
    }

    @Test
    public void skipNullsTest() throws IOException {
        Joiner skipNullsJoiner = joiner.skipNulls();

        StringBuilder builder = skipNullsJoiner.appendTo(new StringBuilder(), "a", null, "b", null, "c");

        assertEquals("a,b,c", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void skipNullsTest_withAlreadySpecifiedUseForNull() {
        joiner.skipNulls().useForNull("NULL");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void skipNullsTest_withMaps() {
        joiner.skipNulls().withKeyValueSeparator(":");
    }

    @Test
    public void mapJoiner_appendToTest_withMap() throws IOException {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        StringBuilder builder = mapJoiner.appendTo(new StringBuilder(), map);

        assertEquals("one:1,two:2,three:3", builder.toString());
    }

    @Test
    public void mapJoiner_joinTest_withMap() {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        String joinedString = mapJoiner.join(map);

        assertEquals("one:1,two:2,three:3", joinedString);
    }

    @Test
    public void mapJoiner_appendToTest_withEntriesIterable() throws IOException {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("one", 1));
        entries.add(new AbstractMap.SimpleEntry<>("two", 2));
        entries.add(new AbstractMap.SimpleEntry<>("three", 3));

        StringBuilder builder = mapJoiner.appendTo(new StringBuilder(), entries);

        assertEquals("one:1,two:2,three:3", builder.toString());
    }

    @Test
    public void mapJoiner_appendToTest_withEntriesIterator() throws IOException {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("one", 1));
        entries.add(new AbstractMap.SimpleEntry<>("two", 2));
        entries.add(new AbstractMap.SimpleEntry<>("three", 3));

        StringBuilder builder = mapJoiner.appendTo(new StringBuilder(), entries.iterator());

        assertEquals("one:1,two:2,three:3", builder.toString());
    }

    @Test
    public void mapJoiner_joinTest_withEntriesIterable() {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("one", 1));
        entries.add(new AbstractMap.SimpleEntry<>("two", 2));
        entries.add(new AbstractMap.SimpleEntry<>("three", 3));

        String joinedString = mapJoiner.join(entries);

        assertEquals("one:1,two:2,three:3", joinedString);
    }

    @Test
    public void mapJoiner_joinTest_withEntriesIterator() {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<>("one", 1));
        entries.add(new AbstractMap.SimpleEntry<>("two", 2));
        entries.add(new AbstractMap.SimpleEntry<>("three", 3));

        String joinedString = mapJoiner.join(entries.iterator());

        assertEquals("one:1,two:2,three:3", joinedString);
    }

    @Test
    public void mapJoiner_useForNullTest() {
        MapJoiner mapJoiner = joiner.withKeyValueSeparator(":").useForNull("NULL");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", null);
        map.put("three", 3);

        StringBuilder builder = mapJoiner.appendTo(new StringBuilder(), map);

        assertEquals("one:1,two:NULL,three:3", builder.toString());
    }
}

