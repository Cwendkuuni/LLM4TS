import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class JoinerTest {

    @Test
    public void testOnString_separator() {
        Joiner joiner = Joiner.on("|");
        assertEquals("|", joiner.separator);
    }

    @Test
    public void testOnChar_separator() {
        Joiner joiner = Joiner.on('|');
        assertEquals("|", joiner.separator);
    }

    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner updatedJoiner = joiner.useForNull("NULL");
        assertNotNull(updatedJoiner);
    }

    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",");
        Joiner updatedJoiner = joiner.skipNulls();
        assertNotNull(updatedJoiner);
    }

    @Test
    public void testWithKeyValueSeparator() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        assertNotNull(mapJoiner);
    }

    @Test
    public void testAppendTo_join() {
        Joiner joiner = Joiner.on("|");
        StringBuilder builder = new StringBuilder();
        String result = joiner.join(Arrays.asList("A", "B", "C"));
        assertEquals("A|B|C", result);
    }

    @Test
    public void testAppendTo_withNull() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Object[] parts = {null, "B", null, "D"};
        String result = joiner.appendTo(builder, parts).toString();
        assertEquals(",B,D", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMultipleUseForNull() {
        Joiner joiner = Joiner.on(",");
        Joiner updatedJoiner = joiner.useForNull("NULL");
        updatedJoiner.useForNull("NULL");
    }

    @Test
    public void testMapJoiner_appendTo() {
        Joiner joiner = Joiner.on(",");
        Joiner.MapJoiner mapJoiner = joiner.withKeyValueSeparator(":");
        StringBuilder builder = new StringBuilder();
        String result = mapJoiner.appendTo(builder, Collections.singletonMap("Key", "Value")).toString();
        assertEquals("Key:Value", result);
    }

}