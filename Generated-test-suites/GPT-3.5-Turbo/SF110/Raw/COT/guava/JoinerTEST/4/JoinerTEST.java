
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

public class JoinerTest {

    @Test
    public void testOn_StringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c,d", joiner.join(Arrays.asList("a", "b", "c", "d")));
        assertEquals("1,2,3,4", joiner.join(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    public void testOn_CharSeparator() {
        Joiner joiner = Joiner.on(',');
        assertEquals("a,b,c,d", joiner.join(Arrays.asList("a", "b", "c", "d")));
        assertEquals("1,2,3,4", joiner.join(Arrays.asList(1, 2, 3, 4)));
    }

    @Test
    public void testAppendTo_iterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c"));
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendTo_iterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", "b", "c").iterator());
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendTo_objectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, new Object[]{"a", "b", "c"});
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testAppendTo_varargs() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "a", "b", "c");
        assertEquals("a,b,c", builder.toString());
    }

    @Test
    public void testJoin_iterable() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testJoin_iterator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(Arrays.asList("a", "b", "c").iterator()));
    }

    @Test
    public void testJoin_objectArray() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join(new Object[]{"a", "b", "c"}));
    }

    @Test
    public void testJoin_varargs() {
        Joiner joiner = Joiner.on(",");
        assertEquals("a,b,c", joiner.join("a", "b", "c"));
    }

    @Test
    public void testUseForNull() throws IOException {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", null, "b"));
        assertEquals("a,NULL,b", builder.toString());
    }

    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",").skipNulls();
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", null, "b"));
        assertEquals("a,b", builder.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNulls_withKeyValueSeparator() throws IOException {
        Joiner joiner = Joiner.on(",").skipNulls().withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, Arrays.asList("a", null, "b"));
    }

    @Test
    public void testWithKeyValueSeparator() throws IOException {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        StringBuilder builder = new StringBuilder();
        mapJoiner.appendTo(builder, Arrays.asList("a", "1", "b", "2").iterator());
        assertEquals("a=1,b=2", builder.toString());
    }
}
