import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        String inputData = "Hello, World!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes());

        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(inputData.length(), copiedBytes);

        byte[] expectedOutput = inputData.getBytes();
        byte[] actualOutput = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }
}