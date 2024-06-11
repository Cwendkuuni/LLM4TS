import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        long expected = 5;
        assertEquals(expected, actual);

        byte[] inputBytes = input.readAllBytes();
        byte[] outputBytes = output.toByteArray();
        byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5};
        assertArrayEquals(expectedBytes, outputBytes);

        assertArrayEquals(new byte[0], inputBytes);

        input = new ByteArrayInputStream(new byte[0]);
        assertThrows(IOException.class, () -> IOUtils.copy(input, output));

        input = new ByteArrayInputStream(new byte[]{-1, 2, 3, 4, 5});
        assertThrows(IOException.class, () -> IOUtils.copy(input, output));

        output = new ByteArrayOutputStream(0);
        assertThrows(IOException.class, () -> IOUtils.copy(input, output));
    }

    @Test
    public void testSkip() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        long actual = IOUtils.skip(input, 5);
        long expected = 5;
        assertEquals(expected, actual);

        byte[] inputBytes = input.readAllBytes();
        byte[] expectedBytes = new byte[]{5, 2, 3, 4};
        assertArrayEquals(expectedBytes, inputBytes);

        assertThrows(IOException.class, () -> IOUtils.skip(null, 5));

        input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        assertThrows(IOException.class, () -> IOUtils.skip(input, -1));
    }

    @Test
    public void testLargeCopy() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[1024 * 1024]);
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 4096);
        long expected = 1024 * 1024;
        assertEquals(expected, actual);

        byte[] actualBytes = output.toByteArray();
        byte[] expectedBytes = new byte[1024 * 1024];
        assertArrayEquals(expectedBytes, actualBytes);

        assertArrayEquals(new byte[0], input.readAllBytes());

        assertThrows(OutOfMemoryError.class, () -> IOUtils.copy(input, output, 10));
    }
}