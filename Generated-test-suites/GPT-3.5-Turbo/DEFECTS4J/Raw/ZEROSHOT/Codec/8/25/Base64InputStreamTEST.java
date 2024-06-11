import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // create a test input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{72, 101, 108, 108, 111});

        // create a Base64InputStream with doEncode set to false
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        try {
            // read the data from the input stream
            int first = base64InputStream.read();
            int second = base64InputStream.read();
            int third = base64InputStream.read();
            int fourth = base64InputStream.read();
            int fifth = base64InputStream.read();
            int eof = base64InputStream.read();

            // verify the data
            assertEquals(72, first);
            assertEquals(101, second);
            assertEquals(108, third);
            assertEquals(108, fourth);
            assertEquals(111, fifth);
            assertEquals(-1, eof);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() {
        // create a test input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{72, 101, 108, 108, 111});

        // create a Base64InputStream with doEncode set to true
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 0, null);

        // create a byte array to store the read data
        byte[] buffer = new byte[10];

        try {
            // read the data into the byte array
            int bytesRead = base64InputStream.read(buffer, 2, 3);

            // verify the number of bytes read
            assertEquals(3, bytesRead);

            // verify the data in the byte array
            assertEquals(0, buffer[0]);
            assertEquals(0, buffer[1]);
            assertEquals(72, buffer[2]);
            assertEquals(101, buffer[3]);
            assertEquals(108, buffer[4]);
            assertEquals(0, buffer[5]);
            assertEquals(0, buffer[6]);
            assertEquals(0, buffer[7]);
            assertEquals(0, buffer[8]);
            assertEquals(0, buffer[9]);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayWithNullArray() throws IOException {
        // create a test input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{72, 101, 108, 108, 111});

        // create a Base64InputStream with doEncode set to true
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 0, null);

        // read data into a null byte array
        base64InputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffset() throws IOException {
        // create a test input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{72, 101, 108, 108, 111});

        // create a Base64InputStream with doEncode set to true
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 0, null);

        // create a byte array to store the read data
        byte[] buffer = new byte[10];

        // read data into the byte array with an invalid offset
        base64InputStream.read(buffer, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLength() throws IOException {
        // create a test input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{72, 101, 108, 108, 111});

        // create a Base64InputStream with doEncode set to true
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 0, null);

        // create a byte array to store the read data
        byte[] buffer = new byte[10];

        // read data into the byte array with an invalid length
        base64InputStream.read(buffer, 0, -1);
    }

    @Test
    public void testMarkSupported() {
        // create a test input stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{72, 101, 108, 108, 111});

        // create a Base64InputStream with doEncode set to false
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // check if mark is supported
        assertFalse(base64InputStream.markSupported());
    }
}