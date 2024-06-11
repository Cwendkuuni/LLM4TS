import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new Base64(), true);
    }

    @After
    public void tearDown() {
        try {
            baseNCodecInputStream.close();
        } catch (IOException e) {
            fail("Failed to close input stream");
        }
    }

    @Test
    public void testRead_eof() throws IOException {
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testRead_byte() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4};
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), new Base64(), true);

        assertEquals(1, baseNCodecInputStream.read());
        assertEquals(2, baseNCodecInputStream.read());
        assertEquals(3, baseNCodecInputStream.read());
        assertEquals(4, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testRead_byteArray_offset_len() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4};
        byte[] output = new byte[4];
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), new Base64(), true);

        int read = baseNCodecInputStream.read(output, 0, 4);

        assertEquals(4, read);
        assertEquals(1, output[0]);
        assertEquals(2, output[1]);
        assertEquals(3, output[2]);
        assertEquals(4, output[3]);
    }

    @Test
    public void testRead_byteArray_null() throws IOException {
        try {
            baseNCodecInputStream.read(null, 0, 4);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testRead_byteArray_offset_negative() throws IOException {
        byte[] output = new byte[4];
        try {
            baseNCodecInputStream.read(output, -1, 4);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testRead_byteArray_len_negative() throws IOException {
        byte[] output = new byte[4];
        try {
            baseNCodecInputStream.read(output, 0, -1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testRead_byteArray_offset_greaterThanLength() throws IOException {
        byte[] output = new byte[4];
        try {
            baseNCodecInputStream.read(output, 5, 2);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testMarkSupported_false() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable_eof() {
        assertEquals(0, baseNCodecInputStream.available());
    }

    @Test
    public void testAvailable_nonEOF() throws IOException {
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[4]), new Base64(), true);
        assertEquals(4, baseNCodecInputStream.available());
    }

    @Test
    public void testSkip_negative() {
        try {
            baseNCodecInputStream.skip(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSkip_greaterThanInputStreamLength() throws IOException {
        byte[] input = new byte[4];
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), new Base64(), true);

        assertEquals(4, baseNCodecInputStream.skip(5));
    }

    @Test
    public void testSkip_withinInputStreamLength() throws IOException {
        byte[] input = new byte[4];
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), new Base64(), true);

        assertEquals(2, baseNCodecInputStream.skip(2));
    }

    @Test
    public void testAvailable_eof() {
        assertEquals(0, baseNCodecInputStream.available());
    }

    @Test
    public void testAvailable_nonEOF() throws IOException {
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[4]), new Base64(), true);
        assertEquals(4, baseNCodecInputStream.available());
    }

    @Test
    public void testAvailable_eof() {
        assertEquals(0, baseNCodecInputStream.available());
    }

    @Test
    public void testAvailable_nonEOF() throws IOException {
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[4]), new Base64(), true);
        assertEquals(4, baseNCodecInputStream.available());
    }

    @Test
    public void testSetDoEncode_true() {
        baseNCodecInputStream.setDoEncode(true);
        assertTrue(baseNCodecInputStream.getDoEncode());
    }

    @Test
    public void testSetDoEncode_false() {
        baseNCodecInputStream.setDoEncode(false);
        assertFalse(baseNCodecInputStream.getDoEncode());
    }

    @Test
    public void testSetDoEncode_thenRead_byte() throws IOException {
        baseNCodecInputStream.setDoEncode(true);

        byte[] input = new byte[]{1, 2, 3, 4};
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), new Base64(), false);

        assertEquals(1, baseNCodecInputStream.read());
        assertEquals(2, baseNCodecInputStream.read());
        assertEquals(3, baseNCodecInputStream.read());
        assertEquals(4, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testSetDoEncode_thenRead_byteArray_offset_len() throws IOException {
        baseNCodecInputStream.setDoEncode(true);

        byte[] input = new byte[]{1, 2, 3, 4};
        byte[] output = new byte[4];
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(input), new Base64(), false);

        int read = baseNCodecInputStream.read(output, 0, 4);

        assertEquals(4, read);
        assertEquals(1, output[0]);
        assertEquals(2, output[1]);
        assertEquals(3, output[2]);
        assertEquals(4, output[3]);
    }

    @Test
    public void testSetDoEncode_thenRead_byteArray_null() throws IOException {
        try {
            baseNCodecInputStream.setDoEncode(true);
            baseNCodecInputStream.read(null, 0, 4);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSetDoEncode_thenRead_byteArray_offset_negative() throws IOException {
        byte[] output = new byte[4];
        try {
            baseNCodecInputStream.setDoEncode(true);
            baseNCodecInputStream.read(output, -1, 4);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSetDoEncode_thenRead_byteArray_len_negative() throws IOException {
        byte[] output = new byte[4];
        try {
            baseNCodecInputStream.setDoEncode(true);
            baseNCodecInputStream.read(output, 0, -1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSetDoEncode_thenRead_byteArray_offset_greaterThanLength() throws IOException {
        byte[] output = new byte[4];
        try {
            baseNCodecInputStream.setDoEncode(true);
            baseNCodecInputStream.read(output, 5, 2);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSetDoEncode_thenAvailable_eof() {
        baseNCodecInputStream.setDoEncode(true);
        assertEquals(0, baseNCodecInputStream.available());
    }

    @Test
    public void testSetDoEncode_thenAvailable_nonEOF() throws IOException {
        baseNCodecInputStream.setDoEncode(true);
        baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[4]), new Base64(), true);
        assertEquals(4, baseNCodecInputStream.available());
    }

}