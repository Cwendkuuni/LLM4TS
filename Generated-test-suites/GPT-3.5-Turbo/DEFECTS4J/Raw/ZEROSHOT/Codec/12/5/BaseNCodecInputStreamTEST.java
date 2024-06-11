import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream inputStream;

    @Before
    public void setUp() {
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] b, int off, int len) throws IOException {
                // Dummy implementation
            }

            @Override
            void decode(byte[] b, int off, int len) throws IOException {
                // Dummy implementation
            }

            @Override
            int readResults(byte[] b, int off, int len) throws IOException {
                // Dummy implementation
                return 0;
            }

            @Override
            boolean hasData() {
                // Dummy implementation
                return false;
            }

            @Override
            protected void ensureBufferSize(int size) {
                // Dummy implementation
            }
        };

        ByteArrayInputStream bais = new ByteArrayInputStream("test".getBytes());
        inputStream = new BaseNCodecInputStream(bais, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        int result = inputStream.read();
        assertEquals('t', result);
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] buffer = new byte[10];
        int result = inputStream.read(buffer);
        assertEquals(4, result);
        assertArrayEquals("test".getBytes(), buffer);
    }

    @Test
    public void testReadArrayOffset() throws IOException {
        byte[] buffer = new byte[10];
        int result = inputStream.read(buffer, 2, 4);
        assertEquals(4, result);
        assertArrayEquals(new byte[]{0, 0, 't', 'e', 's', 't', 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testMarkSupported() {
        boolean result = inputStream.markSupported();
        assertFalse(result);
    }

    @Test
    public void testSkip() throws IOException {
        long result = inputStream.skip(2);
        assertEquals(2, result);
    }

    @Test
    public void testAvailable() throws IOException {
        int result = inputStream.available();
        assertEquals(1, result);
    }
}