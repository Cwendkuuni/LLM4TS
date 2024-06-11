
package org.apache.commons.compress.compressors.deflate;

import org.junit.Test;
import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead_oneByte() {
        byte[] data = {1};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        int result = stream.read();
        assertEquals(1, result);
    }
  
    @Test
    public void testRead_multipleBytes() {
        byte[] data = {1, 2, 3, 4, 5};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        byte[] buffer = new byte[2];
        int result = stream.read(buffer, 0, 2);
        assertEquals(2, result);
        assertArrayEquals(new byte[]{1, 2}, buffer);
    }
  
    @Test
    public void testSkip() {
        byte[] data = {1, 2, 3, 4, 5};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        try {
            long result = stream.skip(3);
            assertEquals(3, result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
  
    @Test
    public void testAvailable() {
        byte[] data = {1, 2, 3, 4, 5};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        try {
            int result = stream.available();
            assertEquals(5, result);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
  
    @Test
    public void testClose() {
        byte[] data = {1, 2, 3, 4, 5};
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        try {
            stream.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
