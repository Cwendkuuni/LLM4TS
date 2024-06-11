import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        // Test case 1
        InputStream input1 = new ByteArrayInputStream("Test1".getBytes());
        ByteArrayOutputStream output1 = new ByteArrayOutputStream();
        long result1 = IOUtils.copy(input1, output1);
        assertEquals(5, result1);
        assertEquals("Test1", output1.toString());

        // Test case 2
        InputStream input2 = new ByteArrayInputStream("Test2".getBytes());
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        long result2 = IOUtils.copy(input2, output2, 10);
        assertEquals(5, result2);
        assertEquals("Test2", output2.toString());
    }

    @Test
    public void testSkip() {
        // Test case 1
        InputStream input1 = new ByteArrayInputStream("Test".getBytes());
        long result1 = IOUtils.skip(input1, 2);
        assertEquals(2, result1);

        // Test case 2
        InputStream input2 = new ByteArrayInputStream("Test".getBytes());
        long result2 = IOUtils.skip(input2, 10);
        assertEquals(4, result2);
    }

    @Test
    public void testReadFully() {
        // Test case 1
        InputStream input1 = new ByteArrayInputStream("Test".getBytes());
        byte[] buffer1 = new byte[10];
        int result1 = IOUtils.readFully(input1, buffer1);
        assertEquals(4, result1);
        assertEquals("Test", new String(buffer1).trim());

        // Test case 2
        InputStream input2 = new ByteArrayInputStream("Test".getBytes());
        byte[] buffer2 = new byte[10];
        int result2 = IOUtils.readFully(input2, buffer2, 0, 10);
        assertEquals(4, result2);
        assertEquals("Test", new String(buffer2).trim());
    }

    @Test
    public void testToByteArray() {
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        assertEquals("Test", new String(result));
    }

    @Test
    public void testCloseQuietly() {
        // Test case 1
        Closeable c1 = null;
        IOUtils.closeQuietly(c1);

        // Test case 2
        Closeable c2 = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(c2);
    }
}