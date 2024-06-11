import static org.junit.Assert.*;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = { 65, 66, 67 }; // input array with ASCII values for A, B, C
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.read();

        assertEquals(65, result); // expected result is 65, which corresponds to ASCII value of A
    }

    @Test
    public void testRead_noData() throws IOException {
        byte[] input = {}; // empty input array
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.read();

        assertEquals(-1, result); // expected result is -1, indicating EOF as there is no data to read
    }

    @Test
    public void testRead_multiByteInput() throws IOException {
        byte[] input = { 65, 66, 67, 68, 69 }; // input array with ASCII values for A, B, C, D, E
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        int readLength = baseNCodecInputStream.read(result, 0, 3);

        assertEquals(3, readLength); // expected result is 3, as 3 bytes are read from the input stream
        assertArrayEquals(new byte[] { 65, 66, 67 }, result); // expected result is array { 65, 66, 67 } corresponding to ASCII values of A, B, C
    }

    @Test
    public void testRead_zeroLengthInput() throws IOException {
        byte[] input = { 65, 66, 67, 68, 69 }; // input array with ASCII values for A, B, C, D, E
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[0];
        int readLength = baseNCodecInputStream.read(result, 0, 0);

        assertEquals(0, readLength); // expected result is 0, as there is no data to read
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullByteArray() throws IOException {
        byte[] input = { 65, 66, 67 }; // input array with ASCII values for A, B, C
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.read(null, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_negativeOffset() throws IOException {
        byte[] input = { 65, 66, 67 }; // input array with ASCII values for A, B, C
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        baseNCodecInputStream.read(result, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset() throws IOException {
        byte[] input = { 65, 66, 67 }; // input array with ASCII values for A, B, C
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        baseNCodecInputStream.read(result, 4, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidLength() throws IOException {
        byte[] input = { 65, 66, 67 }; // input array with ASCII values for A, B, C
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[3];
        baseNCodecInputStream.read(result, 0, 4);
    }

    @Test
    public void testMarkSupported() {
        byte[] input = { 65, 66, 67 }; // input array with ASCII values for A, B, C
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertFalse(baseNCodecInputStream.markSupported()); // expected result is false, as supporting marks is not implemented
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = { 65, 66, 67 }; // input array with ASCII values for A, B, C
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.available();

        assertEquals(1, result); // expected result is 1, as there is more data available to read
    }
    
    @Test
    public void testAvailable_eof() throws IOException {
        byte[] input = {}; // empty input array
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int result = baseNCodecInputStream.available();

        assertEquals(0, result); // expected result is 0, as there is no more data available to read
    }
}