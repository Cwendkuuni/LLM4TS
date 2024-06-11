
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setUp() throws Exception {
        deflateCompressorInputStream = new DeflateCompressorInputStream(new InputStreamStub());
    }

    @Test
    public void read_returnsCorrectBytes() throws Exception {
        byte[] data = new byte[5];
        int bytesRead = deflateCompressorInputStream.read(data, 0, 5);
        assertThat(bytesRead, is(5));
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, data);
    }

    @Test
    public void read_returnsNegativeOne_whenNoBytesToRead() throws Exception {
        byte[] data = new byte[5];
        int bytesRead = deflateCompressorInputStream.read(data, 0, 5);
        assertThat(bytesRead, is(-1));
    }

    @Test
    public void read_returnsZero_whenByteArrayIsEmpty() throws Exception {
        byte[] data = new byte[0];
        int bytesRead = deflateCompressorInputStream.read(data, 0, 0);
        assertThat(bytesRead, is(0));
    }

    @Test
    public void skip_skipsGivenNumberOfBytes() throws Exception {
        int bytesSkipped = (int) deflateCompressorInputStream.skip(2);
        assertThat(bytesSkipped, is(2));
    }

    @Test
    public void available_returnsNumberOfBytesAvailable() throws Exception {
        int bytesAvailable = deflateCompressorInputStream.available();
        assertThat(bytesAvailable, is(5));
    }

    @Test
    public void close_closesInputStream() throws Exception {
        deflateCompressorInputStream.close();
        assertTrue(deflateCompressorInputStream.isClosed());
    }

    @Test
    public void deflateCompressorInputStream_canBeCreatedWithInputStreamAndParameters() throws Exception {
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(new InputStreamStub(), parameters);
        assertNotNull(compressorInputStream);
    }

    @Test
    public void signatureMatchesZlibHeader_whenZlibHeaderIsPresent() throws Exception {
        byte[] signature = new byte[]{0x78, (byte) 0x9C};
        boolean hasZlibHeader = deflateCompressorInputStream.checkSignature(signature, signature.length);
        assertTrue(hasZlibHeader);
    }

    @Test
    public void signatureDoesNotMatchZlibHeader_whenZlibHeaderIsNotPresent() throws Exception {
        byte[] signature = new byte[]{0x01, (byte) 0x02};
        boolean hasZlibHeader = deflateCompressorInputStream.checkSignature(signature, signature.length);
        assertFalse(hasZlibHeader);
    }

    private static class InputStreamStub extends InputStream {

        private int bytesRead;
        private boolean closed;

        @Override
        public int read() {
            if (bytesRead < 5) {
                return bytesRead++ + 1;
            } else {
                return -1;
            }
        }

        @Override
        public int read(byte[] b, int off, int len) {
            for (int i = off; i < off + len && i < 5; i++) {
                b[i] = (byte) (i + 1);
                bytesRead++;
            }
            return len;
        }

        @Override
        public long skip(long n) {
            bytesRead = (int) (bytesRead + n);
            return n;
        }

        @Override
        public int available() {
            return 5 - bytesRead;
        }

        @Override
        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}
