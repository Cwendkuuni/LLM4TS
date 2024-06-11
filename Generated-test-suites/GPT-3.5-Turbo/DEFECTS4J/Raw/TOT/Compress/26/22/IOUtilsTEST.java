import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    // Test case for copy(InputStream, OutputStream)
    @Test
    public void testCopy() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
            OutputStream output = new ByteArrayOutputStream();
            long result = IOUtils.copy(input, output);
            
            byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
            Assert.assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, outputBytes);
            Assert.assertEquals(5, result);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for copy(InputStream, OutputStream, int)
    @Test
    public void testCopyWithBufferSize() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
            OutputStream output = new ByteArrayOutputStream();
            long result = IOUtils.copy(input, output, 2);
            
            byte[] outputBytes = ((ByteArrayOutputStream) output).toByteArray();
            Assert.assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, outputBytes);
            Assert.assertEquals(5, result);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for skip(InputStream, long)
    @Test
    public void testSkip() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
            long result = IOUtils.skip(input, 3);
            
            byte[] remainingBytes = new byte[2];
            input.read(remainingBytes);
            Assert.assertArrayEquals(new byte[] { 4, 5 }, remainingBytes);
            Assert.assertEquals(3, result);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for readFully(InputStream, byte[])
    @Test
    public void testReadFully() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
            byte[] result = new byte[5];
            int bytesRead = IOUtils.readFully(input, result);
            
            Assert.assertEquals(5, bytesRead);
            Assert.assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, result);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for readFully(InputStream, byte[], int, int)
    @Test
    public void testReadFullyWithOffset() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
            byte[] result = new byte[5];
            int bytesRead = IOUtils.readFully(input, result, 1, 3);
            
            Assert.assertEquals(3, bytesRead);
            Assert.assertArrayEquals(new byte[] { 0, 1, 2, 0, 0 }, result);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for toByteArray(InputStream)
    @Test
    public void testToByteArray() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
            byte[] result = IOUtils.toByteArray(input);
            
            Assert.assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, result);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    // Test case for closeQuietly(Closeable)
    @Test
    public void testCloseQuietly() {
        try {
            InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
            IOUtils.closeQuietly(input);
            
            Assert.assertTrue(true); // No exception thrown
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
}