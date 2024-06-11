
package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {
    private DeflateCompressorInputStream deflateCompressorInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setup() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        deflateCompressorInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(0, deflateCompressorInputStream.read());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(0, deflateCompressorInputStream.read(buffer, 0, 10));
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(0, deflateCompressorInputStream.skip(10));
    }

    @Test
    public void testAvailable() throws IOException {
        assertEquals(0, deflateCompressorInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
    }
}
