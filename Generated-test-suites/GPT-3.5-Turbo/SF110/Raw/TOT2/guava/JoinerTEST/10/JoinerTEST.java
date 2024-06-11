import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JoinerTest {

    @Test
    public void testOnStringSeparator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
    }

    @Test
    public void testOnCharSeparator() {
        Joiner joiner = Joiner.on(' ');
        assertNotNull(joiner);
    }

    @Test
    public void testAppendToIterable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("a", "b", "c");
        StringBuilder result = joiner.appendTo(new StringBuilder(), parts);
        assertEquals("a,b,c", result.toString());
    }

    @Test
    public void testAppendToObjectArray() throws IOException {
        Joiner joiner = Joiner.on(",");
        Object[] parts = {"a", "b", "c"};
        StringBuilder result = joiner.appendTo(new StringBuilder(), parts);
        assertEquals("a,b,c", result.toString());
    }

    @Test
    public void testJoinIterable() {
        Joiner joiner = Joiner.on(",");
        List<String> parts = Arrays.asList("a", "b", "c");
        String result = joiner.join(parts);
        assertEquals("a,b,c", result);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NA");
        List<String> parts = Arrays.asList("a", null, "c");
        String result = joiner.join(parts);
        assertEquals("a,NA,c", result);
    }

    @Test
    public void testSkipNulls() throws IOException {
        Joiner joiner = Joiner.on(",").skipNulls();
        List<String> parts = Arrays.asList("a", null, "c");
        StringBuilder result = joiner.appendTo(new StringBuilder(), parts);
        assertEquals("a,c", result.toString());
    }

    @Test
    public void testMapJoiner() throws IOException {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator("=");
        Map<String, String> map = Map.of("key1", "value1", "key2", "value2");
        StringBuilder result = mapJoiner.appendTo(new StringBuilder(), map);
        assertEquals("key1=value1,key2=value2", result.toString());
    }

}