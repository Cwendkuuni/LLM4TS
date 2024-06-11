import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopyMethod() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = this.getClass().getResourceAsStream("/TestData.txt");
        long bytesCopied = IOUtils.copy(input, output);
        assertNotNull(bytesCopied);
        assertFalse(input.available() > 0);
        assertNotNull(output.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testCopyMethodWithNullArguments() throws IOException {
        InputStream input = null;
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
    }

    @Test
    public void testCopyMethodWithDifferentBuffersize() throws IOException {
        ByteArrayOutputStream output1 = new ByteArrayOutputStream(512);
        ByteArrayOutputStream output2 = new ByteArrayOutputStream(1024);
        InputStream input = this.getClass().getResourceAsStream("/TestData.txt");
        long bytesCopied1 = IOUtils.copy(input, output1);
        long bytesCopied2 = IOUtils.copy(input, output2);
        assertFalse(bytesCopied1 == bytesCopied2);
        assertNotNull(output1.toByteArray());
        assertNotNull(output2.toByteArray());
    }

    @Test
    public void testSkipMethod() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/TestData.txt");
        long numToSkip = 100;
        long skipped = IOUtils.skip(input, numToSkip);
        assertNotNull(skipped);
        assertFalse(input.available() > 0);
        long actualSkipped = IOUtils.skip(input, numToSkip);
        assertEquals(skipped, actualSkipped);
    }

    @Test(expected = IOException.class)
    public void testSkipMethodWithNullArguments() throws IOException {
        InputStream input = null;
        long numToSkip = 100;
        IOUtils.skip(input, numToSkip);
    }

    @Test
    public void testReadFullyMethod() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/TestData.txt");
        byte[] buffer = new byte[1024];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertNotNull(bytesRead);
        assertFalse(bytesRead > buffer.length);
        assertNotNull(buffer);
    }

    @Test(expected = IOException.class)
    public void testReadFullyMethodWithNullArguments() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/TestData.txt");
        byte[] buffer = null;
        IOUtils.readFully(input, buffer);
    }

    @Test
    public void testReadFullyMethodWithNegativeOffset() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/TestData.txt");
        byte[] buffer = new byte[1024];
        int bytesRead = IOUtils.readFully(input, buffer, -1, 1024);
        assertFalse(bytesRead > 0);
    }

    @Test
    public void testReadFullyMethodWithNegativeLength() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/TestData.txt");
        byte[] buffer = new byte[1024];
        int bytesRead = IOUtils.readFully(input, buffer, 0, -1);
        assertFalse(bytesRead > 0);
    }

    @Test
    public void testToByteArrayMethod() throws IOException {
        InputStream input = this.getClass().getResourceAsStream("/TestData.txt");
        byte[] byteArray = IOUtils.toByteArray(input);
        assertNotNull(byteArray);
        assertFalse(byteArray.length == 0);
    }

    @Test(expected = IOException.class)
    public void testToByteArrayMethodWithNullArguments() throws IOException {
        InputStream input = null;
        IOUtils.toByteArray(input);
    }

    @Test
    public void testCloseQuietlyMethod() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCloseQuietlyMethodWithNullArgument() {
        Closeable input = null;
        IOUtils.closeQuietly(input);
    }

}