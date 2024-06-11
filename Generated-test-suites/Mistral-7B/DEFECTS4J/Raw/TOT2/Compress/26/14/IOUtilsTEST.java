import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        String source = "Test Data";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes());

        long copiedData = IOUtils.copy(inputStream, outputStream);
        byte[] expectedOutput = source.getBytes();
        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertEquals("Incorrect data copied", expectedOutput.length, copiedData);
        Assert.assertArrayEquals("Output does not match expected data", expectedOutput, actualOutput);

        // Test edge case: same InputStream and OutputStream
        inputStream = outputStream;
        outputStream = new ByteArrayOutputStream();
        copiedData = IOUtils.copy(inputStream, outputStream);
        expectedOutput = source.getBytes();
        actualOutput = outputStream.toByteArray();

        Assert.assertEquals("Incorrect data copied", expectedOutput.length, copiedData);
        Assert.assertArrayEquals("Output does not match expected data", expectedOutput, actualOutput);
    }
}