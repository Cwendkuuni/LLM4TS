import static org.junit.Assert.*;
import org.junit.*;
import com.google.common.base.Joiner;
import java.io.IOException;
import java.util.*;

public class JoinerTest {

    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on('|');
        assertEquals("1|2|3", joiner.join(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testAppendToWithIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<Integer> parts = Arrays.asList(1, 2, 3);
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts);
        assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToWithIterator() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<Integer> parts = Arrays.asList(1, 2, 3);
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts.iterator());
        assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToWithArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        Integer[] parts = {1, 2, 3};
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, parts);
        assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendToWithObjects() throws IOException {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        joiner.appendTo(builder, "1", "2", "3");
        assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<Integer> parts = Arrays.asList(1, 2, 3);
        assertEquals("1,2,3", joiner.join(parts));
    }

    @Test
    public void testJoinIterator() {
        Joiner joiner = Joiner.on(",");
        List<Integer> parts = Arrays.asList(1, 2, 3);
        assertEquals("1,2,3", joiner.join(parts.iterator()));
    }

    @Test
    public void testJoinArray() {
        Joiner joiner = Joiner.on(",");
        Integer[] parts = {1, 2, 3};
        assertEquals("1,2,3", joiner.join(parts));
    }

    @Test
    public void testJoinObjects() {
        Joiner joiner = Joiner.on(",");
        assertEquals("1,2,3", joiner.join("1", "2", "3"));
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        List<String> parts = Arrays.asList("A", null, "B");
        assertEquals("A,NA,B", joiner.join(parts));
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        List<String> parts = Arrays.asList("A", null, "B");
        assertEquals("A,B", joiner.join(parts));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipNullsCannotBeUsedWithMap() {
        Joiner joiner = Joiner.on(",").skipNulls();
        Map<String, String> map = new HashMap<>();
        map.put("A", "1");
        map.put("B", null);
        joiner.join(map);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
        Map<String, String> map = new HashMap<>();
        map.put("A", "1");
        map.put("B", "2");
        assertEquals("A=1,B=2", mapJoiner.join(map));
    }

}