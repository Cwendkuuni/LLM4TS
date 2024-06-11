import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadByte() throws IOException {
        byte[] input = {10, 20, 30};
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodecInputStream stream = new BaseNCodecInputStream(inputStream, codec, false);

        assertEquals(10, stream.read());
        assertEquals(20, stream.read());
        assertEquals(30, stream.read());
        assertEquals(-1, stream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = {10, 20, 30};
        byte[] output = new byte[3];
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodecInputStream stream = new BaseNCodecInputStream(inputStream, codec, false);

        assertEquals(3, stream.read(output, 0, 3));
        assertArrayEquals(input, output);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullArray() throws IOException {
        BaseNCodec codec = new BaseNCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1]);
        BaseNCodecInputStream stream = new BaseNCodecInputStream(inputStream, codec, false);

        stream.read(null, 0, 1);
    }
}