import org.junit.Test;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws Exception {
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };

        ChecksumCalculatingInputStream c = new ChecksumCalculatingInputStream(null, in);
        assertEquals(-1, c.read());
    }

    @Test
    public void testReadByteArray() throws Exception {
        byte[] b = new byte[10];
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };

        ChecksumCalculatingInputStream c = new ChecksumCalculatingInputStream(null, in);
        assertEquals(-1, c.read(b));
    }

    @Test
    public void testReadByteArrayOffsetLength() throws Exception {
        byte[] b = new byte[10];
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };

        ChecksumCalculatingInputStream c = new ChecksumCalculatingInputStream(null, in);
        assertEquals(-1, c.read(b, 0, 10));
    }

    @Test
    public void testSkip() throws Exception {
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };

        ChecksumCalculatingInputStream c = new ChecksumCalculatingInputStream(null, in);
        assertEquals(0, c.skip(1));
    }

    @Test
    public void testGetValue() throws Exception {
        ChecksumCalculatingInputStream c = new ChecksumCalculatingInputStream(null, null);

        assertEquals(0, c.getValue());
    }

}