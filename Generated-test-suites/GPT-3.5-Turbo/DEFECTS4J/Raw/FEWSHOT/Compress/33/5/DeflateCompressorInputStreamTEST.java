
package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.io.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Test case 1: Empty input stream
        InputStream inputStream1 = new ByteArrayInputStream("".getBytes());
        DeflateCompressorInputStream deflateStream1 = new DeflateCompressorInputStream(inputStream1);
        assertEquals(-1, deflateStream1.read());
        deflateStream1.close();

        // Test case 2: Single-byte input stream
        InputStream inputStream2 = new ByteArrayInputStream("F".getBytes());
        DeflateCompressorInputStream deflateStream2 = new DeflateCompressorInputStream(inputStream2);
        assertEquals(70, deflateStream2.read());
        deflateStream2.close();

        // Test case 3: Multiple bytes input stream
        InputStream inputStream3 = new ByteArrayInputStream("Hello World".getBytes());
        DeflateCompressorInputStream deflateStream3 = new DeflateCompressorInputStream(inputStream3);
        assertEquals(72, deflateStream3.read());
        assertEquals(101, deflateStream3.read());
        assertEquals(108, deflateStream3.read());
        assertEquals(108, deflateStream3.read());
        assertEquals(111, deflateStream3.read());
        assertEquals(32, deflateStream3.read());
        assertEquals(87, deflateStream3.read());
        assertEquals(111, deflateStream3.read());
        assertEquals(114, deflateStream3.read());
        assertEquals(108, deflateStream3.read());
        assertEquals(100, deflateStream3.read());
        deflateStream3.close();
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        // Test case 1: Empty input stream
        InputStream inputStream1 = new ByteArrayInputStream("".getBytes());
        DeflateCompressorInputStream deflateStream1 = new DeflateCompressorInputStream(inputStream1);
        byte[] buffer1 = new byte[10];
        assertEquals(-1, deflateStream1.read(buffer1, 0, 10));
        deflateStream1.close();

        // Test case 2: Single-byte input stream
        InputStream inputStream2 = new ByteArrayInputStream("F".getBytes());
        DeflateCompressorInputStream deflateStream2 = new DeflateCompressorInputStream(inputStream2);
        byte[] buffer2 = new byte[10];
        assertEquals(1, deflateStream2.read(buffer2, 0, 10));
        assertEquals(70, buffer2[0]);
        deflateStream2.close();

        // Test case 3: Multiple bytes input stream
        InputStream inputStream3 = new ByteArrayInputStream("Hello World".getBytes());
        DeflateCompressorInputStream deflateStream3 = new DeflateCompressorInputStream(inputStream3);
        byte[] buffer3 = new byte[10];
        assertEquals(5, deflateStream3.read(buffer3, 0, 5));
        assertEquals(72, buffer3[0]);
        assertEquals(101, buffer3[1]);
        assertEquals(108, buffer3[2]);
        assertEquals(108, buffer3[3]);
        assertEquals(111, buffer3[4]);

        byte[] buffer3_2 = new byte[10];
        assertEquals(5, deflateStream3.read(buffer3_2, 0, 5));
        assertEquals(32, buffer3_2[0]);
        assertEquals(87, buffer3_2[1]);
        assertEquals(111, buffer3_2[2]);
        assertEquals(114, buffer3_2[3]);
        assertEquals(108, buffer3_2[4]);
        deflateStream3.close();
    }

    @Test
    public void testSkip() throws IOException {
        // Test case 1: Empty input stream
        InputStream inputStream1 = new ByteArrayInputStream("".getBytes());
        DeflateCompressorInputStream deflateStream1 = new DeflateCompressorInputStream(inputStream1);
        assertEquals(0, deflateStream1.skip(10));
        deflateStream1.close();

        // Test case 2: Single-byte input stream
        InputStream inputStream2 = new ByteArrayInputStream("F".getBytes());
        DeflateCompressorInputStream deflateStream2 = new DeflateCompressorInputStream(inputStream2);
        assertEquals(1, deflateStream2.skip(10));
        deflateStream2.close();

        // Test case 3: Multiple bytes input stream
        InputStream inputStream3 = new ByteArrayInputStream("Hello World".getBytes());
        DeflateCompressorInputStream deflateStream3 = new DeflateCompressorInputStream(inputStream3);
        assertEquals(5, deflateStream3.skip(5));
        assertEquals(6, deflateStream3.skip(7));
        deflateStream3.close();
    }

    @Test
    public void testAvailable() throws IOException {
        // Test case 1: Empty input stream
        InputStream inputStream1 = new ByteArrayInputStream("".getBytes());
        DeflateCompressorInputStream deflateStream1 = new DeflateCompressorInputStream(inputStream1);
        assertEquals(0, deflateStream1.available());
        deflateStream1.close();

        // Test case 2: Single-byte input stream
        InputStream inputStream2 = new ByteArrayInputStream("F".getBytes());
        DeflateCompressorInputStream deflateStream2 = new DeflateCompressorInputStream(inputStream2);
        assertEquals(1, deflateStream2.available());
        deflateStream2.close();

        // Test case 3: Multiple bytes input stream
        InputStream inputStream3 = new ByteArrayInputStream("Hello World".getBytes());
        DeflateCompressorInputStream deflateStream3 = new DeflateCompressorInputStream(inputStream3);
        assertEquals(11, deflateStream3.available());
        deflateStream3.close();
    }

    @Test
    public void testClose() throws IOException {
        // Test case 1: Empty input stream
        InputStream inputStream1 = new ByteArrayInputStream("".getBytes());
        DeflateCompressorInputStream deflateStream1 = new DeflateCompressorInputStream(inputStream1);
        deflateStream1.close();

        // Test case 2: Single-byte input stream
        InputStream inputStream2 = new ByteArrayInputStream("F".getBytes());
        DeflateCompressorInputStream deflateStream2 = new DeflateCompressorInputStream(inputStream2);
        deflateStream2.close();

        // Test case 3: Multiple bytes input stream
        InputStream inputStream3 = new ByteArrayInputStream("Hello World".getBytes());
        DeflateCompressorInputStream deflateStream3 = new DeflateCompressorInputStream(inputStream3);
        deflateStream3.close();
    }
}
