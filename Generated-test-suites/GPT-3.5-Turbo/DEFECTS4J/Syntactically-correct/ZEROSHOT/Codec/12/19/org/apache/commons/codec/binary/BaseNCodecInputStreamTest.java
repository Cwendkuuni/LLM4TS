import static org.junit.Assert.*;
import org.junit.Test;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        int result = codecIn.read();

        assertEquals(0x48, result);
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        byte[] data = new byte[]{};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        int result = codecIn.read();

        assertEquals(-1, result);
    }

    @Test
    public void testReadWithLargeData() throws IOException {
        byte[] data = new byte[50000];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte)i;
        }
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        int result = codecIn.read();

        assertNotEquals(-1, result);
    }

    @Test
    public void testReadWithNullInput() {
        InputStream in = null;
        BaseNCodec baseNCodec = new BaseNCodec();

        try {
            BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        } catch (IOException e) {
            fail("Expected NullPointerException");
        }
    }

    @Test
    public void testReadWithInvalidOffset() throws IOException {
        byte[] data = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        byte[] buffer = new byte[5];

        try {
            codecIn.read(buffer, -1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        }
    }

    @Test
    public void testReadWithInvalidLen() throws IOException {
        byte[] data = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        byte[] buffer = new byte[5];

        try {
            codecIn.read(buffer, 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        }
    }

    @Test
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] data = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        byte[] buffer = new byte[3];

        try {
            codecIn.read(buffer, 0, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        }
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] data = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        byte[] buffer = new byte[0];

        int result = codecIn.read(buffer, 0, 0);

        assertEquals(0, result);
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        boolean result = codecIn.markSupported();

        assertFalse(result);
    }

    @Test
    public void testEOFReturned() throws IOException {
        byte[] data = new byte[]{(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        InputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream codecIn = new BaseNCodecInputStream(in, baseNCodec, true);

        byte[] buffer = new byte[10];

        int result = codecIn.read(buffer, 0, 10);

        assertEquals(-1, result);
    }
}