import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] source = new byte[] { 1, 2, 3, 4, 5 };
        byte[] target = new byte[5];
        int bytesCopied = IOUtils.copy(new ByteArrayInputStream(source), new ByteArrayOutputStream(target.length), IOUtils.COPY_BUF_SIZE);
        assertArrayEquals(source, target);
        assertEquals(bytesCopied, source.length);
    }
}