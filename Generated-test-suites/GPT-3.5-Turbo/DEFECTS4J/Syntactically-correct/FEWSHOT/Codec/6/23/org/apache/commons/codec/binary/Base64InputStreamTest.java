import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        InputStream in = mock(InputStream.class);
        base64InputStream = new Base64InputStream(in);
    }

    @Test
    public void testRead() throws IOException {
        byte[] expectedBytes = Base64.decodeBase64("SGVsbG8gd29ybGQh");
        ByteArrayInputStream in = new ByteArrayInputStream(expectedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        int bytesRead;
        byte[] actualBytes = new byte[expectedBytes.length];
        bytesRead = base64InputStream.read(actualBytes);
        assertEquals(expectedBytes.length, bytesRead);
        assertEquals(expectedBytes, actualBytes);

        bytesRead = base64InputStream.read(actualBytes);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] expectedBytes = Base64.decodeBase64("SGVsbG8gd29ybGQh");
        ByteArrayInputStream in = new ByteArrayInputStream(expectedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        int bytesRead;
        byte[] actualBytes = new byte[expectedBytes.length];
        bytesRead = base64InputStream.read(actualBytes, 2, 6);
        assertEquals(6, bytesRead);
        assertEquals(new byte[]{0, 0, expectedBytes[0], expectedBytes[1], expectedBytes[2], expectedBytes[3]}, actualBytes);

        bytesRead = base64InputStream.read(actualBytes, 0, 6);
        assertEquals(4, bytesRead);
        assertEquals(new byte[]{expectedBytes[4], expectedBytes[5], expectedBytes[6], expectedBytes[7]}, actualBytes);

        bytesRead = base64InputStream.read(actualBytes, 0, 6);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testMarkSupported() {
        boolean markSupported = base64InputStream.markSupported();
        assertEquals(false, markSupported);
    }

}