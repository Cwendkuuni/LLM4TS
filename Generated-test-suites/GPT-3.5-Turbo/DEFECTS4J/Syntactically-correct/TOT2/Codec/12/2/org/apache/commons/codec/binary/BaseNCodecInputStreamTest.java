import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadWithBytes() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{65, 66, 67}), new BaseNCodec(), false);
        assertEquals(65, inputStream.read());
        assertEquals(66, inputStream.read());
        assertEquals(67, inputStream.read());
        assertEquals(-1, inputStream.read());
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullArray() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.read(new byte[5], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLen() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.read(new byte[5], 0, -1);
    }
}