import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] byteArray = {65, 66, 67, 68, 69}; // ABCDE
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        assertEquals(65, inputStream.read());
        assertEquals(66, inputStream.read());
        assertEquals(67, inputStream.read());
        assertEquals(68, inputStream.read());
        assertEquals(69, inputStream.read());
        assertEquals(-1, inputStream.read()); // EOF
    }

    @Test
    public void testReadWithOffsetAndLen() throws IOException {
        byte[] byteArray = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74}; // ABCDEFGHIJ
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        byte[] output = new byte[5];
        assertEquals(5, inputStream.read(output, 0, 5));
        assertArrayEquals(new byte[]{65, 66, 67, 68, 69}, output);

        output = new byte[7];
        assertEquals(5, inputStream.read(output, 2, 7));
        assertArrayEquals(new byte[]{0, 0, 67, 68, 69, 70, 71}, output);

        output = new byte[5];
        assertEquals(3, inputStream.read(output, 2, 3));
        assertArrayEquals(new byte[]{0, 0, 72, 73, 69}, output);
        assertEquals(-1, inputStream.read(output, 0, 2)); // EOF
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testAvailable() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        assertEquals(0, inputStream.available());
    }
}