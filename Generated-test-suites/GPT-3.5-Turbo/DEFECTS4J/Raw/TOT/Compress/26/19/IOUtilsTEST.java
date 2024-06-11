import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        // Test case: Typical use case
        InputStream input1 = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ByteArrayOutputStream output1 = new ByteArrayOutputStream();
        long count1 = IOUtils.copy(input1, output1);
        byte[] result1 = output1.toByteArray();
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result1);
        assertEquals(5, count1);

        // Test case: Edge case - empty input stream
        InputStream input2 = new ByteArrayInputStream(new byte[]{});
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        long count2 = IOUtils.copy(input2, output2);
        byte[] result2 = output2.toByteArray();
        assertArrayEquals(new byte[]{}, result2);
        assertEquals(0, count2);

        // Test case: Error scenario - null input stream
        InputStream input3 = null;
        ByteArrayOutputStream output3 = new ByteArrayOutputStream();
        try {
            IOUtils.copy(input3, output3);
            fail("Expected IOException");
        } catch (IOException e) {
            assertNull(output3.toByteArray());
        }
    }

    @Test
    public void testSkip() throws IOException {
        // Test case: Typical use case
        InputStream input1 = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long skipped1 = IOUtils.skip(input1, 3);
        assertEquals(3, skipped1);

        // Test case: Edge case - skipped more than available bytes
        InputStream input2 = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long skipped2 = IOUtils.skip(input2, 10);
        assertEquals(5, skipped2);

        // Test case: Error scenario - null input stream
        InputStream input3 = null;
        try {
            IOUtils.skip(input3, 3);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testReadFully() throws IOException {
        // Test case: Typical use case
        InputStream input1 = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer1 = new byte[5];
        int bytesRead1 = IOUtils.readFully(input1, buffer1);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer1);
        assertEquals(5, bytesRead1);

        // Test case: Edge case - read less bytes than buffer length
        InputStream input2 = new ByteArrayInputStream(new byte[]{1, 2, 3});
        byte[] buffer2 = new byte[5];
        int bytesRead2 = IOUtils.readFully(input2, buffer2);
        assertArrayEquals(new byte[]{1, 2, 3, 0, 0}, buffer2);
        assertEquals(3, bytesRead2);

        // Test case: Error scenario - null input stream
        InputStream input3 = null;
        byte[] buffer3 = new byte[5];
        try {
            IOUtils.readFully(input3, buffer3);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        // Test case: Typical use case
        InputStream input1 = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] result1 = IOUtils.toByteArray(input1);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result1);

        // Test case: Edge case - empty input stream
        InputStream input2 = new ByteArrayInputStream(new byte[]{});
        byte[] result2 = IOUtils.toByteArray(input2);
        assertArrayEquals(new byte[]{}, result2);

        // Test case: Error scenario - null input stream
        InputStream input3 = null;
        try {
            IOUtils.toByteArray(input3);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testCloseQuietly() {
        // Test case: Typical use case - close a closeable
        ByteArrayOutputStream output1 = new ByteArrayOutputStream();
        IOUtils.closeQuietly(output1);
        assertTrue(output1.isClosed());

        // Test case: Typical use case - close a null closeable
        Closeable c = null;
        IOUtils.closeQuietly(c);  // Should not throw any exception
    }
}