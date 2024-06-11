
package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws Exception {
        byte[] testBytes = "Test String".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int readByte;
        int i = 0;
        while ((readByte = base64InputStream.read()) != -1) {
            assertEquals(testBytes[i], (byte) readByte);
            i++;
        }

        base64InputStream.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        byte[] testBytes = "Test String".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] readBytes = new byte[4];
        int readLen;
        int i = 0;
        while ((readLen = base64InputStream.read(readBytes, 0, readBytes.length)) != -1) {
            for (int j = 0; j < readLen; j++) {
                assertEquals(testBytes[i + j], readBytes[j]);
            }
            i += readLen;
            readBytes = new byte[4];
        }

        base64InputStream.close();
    }

    @Test
    public void testReadWithOffsetAndLengthLargeData() throws Exception {
        byte[] testBytes = new byte[65536];
        for (int i = 0; i < testBytes.length; i++) {
            testBytes[i] = (byte) (i % 256);
        }
        
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] readBytes = new byte[8192];
        int readLen;
        int i = 0;
        while ((readLen = base64InputStream.read(readBytes, 0, readBytes.length)) != -1) {
            for (int j = 0; j < readLen; j++) {
                assertEquals(testBytes[i + j], readBytes[j]);
            }
            i += readLen;
            readBytes = new byte[8192];
        }

        base64InputStream.close();
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("Test String".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
        
        base64InputStream.close();
    }
}
