import static org.junit.Assert.*;

import org.junit.Test;

public class JoinerTest {

    @Test
    public void testOn_String_separator() {
        Joiner joiner = Joiner.on(",");
        assertNotNull(joiner);
        assertEquals(",", joiner.separator);
    }

    @Test
    public void testOn_char_separator() {
        Joiner joiner = Joiner.on(':');
        assertNotNull(joiner);
        assertEquals(":", joiner.separator);
    }

    @Test
    public void testAppendTo_Iterable() {
        Joiner joiner = Joiner.on(",");
        StringBuilder builder = new StringBuilder();
        Iterable<Integer> parts = Arrays.asList(1, 2, 3);
        try {
            joiner.appendTo(builder, parts);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
        assertEquals("1,2,3", builder.toString());
    }

    @Test
    public void testAppendTo_ObjectArray() {
        Joiner joiner = Joiner.on(":");
        StringBuilder builder = new StringBuilder();
        Object[] parts = { "a", "b", "c" };
        try {
            joiner.appendTo(builder, parts);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
        assertEquals("a:b:c", builder.toString());
    }

    // Add more test cases for other methods

}