
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] encodedData = {97, 98, 99}; // "abc" encoded
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec baseNCodec = new Base64(); // Replace with appropriate codec
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        assertEquals(97, baseNCodecInputStream.read());
        assertEquals(98, baseNCodecInputStream.read());
        assertEquals(99, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read()); // EOF
    }
    
    @Test
    public void testReadBuffer() throws IOException {
        byte[] encodedData = {97, 98, 99}; // "abc" encoded
        InputStream in = new ByteArrayInputStream(encodedData);
        BaseNCodec baseNCodec = new Base64(); // Replace with appropriate codec
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        byte[] buffer = new byte[3];
        assertEquals(3, baseNCodecInputStream.read(buffer, 0, 3));
        assertEquals(97, buffer[0]);
        assertEquals(98, buffer[1]);
        assertEquals(99, buffer[2]);
        assertEquals(-1, baseNCodecInputStream.read(buffer, 0, 3)); // EOF
    }

}

