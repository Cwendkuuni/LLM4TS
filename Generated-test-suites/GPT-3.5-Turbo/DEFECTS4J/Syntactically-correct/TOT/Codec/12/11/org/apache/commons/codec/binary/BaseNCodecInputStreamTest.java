
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec() {
            @Override
            public boolean hasData() {
                return false;
            }

            @Override
            public void encode(byte[] in, int inOffset, int len) throws IOException {

            }

            @Override
            public void decode(byte[] in, int inOffset, int len) throws IOException {

            }

            @Override
            public int readResults(byte[] out, int outOffset, int outLen) throws IOException {
                return 0;
            }
        };
        InputStream inputStream = new ByteArrayInputStream("Test string".getBytes());
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }

    @Test
    public void testRead() throws IOException {
        int result = baseNCodecInputStream.read();
        assertEquals(84, result); // ASCII value of 'T'
    }

    @Test
    public void testReadWithEOF() throws IOException {
        baseNCodecInputStream.close();
        int result = baseNCodecInputStream.read();
        assertEquals(-1, result); // EOF should be reached
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[12];
        int result = baseNCodecInputStream.read(buffer);
        assertEquals(12, result); // Full buffer should be read
        String output = new String(buffer);
        assertEquals("Test string", output);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[16];
        int result = baseNCodecInputStream.read(buffer, 4, 12);
        assertEquals(12, result); // 12 bytes should be read
        String output = new String(buffer, 4, 12);
        assertEquals("Test string", output);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        baseNCodecInputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLength() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 0, 12);
    }

    @Test
    public void testMarkSupported() {
        boolean result = baseNCodecInputStream.markSupported();
        assertFalse(result); // Marks are not supported
    }

    @Test
    public void testAvailable() throws IOException {
        int result = baseNCodecInputStream.available();
        assertEquals(1, result); // Expecting 1 as there is more data available
    }
}
