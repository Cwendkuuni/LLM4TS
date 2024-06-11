import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[] {65, 66, 67, 68}; // "ABCD"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);

        Base64InputStream inputStream = new Base64InputStream(bais);

        int byte1 = inputStream.read(); // should return 0x00
        int byte2 = inputStream.read(); // should return 0x00
        int byte3 = inputStream.read(); // should return 0x03
        int byte4 = inputStream.read(); // should return 0xFC

        assertEquals(0x00, byte1);
        assertEquals(0x00, byte2);
        assertEquals(0x03, byte3);
        assertEquals(0xFC, byte4);

        inputStream.close();
    }

    @Test
    public void testReadNullArray() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(System.in);

        try {
            inputStream.read(null, 0, 4);
        } catch (NullPointerException e) {
            // expected NullPointerException
        }

        inputStream.close();
    }

    @Test
    public void testReadInvalidOffset() throws IOException {
        byte[] input = new byte[] {65, 66, 67, 68}; // "ABCD"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);

        Base64InputStream inputStream = new Base64InputStream(bais);

        try {
            inputStream.read(new byte[4], -1, 4);
        } catch (IndexOutOfBoundsException e) {
            // expected IndexOutOfBoundsException
        }

        inputStream.close();
    }

    @Test
    public void testReadInvalidLength() throws IOException {
        byte[] input = new byte[] {65, 66, 67, 68}; // "ABCD"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);

        Base64InputStream inputStream = new Base64InputStream(bais);

        try {
            inputStream.read(new byte[4], 0, -1);
        } catch (IndexOutOfBoundsException e) {
            // expected IndexOutOfBoundsException
        }

        inputStream.close();
    }

    @Test
    public void testReadInvalidBufferLength() throws IOException {
        byte[] input = new byte[] {65, 66, 67, 68}; // "ABCD"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);

        Base64InputStream inputStream = new Base64InputStream(bais);

        try {
            inputStream.read(new byte[3], 0, 4);
        } catch (IndexOutOfBoundsException e) {
            // expected IndexOutOfBoundsException
        }

        inputStream.close();
    }

    @Test
    public void testReadEmptyInput() throws IOException {
        byte[] input = new byte[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(input);

        Base64InputStream inputStream = new Base64InputStream(bais);

        int byte1 = inputStream.read(); // should return -1

        assertEquals(-1, byte1);

        inputStream.close();
    }

    @Test
    public void testReadWithEncodeTrue() throws IOException {
        byte[] input = new byte[] {65, 66, 67, 68}; // "ABCD"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);

        Base64InputStream inputStream = new Base64InputStream(bais, true);

        int byte1 = inputStream.read(); // should return 0x41
        int byte2 = inputStream.read(); // should return 0x42
        int byte3 = inputStream.read(); // should return 0x4B
        int byte4 = inputStream.read(); // should return 0x47

        assertEquals(0x41, byte1);
        assertEquals(0x42, byte2);
        assertEquals(0x43, byte3);
        assertEquals(0x44, byte4);

        inputStream.close();
    }

    @Test
    public void testReadWithEncodeFalse() throws IOException {
        byte[] input = new byte[] {65, 66, 67, 68}; // "ABCD"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);

        Base64InputStream inputStream = new Base64InputStream(bais, false);

        int byte1 = inputStream.read(); // should return 0
        int byte2 = inputStream.read(); // should return 0
        int byte3 = inputStream.read(); // should return 3
        int byte4 = inputStream.read(); // should return -4

        assertEquals(0x00, byte1);
        assertEquals(0x00, byte2);
        assertEquals(0x03, byte3);
        assertEquals(0xFC, byte4);

        inputStream.close();
    }

    @Test
    public void testReadLineLength() throws IOException {
        byte[] input = new byte[] {65, 66, 67, 68}; // "ABCD"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);

        Base64InputStream inputStream = new Base64InputStream(bais, true, 2, new byte[]{10});

        int byte1 = inputStream.read(); // should return 0x41
        int byte2 = inputStream.read(); // should return 0x42
        int byte3 = inputStream.read(); // should return 0x0D
        int byte4 = inputStream.read(); // should return 0x0A
        int byte5 = inputStream.read(); // should return 0x43
        int byte6 = inputStream.read(); // should return 0x44

        assertEquals(0x41, byte1);
        assertEquals(0x42, byte2);
        assertEquals(0x0D, byte3);
        assertEquals(0x0A, byte4);
        assertEquals(0x43, byte5);
        assertEquals(0x44, byte6);

        inputStream.close();
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream inputStream = new Base64InputStream(System.in);

        assertEquals(false, inputStream.markSupported());

        inputStream.close();
    }

}