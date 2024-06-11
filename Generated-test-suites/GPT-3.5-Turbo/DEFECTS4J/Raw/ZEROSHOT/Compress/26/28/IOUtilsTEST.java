import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Closeable;

public class IOUtilsTest {

    private static final int COPY_BUF_SIZE = 8024;
    private static final int SKIP_BUF_SIZE = 4096;

    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() {
        input = new ByteArrayInputStream(new byte[0]);
        output = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        input.close();
        output.close();
    }

    @Test
    public void testCopy() throws IOException {
        long count = IOUtils.copy(input, output);
        assert(count == 0);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        long count = IOUtils.copy(input, output, COPY_BUF_SIZE);
        assert(count == 0);
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = IOUtils.skip(input, SKIP_BUF_SIZE);
        assert(skipped == 0);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[COPY_BUF_SIZE];
        int bytesRead = IOUtils.readFully(input, buffer, 0, COPY_BUF_SIZE);
        assert(bytesRead == 0);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] byteArray = IOUtils.toByteArray(input);
        assert(byteArray.length == 0);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = null;
        IOUtils.closeQuietly(closeable);
    }
}