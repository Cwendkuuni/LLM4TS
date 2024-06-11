import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("TestData".getBytes());

        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        byte[] expectedOutput = "TestData".getBytes();
        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertEquals(expectedOutput.length, copiedBytes);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }
}