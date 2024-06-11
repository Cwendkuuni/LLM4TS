import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        byte[] inputData = "TestIOUtils".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        IOUtils.copy(new ByteArrayInputStream(inputData), outputStream);

        byte[] expectedOutputData = "TestIOUtils".getBytes();
        byte[] outputData = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedOutputData, outputData);
    }
}