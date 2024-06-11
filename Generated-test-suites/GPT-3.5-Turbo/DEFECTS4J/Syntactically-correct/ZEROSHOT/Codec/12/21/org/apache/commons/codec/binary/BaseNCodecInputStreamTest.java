import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        byte[] testData = "This is a test.".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] src, int off, int len) {
                // Do nothing
            }

            @Override
            void decode(byte[] src, int off, int len) {
                // Do nothing
            }

            @Override
            boolean hasData() {
                return false;
            }

            @Override
            int readResults(byte[] dest, int destPos, int destLen) {
                return 0;
            }
        };
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        int result = baseNCodecInputStream.read();
        if (result != -1) {
            assert(result >= 0 && result < 256);
        }
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int result = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assert(result >= 0 && result <= buffer.length);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayWithNullBuffer() throws IOException {
        baseNCodecInputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, -1, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLength() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithOutOfBoundsOffset() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 11, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithOutOfBoundsLength() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 0, 11);
    }

    @Test
    public void testMarkSupported() {
        boolean result = baseNCodecInputStream.markSupported();
        assert(!result);
    }

    @Test
    public void testAvailable() {
        int result = baseNCodecInputStream.available();
        assert(result == 1);
    }
}