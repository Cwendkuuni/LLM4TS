import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    private BaseNCodec baseNCodec;

    @Before
    public void setup() {
        baseNCodec = new BaseNCodec();
        byte[] testData = new byte[]{65, 66, 67, 68}; // ASCII values for A, B, C, D
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(65, baseNCodecInputStream.read()); // A
        assertEquals(66, baseNCodecInputStream.read()); // B
        assertEquals(67, baseNCodecInputStream.read()); // C
        assertEquals(68, baseNCodecInputStream.read()); // D
        assertEquals(-1, baseNCodecInputStream.read()); // EOF
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[2];
        assertEquals(2, baseNCodecInputStream.read(buffer, 0, 2));
        assertEquals(65, buffer[0]); // A
        assertEquals(66, buffer[1]); // B

        byte[] buffer2 = new byte[4];
        assertEquals(2, baseNCodecInputStream.read(buffer2, 2, 2));
        assertEquals(67, buffer2[2]); // C
        assertEquals(68, buffer2[3]); // D
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        baseNCodecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        byte[] buffer = new byte[1];
        baseNCodecInputStream.read(buffer, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        byte[] buffer = new byte[1];
        baseNCodecInputStream.read(buffer, 0, 2);
    }

    @Test
    public void testMarkSupported() {
        assertEquals(false, baseNCodecInputStream.markSupported());
    }

    @Test
    public void testEOF() throws IOException {
        byte[] testData = new byte[]{65, 66, 67, 68};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        
        assertEquals(4, baseNCodecInputStream.read(new byte[4], 0, 4));
        assertEquals(-1, baseNCodecInputStream.read());
    }
}