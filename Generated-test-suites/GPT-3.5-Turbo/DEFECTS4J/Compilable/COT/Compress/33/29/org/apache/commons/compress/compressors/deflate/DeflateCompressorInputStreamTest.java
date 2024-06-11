package org.apache.commons.compress.compressors.deflate;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        int result = stream.read();
        assertEquals(1, result);
        stream.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = {1, 2, 3, 4};
        byte[] buffer = new byte[2];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        int result = stream.read(buffer, 0, 2);
        assertEquals(2, result);
        assertArrayEquals(new byte[]{1, 2}, buffer);
        stream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        long result = stream.skip(2);
        assertEquals(2, result);
        assertEquals(3, stream.read());
        stream.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {1, 2, 3, 4};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(input);
        assertEquals(4, stream.available());
        stream.read();
        assertEquals(3, stream.available());
        stream.close();
    }

}
