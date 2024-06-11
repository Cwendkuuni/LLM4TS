import org.junit.*;
import static org.junit.Assert.*;

public class JoinerTest {

    @Test
    public void testOn() {
        Joiner joiner = Joiner.on(",");

        assertNotNull(joiner);
        assertEquals(joiner.separator, ",");
    }

    @Test
    public void testOnChar() {
        Joiner joiner = Joiner.on(';');

        assertNotNull(joiner);
        assertEquals(joiner.separator, ";");
    }

    @Test
    public void testAppendToIterable() {
        Joiner joiner = Joiner.on(",");

        StringBuilder builder = joiner.appendTo(new StringBuilder(), Arrays.asList("one", "two", "three"));

        assertNotNull(builder);
        assertEquals(builder.toString(), "one,two,three");

        builder = joiner.appendTo(new StringBuilder(), Arrays.asList());

        assertNotNull(builder);
        assertEquals(builder.toString(), "");
    }

    @Test
    public void testAppendToIterator() {
        Joiner joiner = Joiner.on(",");

        StringBuilder builder = joiner.appendTo(new StringBuilder(), Arrays.asList("one", "two", "three").iterator());

        assertNotNull(builder);
        assertEquals(builder.toString(), "one,two,three");

        builder = joiner.appendTo(new StringBuilder(), Arrays.asList().iterator());

        assertNotNull(builder);
        assertEquals(builder.toString(), "");
    }

    @Test
    public void testAppendToArray() {
        Joiner joiner = Joiner.on(",");

        StringBuilder builder = joiner.appendTo(new StringBuilder(), new String[]{"one", "two", "three"});

        assertNotNull(builder);
        assertEquals(builder.toString(), "one,two,three");

        builder = joiner.appendTo(new StringBuilder(), new String[]{});

        assertNotNull(builder);
        assertEquals(builder.toString(), "");
    }

    @Test
    public void testAppendToMultipleParams() {
        Joiner joiner = Joiner.on(",");

        StringBuilder builder = joiner.appendTo(new StringBuilder(), "one", "two", "three");

        assertNotNull(builder);
        assertEquals(builder.toString(), "one,two,three");

        builder = joiner.appendTo(new StringBuilder(), "one");

        assertNotNull(builder);
        assertEquals(builder.toString(), "one");

        builder = joiner.appendTo(new StringBuilder(), null, "two", "three");

        assertNotNull(builder);
        assertEquals(builder.toString(), "null,two,three");

        builder = joiner.appendTo(new StringBuilder(), null, null, null);

        assertNotNull(builder);
        assertEquals(builder.toString(), "null,null,null");

        builder = joiner.appendTo(new StringBuilder(), null);

        assertNotNull(builder);
        assertEquals(builder.toString(), "null");
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");

        String result = joiner.join(Arrays.asList("one", "two", "three"));

        assertNotNull(result);
        assertEquals(result, "one,two,three");

        result = joiner.join(Arrays.asList());

        assertNotNull(result);
        assertEquals(result, "");
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");

        String result = joiner.join(Arrays.asList("one", "two", "three").iterator());

        assertNotNull(result);
        assertEquals(result, "one,two,three");

        result = joiner.join(Arrays.asList().iterator());

        assertNotNull(result);
        assertEquals(result, "");
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");

        String result = joiner.join(new String[]{"one", "two", "three"});

        assertNotNull(result);
        assertEquals(result, "one,two,three");

        result = joiner.join(new String[]{});

        assertNotNull(result);
        assertEquals(result, "");
    }

    @Test
    public void testJoinMultipleParams() {
        Joiner joiner = Joiner.on(",");

        String result = joiner.join("one", "two", "three");

        assertNotNull(result);
        assertEquals(result, "one,two,three");

        result = joiner.join("one");

        assertNotNull(result);
        assertEquals(result, "one");

        result = joiner.join(null, "two", "three");

        assertNotNull(result);
        assertEquals(result, "null,two,three");

        result = joiner.join(null, null, null);

        assertNotNull(result);
        assertEquals(result, "null,null,null");

        result = joiner.join(null);

        assertNotNull(result);
        assertEquals(result, "null");
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("N/A");

        StringBuilder builder = joiner.appendTo(new StringBuilder(), Arrays.asList("one", null, "three"));

        assertNotNull(builder);
        assertEquals(builder.toString(), "one,N/A,three");

        builder = joiner.appendTo(new StringBuilder(), null);

        assertNotNull(builder);
        assertEquals(builder.toString(), "N/A");
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();

        StringBuilder builder = joiner.appendTo(new StringBuilder(), Arrays.asList("one", null, "three"));

        assertNotNull(builder);
        assertEquals(builder.toString(), "one,three");

        builder = joiner.appendTo(new StringBuilder(), null);

        assertNotNull(builder);
        assertEquals(builder.toString(), "");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",").skipNulls();

        joiner.withKeyValueSeparator(":");
    }
}