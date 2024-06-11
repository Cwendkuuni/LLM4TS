import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IOUtilsTest {

    @Test
    public void testCopyMethod() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());

        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        byte[] expectedOutput = "Test Data".getBytes();
        byte[] actualOutput = outputStream.toByteArray();

        assertEquals(expectedOutput.length, bytesCopied, 0);
        assertEquals(expectedOutput, actualOutput);
    }
}