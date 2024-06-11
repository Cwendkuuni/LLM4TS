import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

    private Joiner joiner;

    @Before
    public void setUp() {
        joiner = Joiner.on(",");
    }

    @Test
    public void testAppendTo_withAppendableAndIterable_shouldAppendToAppendable() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterable<String> parts = Arrays.asList("one", "two", "three");

        try {
            joiner.appendTo(stringBuilder, parts);
        } catch (IOException e) {
            fail("Failed to append to appendable");
        }

        assertEquals("one,two,three", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withAppendableAndIterator_shouldAppendToAppendable() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> parts = Arrays.asList("one", "two", "three").iterator();

        try {
            joiner.appendTo(stringBuilder, parts);
        } catch (IOException e) {
            fail("Failed to append to appendable");
        }

        assertEquals("one,two,three", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withAppendableAndArray_shouldAppendToAppendable() {
        StringBuilder stringBuilder = new StringBuilder();
        String[] parts = {"one", "two", "three"};

        try {
            joiner.appendTo(stringBuilder, parts);
        } catch (IOException e) {
            fail("Failed to append to appendable");
        }

        assertEquals("one,two,three", stringBuilder.toString());
    }

    @Test
    public void testAppendTo_withAppendableAndObjects_shouldAppendToAppendable() {
        StringBuilder stringBuilder = new StringBuilder();
        Object[] parts = {"one", "two", "three"};

        try {
            joiner.appendTo(stringBuilder, parts);
        } catch (IOException e) {
            fail("Failed to append to appendable");
        }

        assertEquals("one,two,three", stringBuilder.toString());
    }

    @Test
    public void testJoin_withIterable_shouldReturnJoinedString() {
        Iterable<String> parts = Arrays.asList("one", "two", "three");
        String joinedString = joiner.join(parts);

        assertEquals("one,two,three", joinedString);
    }

    @Test
    public void testJoin_withIterator_shouldReturnJoinedString() {
        Iterator<String> parts = Arrays.asList("one", "two", "three").iterator();
        String joinedString = joiner.join(parts);

        assertEquals("one,two,three", joinedString);
    }

    @Test
    public void testJoin_withArray_shouldReturnJoinedString() {
        String[] parts = {"one", "two", "three"};
        String joinedString = joiner.join(parts);

        assertEquals("one,two,three", joinedString);
    }

    @Test
    public void testJoin_withObjects_shouldReturnJoinedString() {
        Object[] parts = {"one", "two", "three"};
        String joinedString = joiner.join(parts);

        assertEquals("one,two,three", joinedString);
    }

    @Test
    public void testUseForNull_shouldReplaceNullValuesWithSpecifiedText() {
        joiner = joiner.useForNull("null");
        Iterable<String> parts = Arrays.asList("one", null, "two", null, "three");
        String joinedString = joiner.join(parts);

        assertEquals("one,null,two,null,three", joinedString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUseForNull_afterSkipNulls_shouldThrowUnsupportedOperationException() {
        joiner.skipNulls().useForNull("null");
    }

    @Test
    public void testSkipNulls_shouldSkipNullValues() {
        joiner = joiner.skipNulls();
        Iterable<String> parts = Arrays.asList("one", null, "two", null, "three");
        String joinedString = joiner.join(parts);

        assertEquals("one,two,three", joinedString);
    }

    @Test
    public void testSkipNulls_withMapJoiner_shouldSkipNullValues() {
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":").skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", null);
        map.put("key3", "value3");
        String joinedString = mapJoiner.join(map);

        assertEquals("key1:value1,key3:value3", joinedString);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withMapJoiner_shouldThrowUnsupportedOperationException() {
        joiner.skipNulls().withKeyValueSeparator(":");
    }

}