
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    // Test data for encoding and decoding
    private static final String PLAIN_TEXT = "Hello, world!";
    private static final byte[] ENCODED_TEXT = "SGVsbG8sIHdvcmxkIQ==".getBytes();

    @Test
    public void testRead() throws IOException {
        InputStream is = new ByteArrayInputStream(ENCODED_TEXT);
        Base64InputStream bis = new Base64InputStream(is, false);
        
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        
        bis.close();
        
        assertEquals(PLAIN_TEXT, sb.toString());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        InputStream is = new ByteArrayInputStream(ENCODED_TEXT);
        Base64InputStream bis = new Base64InputStream(is, false);
        
        byte[] buf = new byte[6];
        int bytesRead = bis.read(buf, 0, buf.length);
        bis.close();
        
        assertEquals(6, bytesRead);
        assertEquals("SGVsbG", new String(buf));
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        InputStream is = new ByteArrayInputStream(ENCODED_TEXT);
        Base64InputStream bis = new Base64InputStream(is, false);
        
        byte[] buf = null;
        bis.read(buf, 0, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        InputStream is = new ByteArrayInputStream(ENCODED_TEXT);
        Base64InputStream bis = new Base64InputStream(is, false);
        
        byte[] buf = new byte[5];
        bis.read(buf, -1, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        InputStream is = new ByteArrayInputStream(ENCODED_TEXT);
        Base64InputStream bis = new Base64InputStream(is, false);
        
        byte[] buf = new byte[5];
        bis.read(buf, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        InputStream is = new ByteArrayInputStream(ENCODED_TEXT);
        Base64InputStream bis = new Base64InputStream(is, false);
        
        byte[] buf = new byte[5];
        bis.read(buf, 0, 6);
    }
    
    @Test
    public void testMarkSupported() {
        InputStream is = new ByteArrayInputStream(ENCODED_TEXT);
        Base64InputStream bis = new Base64InputStream(is, false);
        
        assertFalse(bis.markSupported());
    }
    
    @Test
    public void testEncode() throws IOException {
        InputStream is = new ByteArrayInputStream(PLAIN_TEXT.getBytes());
        Base64InputStream bis = new Base64InputStream(is, true);
        
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        
        bis.close();
        
        assertEquals("SGVsbG8sIHdvcmxkIQ==", sb.toString());
    }
    
    @Test
    public void testEncodeWithLineLength() throws IOException {
        InputStream is = new ByteArrayInputStream(PLAIN_TEXT.getBytes());
        Base64InputStream bis = new Base64InputStream(is, true, 10, "\r\n".getBytes());
        
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        
        bis.close();
        
        assertEquals("SGVs\r\nbG8sIH\r\ndvcmxk\r\nIQ==", sb.toString());
    }
    
    @Test
    public void testEncodeWithInvalidBufferSize() throws IOException {
        InputStream is = new ByteArrayInputStream(PLAIN_TEXT.getBytes());
        Base64InputStream bis = new Base64InputStream(is, true, 1, null);
        
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        
        bis.close();
        
        assertEquals("SGV\r\nsbG\r\n8sIHdv\r\ncmxkI\r\nQ==", sb.toString());
    }
    
    @Test
    public void testEncodeWithEmptyInput() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Base64InputStream bis = new Base64InputStream(is, true);
        
        int c = 0;
        if ((c = bis.read()) != -1) {
            assertEquals('=', c);
        }
        
        bis.close();
    }
    
    @Test
    public void testEncodeWithBase64InputStreamConstructor() throws IOException {
        InputStream is = new ByteArrayInputStream(PLAIN_TEXT.getBytes());
        Base64InputStream base64Is = new Base64InputStream(is, true);
        Base64InputStream bis = new Base64InputStream(base64Is, true);
        
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        
        bis.close();
        
        assertEquals("SGVsbG8sIHdvcmxkIQ==", sb.toString());
    }
    
    @Test
    public void testEncodeWithDifferentLineSeparator() throws IOException {
        InputStream is = new ByteArrayInputStream(PLAIN_TEXT.getBytes());
        Base64InputStream bis = new Base64InputStream(is, true, -1, "\n".getBytes());
        
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        
        bis.close();
        
        assertEquals("SGVsbG8sIHdvcmxkIQ==", sb.toString());
    }

    @Test
    public void testDecodeWithBase64InputStreamConstructor() throws IOException {
        InputStream is = new ByteArrayInputStream(ENCODED_TEXT);
        Base64InputStream base64Is = new Base64InputStream(is, false);
        Base64InputStream bis = new Base64InputStream(base64Is, false);
        
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        
        bis.close();
        
        assertEquals(PLAIN_TEXT, sb.toString());
    }
    
    @Test
    public void testEncodeDecode() throws IOException {
        InputStream is = new ByteArrayInputStream(PLAIN_TEXT.getBytes());
        Base64InputStream base64Is = new Base64InputStream(is, true);
        Base64InputStream bis = new Base64InputStream(base64Is, false);
        
        int c = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = bis.read()) != -1) {
            sb.append((char) c);
        }
        
        bis.close();
        
        assertEquals(PLAIN_TEXT, sb.toString());
    }

}
