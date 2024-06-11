import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {
    private BaseNCodec baseNCodec;
    private InputStream input;
    private BaseNCodecInputStream stream;

    @Before
    public void setup() throws Exception {
        baseNCodec = new BaseNCodec();
        input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        stream = new BaseNCodecInputStream(input, baseNCodec, false);
    }

    @Test
    public void testReadNormalCase() throws IOException {
        assertEquals(0x01, stream.read());
        assertEquals(0x02, stream.read());
        assertEquals(0x03, stream.read());
        assertEquals(0x04, stream.read());
        assertEquals(0x05, stream.read());
        assertEquals(-1, stream.read()); // EOF
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[5];
        int length = stream.read(buffer, 0, buffer.length);
        assertEquals(5, length);
        for (int i = 0; i < length; i++) {
            assertEquals(buffer[i], (byte) (i + 1));
        }
    }
}