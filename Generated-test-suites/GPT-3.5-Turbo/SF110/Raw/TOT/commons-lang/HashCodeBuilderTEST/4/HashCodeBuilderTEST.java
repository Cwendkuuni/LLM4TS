import org.junit.*;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppend_boolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);

        int expected = 17 * 37 + 0;
        int actual = builder.toHashCode();

        assertEquals(expected, actual);
    }

    @Test
    public void testAppend_booleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new boolean[]{true, false, true});

        int expected = 17 * 37 + (0 * 37 + 1) * 37 + 0;
        int actual = builder.toHashCode();

        assertEquals(expected, actual);
    }

    @Test
    public void testAppend_byte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 1);

        int expected = 17 * 37 + 1;
        int actual = builder.toHashCode();

        assertEquals(expected, actual);
    }

    @Test
    public void testAppend_byteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(new byte[]{1, 2, 3});

        int expected = 17 * 37 + (1 * 37 + 2) * 37 + 3;
        int actual = builder.toHashCode();

        assertEquals(expected, actual);
    }

    // ... continue with tests for other methods ...

}