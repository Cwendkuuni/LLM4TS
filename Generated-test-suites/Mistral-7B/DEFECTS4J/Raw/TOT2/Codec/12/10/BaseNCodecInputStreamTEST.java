import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] testByte = new byte[1];

        int result = inputStream.read();
        assertEquals(-1, result); // EOF

        inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        result = inputStream.read();
        assertEquals((byte) 't', result); // first byte

        inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        result = inputStream.read(testByte, 0, 1);
        assertEquals((byte) 'e', testByte[0]); // second byte

        inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        result = inputStream.read(testByte, 0, 1);
        assertEquals((byte) 's', testByte[0]); // third byte

        inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        result = inputStream.read(testByte, 0, 1);
        assertEquals((byte) 't', testByte[0]); // fourth byte
    }
}