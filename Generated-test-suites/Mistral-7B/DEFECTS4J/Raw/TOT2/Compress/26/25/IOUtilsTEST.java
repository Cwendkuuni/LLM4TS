import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputData = new byte[] { 1, 2, 3, 4, 5 };
        byte[] expectedOutputData = new byte[] { 1, 2, 3, 4, 5 };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(inputData);

        IOUtils.copy(inputStream, outputStream);
        byte[] outputData = outputStream.toByteArray();

        assertArrayEquals(expectedOutputData, outputData);

        // Test edge cases
        byte[] emptyInputData = new byte[0];
        ByteArrayOutputStream emptyOutputStream = new ByteArrayOutputStream();
        InputStream emptyInputStream = new ByteArrayInputStream(emptyInputData);

        IOUtils.copy(emptyInputStream, emptyOutputStream);
        byte[] emptyOutputData = emptyOutputStream.toByteArray();
        assertArrayEquals(emptyInputData, emptyOutputData);

        byte[] largeInputData = new byte[1024 * 1024];
        ByteArrayOutputStream largeOutputStream = new ByteArrayOutputStream();
        InputStream largeInputStream = new ByteArrayInputStream(largeInputData);

        IOUtils.copy(largeInputStream, largeOutputStream, COPY_BUF_SIZE);
        byte[] largeOutputData = largeOutputStream.toByteArray();
        assertArrayEquals(largeInputData, largeOutputData);

        byte[] unexpectedEOFInputData = new byte[] { 1, 2, 3 };
        ByteArrayOutputStream unexpectedEOFOutputStream = new ByteArrayOutputStream();
        InputStream unexpectedEOFInputStream = new ByteArrayInputStream(unexpectedEOFInputData) {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };

        IOUtils.copy(unexpectedEOFInputStream, unexpectedEOFOutputStream);
        byte[] unexpectedEOFOutputData = unexpectedEFOutputStream.toByteArray();
        assertTrue(unexpectedEFOutputData.length == 0);
    }
}