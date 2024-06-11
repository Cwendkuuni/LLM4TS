import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            protected void encode(byte[] data, int off, int len) { }
            @Override
            protected void decode(byte[] data, int off, int len) { }
            @Override
            protected boolean hasData() { return false; }
            @Override
            protected int readResults(byte[] buf, int offset, int len) { return 0; }
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {68, 69, 70, 71});
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(68, codecInputStream.read());
        assertEquals(69, codecInputStream.read());
        assertEquals(70, codecInputStream.read());
        assertEquals(71, codecInputStream.read());
        assertEquals(-1, codecInputStream.read()); // EOF reached
        assertEquals(-1, codecInputStream.read());
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            protected void encode(byte[] data, int off, int len) { }
            @Override
            protected void decode(byte[] data, int off, int len) { }
            @Override
            protected boolean hasData() { return true; }
            @Override
            protected int readResults(byte[] buf, int offset, int len) { return 0; }
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {65});
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        assertEquals(1, codecInputStream.read(new byte[0], 0, 0));
        assertEquals(1, codecInputStream.read(new byte[0], 0, 0));
        assertEquals(1, codecInputStream.read(new byte[0], 0, 0));
        assertEquals(65, codecInputStream.read()); // single byte read
        assertEquals(-1, codecInputStream.read()); // EOF reached
        assertEquals(-1, codecInputStream.read());
    }

    @Test
    public void testReadWithNullBuffer() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            protected void encode(byte[] data, int off, int len) { }
            @Override
            protected void decode(byte[] data, int off, int len) { }
            @Override
            protected boolean hasData() { return true; }
            @Override
            protected int readResults(byte[] buf, int offset, int len) { return 0; }
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        assertEquals(1, codecInputStream.read(null, 0, 0)); // NullPointerException
        assertEquals(65, codecInputStream.read()); // single byte read
        assertEquals(1, codecInputStream.read(null, 0, 0)); // NullPointerException
        assertEquals(66, codecInputStream.read());
        assertEquals(1, codecInputStream.read(null, 0, 0)); // NullPointerException
        assertEquals(67, codecInputStream.read());
        assertEquals(-1, codecInputStream.read()); // EOF reached
        assertEquals(-1, codecInputStream.read());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            protected void encode(byte[] data, int off, int len) { }
            @Override
            protected void decode(byte[] data, int off, int len) { }
            @Override
            protected boolean hasData() { return true; }
            @Override
            protected int readResults(byte[] buf, int offset, int len) { return 0; }
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {65, 66, 67});
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        assertEquals(0, codecInputStream.read(new byte[3], -1, 0)); // IndexOutOfBoundsException
    }

    @Test
    public void testMarkSupported() {
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            protected void encode(byte[] data, int off, int len) { }
            @Override
            protected void decode(byte[] data, int off, int len) { }
            @Override
            protected boolean hasData() { return true; }
            @Override
            protected int readResults(byte[] buf, int offset, int len) { return 0; }
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {65});
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        assertFalse(codecInputStream.markSupported()); // mark is not supported
    }

    @Test
    public void testAvailable() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            protected void encode(byte[] data, int off, int len) { }
            @Override
            protected void decode(byte[] data, int off, int len) { }
            @Override
            protected boolean hasData() { return true; }
            @Override
            protected int readResults(byte[] buf, int offset, int len) { return 0; }
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {65});
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        assertEquals(0, codecInputStream.available()); // underlying codec has no data

        codecInputStream.read(); // single byte read

        assertEquals(1, codecInputStream.available()); // EOF not reached
    }

}