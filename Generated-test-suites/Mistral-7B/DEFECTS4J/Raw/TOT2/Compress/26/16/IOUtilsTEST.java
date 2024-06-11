import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopyMethod() throws IOException {
        byte[] inputData = new byte[] { 1, 2, 3 };
        byte[] outputData = new byte[3];

        int bytesCopied = IOUtils.copy(new ByteArrayInputStream(inputData), new ByteArrayOutputStream(3));
        Assert.assertEquals(3, bytesCopied);

        byte[] copiedData = ((ByteArrayOutputStream)outputData).toByteArray();
        Assert.assertArrayEquals(inputData, copiedData);
    }

    @Test(expected = IOException.class)
    public void testCopyMethodWithException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        OutputStream output = new ByteArrayOutputStream(3);

        IOUtils.copy(input, output); // Exception expected here
    }
}