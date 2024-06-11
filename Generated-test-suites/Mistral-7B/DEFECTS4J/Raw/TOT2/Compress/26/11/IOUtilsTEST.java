import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        byte[] inputData = "IOUtils Test".getBytes();
        byte[] outputData = new byte[inputData.length];
        long bytesCopied = IOUtils.copy(new ByteArrayInputStream(inputData), new ByteArrayOutputStream(inputData.length), IOUtils.COPY_BUF_SIZE);
        IOUtils.readFully(new ByteArrayInputStream(outputData), outputData, 0, outputData.length);
        assertEquals(new String(inputData), new String(outputData));
        assertEquals(bytesCopied, inputData.length);
    }
}