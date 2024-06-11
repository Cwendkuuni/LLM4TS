
import org.junit.Test;

import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        // Test copying with default buffer size
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        try {
            long count = IOUtils.copy(input, output);
            assertEquals(13, count);
            assertEquals("Hello, World!", output.toString());
        } catch (IOException e) {
            fail("An unexpected IOException occurred.");
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }

        // Test copying with custom buffer size
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        output = new ByteArrayOutputStream();
        try {
            long count = IOUtils.copy(input, output, 5);
            assertEquals(13, count);
            assertEquals("Hello, World!", output.toString());
        } catch (IOException e) {
            fail("An unexpected IOException occurred.");
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    @Test
    public void testSkip() {
        // Test skipping with available bytes
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        try {
            long count = IOUtils.skip(input, 7);
            assertEquals(7, count);
            assertEquals('W', input.read());
        } catch (IOException e) {
            fail("An unexpected IOException occurred.");
        } finally {
            IOUtils.closeQuietly(input);
        }
        
        // Test skipping with less available bytes
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        try {
            long count = IOUtils.skip(input, 15);
            assertEquals(13, count);
            assertEquals(-1, input.read());
        } catch (IOException e) {
            fail("An unexpected IOException occurred.");
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testReadFully() {
        // Test reading fully with available bytes
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[7];
        try {
            int count = IOUtils.readFully(input, buffer);
            assertEquals(7, count);
            assertEquals("Hello, ", new String(buffer));
        } catch (IOException e) {
            fail("An unexpected IOException occurred.");
        } finally {
            IOUtils.closeQuietly(input);
        }
        
        // Test reading fully with less available bytes
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        buffer = new byte[15];
        try {
            int count = IOUtils.readFully(input, buffer);
            assertEquals(13, count);
            assertEquals("Hello, World!", new String(buffer));
        } catch (IOException e) {
            fail("An unexpected IOException occurred.");
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testToByteArray() {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        try {
            byte[] data = IOUtils.toByteArray(input);
            assertEquals("Hello, World!", new String(data));
        } catch (IOException e) {
            fail("An unexpected IOException occurred.");
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Test
    public void testCloseQuietly() {
        try {
            InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            IOUtils.closeQuietly(input);
            assertFalse(input instanceof Closeable);
        } catch (IOException e) {
            fail("An unexpected IOException occurred.");
        }
    }
}

