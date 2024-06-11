import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());

        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        byte[] expectedOutput = "Test Data".getBytes();
        byte[] actualOutput = outputStream.toByteArray();

        assertEquals(expectedOutput.length, copiedBytes);
        assertEquals(expectedOutput, actualOutput);
    }
}