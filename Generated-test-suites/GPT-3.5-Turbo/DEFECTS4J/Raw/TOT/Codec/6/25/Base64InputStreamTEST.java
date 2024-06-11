
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64InputStream base64InputStream;
    private ByteArrayOutputStream outputStream;
    private OutputStreamWriter writer;

    @Before
    public void setUp() {
        byte[] testData = "Hello, World!".getBytes();
        inputStream = new ByteArrayInputStream(testData);
    }

    @Test
    public void testReadAndWrite() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);

        outputStream = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(outputStream);
        int data;
        while ((data = base64InputStream.read()) != -1) {
            writer.write(data);
        }

        writer.flush();
        byte[] resultData = outputStream.toByteArray();
        assertEquals("Hello, World!", new String(resultData));
    }
  
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);

        byte[] resultData = new byte[7];
        int bytesRead = base64InputStream.read(resultData, 0, 7);

        assertEquals(7, bytesRead);
        assertEquals("Hello, ", new String(resultData));
    }
  
    @Test
    public void testReadWithNullBuffer() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);

        try {
            base64InputStream.read(null, 0, 7);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }
  
    @Test
    public void testReadWithNegativeOffset() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);

        byte[] resultData = new byte[7];
        try {
            base64InputStream.read(resultData, -1, 7);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }
  
    @Test
    public void testReadWithInvalidOffset() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);

        byte[] resultData = new byte[7];
        try {
            base64InputStream.read(resultData, 8, 7);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }
  
    @Test
    public void testReadWithInvalidLength() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);

        byte[] resultData = new byte[7];
        try {
            base64InputStream.read(resultData, 0, 8);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }
  
    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);

        byte[] resultData = new byte[0];
        int bytesRead = base64InputStream.read(resultData, 0, 0);

        assertEquals(0, bytesRead);
        assertEquals(0, resultData.length);
    }
  
    @Test
    public void testReadWithZeroLength() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);

        byte[] resultData = new byte[7];
        int bytesRead = base64InputStream.read(resultData, 0, 0);

        assertEquals(0, bytesRead);
    }
  
    @Test
    public void testReadWithNoData() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        base64InputStream = new Base64InputStream(emptyInputStream);

        byte[] resultData = new byte[7];
        int bytesRead = base64InputStream.read(resultData, 0, 7);

        assertEquals(-1, bytesRead);
    }
  
    @Test
    public void testMarkSupport() {
        base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
    }

  
}

