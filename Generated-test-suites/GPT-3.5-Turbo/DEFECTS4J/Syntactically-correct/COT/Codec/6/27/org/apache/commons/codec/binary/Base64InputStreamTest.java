// Generated Junit 4 test cases for Base64InputStream


import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQh".getBytes()));
        assertEquals('H', stream.read());
        assertEquals('e', stream.read());
        assertEquals('l', stream.read());
        assertEquals('l', stream.read());
        assertEquals('o', stream.read());
        assertEquals(' ', stream.read());
        assertEquals('W', stream.read());
        assertEquals('o', stream.read());
        assertEquals('r', stream.read());
        assertEquals('l', stream.read());
        assertEquals('d', stream.read());
        assertEquals('!', stream.read());
        assertEquals(-1, stream.read());
        stream.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        byte[] buffer = new byte[5];
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQh".getBytes()));
        int bytesRead = stream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals('H', buffer[1]);
        assertEquals('e', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals(0, buffer[4]);
        stream.close();
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws Exception {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQh".getBytes()));
        stream.read(null, 0, 1);
        stream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws Exception {
        byte[] buffer = new byte[5];
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQh".getBytes()));
        stream.read(buffer, -1, 1);
        stream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws Exception {
        byte[] buffer = new byte[5];
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQh".getBytes()));
        stream.read(buffer, 0, -1);
        stream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws Exception {
        byte[] buffer = new byte[5];
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQh".getBytes()));
        stream.read(buffer, 6, 1);
        stream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws Exception {
        byte[] buffer = new byte[5];
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQh".getBytes()));
        stream.read(buffer, 0, 6);
        stream.close();
    }

    @Test
    public void testEmptyStream() throws Exception {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertEquals(-1, stream.read());
        stream.close();
    }

    @Test
    public void testMarkNotSupported() throws Exception {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream("SGVsbG8gV29ybGQh".getBytes()));
        assertFalse(stream.markSupported());
        stream.close();
    }
}
